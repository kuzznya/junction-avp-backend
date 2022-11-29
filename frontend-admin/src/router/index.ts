import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import CourseView from '@/views/CourseView.vue'
import StageView from '@/views/StageView.vue'
import NewTaskView from '@/views/NewTaskView.vue'
import TaskView from '@/views/TaskView.vue'
import SubmissionsView from '@/views/SubmissionsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/courses/:courseId',
      name: 'course',
      component: CourseView,
      props: route => ({courseId: Number.parseInt(route.params.courseId as string)})
    },
    {
      path: '/stages/:stageId',
      name: 'stage',
      component: StageView,
      props: route => ({stageId: Number.parseInt(route.params.stageId as string)})
    },
    {
      path: '/stages/:stageId/new',
      name: 'newTask',
      component: NewTaskView,
      props: route => ({stageId: Number.parseInt(route.params.stageId as string)})
    },
    {
      path: '/tasks/:taskId',
      name: 'task',
      component: TaskView,
      props: route => ({taskId: Number.parseInt(route.params.taskId as string)})
    },
    {
      path: '/checkpoints/:checkpointId/submissions',
      name: 'submissions',
      component: SubmissionsView,
      props: route => ({checkpointId: Number.parseInt(route.params.checkpointId as string)})
    }
  ]
})

export default router
