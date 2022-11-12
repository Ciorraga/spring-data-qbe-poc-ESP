package com.pocsma.qbe.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocsma.qbe.app.model.OrderStatus;
import com.pocsma.qbe.app.repository.OrderStatusRepository;

@RestController
public class OrderStatusRestController {
	
	@Autowired
	private OrderStatusRepository orderstatusRepository;

	@GetMapping("/orderstatus")
	public ResponseEntity<List<OrderStatus>> getAllOrders() {
		List<OrderStatus> orderstatus = orderstatusRepository.findAll();
		
		return !orderstatus.isEmpty() ? new ResponseEntity<>(orderstatus, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
