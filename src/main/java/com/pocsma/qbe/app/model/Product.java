package com.pocsma.qbe.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Product implements Serializable{
	private static final long serialVersionUID = 2391788910195475031L;
	
	@Id	
	private Integer productId;	
	
	private String name;
}
