<script setup lang="ts">
import api from "@/api";
import {ref} from "vue";
import type {Task} from "@/api/generated";
import {useRouter} from "vue-router";

const router = useRouter()

const props = defineProps<{ stageId: number }>()

const deleteTaskDialogEnabled = ref(false)

const taskToDelete = ref<Task | null>(null)

const stage = await api.StageControllerApi.getStageById(props.stageId).then(r => r.data)
  .then(data => ref(data))

function newTask() {
  router.push({ name: 'newTask', params: {stageId: props.stageId} })
}

async function onTaskClick(taskId?: number) {
  if (taskId == null)
    return
  await router.push({ name: 'task', params: {taskId: taskId} })
}

function onDeleteClick(task: Task) {
  taskToDelete.value = task
  deleteTaskDialogEnabled.value = true
}

async function deleteTask() {
  const task = taskToDelete.value
  if (task == null)
    return
  await api.AdminTaskControllerApi.deleteTask(task.id!!)
  stage.value = await api.StageControllerApi.getStageById(props.stageId).then(r => r.data)
}
</script>

<template>
  <b-container class="mt-3">
    <h2>Stage: {{ stage.name }}</h2>

    <p>{{ stage.description }}</p>

    <b-row>
      <b-col class="mx-6">
        <b-button size="sm"
                  variant="light"
                  class="fw-bold mx-6"
                  @click="newTask"
        >
          Add
        </b-button>
      </b-col>
    </b-row>

    <b-row>
      <b-col>
        <b-card v-for="task in stage.tasks" @click="onTaskClick(task.id)" class="my-2 b-card-clickable">
          <b-row>
            <b-col>
              <b-card-title>{{ task.name }}</b-card-title>
            </b-col>

            <b-col class="text-end">
              <b-button @click.stop="onDeleteClick(task)" class="mr-2" variant="light">
                <i class="bi bi-trash"></i>
              </b-button>
            </b-col>
          </b-row>
          <b-card-text>{{ task.description }}</b-card-text>
        </b-card>
      </b-col>
    </b-row>

    <b-modal title="Delete task"
             v-model="deleteTaskDialogEnabled"
             @ok="deleteTask"
             :hide-header-close="true"
    >
      <p>Are you sure want to delete task "{{ taskToDelete?.name }}"</p>
    </b-modal>

    <h3 class="mt-3">Checkpoint:</h3>
    <b-row v-if="stage.checkpoint != null">
      <b-col>
        <b-card class="my-2">
          <b-row>
            <b-col>
              <b-card-title>{{ stage.checkpoint?.name }}</b-card-title>
            </b-col>
          </b-row>

          <b-card-text>{{ stage.checkpoint?.description }}</b-card-text>

          <b-row>
            <b-col>
              <b-button variant="primary" :to="{name: 'submissions', params: {checkpointId: stage.checkpoint?.id}}">Submissions</b-button>
            </b-col>
          </b-row>
        </b-card>
      </b-col>
    </b-row>
  </b-container>
</template>

<style scoped>

</style>