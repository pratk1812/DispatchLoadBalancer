package com.finan.DispatchLoadBalancer.model.response;

import com.finan.DispatchLoadBalancer.constant.ApiStatus;

public class ApiResponse {
  private String message;
  private String status;

  public ApiResponse() {
  }

  public ApiResponse(String message) {
    this.message = message;
    this.status = ApiStatus.SUCCESS.getValue();
  }

  public ApiResponse(String message, String status) {
    this.message = message;
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "ApiResponse{" +
            "message='" + message + '\'' +
            ", status='" + status + '\'' +
            '}';
  }
}
