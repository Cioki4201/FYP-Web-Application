<template>
  <v-app>
    <!-- Main app container -->
    <v-navigation-drawer v-model="sidebar" app class="text-red">
      <!-- Sidebar navigation -->
      <v-list>
        <!-- List of navigation items -->
        <v-list-tile
          v-for="item in menuItems"
          :key="item.title"
          :to="item.path"
        >
          <v-list-tile-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>{{ item.title }}</v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>

    <v-toolbar app>
      <!-- Toolbar navigation -->
      <span class="hidden-sm-and-up">
        <v-toolbar-side-icon @click="sidebar = !sidebar"> </v-toolbar-side-icon>
      </span>
      <v-toolbar-title>
        <router-link to="/" style="cursor: pointer">
          <!-- Link to homepage -->
          {{ appTitle }}
        </router-link>
      </v-toolbar-title>

      <v-spacer></v-spacer>

      <v-toolbar-items class="hidden-xs-only">
        <!-- List of toolbar items -->
        <v-btn flat v-for="item in menuItems" :key="item.title" :to="item.path">
          <v-icon left dark>{{ item.icon }}</v-icon>
          {{ item.title }}
        </v-btn>

        <!-- Log In Button -->
        <v-btn flat @click="toggleLoginModal">
          Log In
        </v-btn>

        <!-- Sign Up Button -->
        <v-btn flat @click="toggleSignupModal">
          Sign Up
        </v-btn>
      </v-toolbar-items>

    </v-toolbar>

    <v-content>
      <!-- Main content area -->
      <router-view></router-view>
      <!-- Render the appropriate component based on the current URL -->
    </v-content>
  </v-app>

  <!-- Sign Up Modal -->
  <SignUpModal v-if="showSignupModal" @closeSignupModal="toggleSignupModal" />

  <!-- Log In Modal -->
  <LogInModal v-if="showLoginModal" @closeLoginModal="toggleLoginModal" />
</template>


<script>
import SignUpModal from '@/components/SignUpModal.vue'
import LogInModal from './components/LogInModal.vue';

export default {
  name: "App",

  components: {
    SignUpModal,
    LogInModal
  },

  data() {
    return {
      showSignupModal: false,
      showLoginModal: false,
      appTitle: "MyGameList",
      sidebar: false,
      menuItems: [
        { title: "Home", path: "/", icon: "logout" },
        { title: "MyList", path: "/", icon: "face" },
        { title: "Browse", path: "/", icon: "lock_open" },
      ],
    };
  },
  
  methods: {
    toggleSignupModal() {
      this.showSignupModal = !this.showSignupModal
    },

    toggleLoginModal() {
      this.showLoginModal = !this.showLoginModal
    }
  }
};
</script>


<style>
.v-application {
  background-color: #ffffff;
}
</style>
