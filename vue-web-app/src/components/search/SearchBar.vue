<template>
  <div class="search-bar">
    <input
      type="text"
      v-model="searchText"
      placeholder="Search For Games..."
      class="search-input"
    />
    <button @click="search" class="search-button">Search</button>
  </div>

  <!-- Loading Modal -->
  <div v-if="searching" class="loading-modal">
    <div class="loading-modal-content">
      <div class="loading-spinner"></div>
      <h4>Searching For "{{ searchText }}"...</h4>
    </div>
  </div>
</template>
  
<script>
import SearchResults from "./SearchResults.vue";

export default {
  components: {
    SearchResults,
  },

  data() {
    return {
      searchText: "",
      searchResult: null,
      imagesFetched: false,
      searching: false,
    };
  },
  methods: {
    async search() {
      this.imagesFetched = false;
      this.searching = true;
      // Call an API or perform a search action with the search text
      console.log(
        "Performing search for:",
        "http://localhost:4040/api/igdb/search/" + this.searchText
      );

      const response = await fetch(
        "http://localhost:4040/api/igdb/search/" + this.searchText
      );
      const games = await response.json();

      this.searchResult = games;

      // if a game does not have a cover ID remove it from the list
      this.searchResult = this.searchResult.filter((game) => game.cover);

      const coverUrls = await Promise.all(
        games.map((game) =>
          fetch("http://localhost:4040/api/igdb/cover/" + game.cover).then(
            (response) => response.text()
          )
        )
      );
      games.forEach((game, i) => (game.coverUrl = coverUrls[i]));

      this.imagesFetched = true;
      this.searching = false;
      console.log("Search Results Updated");

      this.$router.push({
        name: "SearchResultsView",
        query: {
          games: JSON.stringify(this.searchResult),
          imagesFetched: JSON.stringify(this.imagesFetched),
          searching: JSON.stringify(this.searching),
        },
      });

      setTimeout(function () {
        window.location.reload();
      }, 10);
    },
  },
};
</script>
  
  <style>
.search-bar {
  margin-top: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-input {
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 15px;
  margin-right: 10px;
  width: 300px;
  background-color: white;
}

.search-button {
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: #fff;
  cursor: pointer;
}

.search-button:hover {
  background-color: #0062cc;
}

.loading-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.loading-modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
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