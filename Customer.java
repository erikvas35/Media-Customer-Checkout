package mediaRentalManager;

import java.util.ArrayList;

public class Customer {
	public String name;
	public String address;
	public String plan;
	ArrayList <String> rented = new ArrayList<>(); 
	ArrayList <String> queued = new ArrayList<>();
	
	public Customer (String name, String address, String plan) {
		this.name = name;
		this.address = address;
		this.plan = plan;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setPlan(String plan) {
		this.plan = plan;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getPlan() {
		return plan;
	}
	
	public void addRented(String title) {
		rented.add(title);
	}
	
	public void addQueued(String title) {
		queued.add(title);
	}

	public ArrayList<String> getRented() {
	    return rented;
	}
	
	public ArrayList<String> getQueued() {
	    return queued;
	}
	
	public void removeQueued(String title) {
		queued.remove(title);
	}
	
	public int getTotalRented() {
		int rentedTotal = 0;
		rentedTotal = rented.size();
		return rentedTotal;
	}
	
	public void moveToQueue(int index) {
		rented.add(queued.get(index));
		queued.remove(index);
	}
	
	public void removeLastQueue() {
		queued.remove(0);
	}
	
	public String pickQueued(int index) {
		return queued.get(index);
	}
	
	

	public int getTotalQueued() {
		int info = queued.size();
		return info;
	}
	
	public boolean rentalReturn(String movie) {
		int index = rented.indexOf(movie);
		if(index >= 0) {
			rented.remove(index);
			return true;
		}
		return false;
	}
	
	public String toString() {
		String info = "Name: " + name + ", Address: " + address + ", Plan: " + plan;
		info += "\n";
		info += "Rented: " + rented;
		info += "\n";
		info += "Queue: " + queued;
		
		return info;
	}
	
	
	
}
