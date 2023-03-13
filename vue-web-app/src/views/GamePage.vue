<template>
  <div v-if="dataLoaded">
    <h1>{{ gameDetails.name }}</h1> <br>
	<v-img :src="gameDetails.coverUrl" width="200px"/> <br>

	<h3>Storyline</h3>
	<p>{{ gameDetails.storyline }}</p> <br>

	<h3>Summary</h3>
	<p>{{ gameDetails.summary }}</p> <br>

	<h3>Rating</h3>
	<p>{{ gameDetails.rating }}</p> <br>

	<h3>Rating Count</h3>
	<p>{{ gameDetails.rating_count }}</p> <br>

	<h3>Platforms</h3>
	<p>{{ gameDetails.platforms }}</p> <br>

	<h3>Release Date</h3>
	<p>{{ gameDetails.releaseDate }}</p> <br>

	<h3>Websites</h3>
	<p>{{ gameDetails.websites }}</p> <br>

	<h3>Similar Games</h3>
	<div v-for="game in gameDetails.similarGames" :key="game.id">
		<h4 @click="goToGamePage(game.id)">{{ game.name }}</h4>
	</div>
  </div>

  <div v-else>
    <h4>Loading Game Data...</h4>
    <div class="loader"></div>
  </div>
</template>

<script>
export default {
  name: "GamePage",

  data() {
    return {
      gameID: this.$route.params.id,
      gamePromise: null, // game details promise
      gameDetails: null, // game details promise results

      dataLoaded: false,
    };
  },

  methods: {
    // function that gets game info from the API and returns it as a JSON object
    async getGameInfo() {
      const response = await fetch(
        "http://localhost:4040/api/igdb/game/" + this.gameID
      );
      const game = await response.json();
      return game;
    },

	async getGameInfo() {
		const response = await fetch(
		  "http://localhost:4040/api/igdb/game/" + this.gameID
		);
		const game = await response.json();
		return game;
	},

	// method that reloads page
	goToGamePage(gameId) {
      this.$router.push({path: `/game/${gameId}`})
	  setTimeout(function() { window.location.reload() }, 10)
    },
  },

  // when the page is loaded, get the game info from the API
  async created() {
    // get the result of getGameInfo and store it in the game variable
    this.gameDetails = await this.getGameInfo();

    this.dataLoaded = true;
  },
};
</script>
  
  <style>
</style>