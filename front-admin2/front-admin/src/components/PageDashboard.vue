<template>
  <div class="page">
    <Header></Header>
    <CardList @openModal="opened = true"></CardList>
    <Modal v-show="this.opened" @closeModal="opened = false"></Modal>
  </div>
</template>

<script>
import Header from "./Header";
import CardList from "./CardList";
import Modal from "./Modal";
export default {
  name: "PageDashboard",
  components: {Modal, CardList, Header},
  data(){
    return{
      opened: false
    }
  },
  methods: {
  }
}
</script>

<style scoped>

</style>
