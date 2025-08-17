package com.finan.DispatchLoadBalancer.controller;

import com.finan.DispatchLoadBalancer.constant.Messages;
import com.finan.DispatchLoadBalancer.model.dto.DispatchPlanDTO;
import com.finan.DispatchLoadBalancer.model.request.OrderRequest;
import com.finan.DispatchLoadBalancer.model.request.VehicleRequest;
import com.finan.DispatchLoadBalancer.model.response.ApiResponse;
import com.finan.DispatchLoadBalancer.model.response.DispatchPlanResponse;
import com.finan.DispatchLoadBalancer.service.DispatchLoadBalancerService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing dispatch-related operations, including the ingestion of delivery orders,
 * vehicle details, and dispatch planning data.
 *
 * <p>This controller serves as the entry point for dispatch-related API requests. It performs request validation
 * and delegates persistence and business logic to the {@link DispatchLoadBalancerService}.</p>
 *
 * <p><strong>Exposed Endpoints:</strong></p>
 * <ul>
 *   <li><code>POST /api/dispatch/orders</code> — Accepts and persists delivery order data</li>
 *   <li><code>POST /api/dispatch/vehicles</code> — Accepts and persists vehicle detail data</li>
 *   <li><code>GET /api/dispatch/plan</code> — Retrieves the current dispatch plan</li>
 * </ul>
 *
 * @author Pratyush
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
     * Handles the ingestion of delivery orders.
     * <p>Validates the incoming {@link OrderRequest} and delegates persistence to the service layer.</p>
     *
     * @param orderRequest Request payload containing a list of {@link OrderRequest} objects.
     * @return {@link ResponseEntity} containing a success message upon successful persistence.
     * @throws IllegalArgumentException if the order list is empty.
     */
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse> saveOrders(@RequestBody OrderRequest orderRequest) {
        LOGGER.info("Invoked saveOrders controller with {} orders.", orderRequest.getOrders().size());
        Assert.notEmpty(orderRequest.getOrders(), Messages.ORDER_REQUEST_IS_EMPTY);
        dispatchLoadBalancerService.saveOrders(orderRequest.getOrders());
        return ResponseEntity.ok(new ApiResponse(Messages.DELIVERY_ORDERS_ACCEPTED));
    }

    /**
     * Handles the ingestion of vehicle details.
     * <p>Validates the incoming {@link VehicleRequest} and delegates persistence to the service layer.</p>
     *
     * @param vehicleRequest Request payload containing a list of {@link VehicleRequest} objects.
     * @return {@link ResponseEntity} containing a success message upon successful persistence.
     * @throws IllegalArgumentException if the vehicle list is empty.
     */
    @PostMapping("/vehicles")
    public ResponseEntity<ApiResponse> saveVehicles(@RequestBody VehicleRequest vehicleRequest) {
        LOGGER.info("Invoked saveVehicles controller with {} vehicles.", vehicleRequest.getVehicles().size());
        Assert.notEmpty(vehicleRequest.getVehicles(), Messages.VEHICLE_REQUEST_IS_EMPTY);
        dispatchLoadBalancerService.saveVehicles(vehicleRequest.getVehicles());
        return ResponseEntity.ok(new ApiResponse(Messages.VEHICLE_DETAILS_ACCEPTED));
    }

    /**
     * Retrieves the current dispatch plan.
     * <p>Fetches a list of {@link DispatchPlanDTO} objects from the service layer.</p>
     *
     * @return {@link ResponseEntity} containing the dispatch plan data.
     */
    @GetMapping("/plan")
    public ResponseEntity<DispatchPlanResponse> getPlan() {
        LOGGER.info("Invoked getPlan controller.");
        List<DispatchPlanDTO> dispatchPlanDTOS = dispatchLoadBalancerService.getPlan();
        return ResponseEntity.ok(new DispatchPlanResponse(dispatchPlanDTOS));
    }

}
