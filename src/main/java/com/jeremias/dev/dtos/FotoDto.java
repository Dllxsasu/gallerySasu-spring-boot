package com.jeremias.dev.dtos;

import java.util.List;

import com.jeremias.dev.constantes.PhotoStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FotoDto {
	private String fotoId;
    @NotBlank
    private String userId;


    @Size(min = 1)
    private List<String> tags;
    
    private PhotoStatus photoStatus;
    @NotBlank
    private String url;
    @NotBlank
    private String titulo;
    private String username;
    private int views;
    @Min(value = 0)
    private int likeCount;
    @Min(value = 0)
    private int dislikeCount;
}
