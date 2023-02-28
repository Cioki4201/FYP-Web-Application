package com.paul.fyp;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.paul.fyp.services.IGDBService;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Tests {

	@Autowired
	IGDBService igdbService;

	@Test
	void testGameSearch() throws UnirestException {
		JSONArray testNode = igdbService.gameSearch("call of duty");
		System.out.println(testNode);
	}

	@Test
	void getCoverArtTest() throws UnirestException {
		String returnBody = igdbService.getCoverArt("261332");
		System.out.println(returnBody);
	}

}