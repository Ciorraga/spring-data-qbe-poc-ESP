package com.pocsma.qbe.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
}
