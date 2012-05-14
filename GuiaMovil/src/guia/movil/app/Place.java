package guia.movil.app;

public class Place {
	private String name;
	private String latitude;
	private String longitude;
	private String category;
	private String subcategory;
	private String description;
	private Contact contact;
	
	public Place(String name, String latitude, String longitude, String category, String subcategory, String description, Contact contact) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.category = category;
		this.subcategory = subcategory;
		this.description = description;
		this.contact = contact;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getSubcategory() {
		return subcategory;
	}
	
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Contact getContact() {
		return contact;
	}
	
	public void setContact(Contact contact) {
		this.contact = contact;
	}
}
