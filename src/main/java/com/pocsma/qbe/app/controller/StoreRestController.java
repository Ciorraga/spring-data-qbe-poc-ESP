package com.pocsma.qbe.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pocsma.qbe.app.model.Store;
import com.pocsma.qbe.app.repository.StoreRepository;

@RestController
public class StoreRestController {

	@Autowired
	private StoreRepository storeRepository;

	@GetMapping("/stores")
	public ResponseEntity<List<Store>> getAllStores() {
		List<Store> store = storeRepository.findAll();
		
		return !store.isEmpty() ? new ResponseEntity<>(store, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
