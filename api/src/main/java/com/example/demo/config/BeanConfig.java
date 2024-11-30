package com.example.demo.config;

import com.example.demo.model.CategoryEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(CategoryEntity.class, String.class)
                .setConverter(context -> context.getSource().getName());

        return modelMapper;
    }
}
