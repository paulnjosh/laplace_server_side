package com.example.Primarch.Upload;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image-server")
@CrossOrigin(origins = "*")
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file, @RequestParam("desc") String desc) throws IOException {
        String uploadImage = imageService.uploadImageToFileSystem(file,desc);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @Value("${images.directory}")
    private String imagesDirectory ;

//    @GetMapping("/images/{imageName}")
//    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws MalformedURLException {
//        Path imagePath = Paths.get(imagesDirectory).resolve(imageName);
//        System.out.println("Image path "+imagePath.toString());
//        Resource imageResource = new UrlResource(imagePath.toUri());
//
//        if (imageResource.exists() || imageResource.isReadable()) {
//            return ResponseEntity.ok().body(imageResource);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    @GetMapping("/images/{imageName}")
//    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
//        Path imagePath = Paths.get(imagesDirectory).resolve(imageName);
//
//        if (Files.exists(imagePath) && Files.isReadable(imagePath)) {
//            byte[] imageBytes = Files.readAllBytes(imagePath);
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG
//                    .body(imageBytes);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
        Path imagePath = Paths.get(imagesDirectory).resolve(imageName);

        System.out.println("The path is "+imagesDirectory);
        if (Files.exists(imagePath) && Files.isReadable(imagePath)) {
            byte[] imageBytes = Files.readAllBytes(imagePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG
                    .cacheControl(CacheControl.noCache().mustRevalidate().cachePrivate())
                    .body(imageBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
