<script setup lang="ts">
import api from "@/api";
import {ref} from "vue";
import {CheckpointReview, CheckpointSubmissionResult} from "@/api/generated";

const props = defineProps<{checkpointId: number}>()

const checkpoint = await api.CheckpointControllerApi.getStageCheckpoint(props.checkpointId).then(r => r.data)

const submissions = await api.AdminCheckpointControllerApi.getSubmissions(props.checkpointId).then(r => r.data)
  .then(data => ref(data))

const teamNames = ref<{[id: number] : string}>({})

async function fetchTeamNames() {
  const newValue: {[id: number] : string} = {}
  for (const submission of submissions.value) {
    newValue[submission.teamId as number] = await fetchTeamName(submission.teamId as number)
  }
  teamNames.value = newValue
}

async function fetchTeamName(teamId: number) {
  return await api.AdminTeamControllerApi.getTeam(teamId).then(r => r.data).then(t => t.name)
}

await fetchTeamNames()

const reviews = submissions.value.map(() => ({
  review: ref(''),
  accepted: ref(false),
  points: ref(0)
}))

function getAnswer(submission: CheckpointSubmissionResult, blockId: number | undefined) {
  return (submission.content || {})[blockId ?? 0] ?? '[not provided]'
}

async function sendReview(submissionId: number | undefined) {
  if (submissionId == undefined)
    return
  const idx = submissions.value.findIndex(s => s.id == submissionId)
  try {
    const submission: CheckpointReview = {
      review: reviews[idx].review.value,
      status: reviews[idx].accepted.value ? 'ACCEPTED' : 'DECLINED',
    }
    if (reviews[idx].accepted.value)
      submission.points = reviews[idx].points.value
    await api.AdminCheckpointControllerApi.submitReview(submissionId, {
      review: reviews[idx].review.value,
      status: reviews[idx].accepted.value ? 'ACCEPTED' : 'DECLINED',
      points: reviews[idx].accepted.value ? reviews[idx].points.value : undefined
    })
    submissions.value = await api.AdminCheckpointControllerApi.getSubmissions(props.checkpointId).then(r => r.data)
    await fetchTeamNames()
  } catch (e) {
    console.log("fuck")
  }
}

function updateReview(idx: number, val: string) {
  reviews[idx].review.value = val
}

function updateAccepted(idx: number, val: boolean) {
  reviews[idx].accepted.value = val
}

function updatePoints(idx: number, val: number) {
  reviews[idx].points.value = val
}
</script>

<template>
  <b-container class="mt-3">
    <h2>Submissions for checkpoint: {{ checkpoint.name }}</h2>

    <b-row>
      <b-col>
        <b-card v-for="(submission, idx) in submissions" class="my-2 b-card-clickable">
          <b-row>
            <b-col>
              <b-card-title>{{ teamNames[submission?.teamId ?? 0] ?? '[failed to fetch name]' }}</b-card-title>
            </b-col>
          </b-row>

          <b-row v-for="block in checkpoint.blocks">
            <b-col class="border-top">
              <b-card-text>{{ block.content }}</b-card-text>
              <b-card-text v-if="block.type === 'QUESTION'">
                Answer: {{ getAnswer(submission, block.id) }}
              </b-card-text>
            </b-col>
          </b-row>

          <b-row class="border-top mt-3" v-if="submission.status === 'IN_REVIEW'">
            <b-col>
              <label :for="`review-input-${idx}`">Review:</label>
              <b-form-input :id="`review-input-${idx}`" :model-value="reviews[idx].review.value" @update:model-value="val => updateReview(idx, val)"></b-form-input>

              <label :for="`accepted-input-${idx}`">Accepted:</label>
              <b-form-checkbox :id="`accepted-input-${idx}`" :model-value="reviews[idx].accepted" @update:model-value="val => updateAccepted(idx, val)"></b-form-checkbox>

              <label :for="`points-input-${idx}`">Points:</label>
              <b-form-input type="number" :id="`points-input-${idx}`" :model-value="reviews[idx].points.value" @update:model-value="val => updatePoints(idx, val)" v-if="reviews[idx].accepted"></b-form-input>

              <b-button variant="primary" @click="sendReview(submission.id)">Review</b-button>
            </b-col>
          </b-row>

          <b-row class="botder-top mt-3" v-else>
            <b-col>
              <p>Review: {{ submission.review }}</p>

              <p>Accepted: <b-form-checkbox class="d-inline-block" :id="`accepted-${idx}`" :model-value="submission.status === 'ACCEPTED'" disabled/></p>

              <p v-if="submission.status === 'ACCEPTED'">Points: {{ submission.points }}</p>
            </b-col>
          </b-row>
        </b-card>
      </b-col>
    </b-row>
  </b-container>
</template>

<style scoped>

</style>