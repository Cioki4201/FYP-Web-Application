<template>
    <v-dialog v-model="dialog" max-width="500">
      <v-card v-click-outside="closeModal">
        <v-card-title class="headline">Log In</v-card-title>
        <v-card-text>
          <v-form ref="form" v-model="valid">
            <v-text-field
              v-model="username"
              label="Username"
              required
            ></v-text-field>
  
            <v-text-field
              v-model="password"
              label="Password"
              type="password"
              required
            ></v-text-field> 
          </v-form>
        </v-card-text>
  
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="login" :disabled="!valid">Log In</v-btn>
          <v-btn color="secondary" @click="cancel">Cancel</v-btn>
        </v-card-actions>
  
      </v-card>
    </v-dialog>
  </template>
  
  <script>
  export default {
    name: 'LogInModal',
    data() {
      return {
        dialog: true,
        username: '',
        password: '',
        valid: false,
      }
    },
    methods: {
    async login() {
      if (this.$refs.form.validate()) {
  
        // Post Request Data
        const postData = JSON.stringify({
          username: this.username,
          password: this.password,
        })
  
        // Post Request Options
        const requestOptions = {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: postData,
        };
  
        // Post Request
        const response = await fetch( "http://localhost:4040/api/auth/signin", requestOptions);
        const data = await response.json();
        alert(data.message)

        console.log(data)
  
        // Closing Modal and Reseting Form
        this.$emit("closeLoginModal")
        this.$refs.form.reset()
      }
    },
  
    cancel() {
      this.$emit("closeLoginModal")
      this.$refs.form.reset()
    },
  
    closeModal() {
    this.$emit("closeLoginModal");
  },
  },
  }
  </script>