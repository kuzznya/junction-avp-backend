<template>
  <div class="header">
    <div class="header__content">
      <div class="header__logo">
        <img src="src/public/logo.svg" width="40">
        <div class="header__logo-name">
          Admin
        </div>
      </div>
      <div class="header__user">
        <div class="header__user-name">{{ this.$store.state.username }}</div>
        <button class="header__user-logout">
          <img src="src/public/log-out.svg" width="20">
        </button>
      </div>
    </div>
    <HeaderBreadcrumbs></HeaderBreadcrumbs>
  </div>
</template>

<script>
import HeaderBreadcrumbs from "./HeaderBreadcrumbs";
export default {
  name: "Header",
  components: {HeaderBreadcrumbs}
}
</script>

<style scoped>

</style>
