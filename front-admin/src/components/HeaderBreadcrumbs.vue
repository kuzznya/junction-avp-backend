<template>
  <div class="header__breadcrumbs">
    <div class="header__breadcrumbs-item" @click="moveToMain">Main</div>
    <div v-show="this.$store.state.data.length > 1" class="header__breadcrumbs-separator">/</div>
    <div v-show="this.$store.state.data.length > 1" class="header__breadcrumbs-item" @click="moveToStages">Course</div>
    <div v-show="this.$store.state.data.length > 2" class="header__breadcrumbs-separator">/</div>
    <div v-show="this.$store.state.data.length > 2" class="header__breadcrumbs-item">Stage</div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "HeaderBreadcrumbs",
  data(){
    return{
    }
  },
  methods: {
    moveToMain() {
      this.$store.commit('moveToMain');
      this.getCourses();
    },
    moveToStages() {
      this.$store.commit('moveToStages')
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
