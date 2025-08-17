package com.finan.DispatchLoadBalancer.persistence.mapper;

import com.finan.DispatchLoadBalancer.model.dto.VehicleDTO;
import com.finan.DispatchLoadBalancer.persistence.enitity.VehicleEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehicleMapper {
  VehicleEntity toEntity(VehicleDTO vehicleDTO);

  VehicleDTO toDto(VehicleEntity vehicleEntity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  VehicleEntity partialUpdate(VehicleDTO vehicleDTO, @MappingTarget VehicleEntity vehicleEntity);
}
