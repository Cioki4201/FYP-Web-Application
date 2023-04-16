<template>
  <!-- ALERT -->
  <AutoFadeAlert :message="alertMessage" :type="alertType" :icon="alertIcon" />

  <div v-if="dataLoaded">
    <div class="artwork-background" :style="backgroundStyle">
      <div class="cover-screenshots">
        <div>
          <h1 id="TITLE">
            {{ gameDetails.name }}
            <br />
          </h1>
          <span class="rating" :style="ratingStyle">
            {{ gameDetails.rating }}
          </span>
          <br />

          <div class="cover-title-add">
            <br />
            <v-img
              :src="gameDetails.coverUrl"
              width="200px"
              class="cover-image"
            />
            <h4 id="release-date">
              Release Date: {{ gameDetails.releaseDate }}
            </h4>
            <br />

            <!-- Add Game to MyList -->
            <v-menu>
              <template v-slot:activator="{ props }">
                <v-btn v-bind="props" class="add-game-btn">
                  <font-awesome-icon icon="plus" />
                   Add Game to MyList
                </v-btn>
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

          <br />

          <span
            class="genre-tag"
            v-for="(genre, index) in gameDetails.genres"
            :key="index"
          >
            {{ genre.name }}
          </span>
        </div>

        <div class="carousel" v-if="gameDetails.screenshots.length != 0">
          <Swiper :slides-per-view="1" :loop="true" autoplay>
            <SwiperSlide
              v-for="(screenshot, index) in gameDetails.screenshots"
              :key="index"
            >
              <img :src="screenshot" alt="Screenshot" />
            </SwiperSlide>
          </Swiper>
          <p>Screenshots</p>
        </div>
      </div>
    </div>

    <div class="game-info">
      <div class="summary-platforms">
        <div class="summary">
          <h2>Summary</h2>
          <p>{{ gameDetails.summary }}</p>
        </div>

        <div class="platforms">
          <h2>Platforms</h2>
          <ul v-if="gameDetails.platforms.length != 0">
            <li v-for="platform in gameDetails.platforms" :key="platform.id">
              {{ platform.name }}
            </li>
          </ul>
          <p v-else>No Platforms Found</p>
        </div>
      </div>

      <div class="websites-video">
        <div class="websites">
          <h2>Websites</h2>
          <ul>
            <li v-for="website in gameDetails.websites" :key="website.id">
              <a :href="website.url" target="_blank">{{ website.url }}</a>
            </li>
          </ul>
        </div>

        <iframe
          v-if="gameDetails.videos.length != 0"
          class="video"
          :src="gameDetails.videos[0]"
          frameborder="0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowfullscreen
        ></iframe>
      </div>

      <div class="similar-games">
        <h2>Similar Games</h2>
        <v-row>
          <v-col
            v-for="game in gameDetails.similarGames"
            :key="game.id"
            cols="12"
            sm="6"
            md="3"
            lg="2"
          >
            <v-card @click="goToGamePage(game.id)" class="game-card">
              <v-img :src="game.cover" />
              <v-card-title style="font-size: 1rem">{{
                game.name
              }}</v-card-title>
            </v-card>
          </v-col>
        </v-row>
      </div>
    </div>
  </div>

  <div v-else>
    <h4>Loading Game Data...</h4>
    <div class="loading-spinner"></div>
  </div>
</template>

<!-- ================================================== SCRIPTS ================================================== -->

<script>
import { nextTick } from "vue";
import { Swiper, SwiperSlide } from "swiper/vue";

export default {
  name: "GamePage",
  props: {
    id: {
      type: String,
      default: "",
    },
  },

  components: {
    Swiper,
    SwiperSlide,
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

    goToGamePage(gameId) {
      this.$router.push({ path: `/game/${gameId}` });
    },

    handlePopstate() {
      // Force reload when user uses the back or forward button
      window.location.reload();
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
        this.showAlert(
          "Please Log In to add games to your list.",
          "error",
          "right-to-bracket"
        );
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
    try {
      for (let i = 0; i < this.gameDetails.artworks.length; i++) {
        this.gameDetails.artworks[i] =
          "https://images.igdb.com/igdb/image/upload/t_screenshot_huge/" +
          this.gameDetails.artworks[i].image_id +
          ".jpg";
      }
    } catch (error) {
      console.log("No Artworks");
    }


    try {
      for (let i = 0; i < this.gameDetails.screenshots.length; i++) {
        this.gameDetails.screenshots[i] =
          "https://images.igdb.com/igdb/image/upload/t_screenshot_huge/" +
          this.gameDetails.screenshots[i].image_id +
          ".jpg";
      }
    } catch (error) {
      console.log("No Screenshots");
    }

    // From similarGames, remove the games that have no cover image id
    this.gameDetails.similarGames = this.gameDetails.similarGames.filter(game => game.hasOwnProperty("cover"));

    for (let i = 0; i < this.gameDetails.similarGames.length; i++) {
      this.gameDetails.similarGames[i].cover =
        "https://images.igdb.com/igdb/image/upload/t_cover_big/" +
        this.gameDetails.similarGames[i].cover.image_id +
        ".jpg";
    }


    try {
      // Processing Video url(s)
      for (let i = 0; i < this.gameDetails.videos.length; i++) {
        this.gameDetails.videos[i] =
          "https://www.youtube.com/embed/" +
          this.gameDetails.videos[i].video_id;
      }
    } catch (error) {
      console.log("No Videos");
    }

    console.log("Videos Done")

    // Round Rating to no decimal places
    this.gameDetails.rating = Math.round(this.gameDetails.rating);

    console.log(this.gameDetails);

    this.dataLoaded = true;
  },

  mounted() {
    window.addEventListener("popstate", this.handlePopstate);
  },

  beforeDestroy() {
    window.removeEventListener("popstate", this.handlePopstate);
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

    ratingStyle() {
      if (this.gameDetails && this.gameDetails.rating) {
        const gameRating = this.gameDetails.rating;

        if (gameRating > 80) {
          return "background-color: green; box-shadow: 0 0 15px green;";
        } else if (gameRating > 60) {
          return "background-color: yellow; box-shadow: 0 0 15px yellow;";
        } else if (gameRating > 40) {
          return "background-color: orange; box-shadow: 0 0 15px orange;";
        } else if (gameRating < 20) {
          return "background-color: red; box-shadow: 0 0 15px red;";
        }
      } else {
        return "display: none;";
      }
    },
  },
};
</script>
  
<!-- ================================================== STYLES ================================================== -->

<style scoped>
#similarGame {
  cursor: pointer;
  color: rgb(60, 0, 0);
}

