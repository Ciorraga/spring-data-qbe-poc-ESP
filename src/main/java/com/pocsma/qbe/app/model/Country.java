package com.pocsma.qbe.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Country implements Serializable {	
	private static final long serialVersionUID = 8807215633600118386L;
	
	@Id
	private Integer countryId;	
	
	private String countryName;	
}
