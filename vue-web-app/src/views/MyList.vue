<template>
  <v-main>
    <v-container class="my-container mx-auto my-6 w-400">
      <v-avatar size="200" class="avatar mx-auto mb-2" color="#000000">
        <v-img src="https://cdn.vuetifyjs.com/images/lists/1.jpg"></v-img>
      </v-avatar>
      <h1 class="mx-auto">@{{ username }}</h1>
    </v-container>

    <v-card>
      <!-- Creating Tabs -->
      <v-tabs v-model="tab" bg-color="red" grow>
        <v-tab v-for="item in tabs" :key="item.value" :value="item.value">
          {{ item.label }}
        </v-tab>
      </v-tabs>

      <!-- Tab Contents -->
      <v-card-text>
        <v-window v-model="tab" v-if="gameDataFetched">
          <v-window-item value="wantToPlay">
            <v-card v-for="game in tabs[0].games" :key="game.gameID">
              <v-card-title>{{ game.name }}</v-card-title>
            </v-card>
          </v-window-item>

          <v-window-item value="playing">
            <v-card v-for="game in tabs[1].games" :key="game.gameID">
              <v-card-title>{{ game.name }}</v-card-title>
            </v-card>
          </v-window-item>

          <v-window-item value="completed">
            <v-card v-for="game in tabs[2].games" :key="game.gameID">
              <v-card-title>{{ game.name }}</v-card-title>
            </v-card>
          </v-window-item>

          <v-window-item value="dropped">
            <v-card v-for="game in tabs[3].games" :key="game.gameID">
              <v-card-title>{{ game.name }}</v-card-title>
            </v-card>
          </v-window-item>

          <v-window-item value="onHold">
            <v-card v-for="game in tabs[4].games" :key="game.gameID">
              <v-card-title>{{ game.name }}</v-card-title>
            </v-card>
          </v-window-item>
        </v-window>

        <!-- If games are still being processed show loading icon -->
        <div v-else>
          <div class="loader"></div>
        </div>
      </v-card-text>
    </v-card>
  </v-main>
</template>
  
  <script>
export default {
  data() {
    return {
      username: this.$route.params.username,
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
    };
  },

  methods: {
    async getUserData() {
      const response = await fetch(
        "http://localhost:4040/api/users/" + this.username
      );
      const data = await response.json();
      this.userData = data;

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
  },

  created: async function () {
    await this.getUserData();

    for (let i = 0; i < this.tabs.length; i++) {
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
  box-shadow: 0px 0px 15px 0px rgba(255, 1, 1, 0.75);
}
</style>
  