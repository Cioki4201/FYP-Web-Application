<template>
  <div>
    <SearchResults
      :games="parsedGames"
      :imagesFetched="imagesFetched"
      :searching="searching"
    />
  </div>
</template>
  
  <script>
import SearchResults from "@/components/search/SearchResults.vue";

export default {
  name: "SearchResultsView",
  components: {
    SearchResults,
  },
  props: {
    games: {
      type: Array,
      required: true,
      default: () => [],
    },
    imagesFetched: {
      type: Boolean,
      required: true,
      default: false,
    },
    searching: {
      type: Boolean,
      required: true,
      default: false,
    },
  },

  watch: {
    $route(to, from) {
      this.refreshComponent();
    },
  },

  methods: {
    refreshComponent() {
        setTimeout(function () {
        window.location.reload()
      }, 10);
      console.log("Route changed, refreshing component...");
    },
  },

  data() {
    return {
      parsedGames: [],
    };
  },
  mounted() {
    if (typeof this.games === "string") {
      this.parsedGames = JSON.parse(this.games);
    } else {
      this.parsedGames = this.games;
    }
  },
};
</script>
  