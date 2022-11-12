package com.pocsma.qbe.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.criteria.Predicate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
public class Sale implements Serializable{
	private static final long serialVersionUID = -335857734346264194L;
	
	@Id	
	private String saleId;
	
	private BigDecimal amount;
	
	private LocalDateTime dateSale;
	
	@ManyToOne()
	@JoinColumn(name = "product_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Product product;
	
	@ManyToOne()
	@JoinColumn(name = "store_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Store store;
	
	@ManyToOne()
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "sale")
    private List<OrderStatus> orderStatus;
	
	public static Specification<Sale> buildPredicate(Example<Sale> example){
		return (root, query, builder) -> {
			List<Predicate> predicateList  = new ArrayList<>();
			predicateList.add(builder.and(QueryByExamplePredicateBuilder.getPredicate(root, builder, example)));
			predicateList.add(builder.greaterThan(root.get("amount"), new BigDecimal("3000")));
			return builder.and(predicateList.toArray(new Predicate[predicateList.size()]));
		};
	}
	
}
