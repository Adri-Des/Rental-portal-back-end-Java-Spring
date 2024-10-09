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

@RestController
public class ImageController {
    // Route pour afficher l'image
    @GetMapping("/images/{filename:.+}") // Le regex permet d'accepter les caractères spéciaux
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        // Utilisez le chemin complet en fonction de la structure de votre projet
        Path imagePath = Paths.get("uploads/" + filename);
        Resource resource = new UrlResource(imagePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Déterminez le type de contenu en fonction de l'extension du fichier
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


