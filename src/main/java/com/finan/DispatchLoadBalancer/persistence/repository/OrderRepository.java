package com.finan.DispatchLoadBalancer.persistence.repository;

import com.finan.DispatchLoadBalancer.persistence.enitity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {}
