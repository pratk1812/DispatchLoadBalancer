package com.finan.DispatchLoadBalancer.service;

import com.finan.DispatchLoadBalancer.model.dto.OrderDTO;
import com.finan.DispatchLoadBalancer.model.dto.VehicleDTO;
import com.finan.DispatchLoadBalancer.persistence.enitity.OrderEntity;
import com.finan.DispatchLoadBalancer.persistence.enitity.VehicleEntity;
import com.finan.DispatchLoadBalancer.persistence.mapper.OrderMapper;
import com.finan.DispatchLoadBalancer.persistence.mapper.VehicleMapper;
import com.finan.DispatchLoadBalancer.persistence.repository.OrderRepository;
import com.finan.DispatchLoadBalancer.persistence.repository.VehicleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private VehicleMapper vehicleMapper;
    @InjectMocks
    private VehicleOrderService vehicleOrderService;

    @Test
    @DisplayName("Should map OrderDTOs to OrderEntities and persist them")
    void saveOrders_shouldMapAndPersistEntities() {
        // Arrange
        OrderDTO dto1 = new OrderDTO();
        OrderDTO dto2 = new OrderDTO();
        List<OrderDTO> dtoList = List.of(dto1, dto2);

        OrderEntity entity1 = new OrderEntity();
        OrderEntity entity2 = new OrderEntity();
        List<OrderEntity> entityList = List.of(entity1, entity2);

        when(orderMapper.toEntity(dto1)).thenReturn(entity1);
        when(orderMapper.toEntity(dto2)).thenReturn(entity2);

        // Act
        vehicleOrderService.saveOrders(dtoList);

        // Assert
        verify(orderMapper, times(1)).toEntity(dto1);
        verify(orderMapper, times(1)).toEntity(dto2);
        verify(orderRepository, times(1)).saveAll(entityList);
    }

    @Test
    @DisplayName("Should handle empty OrderDTO list without errors")
    void saveOrders_shouldHandleEmptyListGracefully() {
        // Arrange
        List<OrderDTO> emptyList = List.of();

        // Act
        vehicleOrderService.saveOrders(emptyList);

        // Assert
        verify(orderMapper, never()).toEntity(any());
        verify(orderRepository, times(1)).saveAll(anyList());
    }


    @Test
    @DisplayName("Should map VehicleDTOs to VehicleEntities and persist them")
    void saveVehicles_shouldMapAndPersistEntities() {
        // Arrange
        VehicleDTO dto1 = new VehicleDTO();
        VehicleDTO dto2 = new VehicleDTO();
        List<VehicleDTO> dtoList = List.of(dto1, dto2);

        VehicleEntity entity1 = new VehicleEntity();
        VehicleEntity entity2 = new VehicleEntity();
        List<VehicleEntity> entityList = List.of(entity1, entity2);

        when(vehicleMapper.toEntity(dto1)).thenReturn(entity1);
        when(vehicleMapper.toEntity(dto2)).thenReturn(entity2);

        // Act
        vehicleOrderService.saveVehicles(dtoList);

        // Assert
        verify(vehicleMapper, times(1)).toEntity(dto1);
        verify(vehicleMapper, times(1)).toEntity(dto2);
        verify(vehicleRepository, times(1)).saveAll(entityList);
    }

    @Test
    @DisplayName("Should handle empty VehicleDTO list without errors")
    void saveVehicles_shouldHandleEmptyListGracefully() {
        // Arrange
        List<VehicleDTO> emptyList = List.of();

        // Act
        vehicleOrderService.saveVehicles(emptyList);

        // Assert
        verify(vehicleMapper, never()).toEntity(any());
        verify(vehicleRepository, times(1)).saveAll(anyList());
    }


}