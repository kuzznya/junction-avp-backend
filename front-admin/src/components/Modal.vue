<template>
  <div class="modal">
    <CardAddCourse @closeModal="$emit('closeModal')"></CardAddCourse>
  </div>
</template>

<script>
import CardAddCourse from "./CardAddCourse";
export default {
  name: "Modal",
  components: {CardAddCourse}
}
</script>

<style scoped>

</style>
