package com.jeremias.dev.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern.Flag;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	  @NotBlank(message = "El email es requerido.")
	  @Email(message = "El email es invalido .", flags = { Flag.CASE_INSENSITIVE })
	private String email;
	  @NotBlank(message = "El username es requerido.")
	  @Size(min = 5, max = 100, message = "La longitud del usuario tiene que ser de 5 a 30 caracteres.")
    private String username;
	  @NotBlank(message = "El password es requerido.")
    private String password;
}
