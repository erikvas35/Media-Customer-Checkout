package mediaRentalManager;

public class MovieRental extends Media {
	public String ratings;
	
	public MovieRental(String title, int copiesAvailable, String ratings) {
		super(title, copiesAvailable);
		this.ratings = ratings;
	}

	public void setRatings(String ratings) {
		if(ratings.equals("PG") != true || ratings.equals("R") != true || ratings.equals("NR") != true) {
			throw new IllegalArgumentException ("Ineligible Rating");
		}
		
		this.ratings = ratings;
	}
	
	public String getRatings() {
		return ratings;
	}
	
	@Override
	public String toString() {
		String info = "";
		info += "Title: " + title + ", Copies Available: " + copiesAvailable + ", Rating: " + ratings;
		return info;
	}
}
