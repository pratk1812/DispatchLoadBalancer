package com.finan.DispatchLoadBalancer.service;

import com.finan.DispatchLoadBalancer.model.dto.DispatchPlanDTO;
import com.finan.DispatchLoadBalancer.model.dto.OrderDTO;
import com.finan.DispatchLoadBalancer.model.dto.VehicleDTO;
import com.finan.DispatchLoadBalancer.persistence.mapper.OrderMapper;
import com.finan.DispatchLoadBalancer.persistence.mapper.VehicleMapper;
import com.finan.DispatchLoadBalancer.persistence.repository.OrderRepository;
import com.finan.DispatchLoadBalancer.persistence.repository.VehicleRepository;
import com.finan.DispatchLoadBalancer.util.Coordinated;
import com.finan.DispatchLoadBalancer.util.SpatialTree;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service responsible for building and assigning dispatch plans.
 *
 * <p>This service pulls all pending orders and available vehicles from their respective
 * repositories, converts them to DTOs, computes an optimized assignment based on spatial proximity
 * and capacity constraints, clears the repositories, and returns a list of {@link DispatchPlanDTO}
 * objects summarizing each vehicle’s route, total load, and assigned orders.
 */
@Service
public class DispatchPlanService {
  private static final Logger LOGGER = LogManager.getLogger(DispatchPlanService.class);

  private final OrderRepository orderRepository;
  private final VehicleRepository vehicleRepository;
  private final OrderMapper orderMapper;
  private final VehicleMapper vehicleMapper;

  /**
   * Constructs a new {@code DispatchPlanService} with the required repositories and mappers.
   *
   * @param orderRepository repository for retrieving and deleting orders
   * @param vehicleRepository repository for retrieving and deleting vehicles
   * @param orderMapper component that maps order entities to DTOs
   * @param vehicleMapper component that maps vehicle entities to DTOs
   */
  @Autowired
  public DispatchPlanService(
      OrderRepository orderRepository,
      VehicleRepository vehicleRepository,
      OrderMapper orderMapper,
      VehicleMapper vehicleMapper) {
    this.orderRepository = orderRepository;
    this.vehicleRepository = vehicleRepository;
    this.orderMapper = orderMapper;
    this.vehicleMapper = vehicleMapper;
  }

  /**
   * Builds and returns the dispatch plan for all current orders and vehicles.
   *
   * <p>1. Fetches all orders and vehicles from their repositories.<br>
   * 2. Converts entities to DTOs.<br>
   * 3. Invokes the internal assignment algorithm to match orders to vehicles.<br>
   * 4. Clears both repositories of processed orders and vehicles.<br>
   * 5. Transforms the assignment result into a list of {@link DispatchPlanDTO}.
   *
   * @return a list of {@link DispatchPlanDTO}, one per vehicle that receives at least one order
   */
  public List<DispatchPlanDTO> assign() {
    LOGGER.info("Building dispatch plan");
    List<OrderDTO> orderDTOS = orderRepository.findAll().stream().map(orderMapper::toDto).toList();
    List<VehicleDTO> vehicleDTOS =
        vehicleRepository.findAll().stream().map(vehicleMapper::toDto).toList();
    Map<Vehicle, List<Order>> assignments = assign(orderDTOS, vehicleDTOS);
    orderRepository.deleteAll();
    vehicleRepository.deleteAll();
    return assignments.entrySet().stream()
        .map(
            entry -> {
              DispatchPlanDTO dispatchPlanDTO = new DispatchPlanDTO();
              dispatchPlanDTO.setVehicleId(entry.getKey().vehicleDTO.getVehicleId());
              dispatchPlanDTO.setTotalDistance(String.valueOf(entry.getKey().totalDistance));
              dispatchPlanDTO.setTotalLoad(entry.getKey().assignedCapacity);
              dispatchPlanDTO.setAssignedOrders(
                  entry.getValue().stream().map(order -> order.orderDTO).toList());
              return dispatchPlanDTO;
            })
        .toList();
  }

