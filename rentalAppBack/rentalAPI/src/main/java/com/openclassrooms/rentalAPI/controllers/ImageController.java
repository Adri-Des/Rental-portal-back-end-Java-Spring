package com.openclassrooms.rentalAPI.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class ImageController {
	// Route to display an image
	@Operation(summary = "Display an image for a rental", description = "This route retrieves the path of an image to be associated with a location")
    @GetMapping("/images/{filename:.+}") // Allows handling special characters in the filename
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
    	 // Path of the image
        Path imagePath = Paths.get("uploads/" + filename);
        Resource resource = new UrlResource(imagePath.toUri());

        // Check if the resource exists and is readable; if not, return a 404 NOT FOUND response
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Set the content type based on the file extension
        MediaType mediaType = MediaType.IMAGE_JPEG; // Valeur par défaut
        if (filename.endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                //.header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200") // Autoriser les requêtes CORS
                .body(resource);
    }
}


