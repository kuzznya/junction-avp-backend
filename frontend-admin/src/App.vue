<script setup lang="ts">
import { RouterView } from 'vue-router'
import { BNavbar } from "bootstrap-vue-3";
import {useAuth} from "@/stores/auth";
import AuthForm from "@/components/AuthForm.vue";

const auth = useAuth();
</script>

<template>
  <b-navbar variant="dark" dark="true">
    <b-navbar-brand @click="$router.push('/')" style="cursor: pointer;">Junction Admin</b-navbar-brand>

    <b-navbar-nav>
      <b-nav-item @click="auth.logOut()" v-if="auth.isAuthenticated">Log out</b-nav-item>
    </b-navbar-nav>
  </b-navbar>

  <Suspense v-if="auth.isAuthenticated">
    <RouterView />
  </Suspense>

  <b-container v-else>
    <b-row style="min-height: 75vh;">
      <b-col class="m-auto">
        <AuthForm/>
      </b-col>
    </b-row>
  </b-container>
</template>
