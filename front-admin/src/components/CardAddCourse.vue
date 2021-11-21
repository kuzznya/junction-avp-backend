<template>
  <div class="card card_modal">
    <img class="card__close" src="../public/close.svg" width="40px" @click="$emit('closeModal')">
    <div class="card__header">
      <div class="card__header-name">
        Add new {{ paths[this.$store.state.data.length + 1] }}
      </div>
    </div>
    <div class="card__content">
      <input class="card__input" placeholder="name" v-model="name">
      <input class="card__input" placeholder="description" v-model="description">
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
            <option :value="'question'">Question</option>
            <option :value="'text'">Text</option>
          </optgroup>
        </select>
        <input v-model="block.text" class="card__input" placeholder="text">
        <input v-show="block.type === 'question'" v-model="block.answer" class="card__input" placeholder="answer">
        <button class="card__header-button" @click="addBlock">Add</button>
      </div>
      <div class="tasks__item-block" v-for="block in this.blocks" v-bind:key="block">
        <div class="tasks__item-question">
          {{ block.text }}
        </div>
        <div class="tasks__item-answer">
          {{ block.answer }}
        </div>
      </div>
    </div>
    <div class="card__footer">
      <button class="card__button" @click="addEntity">Add</button>
    </div>
  </div>
</template>

<script>
import {PATHS} from "../constants";
import axios from "axios";

export default {
  name: "CardAddCourse",
  data() {
    return {
      name: '',
      description: '',
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
      this.block = { text: '', answer: '', type: 'question'}
    },
    addEntity(){
      switch (this.$store.state.data.length){
        case 1:
          this.addCourse({
            id: '',
            name: this.name,
            description: this.description
          });
          break;
        case 2:
          this.addStage({
            courseId: this.$store.state.id,
            name: this.name,
            description: this.description
          });
          break;
      }
    },
    async addCourse(course) {
      await axios.post(`http://home.kuzznya.space/api/v1/admin/courses`,
        course,
        {headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${this.$store.state.token}`}})
        .then(() => {
          window.console.log('success');
          this.$store.state.data[0].push(course);
          this.$emit('closeModal');
        })
        .catch(err =>  {
          window.console.log(err);
        })
    },
    async addStage(stage) {
      await axios.post(`http://home.kuzznya.space/api/v1/admin/stages`,
        stage,
        {headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${this.$store.state.token}`}})
        .then(() => {
          this.getStages(stage.courseId);
        })
        .catch(err =>  {
          window.console.log(err);
        })
    },
  }
}
</script>

<style scoped>

</style>
