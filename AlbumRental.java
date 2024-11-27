package mediaRentalManager;

public class AlbumRental extends Media {
	public String artist;
	public String songs;
	
	public AlbumRental(String title, int copiesAvailable, String artist, String songs) {
		super(title, copiesAvailable);
		this.artist = artist;
		this.songs = songs;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public void setSongs(String songs) {
		this.songs = songs;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getSongs() {
		return songs;
	}
	
	@Override
	public String toString() {
		String info = "";
		info += "Title: " + title + ", Copies Available: " + copiesAvailable + ", Artist: " + artist + ", Songs: " + songs;
		return info;
	}
}
