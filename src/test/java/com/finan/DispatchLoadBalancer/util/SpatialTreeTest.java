package com.finan.DispatchLoadBalancer.util;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finan.DispatchLoadBalancer.model.dto.OrderDTO;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SpatialTreeTest {

    private Coordinated genOrderCoordinate(OrderDTO orderDTO) {
        return new Coordinated() {
            final OrderDTO order = orderDTO;
            final List<OrderDTO> samePayloads = new ArrayList<>();

            @Override
            public double getX() {
                return orderDTO.getLatitude();
            }

            @Override
            public double getY() {
                return orderDTO.getLongitude();
            }

            @Override
            public String getId() {
                return "";
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T> List<T> getSameCoordPayload() {
                return (List<T>) samePayloads;
            }

            @Override
            public void setSearchDistance(double distance) {

            }

            @Override
            public double getSearchDistance() {
                return 0;
            }

            @Override
            public boolean isSameCoord(Coordinated o) {
                return Coordinated.super.isSameCoord(o);
            }

            @Override
            public void addSameCoordPayload(Coordinated o) {
                Coordinated.super.addSameCoordPayload(o);
            }
        };
    }

    private Coordinated getCoordinate(double x, double y) {
        return new Coordinated() {
            @Override
            public double getX() {
                return x;
            }

            @Override
            public double getY() {
                return y;
            }

            @Override
            public String getId() {
                return "";
            }

            @Override
            public <T> List<T> getSameCoordPayload() {
                throw new RuntimeException("not required");
            }

            @Override
            public void setSearchDistance(double distance) {

            }

            @Override
            public double getSearchDistance() {
                return 0;
            }

            @Override
            public boolean isSameCoord(Coordinated o) {
                return Coordinated.super.isSameCoord(o);
            }

            @Override
            public void addSameCoordPayload(Coordinated o) {
                Coordinated.super.addSameCoordPayload(o);
            }
        };
    }
    @Test
    void createSimpleSTree() throws IOException {
    OrderDTO[] orderDTOS =
        new ObjectMapper()
            .readValue(
                new File(
                    "C:\\Users\\pratk\\IdeaProjects\\FreightFoxAssignments\\DispatchLoadBalancer\\.idea\\orders.json"),
                OrderDTO[].class);
        assertEquals(4, orderDTOS.length);

        List<Coordinated> ordersArr = Arrays.stream(orderDTOS)
                .map(this::genOrderCoordinate)
                .toList();

        List<Coordinated> orders = new ArrayList<>(ordersArr);

        SpatialTree<Coordinated> tree = SpatialTree.createSimpleSTree(orders);

        assertNotNull(tree.root);

        List<Coordinated> nodes = tree.findNearest(getCoordinate(28.800,77.200));
        assertEquals(2, nodes.size());

      }

    @Test
    void findNearest() {
      }
}