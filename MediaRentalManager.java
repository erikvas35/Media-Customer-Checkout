package mediaRentalManager;

import java.util.ArrayList;
import java.util.Collections;

public class MediaRentalManager implements MediaRentalManagerInt {

	ArrayList <Customer> customerInfo = new ArrayList<>();
	ArrayList <Media> mediaInfo = new ArrayList<>();
	int limitedValue = 2;
	
	@Override
	public void addCustomer(String name, String address, String plan) {
		Customer customer = new Customer(name, address, plan);
		customerInfo.add(customer);
	}

	@Override
	public void addMovie(String title, int copiesAvailable, String rating) {
		MovieRental movie = new MovieRental(title, copiesAvailable, rating);
		mediaInfo.add(movie);
	}

	@Override
	public void addAlbum(String title, int copiesAvailable, String artist, String songs) {
		AlbumRental album = new AlbumRental(title, copiesAvailable, artist, songs);
		mediaInfo.add(album);
	}

	@Override
	public void setLimitedPlanLimit(int value) {
		this.limitedValue = value;
	}

	@Override
	public String getAllCustomersInfo() {
		String allInfo = "***** Customers' Information *****";
		int index = 0;
		
		ArrayList <String> customerNames = new ArrayList<>();
		
		for(Customer customer : customerInfo) {
			customerNames.add(customer.getName());
		}
		
		Collections.sort(customerNames);
		
		for(String customer : customerNames) {
			index = customerLookUp(customer);
			if(index >= 0) {
				allInfo += "\n";
				allInfo += customerInfo.get(index).toString();
			}
		}
		allInfo += "\n";
		
		return allInfo;
	}

	@Override
	public String getAllMediaInfo() {
		String allInfo = "***** Media Information *****";
		int index = 0;
		
		ArrayList <String> mediaTitles = new ArrayList<>();
		
		for(Media media : mediaInfo) {
			mediaTitles.add(media.getTitle());
		}
		
		Collections.sort(mediaTitles);
		
		for(String title : mediaTitles) {
			index = mediaLookUp(title);
			if(index >= 0) {
				allInfo += "\n";
				allInfo += mediaInfo.get(index).toString();
			}
		}
		allInfo += "\n";
		return allInfo;
	}

	@Override
	public boolean addToQueue(String customerName, String mediaTitle) {
		int customerID = customerLookUp(customerName);
		if(customerID != -1) {
			ArrayList <String> queuedList = customerInfo.get(customerID).getQueued();
			for(String duplicate : queuedList) {
				if(duplicate.equals(mediaTitle)) {
					return false;
				}
			}
			customerInfo.get(customerID).addQueued(mediaTitle);
		}
		else {
			return false;
		}
		return true;
	}

	@Override
	public boolean removeFromQueue(String customerName, String mediaTitle) {
		int customerID = customerLookUp(customerName);
		ArrayList <String> queuedList = customerInfo.get(customerID).getQueued();
		for(String title : queuedList) {
			if(title.equals(mediaTitle)) {
				customerInfo.get(customerID).removeQueued(mediaTitle);
				return true;
			}
		}
		return false;
	}

	@Override
	public String processRequests() {
		//Make a new list of the organized customer names
		ArrayList <String> customerList = organizeCustomers(customerInfo);
		String queueAdded = "";
		int customerAmount = customerList.size();
		int index = 0;
		int totalRent = 0;
		int totalQueued = 0;
		int movieIndex = 0;
		boolean inStock = false;
		int checking = 0;
		
		for(int i = 0; i <= customerAmount - 1; i++) {
			
			index = customerLookUp(customerList.get(i));
			String customerPlan = customerInfo.get(index).getPlan();
			totalQueued = customerInfo.get(index).getTotalQueued();
			String pushedQueued = "";
			
			if(customerPlan.equals("UNLIMITED")) {
				for(int j = 0; j < totalQueued; j++){
					pushedQueued = customerInfo.get(index).pickQueued(checking);
					movieIndex = mediaLookUp(pushedQueued);
					inStock = mediaInfo.get(movieIndex).inStock();
					
					if(mediaVerification(mediaInfo, pushedQueued) == true && inStock == true) {
						queueAdded += "Sending " + pushedQueued + " to " + customerInfo.get(index).getName();
						customerInfo.get(index).moveToQueue(checking);
					}
					if(j != totalQueued || j != customerAmount-1) {
						if(inStock == true) {
							queueAdded += "\n";
						}
					}
					if(movieIndex >= 0 && inStock == true) {
						mediaInfo.get(movieIndex).removeStock();
					}
					else {
						checking++;
					}
				}
			} 
			
			else {
				totalRent = customerInfo.get(index).getTotalRented();
				for(int k = totalRent; k < limitedValue; k++) {
					if(customerInfo.get(index).getTotalQueued() != 0) {
						pushedQueued = customerInfo.get(index).pickQueued(checking);
						movieIndex = mediaLookUp(pushedQueued);
						inStock = mediaInfo.get(movieIndex).inStock();
						if(mediaVerification(mediaInfo, pushedQueued) == true && inStock == true) {
							queueAdded += "Sending " + pushedQueued + " to " + customerInfo.get(index).getName();
							customerInfo.get(index).moveToQueue(checking);
						}
						else {
							queueAdded += pushedQueued +  " not found (Movie not rented)";
							customerInfo.get(index).removeLastQueue();
						}
						if(k != totalRent-1 || k != customerAmount-1) {
							queueAdded += "\n";
						}
						if(movieIndex >= 0 && inStock == true) {
							mediaInfo.get(movieIndex).removeStock();
						}
						else {
							checking++;
						}
					}
				}
			}
		}
		
		return queueAdded;
	}

