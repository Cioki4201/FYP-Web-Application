<template>
  <div class="backdrop" @click.self="closeModal">
    <div class="modal">
      <!-- SIGN UP FORM -->
      <form @submit.prevent="handleSubmit">
        <h1>Sign Up</h1>
        <label for="text">Username: </label>
        <input type="text" id="username" v-model="username" /> <br />

        <label for="email">Email:</label>
        <input type="email" id="email" v-model="email" /> <br />

        <label for="password">Password:</label>
        <input type="password" id="password" v-model="password" /> <br />

        <div class="roles">
          <p>Roles:</p>
          <label>Admin:</label>
          <input type="checkbox" id="admin" value="admin" v-model="roles" />

          <label>User:</label>
          <input type="checkbox" id="user" value="user" v-model="roles" />

          <label>Moderator:</label>
          <input
            type="checkbox"
            id="moderator"
            value="moderator"
            v-model="roles"
          />
        </div>

        <div class="terms">
          <label>Accept Terms and Conditions</label>
          <input type="checkbox" required />
        </div>

        <div class="submit">
          <button>Create an Account</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: "",
      email: "",
      password: "",
      roles: [],
    };
  },

  methods: {
    closeModal() {
      this.$emit("closeModal");
    },

    async handleSubmit() {
      const postData = JSON.stringify({
        username: this.username,
        email: this.email,
        password: this.password,
        roles: this.roles,
      });

      const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: postData,
      };

      const response = await fetch( "http://localhost:8080/api/auth/signup", requestOptions);
      const data = await response.json();
      alert(data.message)
      this.$emit("closeModal")
    },
  },
};
</script>

<style scoped>
.backdrop {
  top: 0;
  position: fixed;
  background: rgba(0, 0, 0, 0.5);
  width: 100%;
  height: 100%;
}

.modal {
  width: 400px;
  padding: 20px;
  margin: 100px auto;
  background: white;
  border-radius: 10px;
}

form {
  max-width: 420px;
  margin: 30px auto;
  background: white;
  text-align: left;
  padding: 40px;
  border-radius: 10px;
}

input {
  display: inline;
  margin: 20px 15px auto 3px;
  box-sizing: border-box;
  border: none;
  border-bottom: 1px solid #ddd;
  color: #555;
}

button {
  background: #0b6dff;
  border: 0;
  padding: 10px 20px;
  margin-top: 20px;
  color: white;
  border-radius: 20px;
  cursor: pointer;
}

.submit {
  text-align: center;
}
</style>