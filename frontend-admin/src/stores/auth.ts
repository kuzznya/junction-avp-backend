import { defineStore } from "pinia";
import api from "@/api";

export const useAuth = defineStore('auth', {
  state: () => ({
    token: null as string | null,
    role: null as string | null
  }),

  getters: {
    isAuthenticated: state => state.token != null
  },

  actions: {
    async authenticate(username: string, password: string) {
      try {
        const response = await api.AuthControllerApi.authenticate({
          username: username,
          password: password
        }).then(r => r.data)
        this.token = response.token ?? null
        this.role = response.role ?? null
      } catch (error) {
        this.token = null
        this.role = null
        throw error
      }
    },

    logOut() {
      this.token = null
      this.role = null
    }
  }
})
