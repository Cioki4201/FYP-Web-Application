package com.paul.fyp;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.paul.fyp.models.dto.CoverIDsDTO;
import com.paul.fyp.models.dto.GamesIdsDTO;
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
		String[] ids = {"41111", "224339", "196852", "104928"};
		CoverIDsDTO coverIDsDTO = new CoverIDsDTO(ids);
		String returnBody = igdbService.getCoverArt(coverIDsDTO).getBody();
		System.out.println(returnBody);
	}

	@Test
	void getGameInfoTest() throws UnirestException {
		String gameID = "1942";
		String returnBody = igdbService.getGameInfo(gameID).toString();
		System.out.println(returnBody);
	}

	@Test
	void getRawGameDataTest() throws UnirestException {
		String[] ids = {"41111", "224339", "196852", "104928"};
		GamesIdsDTO gamesIdsDTO = new GamesIdsDTO();
		gamesIdsDTO.setGameIDs(ids);
		String returnBody = igdbService.getRawGameData(gamesIdsDTO).getBody();
		System.out.println(returnBody);
	}

	@Test
	void getGameNameCoverAndIdTest() throws UnirestException {
		String[] ids = {"41111", "224339", "196852", "104928"};
		GamesIdsDTO gamesIdsDTO = new GamesIdsDTO();
		gamesIdsDTO.setGameIDs(ids);
		String returnBody = igdbService.getGameNameCoverAndId(gamesIdsDTO).getBody();
		System.out.println(returnBody);
	}

}
