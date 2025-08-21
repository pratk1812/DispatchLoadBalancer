package com.finan.DispatchLoadBalancer.model.response;

import com.finan.DispatchLoadBalancer.model.dto.DispatchPlanDTO;
import java.util.List;

public class DispatchPlanResponse {
  private List<DispatchPlanDTO> dispatchPlan;

  public DispatchPlanResponse(List<DispatchPlanDTO> dispatchPlan) {
    this.dispatchPlan = dispatchPlan;
  }

  public List<DispatchPlanDTO> getDispatchPlan() {
    return dispatchPlan;
  }

  public void setDispatchPlan(List<DispatchPlanDTO> dispatchPlan) {
    this.dispatchPlan = dispatchPlan;
  }

  @Override
  public String toString() {
    return "DispatchPlanResponse{" + "dispatchPlan=" + dispatchPlan + '}';
  }
}
