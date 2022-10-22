<script setup lang="ts">
import api from "@/api";
import {ref} from "vue";
import NewStageDialog from "@/components/NewStageDialog.vue";
import type {Stage} from "@/api/generated";
import {useRouter} from "vue-router";

const router = useRouter()

const props = defineProps<{courseId: number}>()

const newStageDialogEnabled = ref(false)

const deleteStageDialogEnabled = ref(false)

const stageToDelete = ref<Stage | null>(null)

const course = await api.AdminCourseControllerApi.getAllCourses().then(r => r.data)
  .then(courses => courses.find(course => course.id = props.courseId)) // TODO create endpoint for that
  ?? { name: 'undefined', description: 'undefined' }

const stages = await api.CourseControllerApi.getStages(props.courseId).then(r => r.data)
  .then(data => ref(data))

function onStageClick(id?: number) {
  if (id == null)
    return
  router.push({ name: 'stage', params: {stageId: id} })
}

function onDeleteClick(stage: Stage) {
  stageToDelete.value = stage
  deleteStageDialogEnabled.value = true
}

async function deleteStage() {
  const stage = stageToDelete.value
  if (stage == null)
    return
  await api.AdminStageControllerApi.deleteStage(stage.id!!)
  stages.value = await api.CourseControllerApi.getStages(props.courseId).then(r => r.data)
}

function enableNewStageDialog() {
  newStageDialogEnabled.value = true
}

async function addStage(name: string, description: string) {
  await api.AdminStageControllerApi.createStage({
    courseId: props.courseId,
    name: name, description:
    description
  })
  stages.value = await api.CourseControllerApi.getStages(props.courseId).then(r => r.data)
}
</script>

<template>
  <b-container class="mt-3">
    <h2>{{ course.name }}</h2>

    <p>{{ course.description  }}</p>

    <b-row>
      <b-col class="mx-6">
        <b-button @click="enableNewStageDialog"
                  size="sm"
                  variant="light"
                  class="fw-bold mx-6"
        >
          Add
        </b-button>
      </b-col>
    </b-row>

    <b-row>
      <b-col>
        <b-card v-for="stage in stages" @click="onStageClick(stage.id)" class="my-2 b-card-clickable">
          <b-row>
            <b-col>
              <b-card-title>{{ stage.name }}</b-card-title>
            </b-col>

            <b-col class="text-end">
              <b-button @click.stop="onDeleteClick(stage)" class="mr-2" variant="light">
                <i class="bi bi-trash"></i>
              </b-button>
            </b-col>
          </b-row>
          <b-card-text>{{ stage.description }}</b-card-text>
        </b-card>
      </b-col>
    </b-row>

    <new-stage-dialog v-model="newStageDialogEnabled" :callback="addStage"/>

    <b-modal title="Delete stage"
             v-model="deleteStageDialogEnabled"
             @ok="deleteStage"
             :hide-header-close="true"
    >
      <p>Are you sure want to delete stage "{{ stageToDelete?.name }}"?</p>
    </b-modal>
  </b-container>
</template>

<style scoped>
.b-card-clickable:hover {
  background-color: #f8f8f8;
  cursor: pointer;
}
</style>