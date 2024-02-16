package com.jchojdak.restaurant.controller;

import com.jchojdak.restaurant.model.Photo;
import com.jchojdak.restaurant.service.IPhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {
    private final IPhotoService photoService;


    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Create new photo", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> createPhoto(@RequestParam("file") MultipartFile file) {
        try {
            byte[] imageArr = file.getBytes();

            Photo photo = new Photo();
            photo.setImageData(imageArr);
            return ResponseEntity.ok(photoService.savePhoto(photo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get photo by ID")
    public ResponseEntity<byte[]> getPhotoById(@PathVariable Long id) {
        try {
            Photo photo = photoService.getPhotoById(id);
            if (photo != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(photo.getImageData());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete photo by id", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deletePhotoById(@PathVariable Long id) {
        try {
            photoService.deletePhotoById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
