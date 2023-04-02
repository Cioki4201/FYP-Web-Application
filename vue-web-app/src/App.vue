<template>
  <v-app>
    <!-- Main app container -->

    <!-- Alert -->
    <AutoFadeAlert
      :message="alertMessage"
      :type="alertType"
      :icon="alertIcon"
    />

    <v-toolbar app class="nav-bar">
      <!-- Toolbar navigation -->
      <span class="hidden-sm-and-up">
        <v-app-bar-nav-icon @click="sidebar = !sidebar"> </v-app-bar-nav-icon>
      </span>

      <v-toolbar-title>
        <router-link to="/" class="logo">
          <!-- Link to homepage -->
          {{ appTitle }}
        </router-link>
      </v-toolbar-title>

      <v-spacer></v-spacer>

      <!-- SEARCH BAR -->
      <SearchBar />

      <!-- TOOLBAR LINKS -->
      <v-toolbar-items class="hidden-xs-only">
        <!-- List of Navbar items -->
        <v-btn
          flat
          v-for="item in updatedMenuItems"
          :key="item.title"
          :to="item.path"
        >
          <span>
            {{ item.title }}
            <font-awesome-icon :icon="item.icon" />
          </span>
        </v-btn>

        <!-- Log In Button -->
        <v-btn flat @click="toggleLoginModal" v-if="!loggedIn">
          <span>
            Log In
            <font-awesome-icon icon="sign-in-alt" />
          </span>
        </v-btn>

        <!-- Sign Up Button -->
        <v-btn flat @click="toggleSignupModal" v-if="!loggedIn">
          <span>
            Sign Up
            <font-awesome-icon icon="user-plus" />
          </span>
        </v-btn>

        <!-- Log Out Button -->
        <v-btn flat @click="logOut" v-if="loggedIn">
          <span>
            Log Out
            <font-awesome-icon icon="sign-out-alt" />
          </span>
        </v-btn>
      </v-toolbar-items>
    </v-toolbar>

    <v-main>
      <!-- Main content area -->
      <router-view @mustLogInAlert="showAlert('You must be Logged In to view this page', 'error', 'exclamation-triangle')"></router-view>
      <!-- Render the appropriate component based on the current URL -->
    </v-main>
  </v-app>

  <!-- Sign Up Modal -->
  <SignUpModal v-if="showSignupModal" @closeSignupModal="toggleSignupModal" />

  <!-- Log In Modal -->
  <LogInModal
    v-if="showLoginModal"
    @closeLoginModal="toggleLoginModal"
    @changeLoginStatus="loggedIn = !loggedIn"
    @loggedInAlert='showAlert("Logged In Successfully", "success", "check")'
  />
</template>


<script>
import SignUpModal from "@/components/SignUpModal.vue";
import LogInModal from "./components/LogInModal.vue";
import SearchBar from "./components/search/SearchBar.vue";

import { nextTick } from 'vue';

export default {
  name: "App",

  components: {
    SignUpModal,
    LogInModal,
    SearchBar,
  },

  data() {
    return {
      currentUsername: "",
      loggedIn: false,
      showSignupModal: false,
      showLoginModal: false,
      appTitle: "MyGameList",
      sidebar: false,
      menuItems: [
        { title: "Home", path: "/", icon: "house" },
        {
          title: "MyList",
          path: "/mylist/" + this.currentUsername,
          icon: "list",
        },
        { title: "Browse", path: "/", icon: "gamepad" },
      ],

      alertMessage: "",
      alertType: "",
      alertIcon: "",
    };
  },

  methods: {
    showAlert(message, type, icon) {
      this.alertMessage = '';
      nextTick(() => {
        this.alertType = type;
        this.alertIcon = icon;
        this.alertMessage = message;
      });
    },

    toggleSignupModal() {
      this.showSignupModal = !this.showSignupModal;
    },

    toggleLoginModal() {
      this.showLoginModal = !this.showLoginModal;
    },

    logOut() {
      localStorage.removeItem("signInObj");
      this.loggedIn = false;
      console.log("Successfully logged out");
      this.showAlert("Logged Out...", "info", "check-circle");
      this.$router.push("/");
    },
  },

  created() {
    // Check if user is logged in
    if (localStorage.getItem("signInObj")) {
      this.loggedIn = true;
      console.log("User is logged in");

      // Get current username
      var signInObj = JSON.parse(localStorage.getItem("signInObj"));
      this.currentUsername = signInObj.username;
      console.log("Current username: " + this.currentUsername);
    } else {
      this.loggedIn = false;
      console.log("User is not logged in");
    }
  },

  computed: {
    // Update the path of the MyList menu item if the user is logged in
    updatedMenuItems() {
      return this.menuItems.map((item) => {
        if (item.title === "MyList") {
          return { ...item, path: `/mylist/${this.currentUsername}` };
        } else {
          return item;
        }
      });
    },
  },
  
};
</script>


<style>
.nav-bar {
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.26);
  background-color: var(--darker);
}

.search-bar {
  margin: auto 40px;
}

.logo {
  font-size: 2rem;
  color: #da0000;
  cursor: pointer;
  text-decoration: none;
  font-family: 'Bungee Spice', cursive;
}

.logo:hover {
  text-shadow: 0 0 7px rgba(255, 132, 0, 0.5);
}

body {
  background-color: blue;
}
</style>