  /**
   * Core assignment algorithm. For each order (in descending priority order), finds nearby vehicles
   * via a spatial index, filters by remaining capacity, computes a distance/slack score, and
   * assigns each order to the best vehicle. Vehicles whose capacity hits zero are removed from the
   * spatial index.
   *
   * @param orderDTOS list of order DTOs to assign
   * @param vehicleDTOS list of vehicle DTOs available for assignment
   * @return a map from each selected {@link Vehicle} to its list of assigned {@link Order} objects
   */
  private Map<Vehicle, List<Order>> assign(List<OrderDTO> orderDTOS, List<VehicleDTO> vehicleDTOS) {
    LOGGER.info("assigning orders to vehicles");
    List<Order> orders =
        orderDTOS.stream()
            .sorted(Comparator.comparing(OrderDTO::getPriority).reversed())
            .map(Order::new)
            .toList();
    List<Vehicle> vehicles = vehicleDTOS.stream().map(Vehicle::new).toList();

    SpatialTree<Vehicle> spatialTree = SpatialTree.createSimpleSTree(vehicles);

    Map<Vehicle, List<Order>> assignments = new HashMap<>();

    for (Order order : orders) {

      List<Vehicle> nearestVehicles = spatialTree.findNearest(order);

      List<Vehicle> assignAble = new ArrayList<>();

      int maximumRemainingCapacity = -1;
      int minimumRemainingCapacity = -1;
      int vehicleIndex = -1;
      for (Vehicle vehicle : nearestVehicles) {
        int remainingCapacity = vehicle.vehicleDTO.getCapacity() - vehicle.assignedCapacity;
        int slack = remainingCapacity - order.orderDTO.getPackageWeight();
        if (slack > 0) {
          assignAble.add(vehicle);
          if (maximumRemainingCapacity <= slack) {
            vehicleIndex = assignAble.size() - 1;
            maximumRemainingCapacity = slack;
          } else {
            minimumRemainingCapacity = slack;
          }
        }
        if (remainingCapacity == 0) {
          spatialTree.remove(vehicle.vehicleDTO.getVehicleId(), vehicle);
        }
      }

      if (vehicleIndex == -1) continue;

      Vehicle nearestVehicle = assignAble.get(0);

      double minDist = nearestVehicle.searchDistance;
      double maxDist = nearestVehicles.get(nearestVehicles.size() - 1).searchDistance;
      double costDist = (minDist / maxDist);

      Vehicle bestFitVehicle = assignAble.get(vehicleIndex);
      double minSlack = minimumRemainingCapacity;
      double maxSlack = maximumRemainingCapacity;
      double costSlack = 1 - (minSlack / maxSlack);

      Vehicle assigned;
      if (bestFitVehicle.searchDistance < 15) {

        // preferring Slack to distance
        double a = 0.4, b = 0.6;
        boolean score = a * (costSlack) < b * (costDist);

        assigned = score ? nearestVehicle : bestFitVehicle;

      } else {
        assigned = nearestVehicle;
      }
      assigned.totalDistance += assigned.searchDistance;
      assigned.assignedCapacity += order.orderDTO.getPackageWeight();

      assignments.computeIfAbsent(assigned, k -> new ArrayList<>()).add(order);
    }
    return assignments;
  }

  /**
   * Internal wrapper for {@link OrderDTO} that implements {@link Coordinated} for use in the
   * KD-tree spatial index.
   */
  private static class Order implements Coordinated {
    private final List<Order> sameOrders = new ArrayList<>();
    OrderDTO orderDTO;
    double searchDistance;

    public Order(OrderDTO orderDTO) {
      this.orderDTO = orderDTO;
    }

    @Override
    public double getX() {
      return orderDTO.getLatitude();
    }

    @Override
    public double getY() {
      return orderDTO.getLongitude();
    }

    @Override
    public String getId() {
      return orderDTO.getOrderId();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getSameCoordPayload() {
      return (List<T>) sameOrders;
    }

    @Override
    public double getSearchDistance() {
      return searchDistance;
    }

    @Override
    public void setSearchDistance(double distance) {
      this.searchDistance = distance;
    }

    @Override
    public String toString() {
      return "Order{" + "orderDTO=" + orderDTO + ", searchDistance=" + searchDistance + '}';
    }
  }

  /**
   * Internal wrapper for {@link VehicleDTO} that implements {@link Coordinated} for use in the
   * KD-tree spatial index.
   */
  private static class Vehicle implements Coordinated {
    private final List<Vehicle> sameVehicles = new ArrayList<>();
    VehicleDTO vehicleDTO;
    int assignedCapacity;
    double searchDistance;
    double totalDistance;

    public Vehicle(VehicleDTO vehicleDTO) {
      this.vehicleDTO = vehicleDTO;
    }

    @Override
    public double getX() {
      return vehicleDTO.getCurrentLatitude();
    }

    @Override
    public double getY() {
      return vehicleDTO.getCurrentLongitude();
    }

    @Override
    public String getId() {
      return vehicleDTO.getVehicleId();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getSameCoordPayload() {
      return (List<T>) sameVehicles;
    }

    @Override
    public double getSearchDistance() {
      return searchDistance;
    }

    @Override
    public void setSearchDistance(double distance) {
      this.searchDistance = distance;
    }

    @Override
    public String toString() {
      return "Vehicle{"
          + "vehicleDTO="
          + vehicleDTO
          + ", assignedCapacity="
          + assignedCapacity
          + ", searchDistance="
          + searchDistance
          + ", totalDistance="
          + totalDistance
          + '}';
    }
  }
}
