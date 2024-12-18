package com.example.shipment_model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

@Entity
@Table( name = "shipment")
public class Shipment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@NotNull
	private Integer shipId;

	@Column
	@NotNull
	private String trackNo;
	
	@Column
	@NotNull
	private String origin;
	
	@Column
	@NotNull
	private String destination;

	@Column
	@NotNull
	private String status;
	

	public Shipment() {
	}

	public Shipment(String trackNo, String origin, String destination, String status) {
		this.trackNo = trackNo;
		this.origin = origin;
		this.destination = destination;
		this.status = status;
	}

	public Integer getShipId() {
		return shipId;
	}

	public void setShipId(Integer shipId) {
		this.shipId = shipId;
	}

	public String getTrackNo() {
		return trackNo;
	}

	public void setTrackNo(String trackNo) {
		this.trackNo = trackNo;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "Shipment [shipId=" + shipId + ", trackNo=" + trackNo + ", origin=" + origin + ", destination="
				+ destination + ", status=" + status + "]";
	}
	
	
}
