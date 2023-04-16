<template>
  <!-- Alert -->
  <AutoFadeAlert :message="alertMessage" :type="alertType" :icon="alertIcon" />

  <div class="main">
    <div class="title">
      <h1>Browse Games</h1>
    </div>
    <v-container>
      <v-row>
        <v-col
          v-for="(genre, index) in genres"
          :key="index"
          cols="12"
          sm="6"
          md="4"
          lg="3"
        >
          <div :class="genre">
            <div class="genre-tile" @click="selectGenre(genre)">
              <div class="genre-name">{{ genre }}</div>
            </div>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import { nextTick } from 'vue';

export default {
  data() {
    return {
      genres: [
        "Fighting",
        "Shooter",
        "Music",
        "Puzzle",
        "Racing",
        "RTS",
        "RPG",
        "Sport",
        "Strategy",
        "Adventure",
        "Arcade",
        "Indie",
      ],

      games: null,
      imagesFetched: false,
      searching: false,

      // Alert Data
      alertMessage: "",
      alertType: "",
      alertIcon: "",
    };
  },

  methods: {
    async selectGenre(genre) {
      console.log("Selected genre:", genre);
      // Handle the click event here, e.g., navigate to another page or fetch game data
      this.imagesFetched = false;
      this.searching = true;

      if (genre == "RTS"){
        genre = "Real Time Strategy (RTS)";
      } 
      else if (genre == "RPG"){
        genre = "Role-Playing (RPG)";
      }

      try {
        // Call an API or perform a search action with the search text
        const response = await fetch(
          "http://localhost:4040/api/igdb/genres/" + genre
        );

        if (response.status != 200) {
          alert("Error: " + response.status);
          return;
        }

        const games = await response.json();

        this.games = games;

        // if a game does not have a cover ID remove it from the list
        this.games = this.games.filter((game) => game.cover);

        // fetch the cover image for each game and store it in a list
        let coverIds = this.games.map((game) => game.cover);

        // Create a JSON object with a key 'coverIDs' containing the coverIds array
        const requestBody = {
          coverIDs: coverIds,
        };

        // Getting the cover URLS for each search result

        const requestOptions = {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(requestBody),
        };

        // Send a single request with the requestBody containing all coverIds
        const coverResponse = await fetch(
          "http://localhost:4040/api/igdb/cover/",
          requestOptions
        );
        const coverUrls = await coverResponse.json();

        // Replace the game cover with the corresponding URL
        this.games.forEach((game) => {
          const coverInfo = coverUrls.find((cover) => cover.game === game.id);
          if (coverInfo) {
            game.coverUrl = coverInfo.url;
          } else {
            console.log("Cover not found for game ID:", game.id);
          }
        });

        this.imagesFetched = true;
        this.searching = false;
        console.log("Search Results Updated");

        this.$router.push({
          name: "SearchResultsView",
          query: {
            games: JSON.stringify(this.games),
            imagesFetched: JSON.stringify(this.imagesFetched),
            searching: JSON.stringify(this.searching),
          },
        });

        setTimeout(function () {
          window.location.reload();
        }, 10);
      } catch (error) {
        this.searching = false;
        this.showAlert(
          "No results found for: '" + this.searchText + "'",
          "error",
          "face-surprise"
        );
        this.searchText = "";
      }
    },

    showAlert(message, type, icon) {
      this.alertMessage = "";
      nextTick(() => {
        this.alertType = type;
        this.alertIcon = icon;
        this.alertMessage = message;
      });
    },
  },
};
</script>

<style scoped>
.main {
  margin-bottom: 5rem;
}
.title {
  text-align: center;
  margin-top: 2rem;
  margin-bottom: 2rem;
  font-family: "Bungee Spice", cursive;
  font-size: 3rem;
}
.genre-tile {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: transparent;
  height: 100%;
  width: 100%;
  margin-bottom: 2rem;
  cursor: pointer;
  transition: 200ms;
  z-index: 1;
  position: relative;
}

.genre-tile:hover {
  background-color: rgba(0, 0, 0, 0.2);
}

.genre-name {
  font-weight: bold;
  text-align: center;
  font-size: 2rem;
}

/* Fighting Background */
.Fighting {
  position: relative;
  height: 15rem;
  width: 15rem;
  overflow: hidden;
  border-radius: 12%;
  border: var(--orange) 4px solid;
  box-shadow: 0 0 15px var(--orange);
}

