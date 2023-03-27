<template>
  <div class="home">
    <h1>Home</h1>
    <button @click="showAlert('Must Be Logged In')">Show Alert</button>
    <!-- create a text input and bind it to the alertMessage -->
    <AutoFadeAlert
      :message="alertMessage"
      type="error"
      icon="info-circle"
    />
  </div>
</template>

<script>
import AutoFadeAlert from '@/components/AutoFadeAlert.vue';
import { nextTick } from 'vue';

export default {
  name: 'HomeView',
  data() {
    return {
      showModal: false,
      alertMessage: '',
    };
  },
  components: {
    AutoFadeAlert,
  },
  methods: {
    showAlert(message) {
      this.alertMessage = '';
      nextTick(() => {
        this.alertMessage = message;
      });
    },
  },
};
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