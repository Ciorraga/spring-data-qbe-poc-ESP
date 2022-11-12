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
public class City implements Serializable{
	private static final long serialVersionUID = 5264850844662593793L;

	@Id
	private Integer cityId;
	
	private String cityName;	
	
	@ManyToOne()
	@JoinColumn(name = "country_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Country country;
}
