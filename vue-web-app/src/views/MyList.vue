<template>
  <!-- Alert -->
  <AutoFadeAlert :message="alertMessage" :type="alertType" :icon="alertIcon" />

  <v-main>
    <v-container class="my-container mx-auto my-6 w-400">
      <v-avatar size="200" class="avatar mx-auto mb-2" color="#000000">
        <v-img src="https://cdn.vuetifyjs.com/images/lists/1.jpg"></v-img>
      </v-avatar>
      <h1 class="mx-auto">@{{ username }}</h1>
    </v-container>

    <v-card class="tab-view mx-15 rounded-xl">
      <!-- Creating Tabs -->
      <v-tabs v-model="tab" bg-color="orange-darken-2" grow>
        <v-tab v-for="item in tabs" :key="item.value" :value="item.value">
          {{ item.label }}
        </v-tab>
      </v-tabs>

      <!-- Tab Contents -->
      <v-card-text>
        <v-window v-model="tab" v-if="gameDataFetched">
          <!-- Want To Play Tab -->
          <v-window-item value="wantToPlay">
            <!-- If there are no games in this tab, display a message -->
            <div v-if="tabs[0].games.length == 0" class="text-center">
              <h3 class="text-center">No games in Want to Play</h3>
            </div>

            <v-card
              v-else
              v-for="game in tabs[0].games"
              :key="game.id"
              class="game-title d-flex my-3 py-1 align-center"
            >
              <v-card-title class="text-truncate" @click="goToGame(game.id)">{{
                game.name
              }}</v-card-title>

              <!-- Remove Button -->
              <v-card-actions class="ml-auto align-center">
                <v-btn
                  @click="removeGame(game.id)"
                  class="removeButton my-auto"
                >
                  <font-awesome-icon icon="trash" />
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-window-item>

          <!-- Playing Tab -->
          <v-window-item value="playing">
            <!-- If there are no games in this tab, display a message -->
            <div v-if="tabs[1].games.length == 0" class="text-center">
              <h3 class="text-center">Not Playing any games right now</h3>
            </div>

            <v-card
              v-else
              v-for="game in tabs[1].games"
              :key="game.id"
              class="game-title d-flex my-3"
            >
              <v-card-title @click="goToGame(game.id)">{{
                game.name
              }}</v-card-title>

              <!-- Remove Button -->
              <v-card-actions class="ml-auto">
                <v-btn @click="removeGame(game.id)" class="removeButton">
                  <font-awesome-icon icon="trash" />
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-window-item>

          <!-- Completed Tab -->
          <v-window-item value="completed">
            <!-- If there are no games in this tab, display a message -->
            <div v-if="tabs[2].games.length == 0" class="text-center">
              <h3 class="text-center">No games Dropped</h3>
            </div>

            <v-card
              v-else
              v-for="game in tabs[2].games"
              :key="game.id"
              class="game-title d-flex my-3"
            >
              <v-card-title @click="goToGame(game.id)">{{
                game.name
              }}</v-card-title>

              <!-- Remove Button -->
              <v-card-actions class="ml-auto">
                <v-btn @click="removeGame(game.id)" class="removeButton">
                  <font-awesome-icon icon="trash" />
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-window-item>

          <!-- Dropped Tab -->
          <v-window-item value="dropped">
            <!-- If there are no games in this tab, display a message -->
            <div v-if="tabs[3].games.length == 0" class="text-center">
              <h3 class="text-center">No games Dropped</h3>
            </div>

            <v-card
              v-else
              v-for="game in tabs[3].games"
              :key="game.id"
              class="game-title d-flex my-3"
            >
              <v-card-title @click="goToGame(game.id)">{{
                game.name
              }}</v-card-title>

              <!-- Remove Button -->
              <v-card-actions class="ml-auto">
                <v-btn @click="removeGame(game.id)" class="removeButton">
                  <font-awesome-icon icon="trash" />
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-window-item>

          <!-- On Hold Tab -->
          <v-window-item value="onHold">
            <!-- If there are no games in this tab, display a message -->
            <div v-if="tabs[4].games.length == 0" class="text-center">
              <h3 class="text-center">No games On Hold</h3>
            </div>

            <v-card
              v-else
              v-for="game in tabs[4].games"
              :key="game.id"
              class="game-title d-flex my-3"
            >
              <v-card-title @click="goToGame(game.id)">{{
                game.name
              }}</v-card-title>

              <!-- Remove Button -->
              <v-card-actions class="ml-auto">
                <v-btn @click="removeGame(game.id)" class="removeButton">
                  <font-awesome-icon icon="trash" />
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-window-item>
        </v-window>

        <!-- If games are still being processed show loading icon -->
        <div v-else>
          <div class="loading-spinner"></div>
        </div>
      </v-card-text>
    </v-card>
  </v-main>
