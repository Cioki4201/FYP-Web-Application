import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import '@/assets/global.css';
import 'swiper/css';

// Import FontAwesome packages and components
import { library } from '@fortawesome/fontawesome-svg-core';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

// Importing Alert Component
import AutoFadeAlertVue from './components/AutoFadeAlert.vue';

// Add the Font Awesome icons to the library
library.add(fas);

const app = createApp(App);

app
  .component('AutoFadeAlert', AutoFadeAlertVue)
  .component('font-awesome-icon', FontAwesomeIcon)
  .use(router)
  .use(vuetify)
  .mount('#app')