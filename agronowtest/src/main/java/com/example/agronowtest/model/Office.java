package com.example.agronowtest.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Office implements Serializable {

	private static final long serialVersionUID = -8574699109540885192L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long officeId;
	
	private String officeName;
	private String officeAddress;
	private String officeCoordinates;
	
	public Long getOfficeId() {
		return officeId;
	}
	public void setOfficeId(Long id) {
		this.officeId = id;
	}
	
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String name) {
		this.officeName = name;
	}
	
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String address) {
		this.officeAddress = address;
	}
	
	public String getOfficeCoordinates() {
		return officeCoordinates;
	}
	public void setOfficeCoordinates(String coordinates) {
		this.officeCoordinates = coordinates;
	}
	
}