</template>
  
<script>
import { nextTick } from "vue";

export default {
  emits: ["mustLogInAlert"],

  data() {
    return {
      username: "",
      tab: null,
      tabs: [
        { value: "wantToPlay", label: "Want to Play", games: [] },
        { value: "playing", label: "Playing", games: [] },
        { value: "completed", label: "Completed", games: [] },
        { value: "dropped", label: "Dropped", games: [] },
        { value: "onHold", label: "On Hold", games: [] },
      ],
      userData: null,
      gameDataFetched: false,

      alertMessage: "",
      alertType: "",
      alertIcon: "",
    };
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

    // Go to game page given game id
    goToGame(gameID) {
      this.$router.push("/game/" + gameID);
    },

    async getUserData() {
      // if this.username is undefined, redirect to home page
      if (!localStorage.getItem("signInObj")) {
        this.$emit("mustLogInAlert");
        this.$router.push("/");
        return false;
      }
      const response = await fetch(
        "http://localhost:4040/api/users/" + this.username
      );
      const data = await response.json();
      this.userData = data;

      console.log(this.userData)

      // assign games to tabs
      this.tabs[0].games = this.userData.games[0];
      this.tabs[1].games = this.userData.games[1];
      this.tabs[2].games = this.userData.games[2];
      this.tabs[3].games = this.userData.games[3];
      this.tabs[4].games = this.userData.games[4];
    },

    // Returns name and gameID of every gameID in gameIDs
    async getGames(gameIDs) {
      const postData = JSON.stringify({
        gameIDs: gameIDs,
      });

      const response = await fetch(
        "http://localhost:4040/api/igdb/rawGameData",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: postData,
        }
      );

      const data = await response.json();
      return data;
    },

    async removeGame(gameID) {
      const response = await fetch(
        "http://localhost:4040/api/users/" +
          this.username +
          "/remove_game/" +
          gameID,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
        }
      );

      const data = await response.text();
      this.showAlert(data, "info", "trash");

      // remove game from tab
      for (let i = 0; i < this.tabs.length; i++) {
        for (let j = 0; j < this.tabs[i].games.length; j++) {
          if (this.tabs[i].games[j].id == gameID) {
            this.tabs[i].games.splice(j, 1);
          }
        }
      }
    },
  },

  created: async function () {
    if (!localStorage.getItem("signInObj")) {
      this.showAlert(
        "You must be signed in to view this page",
        "error",
        "exclamation-triangle"
      );
      this.$router.push("/");
    }

    this.username = this.$route.params.username; // set username from route params

    await this.getUserData();

    for (let i = 0; i < this.tabs.length; i++) {
      if (this.tabs[i].games.length == 0) {
        continue;
      }

      this.tabs[i].games = await this.getGames(this.tabs[i].games);
    }

    this.gameDataFetched = true;
  },
};
</script>
  
  <style scoped>
.my-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 280px;
}

.avatar {
  border: 7px solid #000000;
  box-shadow: 0px 0px 15px 0px rgba(255, 128, 1, 0.75);
}

.tab-view {
  box-shadow: rgba(255, 128, 0, 0.6) 0px 0px 30px 0px;
  outline: 4px solid var(--orange);
}

.removeButton {
  background-color: #f44336;
  color: white;
  border-radius: 10px;
}

.game-title {
  font-size: 1.2rem;
  font-weight: bold;
  background-color: var(--dark);
  padding-right: 10px;
  border-radius: 1em;
}

.v-card-title {
  cursor: pointer;
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
</style>
  