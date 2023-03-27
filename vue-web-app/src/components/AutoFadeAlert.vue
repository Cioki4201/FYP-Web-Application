<template>
    <transition name="fade">
      <v-alert
        v-if="showAlert"
        :type="type"
        :icon="false"
        class="auto-fade-alert"
      >
        <font-awesome-icon :icon="icon" />
        {{ message }}
      </v-alert>
    </transition>
  </template>
  
  <script>
  import { watch, ref } from 'vue';
  
  export default {
    name: 'AutoFadeAlert',
  
    props: {
      message: String,
      type: {
        type: String,
        default: '',
      },
      duration: {
        type: Number,
        default: 3000,
      },
      icon: {
        type: String,
        default: 'check',
      }
    },
  
    setup(props) {
      const showAlert = ref(false);
  
      watch(
        () => props.message,
        (newValue) => {
          if (newValue) {
            showAlert.value = true;
            setTimeout(() => {
              showAlert.value = false;
            }, props.duration);
          }
        }
      );
  
      return { showAlert };
    },
  };
  </script>
  
<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.auto-fade-alert {
  position: fixed;
  top: 5px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 2000;
  width: auto;
  white-space: nowrap;
  border-radius: 20px;
}
</style>