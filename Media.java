package mediaRentalManager;

public class Media {
	public String title;
	public int copiesAvailable = 0;
	
	public Media(String title, int copiesAvailable) {
		this.title = title;
		this.copiesAvailable = copiesAvailable;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setCopies(int copiesAvailable) {
		this.copiesAvailable = copiesAvailable;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public int getCopies() {
		return this.copiesAvailable;
	}
	
	public void removeStock() {
		copiesAvailable--;
	}
	
	public boolean inStock() {
		if(this.copiesAvailable > 0) {
			return true;
		}
		return false;
	}
	
	public void addStock() {
		copiesAvailable++;
	}
	
	public String toString() {
		String info = "";
		info += "Title: " + title + ", Copies Available: " + copiesAvailable;
		return info;
	}
}


