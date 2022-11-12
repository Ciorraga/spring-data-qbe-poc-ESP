package com.pocsma.qbe.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;

@Data
@Entity
public class Store implements Serializable{
	private static final long serialVersionUID = 4057063708452463517L;
	
	@Id	
	private Integer storeId;	
	
	private String name;
	
	@ManyToOne()
	@JoinColumn(name = "city_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private City city;
}
