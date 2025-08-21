package com.finan.DispatchLoadBalancer.model.dto;

import com.finan.DispatchLoadBalancer.constant.Priority;

public class OrderDTO {
  private Long id;
  private String orderId;
  private double latitude;
  private double longitude;
  private String address;
  private int packageWeight;
  private Priority priority;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getPackageWeight() {
    return packageWeight;
  }

  public void setPackageWeight(int packageWeight) {
    this.packageWeight = packageWeight;
  }

  public Priority getPriority() {
    return priority;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof OrderDTO orderDTO)) return false;

    return orderId.equals(orderDTO.orderId);
  }

  @Override
  public int hashCode() {
    return orderId.hashCode();
  }

  @Override
  public String toString() {
    return "OrderDTO{"
        + "id="
        + id
        + ", orderId='"
        + orderId
        + '\''
        + ", latitude="
        + latitude
        + ", longitude="
        + longitude
        + ", address='"
        + address
        + '\''
        + ", packageWeight="
        + packageWeight
        + ", priority="
        + priority
        + '}';
  }
}
