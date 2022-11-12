package com.pocsma.qbe.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pocsma.qbe.app.model.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long>{

}
