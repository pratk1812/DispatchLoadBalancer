package com.finan.DispatchLoadBalancer.model.request;

import com.finan.DispatchLoadBalancer.model.dto.VehicleDTO;
import java.util.List;

public class VehicleRequest {
    private List<VehicleDTO> vehicles;

    public List<VehicleDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleDTO> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public String toString() {
        return "VehicleRequest{" +
                "vehicles=" + vehicles +
                '}';
    }
}
