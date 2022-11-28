<script setup lang="ts">
import api from "@/api";

const props = defineProps<{ taskId: number }>()

const task = await api.TaskControllerApi.getStageTaskById(props.taskId).then(r => r.data)

</script>

<template>
  <b-container class="mt-3">
    <h2>Task: {{ task.name }}</h2>

    <p>{{ task.description }}</p>

    <b-row>
      <b-col>
        <b-card v-for="block in task.blocks" class="my-2 b-card-clickable">
          <b-row>
            <b-col>
              <b-card-title>{{ block.type }}</b-card-title>
            </b-col>
          </b-row>

          <b-card-text>{{ block.content }}</b-card-text>
        </b-card>
      </b-col>
    </b-row>
  </b-container>
</template>

<style scoped>

</style>