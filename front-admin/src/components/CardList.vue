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
        <div class="card__list-item-chunk card__list-item-chunk_title" v-for="item in Object.keys(this.$store.state.data[this.$store.state.data.length - 1][0])">
          {{ item }}
        </div>
      </div>
      <div class="card__list-item" v-for="item in this.$store.state.data[this.$store.state.data.length - 1]" @click="addEntities">
        <div class="card__list-item-chunk" v-for="key in Object.keys(item)">{{item[key]}}</div>
      </div>
    </div>
      <div v-show="this.$store.state.data.length === 3" class="tasks">
        <div class="tasks__item" v-for="item in this.$store.state.data[this.$store.state.data.length - 1]">
          <div class="tasks__item-name">
            {{item.name}}
          </div>
          <div class="tasks__item-description">
            {{item.description}}
          </div>
          <div class="tasks__item-block" v-for="block in item.blocks">
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

export default {
  name: "CardList",
  methods: {
    addEntities(){
      switch (this.$store.state.data.length){
        case 1:
          this.$store.commit('addStages', [
            {
              id: 0,
              name: 'string',
              description: 'string',
              status: 'NEW'
            }
          ])
          break;
        case 2:
          this.$store.commit('addTasks', {
            "id": 0,
            "courseId": 0,
            "name": "string",
            "description": "string",
            "tasks": [
              {
                "id": 0,
                "stageId": 0,
                "name": "string",
                "description": "string",
                "index": 0,
                "blocks": [
                  {
                    "id": 0,
                    "content": "string",
                    "type": "TEXT",
                    "index": 0
                  }
                ],
                "points": 0
              }
            ],
            "checkpoint": {
              "id": 0,
              "stageId": 0,
              "name": "checkpoint",
              "description": "string",
              "blocks": [
                {
                  "id": 0,
                  "content": "string",
                  "type": "TEXT",
                  "index": 0
                }
              ],
              "status": "NEW",
              "points": 0
            }
          })
          break;
      }
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
