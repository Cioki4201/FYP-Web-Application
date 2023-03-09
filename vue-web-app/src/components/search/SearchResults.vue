<template>
  <div>
    <div v-if="imagesFetched && !searching">
      <div v-for="game in games" :key="game.name" class="gameCard">
        <h3>{{game.name}}</h3>
        <img :src="game.coverUrl" @error="$event.target.src='https://publications.iarc.fr/uploads/media/default/0001/02/thumb_1205_default_publication.jpeg'"/>
      </div>
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
  methods: {},
};
</script>

<style>
.gameCard {
  display: inline-block;
  width: 18%;
  margin: 10px;
  border-radius: 20px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  transition: 0.3s;
  padding: 1%;
  background-color: #ffe3e3;
}

/* make the image responsive */
.gameCard img {
  width: auto;
  height: 100%;
  border-radius: 20px;
  border: #ffacac 7px solid;
}

/* when the mouse hovers over the card, add an effect */
.gameCard:hover {
  box-shadow: 0 8px 16px 0 rgba(255, 0, 0, 0.4);
  cursor: pointer;
  background-color: #ff5656;
}

/* when the mouse hovers over the card, add an effect */
.gameCard:hover img{
  border: #d55b5b 7px solid;
}



/* Loading Icon if Images not fetched */
.loader {
  /* center the icon */
  margin : 5% auto;
  border: 16px solid #f3f3f3; /* Light grey */
  border-top: 16px solid #3498db; /* Blue */
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