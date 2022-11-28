<script setup lang="ts">
import api from "@/api";
import type {Course} from "@/api/generated";
import {useRouter} from "vue-router";

const router = useRouter()

const courses = await api.AdminCourseControllerApi.getAllCourses().then(r => r.data)

function onCourseClick(course: Course) {
  router.push({ name: 'course', params: { courseId: course.id } })
}
</script>

<template>
  <b-container class="mt-3">
    <h2>Courses</h2>

    <b-row>
      <b-col>
        <b-card v-for="course in courses" class="m-2 b-card-clickable" @click="onCourseClick(course)">
          <b-card-title>{{ course.name }}</b-card-title>

          <b-card-text>{{ course.description }}</b-card-text>
        </b-card>
      </b-col>
    </b-row>
  </b-container>
</template>

<style scoped>
.b-card-clickable:hover {
  background-color: #f8f8f8;
  cursor: pointer;
}
</style>