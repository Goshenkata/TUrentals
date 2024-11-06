package com.example.demo.dto.common;

import java.io.Serializable;

public record MessageResponseDTO(Integer status, String message) implements Serializable {

}
