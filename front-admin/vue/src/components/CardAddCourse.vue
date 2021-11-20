<template>
  <div class="card card_modal">
    <img class="card__close" src="src/public/close.svg" width="40px" @click="$emit('closeModal')">
    <div class="card__header">
      <div class="card__header-name">
        Add new {{ paths[this.$store.state.data.length + 1] }}
      </div>
    </div>
    <div class="card__content">
      <input class="card__input" placeholder="name">
      <input class="card__input" placeholder="description">
    </div>
    <div v-show="this.$store.state.data.length === 3" class="card__task">
      <div class="card__task-row">
      <input type="number" class="card__input card__task-index" placeholder="index">
      <select class="card__header-button" style="font-weight: 400" v-model="taskType">
        <optgroup label="Task type">
          <option>Task</option>
          <option>Checkpoint</option>
        </optgroup>
      </select>
      </div>
      <div class="card__task-block">
        <div class="card__task-block-name">
          Block
        </div>
        <select class="card__header-button" style="font-weight: 400" v-model="block.type">
          <optgroup label="Block type">
            <option :value="'text'">Text</option>
            <option :value="'question'">Question</option>
          </optgroup>
        </select>
        <input v-model="block.question" class="card__input" placeholder="text">
        <input v-show="block.type === 'question'" v-model="block.answer" class="card__input" placeholder="answer">
        <button class="card__header-button" @click="addBlock">Add</button>
      </div>
      <div class="tasks__item-block" v-for="block in this.blocks">
        <div class="tasks__item-question">
          {{ block.text }}
        </div>
        <div class="tasks__item-answer">
          {{ block.answer }}
        </div>
      </div>
    </div>
    <div class="card__footer">
      <button class="card__button">Add</button>
    </div>
  </div>
</template>

<script>
import {PATHS} from "../constants";

export default {
  name: "CardAddCourse",
  data() {
    return {
      taskType: 'Task',
      paths: PATHS,
      block: {
        type: 'question',
        text: '',
        answer: '',
      },
      blocks: []
    }
  },
  methods: {
    addBlock(){
      if (this.block.type === "question"){
        this.blocks.push({text: this.block.text, answer: this.block.answer, type: this.block.type});
      }
      else {
        this.blocks.push({text: this.block.text, type: this.block.type});
      }
      this.block = { question: '', answer: '', type: 'question'}
    }
  }
}
</script>

<style scoped>

</style>
