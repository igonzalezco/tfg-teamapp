import { defineStore } from 'pinia'
import axios from 'axios'
import services from '@/services/services'
import { getToken, setToken, removeToken, getUsuario, setUsuario, removeUsuario } from './usuario'

export const authStore = defineStore('auth', {
  state: () => ({
    token: getToken(),
    usuario: getUsuario(),
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    hasTeams: (state) => !!state.usuario?.equipos?.length,
    userTeams: (state) => state.usuario?.equipos || [],
  },
  actions: {
    async login(usuario) {
      return new Promise((resolve, reject) => {
        services.usuarioService
          .login(usuario)
          .then((r) => {
            const token = r.data.token
            axios.defaults.headers.common['Authorization'] = 'Bearer' + token
            this.token = token
            this.usuario = r.data.usuario
            setToken(token)
            setUsuario(r.data.usuario)
            resolve()
          })
          .catch((err) => {
            this.logout()
            reject(err)
          })
      })
    },

    logout() {
      removeToken()
      removeUsuario()
      this.token = null
      this.usuario = null
      this.equipos = null
      delete axios.defaults.headers.common['Authorization']
    },

    async createTeamAction(team) {
      return new Promise((resolve, reject) => {
        services.teamService
          .createTeam(team)
          .then((r) => {
            //TODO añadir equipo al usuario
            resolve()
          })
          .catch((err) => {
            reject(err)
          })
      })
    },
  },
})
