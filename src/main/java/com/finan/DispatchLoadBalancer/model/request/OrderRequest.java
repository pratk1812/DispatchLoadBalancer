package com.finan.DispatchLoadBalancer.model.request;

import com.finan.DispatchLoadBalancer.model.dto.OrderDTO;
import java.util.List;

public class OrderRequest {
  private List<OrderDTO> orders;

  public OrderRequest() {}

  public OrderRequest(List<OrderDTO> orders) {
    this.orders = orders;
  }

  public List<OrderDTO> getOrders() {
    return orders;
  }

  public void setOrders(List<OrderDTO> orders) {
    this.orders = orders;
  }

  @Override
  public String toString() {
    return "OrderRequest{" + "orders=" + orders + '}';
  }
}
