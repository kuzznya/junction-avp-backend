<template>
    <div class="card">
      <div class="card__header card__header_action">
        <div class="card__header-name">
          Review
        </div>
      </div>
      <div v-show="this.$store.state.data[this.$store.state.data.length - 1].length">There is no submissions</div>
      <div class="tasks">
        <div class="review" v-for="review in this.$store.state.reviews" v-bind:key="review">
          <div class="review__time">{{ review.submissionTimestamp }}</div>
<!--          <div class="review__description" v-for="answer in review.content" v-bind:key="answer">-->
<!--            {{answer}}-->
<!--          </div>-->
          <div class="review__row">
            <input class="card__input review__input" placeholder="your coomment">
            <button class="card__button review__button">Accept</button>
            <button class="card__button review__button review__button_black">Decline</button>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
export default {
  name: "CardReview",
  data(){
    return{

    }
  }
}
</script>

<style scoped>

</style>
