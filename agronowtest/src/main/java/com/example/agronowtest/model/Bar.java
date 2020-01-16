package com.example.agronowtest.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Bar implements Serializable {

	private static final long serialVersionUID = -6156959613548625127L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long barId;
	
	private String barName;
	private String barAddress;
	
	@Column(unique=true)
	private String barCoordinates;
	
	public Long getBarId() {
		return barId;
	}
	public void setBarId(Long id) {
		this.barId = id;
	}
	
	public String getBarName() {
		return barName;
	}
	public void setBarName(String name) {
		this.barName = name;
	}
	
	public String getBarAddress() {
		return barAddress;
	}
	public void setBarAddress(String address) {
		this.barAddress = address;
	}
	
	public String getBarCoordinates() {
		return barCoordinates;
	}
	public void setBarCoordinates(String coordinates) {
		this.barCoordinates = coordinates;
	}
	
}
