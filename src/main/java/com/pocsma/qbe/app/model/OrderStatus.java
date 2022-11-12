package com.pocsma.qbe.app.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
public class OrderStatus implements Serializable{
	private static final long serialVersionUID = 5497999890177664882L;

	@Id
	private String orderStatusId;
	
	LocalDate updateAt;
	
	@JsonBackReference
	@ManyToOne()
	@JoinColumn(name = "sale_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Sale sale;
	
	@ManyToOne()
	@JoinColumn(name = "status_name_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private StatusName statusName;		
}
