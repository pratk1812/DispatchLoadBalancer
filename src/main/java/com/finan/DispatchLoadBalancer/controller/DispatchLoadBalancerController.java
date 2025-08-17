package com.finan.DispatchLoadBalancer.controller;

import com.finan.DispatchLoadBalancer.constant.Messages;
import com.finan.DispatchLoadBalancer.model.dto.DispatchPlanDTO;
import com.finan.DispatchLoadBalancer.model.request.OrderRequest;
import com.finan.DispatchLoadBalancer.model.request.VehicleRequest;
import com.finan.DispatchLoadBalancer.model.response.ApiResponse;
import com.finan.DispatchLoadBalancer.model.response.DispatchPlanResponse;
import com.finan.DispatchLoadBalancer.service.DispatchLoadBalancerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for handling dispatch-related operations such as saving delivery orders and vehicle details.
 * <p>
 * This controller acts as an entry point for dispatch data ingestion, validating incoming requests and delegating
 * persistence logic to the {@link DispatchLoadBalancerService}.
 * </p>
 *
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li><code>POST /api/dispatch/orders</code> — Save delivery orders</li>
 *   <li><code>POST /api/dispatch/vehicles</code> — Save vehicle details</li>
 * </ul>
 *
 */
@RestController
@RequestMapping("/api/dispatch")
public class DispatchLoadBalancerController {
    private static final Logger LOGGER = LogManager.getLogger(DispatchLoadBalancerController.class);

    private final DispatchLoadBalancerService dispatchLoadBalancerService;

    /**
     * Constructs a new {@code DispatchLoadBalancerController} with the required service dependency.
     *
     * @param dispatchLoadBalancerService Service responsible for dispatch data persistence.
     */
    public DispatchLoadBalancerController(DispatchLoadBalancerService dispatchLoadBalancerService) {
        this.dispatchLoadBalancerService = dispatchLoadBalancerService;
    }

    /**
     * Saves a list of delivery orders received in the request body.
     *
     * @param orderRequest Request containing a list of {@code OrderDTO} objects.
     * @return {@code ResponseEntity} with success message upon successful persistence.
     * @throws IllegalArgumentException if the order list is empty.
     */
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse> saveOrders(@RequestBody OrderRequest orderRequest) {
        Assert.notEmpty(orderRequest.getOrders(), Messages.ORDER_REQUEST_IS_EMPTY);
        dispatchLoadBalancerService.saveOrders(orderRequest.getOrders());
        return ResponseEntity.ok(new ApiResponse(Messages.DELIVERY_ORDERS_ACCEPTED));
    }

    /**
     * Saves a list of vehicle details received in the request body.
     *
     * @param vehicleRequest Request containing a list of {@code VehicleDTO} objects.
     * @return {@code ResponseEntity} with success message upon successful persistence.
     * @throws IllegalArgumentException if the vehicle list is empty.
     */
    @PostMapping("/vehicles")
    public ResponseEntity<ApiResponse> saveVehicles(@RequestBody VehicleRequest vehicleRequest) {
        Assert.notEmpty(vehicleRequest.getVehicles(), Messages.VEHICLE_REQUEST_IS_EMPTY);
        dispatchLoadBalancerService.saveVehicles(vehicleRequest.getVehicles());
        return ResponseEntity.ok(new ApiResponse(Messages.VEHICLE_DETAILS_ACCEPTED));

    }

    @GetMapping("/plan")
    public ResponseEntity<DispatchPlanResponse> getPlan() {
        List<DispatchPlanDTO> dispatchPlanDTOS = dispatchLoadBalancerService.getPlan();
        return ResponseEntity.ok(new DispatchPlanResponse(dispatchPlanDTOS));
    }
}