	@Override
	public boolean returnMedia(String customerName, String mediaTitle) {
		int customerIndex = customerLookUp(customerName);
		int movieIndex = mediaLookUp(mediaTitle);
		if(customerIndex >= 0) {
			if(mediaVerification(mediaInfo, mediaTitle) == true) {
				customerInfo.get(customerIndex).rentalReturn(mediaTitle);
				mediaInfo.get(movieIndex).addStock();
				return true;
			}
		}
		return false;
	}

	@Override
	public ArrayList<String> searchMedia(String title, String rating, String artist, String songs) {
		ArrayList<String> resultsFound = new ArrayList<>();
		String tempTitle = "notHeld";
		String tempRating = "notHeld";
		String tempArtist = "notHeld";
		String tempSongs = "notHeld";
		String[] songsArray;
		String[] tempSongsArray;
		
		ArrayList <Media> organizedList = organizeMedia(mediaInfo);
		
		if(title == null && rating == null && artist == null && songs == null) {
			for(Media media : organizedList) {
				resultsFound.add(media.title);
			}
			return resultsFound;
		}
	
		for(Media media : organizedList) {
			tempTitle = media.getTitle();
			if(media instanceof AlbumRental) {
				tempArtist = ((AlbumRental) media).getArtist();
				tempSongs = ((AlbumRental) media).getSongs();
				
				if(songs != null) {
					tempSongsArray = tempSongs.split(",\\s*");
					songsArray = songs.split(",\\s*");
					
					for (String song : songsArray) {
					    for (String tempSong : tempSongsArray) {
					        if (tempSong.toLowerCase().contains(song.toLowerCase())) {
					            resultsFound.add(tempTitle);
					        }
					    }
					    break;
					}
				}
				
				if(tempTitle.equals(title) || tempArtist.equals(artist) || tempSongs.equals(songs)) {
				   resultsFound.add(tempTitle);
				}
				
			}
			else if(media instanceof MovieRental) {
				tempRating = ((MovieRental) media).getRatings();
				if(tempTitle.equals(title) || tempRating.equals(rating)) {
					resultsFound.add(tempTitle);
				}
			}
		}
		
		return resultsFound;
	}
	
	private ArrayList<Media> organizeMedia(ArrayList<Media> media){
		ArrayList<Media> organizedList = new ArrayList<>();
		ArrayList <String> mediaTitles = new ArrayList<>();
		int index = 0;
		
		for(Media mediaName : media) {
			mediaTitles.add(mediaName.getTitle());
		}
		
		Collections.sort(mediaTitles);
		
		for(String title : mediaTitles) {
			index = mediaLookUp(title);
			if(index >= 0) {
				organizedList.add(mediaInfo.get(index));
			}
		}
		return organizedList;
		
	}
	
	private int customerLookUp(String customerName) {
		for(Customer customer : customerInfo) {
			if(customer.getName().equals(customerName)) {
				return customerInfo.indexOf(customer);
			}
		}
		//Case if customer is not found
		return -1;
	}

	private ArrayList<String> organizeCustomers(ArrayList<Customer> customerList){
		ArrayList<String> customerNames = new ArrayList<>();
		
		for(Customer customer : customerList) {
			customerNames.add(customer.getName());
		}
		
		Collections.sort(customerNames);
		
		return customerNames;
	}
	
	private boolean mediaVerification(ArrayList<Media> mediaList, String media) {
		boolean exist = false;
		
		for(Media mediaCheck : mediaList) {
			if(media.equals(mediaCheck.getTitle())) {
				exist = true;
			}
		}
		
		return exist;
	}
	
	private int mediaLookUp (String media) {
		int index = 0;
		String title = "";
	
		for(Media mediaCheck : mediaInfo) {
			title = mediaCheck.getTitle();
			if(title.equals(media)) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	
}
