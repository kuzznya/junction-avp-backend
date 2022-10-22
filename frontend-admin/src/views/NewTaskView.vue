<script setup lang="ts">
import api from "@/api";
import {computed, ref} from "vue";
import type {TaskBlockRequest} from "@/api/generated";
import {TaskBlockRequestTypeEnum} from "@/api/generated";
import ErrorModal from "@/components/ErrorModal.vue";
import {useRouter} from "vue-router";

const router = useRouter()

const props = defineProps<{ stageId: number }>()

const stage = await api.StageControllerApi.getStageById(props.stageId).then(r => r.data)

const name = ref("")
const description = ref("")

const blocks = ref<TaskBlockRequest[]>([])

const blockTypeOptions: TaskBlockRequestTypeEnum[] = Object.values(TaskBlockRequestTypeEnum)

const error = ref<Error | string | null>(null)

function allDataProvided(): boolean {
  return name.value.length > 0 &&
    description.value.length > 0 &&
    blocks.value.length > 0 &&
    blocks.value.every(block =>
      block.type != null &&
      blockTypeOptions.indexOf(block.type) >= 0 &&
      block.content.length > 0 && (block.type == 'TEXT' || block.answer != null)
    ) // TODO 22.10 add check that answer is filled, currently impossible because answer type is object, not string
}

const dataProvided = computed(allDataProvided)


function addBlock() {
  blocks.value.push({
    type: TaskBlockRequestTypeEnum.Text,
    content: ""
  })
}

function deleteBlock(index: number) {
  blocks.value.splice(index, 1)
}

async function createTask() {
  blocks.value.forEach(block => {
    if (block.answer != null) // @ts-ignore TODO fix answer type
      block.answer = JSON.stringify(block.answer)
  })
  try {
    await api.AdminTaskControllerApi.createNewTask({
      stageId: props.stageId,
      name: name.value,
      description: description.value,
      blocks: blocks.value
    })
    await router.push({ name: 'stage', params: { stageId: props.stageId } })
  } catch (e) {
    error.value = e instanceof Error ? e : e as string;
  }
}

// @ts-ignore TODO remove
function blockAnswer(block): string {
  return block.answer
}
</script>

<template>
  <b-container class="mt-3">
    <h2>New task</h2>

    <p>Stage: {{ stage.name }}</p>

    <label for="name-input">Name:</label>
    <b-form-input id="name-input" v-model="name" required/>

    <label for="description-input">Description:</label>
    <b-form-input id="description-input" v-model="description" required/>

    <h3 class="mt-3">Task blocks:</h3>

    <b-row class="mt-2">
      <b-col class="mx-6">
        <b-button @click="addBlock"
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
        <b-card v-for="(block, index) in blocks" class="my-2 b-card-clickable">
          <b-row>
            <b-col>
              <b-form-select :options="blockTypeOptions" v-model="block.type"/>
            </b-col>

            <b-col class="text-end">
              <b-button @click.stop="deleteBlock(index)" class="mr-2" variant="light">
                <i class="bi bi-trash"></i>
              </b-button>
            </b-col>
          </b-row>

          <label :for="'block-content-' + index">Content:</label>
          <b-form-input :id="'block-content-' + index" v-model="block.content"/>

          <label :for="'block-answer-' + index" v-if="block.type === 'QUESTION'">Answer:</label>
          <b-form-input :id="'block-answer-' + index"  :v-model="blockAnswer(block)" v-if="block.type === 'QUESTION'"/>
        </b-card>
      </b-col>
    </b-row>

    <b-row>
      <b-col class="text-center">
        <b-button :disabled="!dataProvided" @click="createTask" variant="primary" class="m-2">Create</b-button>
        <b-button :to="{ name: 'stage', params: { stageId: props.stageId } }" variant="secondary" class="m-2">Cancel</b-button>
      </b-col>
    </b-row>

    <error-modal v-model="error"/>
  </b-container>
</template>

<style scoped>

</style>