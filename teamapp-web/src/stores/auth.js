import { defineStore } from 'pinia'

export const authStore = defineStore('auth', {
  state: () => ({
    sessionData: null,
    usuario: localStorage.getItem("userData")
  }),
  actions: {
    async login(usuario) {
      return new Promise((resolve, reject) => {

        this.usuario = usuario
      }).catch(err => {
        localStorage.removeItem("userData")
        reject(err)
      })
    },
    logout() {
      localStorage.removeItem("sessionData")
      localStorage.removeItem("userData")
      this.sessionData = null
      this.usuario = null
    }
  },
  getters: {
    isAuthenticated: (state) => !!state.usuario
  }
})