package com.openclassrooms.rentalAPI.models;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
public class Users {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	 private String name;
	 private String email;
	 @JsonIgnore
	 private String password;
	 
	 @CreationTimestamp
	 @JsonProperty("created_at")
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
	 private LocalDateTime createdAt;
	 
	 @JsonProperty("updated_at")
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
	 @UpdateTimestamp
	 private LocalDateTime updatedAt;
	 
	 
	/* // Constructeur pour initialiser uniquement l'ID
	public Users(Integer id) {
	    this.id = id;
	}*/
	 
	 
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;	
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String passwordString) {
		this.password = passwordString;
	}
	
	public Integer getId() {
		return id;
	}
	 
	/*  @Override
	    public String toString() {
	        return "Users{" +
	                "id=" + id +
	                ", email='" + email + '\'' +
	                ", name='" + name + '\'' +
	                ", createdAt=" + createdAt +
	                ", updatedAt=" + updatedAt +
	                '}';
	    }*/


}
