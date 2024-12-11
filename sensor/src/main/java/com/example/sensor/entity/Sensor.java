package com.example.sensor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

@Entity
@Table( name = "sensor")
public class Sensor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@NotNull
	private Integer sensorId;
	
	@Column
	@NotNull
	private String sensorName;
	
	@Column
	@NotNull
	private String sensorTypeName;
	
	@Column
	@NotNull
	private String unitsOfMeasure;
	
	@Column
	@NotNull
	private String locationName;
	
	
	public Sensor(String sensorName, String sensorTypeName, String unitsOfMeasure, String locationName) {
		super();
		this.sensorName = sensorName;
		this.sensorTypeName = sensorTypeName;
		this.unitsOfMeasure = unitsOfMeasure;
		this.locationName = locationName;
	}

	public Sensor() {
	}

	public Integer getSensorId() {
		return sensorId;
	}

	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public String getSensorTypeName() {
		return sensorTypeName;
	}

	public void setSensorTypeName(String sensorTypeName) {
		this.sensorTypeName = sensorTypeName;
	}

	public String getUnitsOfMeasure() {
		return unitsOfMeasure;
	}

	public void setUnitsOfMeasure(String unitsOfMeasure) {
		this.unitsOfMeasure = unitsOfMeasure;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

    @Override
    public String toString() {
        return "Sensor [sensorId=" + sensorId + ", sensorName=" + sensorName + ", sensorTypeName=" + sensorTypeName
                + ", unitsOfMeasure=" + unitsOfMeasure + ", locationName=" + locationName + "]";
    }

	
	
}
