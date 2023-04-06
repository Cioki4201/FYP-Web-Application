<template>
  <!-- ALERT -->
  <AutoFadeAlert :message="alertMessage" :type="alertType" :icon="alertIcon" />

  <div v-if="dataLoaded">
    <div class="artwork-background" :style="backgroundStyle">
      <h1>{{ gameDetails.name }}</h1>
      <div class="cover-title-add">
        
        <br />
        <v-img :src="gameDetails.coverUrl" width="200px" class="cover-image"/> <br />

        <!-- Add Game to MyList -->
        <v-menu>
          <template v-slot:activator="{ props }">
            <v-btn color="orange" v-bind="props"> Add Game to MyList </v-btn>
          </template>
          <v-list>
            <v-list-item
              v-for="(item, index) in menuItems"
              :key="index"
              :value="index"
            >
              <v-list-item-title @click="addGameToMyList(item.value)">{{
                item.label
              }}</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
    </div>

    <h3>Storyline</h3>
    <p>{{ gameDetails.storyline }}</p>
    <br />

    <h3>Summary</h3>
    <p>{{ gameDetails.summary }}</p>
    <br />

    <h3>Rating</h3>
    <p>{{ gameDetails.rating }}</p>
    <br />

    <h3>Rating Count</h3>
    <p>{{ gameDetails.rating_count }}</p>
    <br />

    <h3>Platforms</h3>
    <p>{{ gameDetails.platforms }}</p>
    <br />

    <h3>Release Date</h3>
    <p>{{ gameDetails.releaseDate }}</p>
    <br />

    <h3>Websites</h3>
    <p>{{ gameDetails.websites }}</p>
    <br />

    <h3>Similar Games</h3>
    <div v-for="game in gameDetails.similarGames" :key="game.id">
      <h4 @click="goToGamePage(game.id)" id="similarGame">{{ game.name }}</h4>
    </div>
  </div>

  <div v-else>
    <h4>Loading Game Data...</h4>
    <div class="loading-spinner"></div>
  </div>
</template>

<script>
import { nextTick } from "vue";

export default {
  name: "GamePage",
  props: {
    id: {
      type: String,
      default: "",
    },
  },
  emits: ["mustLogInAlert"],

  data() {
    return {
      gameID: this.$route.params.id,
      gamePromise: null, // game details promise
      gameDetails: null, // game details promise results

      dataLoaded: false,

      menuItems: [
        { value: 0, label: "Want To Play" },
        { value: 1, label: "Playing" },
        { value: 2, label: "Completed" },
        { value: 3, label: "Dropped" },
        { value: 4, label: "On Hold" },
      ],

      // ALERT DATA
      alertMessage: "",
      alertType: "",
      alertIcon: "",
    };
  },

  methods: {
    // Alert Method
    showAlert(message, type, icon) {
      this.alertMessage = "";
      nextTick(() => {
        this.alertType = type;
        this.alertIcon = icon;
        this.alertMessage = message;
      });
    },

    // function that gets game info from the API and returns it as a JSON object
    async getGameInfo() {
      const response = await fetch(
        "http://localhost:4040/api/igdb/game/" + this.gameID
      );
      const game = await response.json();
      return game;
    },

    // method that reloads page
    goToGamePage(gameId) {
      this.$router.push({ path: `/game/${gameId}` });
      setTimeout(function () {
        window.location.reload();
      }, 10);
    },

    // method that adds game to current user's game list
    async addGameToMyList(listIndex) {
      // Get the current user's username from the session storage
      if (localStorage.getItem("signInObj") == null) {
        alert("Please sign in to add games to your list.");
        return;
      } else {
        let currentUser = localStorage.getItem("signInObj");
        currentUser = JSON.parse(currentUser);
        let username = currentUser.username;

        // Post Request Data
        const postData = JSON.stringify({
          gameID: this.gameID,
          category: listIndex,
        });

        // Post Request Options
        const requestOptions = {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: postData,
        };

        const response = await fetch(
          "http://localhost:4040/api/users/" + username + "/add_game",
          requestOptions
        );

        // If the game was added to the user's list, alert the user
        if (response.status == 200) {
          // print the response string to the console
          const responseString = await response.text();
          this.showAlert(responseString, "success", "check");
        } else {
          this.showAlert("Error adding game to your list.", "error", "xmark");
        }
      }
    },
  },

  // when the page is loaded, get the game info from the API
  async created() {
    // get the result of getGameInfo and store it in the game variable
    this.gameDetails = await this.getGameInfo();

    // Processing cover url
    const coverObj = JSON.parse(this.gameDetails.coverUrl);
    this.gameDetails.coverUrl = coverObj[0].url;

    // Processing artwork/screenshot/similar game cover url(s)
    for (let i = 0; i < this.gameDetails.artworks.length; i++) {
      this.gameDetails.artworks[i] =
        "https://images.igdb.com/igdb/image/upload/t_screenshot_huge/" +
        this.gameDetails.artworks[i].image_id +
        ".jpg";
    }

    for (let i = 0; i < this.gameDetails.screenshots.length; i++) {
      this.gameDetails.screenshots[i] =
        "https://images.igdb.com/igdb/image/upload/t_screenshot_huge/" +
        this.gameDetails.screenshots[i].image_id +
        ".jpg";
    }

    for (let i = 0; i < this.gameDetails.similarGames.length; i++) {
      this.gameDetails.similarGames[i].cover =
        "https://images.igdb.com/igdb/image/upload/t_cover_big/" +
        this.gameDetails.similarGames[i].cover.image_id +
        ".jpg";
    }

    // Processing Video url(s)
    for (let i = 0; i < this.gameDetails.videos.length; i++) {
      this.gameDetails.videos[i] =
        "https://www.youtube.com/embed/" + this.gameDetails.videos[i].video_id;
    }

    console.log(this.gameDetails);

    this.dataLoaded = true;
  },

  computed: {
  backgroundStyle() {
    if (this.gameDetails && this.gameDetails.artworks.length > 0) {
      var lastArtwork = this.gameDetails.artworks.length - 1;
      return {
        "--background-image": `url('${this.gameDetails.artworks[lastArtwork]}')`,
      };
    } else {
      return {
        "--background-image": `url(${require("@/assets/default-orange-background.jpg")})`,
      };
    }
  },
},
};
</script>
  
<style>
#similarGame {
  cursor: pointer;
  color: rgb(60, 0, 0);
}

.loading-spinner {
  border: 5px solid rgba(0, 0, 0, 0.1);
  border-left-color: #007bff;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin: auto;
}

@keyframes spin {
  100% {
    transform: rotate(360deg);
  }
}

.artwork-background {
  position: relative;
  width: 100%;
  padding: 4rem;
  overflow: hidden; /* Add this to prevent the blurred pseudo-element from going outside the div */
  z-index: 0;
}

.artwork-background:before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: var(--background-image); /* Use the new variable here */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  filter: blur(10px);
  z-index: -1;
}

.cover-image {
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.6);
  border-radius: 5%;
}

.cover-title-add {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: fit-content;
}
</style>