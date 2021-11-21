<template>
  <div class="card card_modal">
    <div class="card__header">
      <img src="../public/logo.svg" width="40">
      <div class="card__header-name card__header-name_moved">
        Admin
      </div>
    </div>
    <div class="card__content">
      <input class="card__input" placeholder="login" v-model="username">
      <input type="password" class="card__input" placeholder="password" v-model="password">
    </div>
    <div class="card__footer">
      <button class="card__button" @click="authRequest">Log in</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "CardLogIn",
  data(){
    return{
      username: '',
      password: '',
    }
  },
  methods: {
    async authRequest() {
      await axios.post('http://home.kuzznya.space/api/v1/authentication',
        {username: this.username, password: this.password},
        {headers: {'Content-Type': 'application/json'}})
        .then(response => {
            this.$store.commit('changeUsername', [this.username, response.data.token]);
            window.console.log(this.$store.state);
            this.getCourses();
            localStorage.setItem('token', response.data.token);
          }
        )
        .catch(err =>  {
          window.console.log(err);
        })
    },
    async getCourses() {
      await axios.get('http://home.kuzznya.space/api/v1/admin/courses',
        {headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${this.$store.state.token}`}})
        .then(response => {
          this.$store.commit('addCourses', response.data);
          }
        )
        .catch(err =>  {
          window.console.log(err);
        })
    }
  }
}
</script>

<style scoped>
</style>
