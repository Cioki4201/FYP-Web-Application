<template>
  <div class="home">
    <SearchBar />
  </div>
</template>

<script>
import SearchBar from '@/components/search/SearchBar.vue'
export default {
  name: 'HomeView',
  data () {
    return {
      showModal: false,
    }
  },
  components: {
    SearchBar
  },
  methods: {
    
  }
}
</script>

<style>
button {
  background-color: rgb(57, 140, 255);
  color: white;
  cursor: pointer;
  border: none;
  padding: 8px;
  border-radius: 10px;
  font-weight: bold;
}
</style>