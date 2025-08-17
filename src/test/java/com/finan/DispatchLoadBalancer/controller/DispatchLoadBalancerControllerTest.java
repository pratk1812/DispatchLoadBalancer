package com.finan.DispatchLoadBalancer.controller;

import com.finan.DispatchLoadBalancer.constant.ApiStatus;
import com.finan.DispatchLoadBalancer.constant.Messages;
import com.finan.DispatchLoadBalancer.model.dto.DispatchPlanDTO;
import com.finan.DispatchLoadBalancer.model.dto.OrderDTO;
import com.finan.DispatchLoadBalancer.model.dto.VehicleDTO;
import com.finan.DispatchLoadBalancer.model.request.OrderRequest;
import com.finan.DispatchLoadBalancer.model.request.VehicleRequest;
import com.finan.DispatchLoadBalancer.model.response.ApiResponse;
import com.finan.DispatchLoadBalancer.model.response.DispatchPlanResponse;
import com.finan.DispatchLoadBalancer.service.DispatchLoadBalancerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DispatchLoadBalancerControllerTest {

    @Mock
    private DispatchLoadBalancerService dispatchLoadBalancerService;
    @InjectMocks
    private DispatchLoadBalancerController dispatchLoadBalancerController;

    @Test
    @DisplayName("Valid OrderRequest should return success response")
    void saveOrders_shouldReturnSuccessResponse() throws Exception {
        OrderDTO order1 = new OrderDTO();
        OrderDTO order2 = new OrderDTO();
        OrderRequest request = new OrderRequest(List.of(order1, order2));

        doNothing().when(dispatchLoadBalancerService).saveOrders(anyList());

        ResponseEntity<ApiResponse> response = dispatchLoadBalancerController.saveOrders(request);

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(ApiStatus.SUCCESS.getValue(), response.getBody().getStatus(), "Expected success status");
        assertEquals(Messages.DELIVERY_ORDERS_ACCEPTED, response.getBody().getMessage(), "Expected success message to match DELIVERY_ORDERS_ACCEPTED");

    }

    @Test
    @DisplayName("Invalid OrderRequest should throw illegal argument exception")
    void saveOrders_shouldReturnBadRequestForEmptyOrders() throws Exception {
        OrderRequest request = new OrderRequest(List.of());
        Executable executable = () -> dispatchLoadBalancerController.saveOrders(request);
        assertThrows(IllegalArgumentException.class, executable, "Expected IllegalArgumentException for empty order list");
    }

    @Test
    @DisplayName("Valid VehicleRequest should return success response")
    void saveVehicles_shouldReturnSuccessResponse() throws Exception {
        VehicleDTO vehicle1 = new VehicleDTO();
        VehicleDTO vehicle2 = new VehicleDTO();
        VehicleRequest request = new VehicleRequest(List.of(vehicle1, vehicle2));


        doNothing().when(dispatchLoadBalancerService).saveVehicles(anyList());

        ResponseEntity<ApiResponse> response = dispatchLoadBalancerController.saveVehicles(request);

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(ApiStatus.SUCCESS.getValue(), response.getBody().getStatus(), "Expected success status");
        assertEquals(Messages.VEHICLE_DETAILS_ACCEPTED, response.getBody().getMessage(),
                "Expected success message to match DELIVERY_VEHICLES_ACCEPTED");

    }

  @Test
  @DisplayName("Invalid OrderRequest should throw illegal argument exception")
  void saveVehicles_shouldReturnBadRequestForEmptyVehicles() throws Exception {
        VehicleRequest request = new VehicleRequest(List.of());

        Executable executable = () -> dispatchLoadBalancerController.saveVehicles(request);

      assertThrows(IllegalArgumentException.class, executable, "Expected IllegalArgumentException for empty vehicle list");
    }

    @Test
    @DisplayName("DispatchPlanDTO plan should be returned")
    void getPlan_shouldReturnDispatchPlanResponse() throws Exception {
        DispatchPlanDTO plan1 = new DispatchPlanDTO(/* sample data */);
        DispatchPlanDTO plan2 = new DispatchPlanDTO(/* sample data */);
        List<DispatchPlanDTO> plans = List.of(plan1, plan2);

        when(dispatchLoadBalancerService.getPlan()).thenReturn(plans);

        ResponseEntity<DispatchPlanResponse> response = dispatchLoadBalancerController.getPlan();
        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertNotNull(response.getBody().getDispatchPlan(), "Dispatch plan should not be null");
        assertEquals(plans, response.getBody().getDispatchPlan(), "Returned dispatch plans should match expected list");

    }


}
