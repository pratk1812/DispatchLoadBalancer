package com.finan.DispatchLoadBalancer.model.dto;

import java.util.List;

public class DispatchPlanDTO {
    private String vehicleId;
    private int totalLoad;
    private String totalDistance;
    private List<OrderDTO> assignedOrders;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getTotalLoad() {
        return totalLoad;
    }

    public void setTotalLoad(int totalLoad) {
        this.totalLoad = totalLoad;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public List<OrderDTO> getAssignedOrders() {
        return assignedOrders;
    }

    public void setAssignedOrders(List<OrderDTO> assignedOrders) {
        this.assignedOrders = assignedOrders;
    }

    @Override
    public String toString() {
        return "DispatchPlan{" +
                "vehicleId='" + vehicleId + '\'' +
                ", totalLoad=" + totalLoad +
                ", totalDistance='" + totalDistance + '\'' +
                ", assignedOrders=" + assignedOrders +
                '}';
    }
}
