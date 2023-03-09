<template>
  <v-dialog v-model="dialog" max-width="500">
    <v-card v-click-outside="closeModal">
      <v-card-title class="headline">Registration</v-card-title>
      <v-card-text>
        <v-form ref="form" v-model="valid">

          <v-text-field
            v-model="firstName"
            :rules="nameRules"
            label="First Name"
            required
          ></v-text-field>

          <v-text-field
            v-model="lastName"
            :rules="nameRules"
            label="Last Name"
            required
          ></v-text-field>

          <v-text-field
            v-model="email"
            :rules="emailRules"
            label="Email"
            required
          ></v-text-field>

          <v-text-field
            v-model="emailConfirmation"
            :rules="emailConfirmationRules"
            label="Confirm Email"
            required
          ></v-text-field>

          <v-text-field
            v-model="username"
            :rules="usernameRules"
            label="Username"
            required
          ></v-text-field>

          <v-text-field
            v-model="password"
            :rules="passwordRules"
            label="Password"
            type="password"
            required
          ></v-text-field>

          <v-text-field
            v-model="passwordConfirmation"
            :rules="passwordConfirmationRules"
            label="Confirm Password"
            type="password"
            required
          ></v-text-field>

        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="save" :disabled="!valid">Save</v-btn>
        <v-btn color="secondary" @click="cancel">Cancel</v-btn>
      </v-card-actions>

    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'SignUpModal',
  data() {
    return {
      dialog: true,
      firstName: '',
      lastName: '',
      email: '',
      emailConfirmation: '',
      username: '',
      password: '',
      passwordConfirmation: '',
      roles: ["user"],
      nameRules: [
        v => !!v || 'Name is required',
        v => (v && v.length <= 50) || 'Name must be less than 50 characters',
      ],
      emailRules: [
        v => !!v || 'Email is required',
        v =>
          /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) ||
          'Email must be valid',
      ],
      emailConfirmationRules: [
        v =>
          !!v || 'Confirmation Email is required',
        v =>
          this.email === v || 'Email confirmation must match email',
      ],
      usernameRules: [
        v => !!v || 'Username is required',
        v => (v && v.length <= 20) || 'Username must be less than 20 characters',
      ],
      passwordRules: [
        v => !!v || 'Password is required',
        v => (v && v.length >= 8) || 'Password must be at least 8 characters',
      ],
      passwordConfirmationRules: [
        v =>
          !!v || 'Confirmation Password is required',
        v =>
          this.password === v || 'Password confirmation must match password',
      ],
      valid: false,
    }
  },
  methods: {
  async save() {
    if (this.$refs.form.validate()) {

      // Post Request Data
      const postData = JSON.stringify({
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
        username: this.username,
        password: this.password,
        roles: this.roles,
      })

      // Post Request Options
      const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: postData,
      };

      // Post Request
      const response = await fetch( "http://localhost:4040/api/auth/signup", requestOptions);
      const data = await response.json();
      alert(data.message)

      // Closing Modal and Reseting Form
      this.$emit("closeSignupModal")
      this.$refs.form.reset()
    }
  },

  cancel() {
    this.$emit("closeSignupModal")
    this.$refs.form.reset()
  },

  closeModal() {
  this.$emit("closeSignupModal");
},
},
}
</script>