package com.pocsma.qbe.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable{	
	private static final long serialVersionUID = 2214981045140252170L;
	
	@Id	
	private Integer userId;
	
	private String name;
}
