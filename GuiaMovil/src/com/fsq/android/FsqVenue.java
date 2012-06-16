package com.fsq.android;

import android.location.Location;

public class FsqVenue {
	public String id;
	public String name;
	public String address;
	public String type;
	public Location location;
	public int direction;
	public int distance;
	public int herenow;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getHerenow() {
		return herenow;
	}
	public void setHerenow(int herenow) {
		this.herenow = herenow;
	}
}