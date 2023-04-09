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
      <h1>Recommendations</h1>
      <v-container>
        <v-row>
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="game-card">
              <v-img
                src="https://external-preview.redd.it/zzgctwJ58xJ6cSerODdCReYJ27-99SoD5RpyFl0Lf1o.png?auto=webp&s=0f04ccc0d9bbfb455cccda349c26ae9e7dc0e4f2"
              />
              <v-card-title>To Be Implemented...</v-card-title>
            </v-card>
          </v-col>
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="game-card">
              <v-img
                src="https://external-preview.redd.it/zzgctwJ58xJ6cSerODdCReYJ27-99SoD5RpyFl0Lf1o.png?auto=webp&s=0f04ccc0d9bbfb455cccda349c26ae9e7dc0e4f2"
              />
              <v-card-title>To Be Implemented...</v-card-title>
            </v-card>
          </v-col>
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="game-card">
              <v-img
                src="https://external-preview.redd.it/zzgctwJ58xJ6cSerODdCReYJ27-99SoD5RpyFl0Lf1o.png?auto=webp&s=0f04ccc0d9bbfb455cccda349c26ae9e7dc0e4f2"
              />
              <v-card-title>To Be Implemented...</v-card-title>
            </v-card>
          </v-col>
          <v-col cols="12" sm="6" md="4" lg="3">
            <v-card class="game-card">
              <v-img
                src="https://external-preview.redd.it/zzgctwJ58xJ6cSerODdCReYJ27-99SoD5RpyFl0Lf1o.png?auto=webp&s=0f04ccc0d9bbfb455cccda349c26ae9e7dc0e4f2"
              />
              <v-card-title>To Be Implemented...</v-card-title>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </div>

    <!-- Alert -->
    <AutoFadeAlert :message="alertMessage" type="error" icon="info-circle" />
  </div>
</template>

<script>
import { nextTick } from "vue";

export default {
  name: "HomeView",
  data() {
    return {
      showModal: false,
      alertMessage: "",
      recommendedGames: [],
      loginStatus: false,
    };
  },
  components: {},
  methods: {
    showAlert(message) {
      this.alertMessage = "";
      nextTick(() => {
        this.alertMessage = message;
      });
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
        console.log(this.recommendedGames)
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
</style>