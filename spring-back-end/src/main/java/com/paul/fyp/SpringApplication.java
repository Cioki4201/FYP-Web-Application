package com.paul.fyp;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.paul.fyp.models.IGDBRequest;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;


@SpringBootApplication
public class SpringApplication {

	public static void main(String[] args) throws IOException, InterruptedException, UnirestException {
		org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
	}

}
