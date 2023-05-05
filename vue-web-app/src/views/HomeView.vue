<template>
  <div class="home">
    <div class="intro">
      <h1>MyGameList</h1>
      <h3>
        MyGameList is the ultimate gaming tool that lets you manage your entire
        game collection, keep track of your progress, and discover new games
        tailored to your unique gaming preferences. With a user-friendly
        interface and a personalized recommendation system, MyGameList offers
        the complete package that every gamer is sure to love.
      </h3>
    </div>

    <div class="game-recommendations">
      <h1>Recommendations for you</h1>
      <!-- If the user has less than 6 games in their list, do not show recommendations -->
      <v-container v-if="this.loginStatus && !enoughGames">
        <h2>Add <span style="color: orange">{{ 6 - this.gameCount }}</span> more games to your list to start receiving personalised recommendations!</h2>
      </v-container>

      <v-container v-else-if="this.loginStatus && this.enoughGames">
        <v-row>
          <v-col
            v-for="game in this.recommendedGames"
            :key="game.id"
            cols="12"
            sm="6"
            md="3"
            lg="3"
          >
            <v-card @click="goToGamePage(game.id)" class="game-card">
              <v-img :src="game.coverImageUrl" />
              <v-card-title style="font-size: 1rem">{{
                game.name
              }}</v-card-title>
            </v-card>
          </v-col>
        </v-row>
      </v-container>

      <v-container v-else>
        <h3>You must Log in to receive personalised recommendations!</h3>
        <br />
        <button @click="emitLogInModal" class="logInBtn">Log In</button>
      </v-container>
    </div>

    <!-- Alert -->
    <AutoFadeAlert
      :message="alertMessage"
      :type="alertType"
      :icon="alertIcon"
    />
  </div>
</template>

<script>
import { nextTick } from "vue";

export default {
  name: "HomeView",

  emits: ["showLoginModal", "updateLoginStatus"],

  data() {
    return {
      showModal: false,
      alertMessage: "",
      recommendedGames: [],
      loginStatus: false,
      showLoginModal: false,

      alertType: "",
      alertIcon: "",
      alertMessage: "",
      gameCount: 0,
      enoughGames: false,
    };
  },

  components: {},

  watch: {
    loginStatus: function (newVal, oldVal) {
      if (newVal) {
        this.getUserRecommendedGames();
      }
      this.$emit("updateLoginStatus", newVal);
    },
  },

  methods: {
    showAlert(message, type, icon) {
      this.alertMessage = "";
      nextTick(() => {
        this.alertType = type;
        this.alertIcon = icon;
        this.alertMessage = message;
      });
    },

    async getUserGameCount() {
      try {
        let signInObj = JSON.parse(localStorage.getItem("signInObj"));
        let currentUsername = signInObj.username;
        const response = await fetch(
          "http://localhost:4040/api/users/" + currentUsername + "/count"
        );
        const data = await response.json();
        this.gameCount = data;
      } catch (error) {
        console.log(error);
      }
    },

    goToGamePage(gameId) {
      this.$router.push({ path: `/game/${gameId}` });
    },

    emitLogInModal() {
      this.$emit("showLoginModal");
    },

    async getUserRecommendedGames() {
      var signInObj = JSON.parse(localStorage.getItem("signInObj"));
      let currentUsername = signInObj.username;

      try {
        const response = await fetch(
          "http://localhost:4040/api/users/" + currentUsername + "/recommended"
        );
        const recommendedGames = await response.json();

        this.recommendedGames = recommendedGames;

        // Handling all the recommended game ID's so that they can be displayed
        const requestBody = {
          gameIDs: this.recommendedGames,
        };

        const requestOptions = {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(requestBody),
        };

        // Send a single request with the requestBody containing all coverIds
        const coverResponse = await fetch(
          "http://localhost:4040/api/igdb/coverAndNames",
          requestOptions
        );

        const gameRecommendations = await coverResponse.json();
        this.recommendedGames = gameRecommendations;
      } catch (error) {
        console.log(error);
      }
    },
  },

  async created() {
    // check if user is logged in
    if (localStorage.getItem("signInObj")) {
      this.loginStatus = true;
      await this.getUserRecommendedGames();
    }

    await this.getUserGameCount();
    if (this.gameCount >= 6) {
      this.enoughGames = true;
    }
  },
};
</script>

<style>
.home {
  height: 100%;
  width: 100%;
}

.intro {
  padding: 3% 0px 4% 0px;
  background-image: url("../assets/orange-keyboard-background.jpg");
  background-size: 100% auto;
  border-radius: 5px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.3);
  text-align: center;
}

.intro h1 {
  color: #fff;
  font-size: 15vmin;
  margin-bottom: 10px;
  font-family: "Bungee Spice", cursive;
  text-shadow: 0 0 16px rgba(255, 98, 0, 0.7);
}

.intro h3 {
  color: #ff8c00;
  font-size: 3vmin;
  font-weight: 400;
  text-shadow: 0 0 16px rgba(255, 98, 0, 0.7);
  margin-left: 15%;
  margin-right: 15%;
}

/* Recommendations section styling */

.game-recommendations {
  padding-top: 30px;
  background-size: 100% auto;
  border-radius: 5px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.3);
  text-align: center;
}

.game-recommendations h1 {
  color: #ff8c00;
  font-size: 3.5rem;
  margin-bottom: 10px;
  font-family: "Bungee Spice", cursive;
  text-shadow: 0 0 16px rgba(255, 98, 0, 0.7);
}

.game-card {
  border: var(--orange) 4px solid;
  margin: 10px;
  border-radius: 20px;
  box-shadow: 0 0 15px var(--orange);
}

.game-card:hover {
  border: var(--orange) 5px solid;
  box-shadow: 0 0 30px var(--orange);
}

.logInBtn {
  background-color: var(--orange);
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 1.3rem;
  font-size: 1.5rem;
  cursor: pointer;
  margin-top: 20px;
  transition: 300ms;
}

.logInBtn:hover {
  box-shadow: 0 0 16px rgba(255, 123, 0, 0.4);
  background-color: #ff8c00;
}
</style>