package com.finan.DispatchLoadBalancer.service;

import com.finan.DispatchLoadBalancer.constant.Priority;
import com.finan.DispatchLoadBalancer.model.dto.DispatchPlanDTO;
import com.finan.DispatchLoadBalancer.model.dto.OrderDTO;
import com.finan.DispatchLoadBalancer.model.dto.VehicleDTO;
import com.finan.DispatchLoadBalancer.persistence.enitity.OrderEntity;
import com.finan.DispatchLoadBalancer.persistence.enitity.VehicleEntity;
import com.finan.DispatchLoadBalancer.persistence.mapper.OrderMapper;
import com.finan.DispatchLoadBalancer.persistence.mapper.VehicleMapper;
import com.finan.DispatchLoadBalancer.persistence.repository.OrderRepository;
import com.finan.DispatchLoadBalancer.persistence.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DispatchPlanServiceTest {

  @Mock private OrderRepository orderRepository;
  @Mock private VehicleRepository vehicleRepository;
  @Mock private OrderMapper orderMapper;
  @Mock private VehicleMapper vehicleMapper;
  @InjectMocks private DispatchPlanService dispatchPlanService;

  @Test
  void assign() {}

  @Test
  void whenNoVehicleCanCarryOrder_assignReturnsEmpty_andDeletesAll() {
    OrderDTO oDto = new OrderDTO();
    oDto.setPackageWeight(100);
    oDto.setPriority(Priority.HIGH);
    when(orderRepository.findAll()).thenReturn(List.of(new OrderEntity()));
    when(orderMapper.toDto(any(OrderEntity.class))).thenReturn(oDto);

    VehicleDTO vDto = new VehicleDTO();
    vDto.setVehicleId("V1");
    vDto.setCapacity(50);
    when(vehicleRepository.findAll()).thenReturn(List.of(new VehicleEntity()));
    when(vehicleMapper.toDto(any(VehicleEntity.class))).thenReturn(vDto);

    // 3) The tree returns that one vehicle

    // Act
    List<DispatchPlanDTO> plans = dispatchPlanService.assign();

    // Assert
    assertTrue(plans.isEmpty(), "No vehicle had enough slack so result should be empty");

    // verify cleanup calls
    verify(orderRepository).deleteAll();
    verify(vehicleRepository).deleteAll();
  }

  @Test
  void whenSlackDominates_scoreTrue_nearestVehicleIsAssigned() {
    // Build a single order weight=3, priority=1
    OrderDTO oDto = new OrderDTO();
    oDto.setPackageWeight(3);
    oDto.setPriority(Priority.HIGH);
    oDto.setLatitude(21.1958);
    oDto.setLongitude(79.1282);
    when(orderRepository.findAll()).thenReturn(List.of(new OrderEntity()));
    when(orderMapper.toDto(any(OrderEntity.class))).thenReturn(oDto);

    // Two vehicles:
    VehicleEntity vehicleEntity1 = new VehicleEntity();
    vehicleEntity1.setId(1L);
    vehicleEntity1.setVehicleId("V1");
    VehicleEntity vehicleEntity2 = new VehicleEntity();
    vehicleEntity2.setId(2L);
    vehicleEntity2.setVehicleId("V2");

    VehicleDTO v1Dto = new VehicleDTO();
    v1Dto.setVehicleId("V1");
    v1Dto.setCapacity(50);
    v1Dto.setCurrentLatitude(21.0758);
    v1Dto.setCurrentLongitude(79.0282);
    VehicleDTO v2Dto = new VehicleDTO();
    v2Dto.setVehicleId("V2");
    v2Dto.setCapacity(8);
    v2Dto.setCurrentLatitude(21.1758);
    v2Dto.setCurrentLongitude(79.0382);

    when(vehicleRepository.findAll()).thenReturn(List.of(vehicleEntity1, vehicleEntity2));
    when(vehicleMapper.toDto(vehicleEntity1)).thenReturn(v1Dto);
    when(vehicleMapper.toDto(vehicleEntity2)).thenReturn(v2Dto);

    // Act
    List<DispatchPlanDTO> plans = dispatchPlanService.assign();

    // Assert: the “nearest vehicle” (v1) wins because
    // costSlack = 1−(5/7)=~0.286, costDist=5/10=0.5 → 0.4·0.286<0.6·0.5
    assertEquals(1, plans.size());
    DispatchPlanDTO plan = plans.get(0);
    assertEquals("V2", plan.getVehicleId());
    assertEquals("9.592518362371141", plan.getTotalDistance());  // v1.totalDistance == v1.searchDistance
    assertEquals(3, plan.getTotalLoad());
    assertEquals(List.of(oDto), plan.getAssignedOrders());

    verify(orderRepository).deleteAll();
    verify(vehicleRepository).deleteAll();
  }



}