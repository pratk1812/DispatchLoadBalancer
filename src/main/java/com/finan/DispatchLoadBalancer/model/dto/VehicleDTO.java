package com.finan.DispatchLoadBalancer.model.dto;

public class VehicleDTO {
  private Long id;
  private String vehicleId;
  private int capacity;
  private double currentLatitude;
  private double currentLongitude;
  private String currentAddress;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getVehicleId() {
    return vehicleId;
  }

  public void setVehicleId(String vehicleId) {
    this.vehicleId = vehicleId;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public double getCurrentLatitude() {
    return currentLatitude;
  }

  public void setCurrentLatitude(double currentLatitude) {
    this.currentLatitude = currentLatitude;
  }

  public double getCurrentLongitude() {
    return currentLongitude;
  }

  public void setCurrentLongitude(double currentLongitude) {
    this.currentLongitude = currentLongitude;
  }

  public String getCurrentAddress() {
    return currentAddress;
  }

  public void setCurrentAddress(String currentAddress) {
    this.currentAddress = currentAddress;
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof VehicleDTO that)) return false;

    return vehicleId.equals(that.vehicleId);
  }

  @Override
  public int hashCode() {
    return vehicleId.hashCode();
  }

  @Override
  public String toString() {
    return "VehicleDTO{"
        + "id="
        + id
        + ", vehicleId='"
        + vehicleId
        + '\''
        + ", capacity="
        + capacity
        + ", currentLatitude="
        + currentLatitude
        + ", currentLongitude="
        + currentLongitude
        + ", currentAddress='"
        + currentAddress
        + '\''
        + '}';
  }
}
