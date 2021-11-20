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
export default {
  name: "HeaderBreadcrumbs",
  data(){
    return{
    }
  },
  methods: {
    moveToMain() {
      this.$store.commit('moveToMain')
    },
    moveToStages() {
      this.$store.commit('moveToStages')
    },
  }
}
</script>

<style scoped>

</style>
