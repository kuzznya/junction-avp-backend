<template>
  <div class="card">
    <div class="card__header card__header_action">
      <div class="card__header-name">
        {{
          this.cardNames[this.$store.state.data.length]
        }}
      </div>
      <button class="card__header-button" @click="$emit('openModal')">
        Add new {{ paths[this.$store.state.data.length + 1] }}
      </button>
    </div>
    <div v-show="this.$store.state.data.length !== 3" class="card__content">
      <div class="card__list-titles">
        <div class="card__list-item-chunk card__list-item-chunk_title" v-for="item in Object.keys(this.$store.state.data[this.$store.state.data.length - 1][0] || {})" v-bind:key="item.id">
          {{ item }}
        </div>
      </div>
      <div class="card__list-item" v-for="item in this.$store.state.data[this.$store.state.data.length - 1]" v-bind:key="item.id" @click="addEntities(item.id)">
        <div class="card__list-item-chunk" v-for="key in Object.keys(item)" v-bind:key="key">{{item[key]}}</div>
      </div>
    </div>
      <div v-show="this.$store.state.data.length === 3" class="tasks">
        <div class="tasks__item" v-for="item in this.$store.state.data[this.$store.state.data.length - 1]" v-bind:key="item.id" :class="{'tasks__item_checkpoint': item.isCheckpoint}">
          <div class="tasks__item-name">
            {{item.name}}
          </div>
          <div class="tasks__item-description">
            {{item.description}}
          </div>
          <div class="tasks__item-block" v-for="block in item.blocks" v-bind:key="block">
            <div class="tasks__item-question">
              {{ block.content }}
            </div>
            <div class="tasks__item-answer">
              {{ block.content }}
            </div>
          </div>
      </div>
    </div>
  </div>
</template>

<script>
import {CARD_NAMES, PATHS} from "../constants";
import axios from "axios";

export default {
  name: "CardList",
  methods: {
    addEntities(id){
      switch (this.$store.state.data.length){
        case 1:
          this.getStages(id);
          break;
        case 2:
          this.getTasks(id);
          console.log(this.$store.state.checkpoint.id);
          break;
      }
    },
    async getStages(id) {
      await axios.get(`http://home.kuzznya.space/api/v1/courses/${id}/stages`,
        {headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${this.$store.state.token}`}})
        .then(response => {
          this.$store.commit('addId', id);
            this.$store.commit('addStages', response.data);
          }
        )
        .then(() => {
          const tasks = this.$store.state.data[this.$store.state.data.length - 1];
          this.getReviews(tasks[tasks.length - 1].id);
        })
        .catch(err =>  {
          window.console.log(err);
        })
    },
    async getTasks(id) {
      await axios.get(`http://home.kuzznya.space/api/v1/stages/${id}`,
        {headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${this.$store.state.token}`}})
        .then(response => {
            this.$store.commit('addTasks', response.data);
          }
        )
        .catch(err =>  {
          window.console.log(err);
        })
    },
    async getReviews(id){
      await axios.get(`http://home.kuzznya.space/api/v1/admin/checkpoints/${id}/submissions`,
        {headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${this.$store.state.token}`}})
        .then(response => {
            this.$store.commit('addReviews', response.data);
            console.log(response)
          }
        )
        .catch(err =>  {
          window.console.log(err);
        })
    }
  },
  data() {
    return{
      cardNames: CARD_NAMES,
      paths: PATHS
    }
  }
}
</script>

<style scoped>

</style>
