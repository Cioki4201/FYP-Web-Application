<template>
  <div>
    <div v-if="imagesFetched && !searching">
      <v-container>
        <v-row>
          <v-col v-for="game in games" :key="game.name" cols="12" sm="6" md="4" lg="3">
            <v-card @click="goToGamePage(game.id)" class="game-card">
              <v-img :src="game.coverUrl"/>
              <v-card-title>{{game.name}}</v-card-title>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </div>
    <div v-if="searching">
      <div class="loader" v-if="!imagesFetched">
      </div>
    </div>
  </div>
</template>


<script>
export default {
  props: ["games", "imagesFetched", "searching"],

  data() {
    return {};
  },

  methods: {
    goToGamePage(gameId) {
      this.$router.push({path: `/game/${gameId}`})
    }
  },
};
</script>


<style>

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

/* LOADING ICON */
.loader {
  margin : 5% auto;
  border: 16px solid #f3f3f3;
  border-top: 16px solid #3498db;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 2s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>