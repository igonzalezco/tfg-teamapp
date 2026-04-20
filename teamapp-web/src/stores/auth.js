import { defineStore } from 'pinia'
import axios from 'axios'
import services from '@/services/services'
import {
  getToken,
  setToken,
  removeToken,
  getUsuario,
  setUsuario,
  removeUsuario,
  setSelectedTeam,
  getSelectedTeam,
  removeSelectedTeam,
} from './usuario'

export const authStore = defineStore('auth', {
  state: () => ({
    token: getToken(),
    usuario: JSON.parse(getUsuario()),
    selectedTeam: JSON.parse(getSelectedTeam()),
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    hasTeams: (state) => !!state.usuario?.usuarioEquipos?.length,
    getUserTeams: (state) => state.usuario?.usuarioEquipos || [],
    getSelectedTeam: (state) => state.selectedTeam,
    getTeamId: (state) => state.selectedTeam?.equipo?.id || null,
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
            setUsuario(JSON.stringify(this.usuario))
            const team = this.usuario?.usuarioEquipos ? this.usuario.usuarioEquipos[0] : null
            this.setSelectedTeamAction(team)
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
      removeSelectedTeam()
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
            if (this.getUserTeams == []) {
              const equipos = []
              equipos.push(r.data)
              this.usuario.usuarioEquipos = equipos
            } else {
              this.usuario.usuarioEquipos.push(r.data)
            }
            setUsuario(JSON.stringify(this.usuario))
            this.setSelectedTeamAction(r.data)
            resolve()
          })
          .catch((err) => {
            reject(err)
          })
      })
    },

    async deleteTeamAction() {
      return new Promise((resolve, reject) => {
        services.teamService
          .deleteTeam(this.selectedTeam.equipo.id)
          .then((r) => {
            const index = this.getUserTeams.findIndex((team) => team === this.getSelectedTeam)
            if (index !== -1) {
              this.usuario.usuarioEquipos.splice(index, 1)
            }
            setUsuario(JSON.stringify(this.usuario))
            if (this.getUserTeams != []) {
              this.setSelectedTeamAction(this.getUserTeams[0])
            } else {
              this.setSelectedTeamAction(null)
            }
            resolve()
          })
          .catch((err) => {
            reject(err)
          })
      })
    },

    setSelectedTeamAction(team) {
      this.selectedTeam = team
      if (team) {
        setSelectedTeam(JSON.stringify(this.selectedTeam))
      } else {
        setSelectedTeam(null)
      }
    },
  },
})
