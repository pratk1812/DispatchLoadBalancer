package com.finan.DispatchLoadBalancer.service;

import com.finan.DispatchLoadBalancer.model.dto.OrderDTO;
import com.finan.DispatchLoadBalancer.model.dto.VehicleDTO;
import com.finan.DispatchLoadBalancer.persistence.enitity.OrderEntity;
import com.finan.DispatchLoadBalancer.persistence.enitity.VehicleEntity;
import com.finan.DispatchLoadBalancer.persistence.mapper.OrderMapper;
import com.finan.DispatchLoadBalancer.persistence.mapper.VehicleMapper;
import com.finan.DispatchLoadBalancer.persistence.repository.OrderRepository;
import com.finan.DispatchLoadBalancer.persistence.repository.VehicleRepository;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Service responsible for persisting dispatch-related data such as orders and vehicles. Acts as a
 * bridge between DTOs received from external layers and entities stored in the database.
 *
 * <p>This service uses mappers to convert DTOs to entities and repositories to persist them. It is
 * typically invoked during dispatch initialization or bulk data ingestion.
 *
 * @author Pratyush
 */
@Service
public class VehicleOrderService {

  private static final Logger LOGGER = LogManager.getLogger(VehicleOrderService.class);

  private final OrderRepository orderRepository;
  private final VehicleRepository vehicleRepository;
  private final OrderMapper orderMapper;
  private final VehicleMapper vehicleMapper;

  /**
   * Constructs a new {@code DispatchLoadBalancerService} with required dependencies.
   *
   * @param orderRepository Repository for persisting order entities.
   * @param vehicleRepository Repository for persisting vehicle entities.
   * @param orderMapper Mapper to convert {@code OrderDTO} to {@code OrderEntity}.
   * @param vehicleMapper Mapper to convert {@code VehicleDTO} to {@code VehicleEntity}.
   */
  @Autowired
  public VehicleOrderService(
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
   * Saves a list of orders to the database.
   *
   * @param orderDTOS List of {@code OrderDTO} objects to be persisted.
   */
  public void saveOrders(@NonNull List<OrderDTO> orderDTOS) {
    LOGGER.info("Received {} orders for persistence.", orderDTOS.size());
    List<OrderEntity> orderEntities = orderDTOS.stream().map(orderMapper::toEntity).toList();
    orderRepository.saveAll(orderEntities);
    LOGGER.debug("Successfully saved {} order entities.", orderEntities.size());
  }

  /**
   * Saves a list of vehicles to the database.
   *
   * @param vehicleDTOS List of {@code VehicleDTO} objects to be persisted.
   */
  public void saveVehicles(@NonNull List<VehicleDTO> vehicleDTOS) {
    LOGGER.info("Received {} vehicles for persistence.", vehicleDTOS.size());
    List<VehicleEntity> vehicleEntities =
        vehicleDTOS.stream().map(vehicleMapper::toEntity).toList();
    vehicleRepository.saveAll(vehicleEntities);
    LOGGER.debug("Successfully saved {} vehicle entities.", vehicleEntities.size());
  }
}
