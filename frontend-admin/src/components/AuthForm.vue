<script setup lang="ts">
import {useAuth} from "@/stores/auth";
import {ref} from "vue";

const auth = useAuth()

const username = ref("")
const password = ref("")

const loading = ref(false)
const authFailed = ref(false)

async function authenticate() {
  loading.value = true
  authFailed.value = false
  try {
    await auth.authenticate(username.value, password.value)
  } catch (e) {
    password.value = ""
    authFailed.value = true
  }
  loading.value = false
}
</script>

<template>
  <b-card class="mx-auto" style="max-width: 25rem;">
    <b-card-title>Authenticate</b-card-title>

    <b-card-body>
      <label for="username-input">Username:</label>
      <b-form-input id="username-input" v-model="username" :required="true"></b-form-input>

      <label for="password-input">Password:</label>
      <b-form-input id="password-input" v-model="password" :required="true" type="password"></b-form-input>
    </b-card-body>

    <b-card-footer bg-variant="white" border-variant="white">
      <b-row>
        <b-col class="text-center">
          <b-button variant="primary"
                    @click="authenticate"
                    :disabled="username.length === 0 || password.length === 0" v-if="!loading">
            Log in
          </b-button>

          <b-spinner v-else/>
        </b-col>
      </b-row>

      <b-row class="mt-1">
        <b-col>
          <b-alert :show="authFailed" :dismissible="true" variant="danger">Authentication failed</b-alert>
        </b-col>
      </b-row>
    </b-card-footer>
  </b-card>
</template>

<style scoped>

</style>