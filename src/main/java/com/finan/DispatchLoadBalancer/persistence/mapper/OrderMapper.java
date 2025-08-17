package com.finan.DispatchLoadBalancer.persistence.mapper;

import com.finan.DispatchLoadBalancer.model.dto.OrderDTO;
import com.finan.DispatchLoadBalancer.persistence.enitity.OrderEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
  OrderEntity toEntity(OrderDTO orderDTO);

  OrderDTO toDto(OrderEntity orderEntity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  OrderEntity partialUpdate(OrderDTO orderDTO, @MappingTarget OrderEntity orderEntity);
}
