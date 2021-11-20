<template>
  <div class="card card_modal">
    <div class="card__header">
      <img src="src/public/logo.svg" width="40">
      <div class="card__header-name card__header-name_moved">
        Admin
      </div>
    </div>
    <div class="card__content">
      <input class="card__input" placeholder="login">
      <input type="password" class="card__input" placeholder="password">
    </div>
    <div class="card__footer">
      <button class="card__button">Log in</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "CardLogIn"
}
</script>

<style scoped>
</style>
