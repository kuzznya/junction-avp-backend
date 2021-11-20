<template>
  <div style="height: 100%; width: 100%">
    <PageLogIn v-show="false"></PageLogIn>
    <PageDashboard></PageDashboard>
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
    }
  },
  methods: {
  }
}
</script>