#TITLE {
  font-size: 2.5rem;
  font-weight: 900;
  color: white;
  text-shadow: 0 0 4px rgba(0, 0, 0, 1);
}

#release-date {
  display: inline-block;
  margin-left: 0.4rem;
  text-shadow: 0 0 4px rgba(0, 0, 0, 1);
  color: white;
}

.add-game-btn {
  border-radius: ;
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

.swiper-slide img {
  max-width: 100%;
}

.carousel {
  width: 40%;
  text-align: center;
  box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.7);
  background-color: var(--dark);
  padding: 1rem;
}

.cover-screenshots {
  display: flex;
  align-items: center;
  gap: 25rem;
}

.rating {
  border-radius: 50%;
  color: black;
  padding: 2%;
  font-size: 1.5rem;
  text-align: center;
  font-weight: 900;
  text-shadow: none;
  margin-right: 1rem;
}

/* Game Details */

.summary-platforms {
  display: flex;
  gap: 3rem;
  padding: 3rem;
}

.summary-platforms h2 {
  font-size: 2rem;
  font-weight: 900;
  margin-bottom: 1.2rem;
  text-align: center;
  color: white;
  text-shadow: 0 0 10px rgba(255, 136, 0, 0.7);
}

.summary-platforms p {
  font-size: 1.2rem;
}

.summary {
  background-color: var(--dark);
  padding: 2rem;
  border-radius: 2rem;
  box-shadow: rgba(255, 128, 0, 0.6) 0px 0px 30px 0px;
  outline: 4px solid var(--orange);
  width: 80%;
}

.platforms {
  background-color: var(--dark);
  padding: 2rem;
  width: 20%;
  border-radius: 2rem;
  box-shadow: rgba(255, 128, 0, 0.6) 0px 0px 30px 0px;
  outline: 4px solid var(--orange);
  text-align: center;
}

.platforms ul {
  list-style: none;
  padding: 0;
}

.genre-tag {
  display: inline-block;
  padding: 8px 12px;
  background-color: var(--orange);
  color: #fff;
  border-radius: 2rem;
  font-size: 1rem;
  text-decoration: none;
  transition: background-color 0.3s;
  margin-right: 0.6rem;
}

.genre-tag:hover {
  background-color: #ec7200;
  cursor: default;
}

.websites-video {
  display: flex;
  gap: 3rem;
  padding: 3rem;
}

.websites-video h2 {
  font-size: 2rem;
  font-weight: 900;
  margin-bottom: 1.2rem;
  text-align: center;
  color: white;
  text-shadow: 0 0 10px rgba(255, 136, 0, 0.7);
}

.websites {
  background-color: var(--dark);
  padding: 2rem;
  border-radius: 2rem;
  box-shadow: rgba(255, 128, 0, 0.6) 0px 0px 30px 0px;
  outline: 4px solid var(--orange);
  width: 35%;
  overflow: hidden;
}

.websites ul {
  text-align: center;
  list-style: none;
  padding: 0;
}

.websites ul li {
  margin: 0.5rem 0;
}

.websites ul li a {
  text-decoration: none;
  color: white;
  font-size: 1.2rem;
  transition: 0.3s;
}

.websites ul li a:hover {
  text-decoration: none;
  color: rgb(245, 148, 21);
  text-shadow: 0 0 5px rgba(255, 136, 0, 0.5);
  font-size: 1.23rem;
}

.video {
  border-radius: 2rem;
  box-shadow: rgba(255, 128, 0, 0.6) 0px 0px 30px 0px;
  outline: 4px solid var(--orange);
  width: 65%;
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

.similar-games {
  width: 80%;
  margin: 4rem auto;

  background-color: var(--dark);
  padding: 2rem;
  border-radius: 2rem;
  box-shadow: rgba(255, 128, 0, 0.6) 0px 0px 30px 0px;
  outline: 4px solid var(--orange);
  text-align: center;
}

.similar-games h2 {
  font-size: 2rem;
  font-weight: 900;
  margin-bottom: 1.2rem;
  text-align: center;
  color: white;
  text-shadow: 0 0 10px rgba(255, 136, 0, 0.7);
}
</style>