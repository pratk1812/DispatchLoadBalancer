package com.finan.DispatchLoadBalancer.persistence.enitity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicles")
public class VehicleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "vehicle_id", nullable = false)
  private String vehicleId;

  @Column(name = "capacity", nullable = false)
  private int capacity;

  @Column(name = "current_latitude", nullable = false)
  private double currentLatitude;

  @Column(name = "current_longitude", nullable = false)
  private double currentLongitude;

  @Column(name = "current_address")
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
    if (!(o instanceof VehicleEntity that)) return false;

    return capacity == that.capacity
        && Double.compare(currentLatitude, that.currentLatitude) == 0
        && Double.compare(currentLongitude, that.currentLongitude) == 0
        && id.equals(that.id)
        && vehicleId.equals(that.vehicleId);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + vehicleId.hashCode();
    result = 31 * result + capacity;
    result = 31 * result + Double.hashCode(currentLatitude);
    result = 31 * result + Double.hashCode(currentLongitude);
    return result;
  }

  @Override
  public String toString() {
    return "VehicleEntity{"
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
