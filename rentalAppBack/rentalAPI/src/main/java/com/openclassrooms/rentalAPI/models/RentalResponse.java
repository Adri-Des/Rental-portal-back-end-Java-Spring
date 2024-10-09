package com.openclassrooms.rentalAPI.models;


import com.fasterxml.jackson.annotation.JsonProperty;

public class RentalResponse {
	
	private Integer id;
    private String name;
    private double surface;
    private double price;
    private String picture;
    private String description;
    private Integer owner_id; 
    @JsonProperty("created_at")
	
    private String createdAt;
    @JsonProperty("updated_at")
	
    private String updatedAt;

    // Constructeur
    public RentalResponse(Integer id, String name, double surface, double price, String picture,
                          String description, Integer owner_id, String createdAt, String updatedAt) {
        this.setId(id);
        this.setName(name);
        this.setSurface(surface);
        this.setPrice(price);
        this.setPicture(picture);
        this.setDescription(description);
        this.setOwner_id(owner_id);
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);

}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSurface() {
		return surface;
	}

	public void setSurface(double surface) {
		this.surface = surface;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(Integer owner_id) {
		this.owner_id = owner_id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
}
