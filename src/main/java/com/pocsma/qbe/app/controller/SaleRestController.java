package com.pocsma.qbe.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pocsma.qbe.app.model.Sale;
import com.pocsma.qbe.app.repository.SaleRepository;

@RestController()
public class SaleRestController{
	
	@Autowired
	private SaleRepository saleRepository;

	/**
	 * Return all sales objects list with no filter
	 * @return
	 */
	@GetMapping("/sales")
	public ResponseEntity<List<Sale>> getAllSales() {
		List<Sale> sales = saleRepository.findAll();
		
		return !sales.isEmpty() ? new ResponseEntity<>(sales, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * This controller use QBE to filter by any fild of Sale object with exactly values
	 * @param sale
	 * @return
	 */
	@PostMapping("/searchFullObject")
	public ResponseEntity<List<Sale>> getAllSalesSearch(@RequestBody Sale sale){
		
		List<Sale> sales = saleRepository.findAll(Example.of(sale));
		
		return !sales.isEmpty() 
				? new ResponseEntity<>(sales, HttpStatus.OK) 
						: new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	/**
	 * This controller filter the entry object filtering the property product.name with contains clause
	 * @param sale
	 * @return
	 */	
	@PostMapping("/searchWithProductNameContain")
	public ResponseEntity<List<Sale>> getAllSalesSearchWithProductNameContains(@RequestBody Sale sale){
		
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withMatcher("product.name", match -> match.contains());
//				.withMatcher("product.name", match -> match.endsWith())
//				.withMatcher("product.name", match -> match.startsWith())...;

		List<Sale> sales = saleRepository.findAll(Example.of(sale, matcher));		
		
		return !sales.isEmpty() 
				? new ResponseEntity<>(sales, HttpStatus.OK) 
						: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * QBE don't accept seeks whic are used to find ranges, numbers greather or less than or anidated seeks so,
	 * we could use speceficitaions and mixing this with QBE
	 * @param sale
	 * @return
	 */
	@PostMapping("/searchWithQBEAndSpecification")
	public ResponseEntity<List<Sale>> getAllSalesSearchWithProductNameContainsAndAmountGreater(@RequestBody Sale sale){
		
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withMatcher("product.name", match -> match.contains());

		List<Sale> sales = saleRepository.findAll(Sale.buildPredicate(Example.of(sale, matcher)));		
		
		return !sales.isEmpty() 
				? new ResponseEntity<>(sales, HttpStatus.OK) 
						: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
