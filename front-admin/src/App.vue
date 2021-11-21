<template>
  <div style="height: 100%; width: 100%">
    <PageLogIn v-show="this.$store.state.username === ''" @logIn = "page++"></PageLogIn>
    <PageDashboard v-show="this.$store.state.username !== ''" @logOut = "page--"></PageDashboard>
  </div>
</template>

<script>
import PageLogIn from "./components/PageLogIn";
import PageDashboard from "./components/PageDashboard";

export default {
  name: 'app',
  components: {PageDashboard, PageLogIn},
  data () {
    return {
      page: 0
    }
  },
  methods: {
  }
}
</script>
