<template>
  <div class="home">
    <button @click="toggleModal">Sign Up</button>
    <SignUpModal v-if="showModal" @closeModal="toggleModal"/>
    <SearchBar />
  </div>
</template>

<script>
import SignUpModal from '@/components/SignUpModal.vue'
import SearchBar from '@/components/search/SearchBar.vue'

export default {
  name: 'HomeView',

  data () {
    return {
      showModal: false,
    }
  },

  components: {
    SignUpModal,
    SearchBar
  },

  methods: {
    toggleModal() {
      this.showModal = !this.showModal
    },
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
