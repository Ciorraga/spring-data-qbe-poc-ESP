package com.pocsma.qbe.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class StatusName implements Serializable{
	private static final long serialVersionUID = 5901612175618837975L;

	@Id
	private Integer statusNameId;	
	
	private String statusName;
}
