<script setup lang="ts">
import {ref} from "vue";

const props = defineProps<{
  modelValue: boolean,
  callback: (name: string, description: string) => void
}>()

const emits = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
}>()

const name = ref("")
const description = ref("")

async function onOk() {
  await props.callback(name.value, description.value)
}
</script>

<template>
  <b-modal title="New stage"
           :model-value="modelValue"
           @update:model-value="value => emits('update:modelValue', value)"
           @ok="onOk"
           :hide-header-close="true"
           :ok-disabled="name.length === 0 || description.length === 0"
    >
    <label for="title-input">Name:</label>
    <b-form-input id="title-input" v-model="name" :required="true"/>

    <label for="description-input">Description:</label>
    <b-form-input id="description-input" v-model="description" :required="true"/>
  </b-modal>
</template>

<style scoped>

</style>