package com.example.demo.service;

import com.example.demo.model.ImageEntity;
import com.example.demo.repository.ImageRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final MinioClient minioClient;
    private final ImageRepository imageRepository;

    @Value("${minio.bucket-name}")
    private String bucketName;
    @Value("${minio.url}")
    private String minioUrl;

    public String uploadImage(MultipartFile file) throws Exception {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        String imageUrl = String.format("%s%s/%s", minioUrl, bucketName, fileName);
        ImageEntity imageEntity = new ImageEntity(imageUrl);
        imageRepository.save(imageEntity);
        return imageUrl;
    }

    public void seedDefaultImage() {
        String defaultUrl = "https://t4.ftcdn.net/jpg/04/73/25/49/360_F_473254957_bxG9yf4ly7OBO5I0O5KABlN930GwaMQz.jpg";
        if (imageRepository.findByUrl(defaultUrl).isPresent()) {
            return;
        }
        ImageEntity imageEntity = new ImageEntity(defaultUrl);
        imageRepository.save(imageEntity);
    }
}
