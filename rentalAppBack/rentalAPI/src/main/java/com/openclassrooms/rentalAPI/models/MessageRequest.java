package com.openclassrooms.rentalAPI.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageRequest {
	@JsonProperty("rental_id")
    private Integer rentalId;
	@JsonProperty("user_id")
    private Integer userId;
    private String message;
    
    
    
	public Integer getRentalId() {
		return rentalId;
	}
	public void setRentalId(Integer rentalId) {
		this.rentalId = rentalId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

    
}