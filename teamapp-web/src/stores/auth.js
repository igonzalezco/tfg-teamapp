import { defineStore } from 'pinia'
import axios from 'axios'
import services from '@/services/services'
import { getToken, setToken, removeToken, getUsuario, setUsuario, removeUsuario, getEquipos, setEquipos, removeEquipos } from './usuario'

export const authStore = defineStore('auth', {
  state: () => ({
    token: getToken(),
    usuario: getUsuario(),
    equipos: getEquipos(),
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
  },
  actions: {
    async login(usuario) {
      return new Promise((resolve, reject) => {
        services.usuarioService.login(usuario)
        .then(r => {
          const token = r.token;
          axios.defaults.headers.common["Authorization"] = "Bearer" + token;
          this.token = token;
          setToken(token);
          resolve();
        }).catch(err => {
          reject(err)
        })
      });
    },

    async getSessionData(username) {
      return new Promise((resolve, reject) => {
        services.usuarioService.getSessionData(username)
        .then(r => {
          const usuario = r;
          this.usuario = usuario;
          setUsuario(usuario);
          this.equipos = usuario.usuarioEquipos;
          setEquipos(usuarioEquipos);
          resolve();
        }).catch(err => {
          reject(err);
        });
      });
    },

    logout() {
      removeToken();
      removeUsuario();
      removeEquipos();
      this.token = null;
      this.usuario = null;
      this.equipos = null;
      delete axios.defaults.headers.common["Authorization"];
    }
  },
})