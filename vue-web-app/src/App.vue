<template>
  <v-app>
    <!-- Main app container -->
    <v-navigation-drawer v-model="sidebar" app class="text-red">
      <!-- Sidebar navigation -->
      <v-list>
        <!-- List of navigation items -->
        <v-list-item
          v-for="item in menuItems"
          :key="item.title"
          :to="item.path"
        >
          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <span>{{ item.title }}</span>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-toolbar app>
      <!-- Toolbar navigation -->
      <span class="hidden-sm-and-up">
        <v-app-bar-nav-icon @click="sidebar = !sidebar"> </v-app-bar-nav-icon>
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
        <v-btn flat @click="toggleLoginModal" v-if="!loggedIn">
          Log In
        </v-btn>

        <!-- Sign Up Button -->
        <v-btn flat @click="toggleSignupModal" v-if="!loggedIn">
          Sign Up
        </v-btn>

        <!-- Log Out Button -->
        <v-btn flat @click="logOut" v-if="loggedIn">
          Log Out
        </v-btn>
      </v-toolbar-items>

    </v-toolbar>

    <v-main>
      <!-- Main content area -->
      <router-view></router-view>
      <!-- Render the appropriate component based on the current URL -->
    </v-main>
  </v-app>

  <!-- Sign Up Modal -->
  <SignUpModal v-if="showSignupModal" @closeSignupModal="toggleSignupModal" />

  <!-- Log In Modal -->
  <LogInModal v-if="showLoginModal" @closeLoginModal="toggleLoginModal" @changeLoginStatus="loggedIn = !loggedIn"/>
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
      loggedIn: false,
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
    },

    logOut() {
      localStorage.removeItem("signInObj");
      this.loggedIn = false;
      console.log("Successfully logged out")
    }
  },

  created() {
    // Check if user is logged in
    if (localStorage.getItem("signInObj")){
      this.loggedIn = true;
      console.log("User is logged in")
    } else {
      this.loggedIn = false;
      console.log("User is not logged in")
    }
  }
}
</script>


<style>
.v-application {
  background-color: #ffffff;
}
</style>
