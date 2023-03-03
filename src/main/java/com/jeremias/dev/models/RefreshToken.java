package com.jeremias.dev.models;

import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
@Document(value = "RefreshToken")
public class RefreshToken {
	 private String id;
    private String token;
    private Instant createdDate;
}