.Fighting::before {
  content: "";
  background-image: url("@/assets/browseGamesIcons/fighting.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  filter: blur(2px);
}

/* Shooter Background */
.Shooter {
  position: relative;
  height: 15rem;
  width: 15rem;
  overflow: hidden;
  border-radius: 12%;
  border: var(--orange) 4px solid;
  box-shadow: 0 0 15px var(--orange);
}

.Shooter::before {
  content: "";
  background-image: url("@/assets/browseGamesIcons/shooter.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  filter: blur(2px);
}

/* Music Background */
.Music {
  position: relative;
  height: 15rem;
  width: 15rem;
  overflow: hidden;
  border-radius: 12%;
  border: var(--orange) 4px solid;
  box-shadow: 0 0 15px var(--orange);
}

.Music::before {
  content: "";
  background-image: url("@/assets/browseGamesIcons/music.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  filter: blur(2px);
}

/* Puzzle Background */
.Puzzle {
  position: relative;
  height: 15rem;
  width: 15rem;
  overflow: hidden;
  border-radius: 12%;
  border: var(--orange) 4px solid;
  box-shadow: 0 0 15px var(--orange);
}

.Puzzle::before {
  content: "";
  background-image: url("@/assets/browseGamesIcons/puzzle.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  filter: blur(2px);
}

/* Racing Background */
.Racing {
  position: relative;
  height: 15rem;
  width: 15rem;
  overflow: hidden;
  border-radius: 12%;
  border: var(--orange) 4px solid;
  box-shadow: 0 0 15px var(--orange);
}

.Racing::before {
  content: "";
  background-image: url("@/assets/browseGamesIcons/racing.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  filter: blur(2px);
}

/* RTS Background */
.RTS {
  position: relative;
  height: 15rem;
  width: 15rem;
  overflow: hidden;
  border-radius: 12%;
  border: var(--orange) 4px solid;
  box-shadow: 0 0 15px var(--orange);
}

.RTS::before {
  content: "";
  background-image: url("@/assets/browseGamesIcons/rts.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  filter: blur(2px);
}

/* RPG Background */
.RPG {
  position: relative;
  height: 15rem;
  width: 15rem;
  overflow: hidden;
  border-radius: 12%;
  border: var(--orange) 4px solid;
  box-shadow: 0 0 15px var(--orange);
}

.RPG::before {
  content: "";
  background-image: url("@/assets/browseGamesIcons/rpg.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  filter: blur(2px);
}

/* Sport Background */
.Sport {
  position: relative;
  height: 15rem;
  width: 15rem;
  overflow: hidden;
  border-radius: 12%;
  border: var(--orange) 4px solid;
  box-shadow: 0 0 15px var(--orange);
}

.Sport::before {
  content: "";
  background-image: url("@/assets/browseGamesIcons/sport.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  filter: blur(2px);
}

/* Strategy Background */
.Strategy {
  position: relative;
  height: 15rem;
  width: 15rem;
  overflow: hidden;
  border-radius: 12%;
  border: var(--orange) 4px solid;
  box-shadow: 0 0 15px var(--orange);
}

.Strategy::before {
  content: "";
  background-image: url("@/assets/browseGamesIcons/strategy.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  filter: blur(2px);
}

/* Adventure Background */
.Adventure {
  position: relative;
  height: 15rem;
  width: 15rem;
  overflow: hidden;
  border-radius: 12%;
  border: var(--orange) 4px solid;
  box-shadow: 0 0 15px var(--orange);
}

.Adventure::before {
  content: "";
  background-image: url("@/assets/browseGamesIcons/adventure.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  filter: blur(2px);
}

/* Arcade Background */
.Arcade {
  position: relative;
  height: 15rem;
  width: 15rem;
  overflow: hidden;
  border-radius: 12%;
  border: var(--orange) 4px solid;
  box-shadow: 0 0 15px var(--orange);
}

.Arcade::before {
  content: "";
  background-image: url("@/assets/browseGamesIcons/arcade.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  filter: blur(2px);
}

/* Hack & Slash Background */
.Indie {
  position: relative;
  height: 15rem;
  width: 15rem;
  overflow: hidden;
  border-radius: 12%;
  border: var(--orange) 4px solid;
  box-shadow: 0 0 15px var(--orange);
}

.Indie::before {
  content: "";
  background-image: url("@/assets/browseGamesIcons/indie.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  filter: blur(2px);
}
</style>
