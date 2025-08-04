package com.alaeldin.user_service;

import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {

		SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
		System.out.println("Your Secret Key (Base64): " + Encoders.BASE64.encode(key.getEncoded()));
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
