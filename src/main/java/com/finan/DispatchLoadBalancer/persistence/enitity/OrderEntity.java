package com.finan.DispatchLoadBalancer.persistence.enitity;

import com.finan.DispatchLoadBalancer.constant.Priority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class OrderEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "order_id", nullable = false)
  private String orderId;

  @Column(name = "latitude", nullable = false)
  private double latitude;

  @Column(name = "longitude", nullable = false)
  private double longitude;

  @Column(name = "address")
  private String address;

  @Column(name = "package_weight", nullable = false)
  private int packageWeight;

  @Enumerated(EnumType.STRING)
  @Column(name = "priority", nullable = false)
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
    if (!(o instanceof OrderEntity that)) return false;

    return Double.compare(latitude, that.latitude) == 0
        && Double.compare(longitude, that.longitude) == 0
        && packageWeight == that.packageWeight
        && id.equals(that.id)
        && orderId.equals(that.orderId)
        && priority == that.priority;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + orderId.hashCode();
    result = 31 * result + Double.hashCode(latitude);
    result = 31 * result + Double.hashCode(longitude);
    result = 31 * result + packageWeight;
    result = 31 * result + Objects.hashCode(priority);
    return result;
  }

  @Override
  public String toString() {
    return "OrderEntity{"
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
