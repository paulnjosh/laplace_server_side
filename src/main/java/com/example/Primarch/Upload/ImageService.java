package com.example.Primarch.Upload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final ImageRepo imageRepo;

    @Value("${images.directory}")
    String filePath;

    public String uploadImageToFileSystem(MultipartFile file, String desc) throws IOException {
//        String filePath="C:/projects/Files/uploaded/";

        byte[] bytes = file.getBytes();
        Path path = Paths.get(filePath+file.getOriginalFilename());
        Files.write(path, bytes);

        Image fileData=imageRepo.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                        .description(desc)
                .filePath(filePath).build());

//        file.transferTo(new File(filePath));

        if (fileData != null) {
            return "file uploaded successfully : " + filePath;
        }
        return null;
    }
}
