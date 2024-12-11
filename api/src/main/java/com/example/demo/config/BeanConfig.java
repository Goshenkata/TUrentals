package com.example.demo.config;

import com.example.demo.model.CategoryEntity;
import io.minio.MinioClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Value("${minio.url}")
    private String minioUrl;
    @Value("${minio.access-key}")
    private String minioAccess;
    @Value("${minio.secret-key}")
    private String minioSecret;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(CategoryEntity.class, String.class)
                .setConverter(context -> context.getSource().getName());

        return modelMapper;
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioAccess, minioSecret)
                .build();
    }
}