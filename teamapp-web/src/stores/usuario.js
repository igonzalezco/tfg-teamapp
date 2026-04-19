const tokenKey = 'Token'
const usuarioKey = 'Usuario'
const selectedTeamKey = 'selectedTeam'

export function getToken() {
  return localStorage.getItem(tokenKey)
}

export function setToken(token) {
  return localStorage.setItem(tokenKey, token)
}

export function removeToken() {
  return localStorage.removeItem(tokenKey)
}

export function getUsuario() {
  return localStorage.getItem(usuarioKey)
}

export function setUsuario(usuario) {
  return localStorage.setItem(usuarioKey, usuario)
}

export function removeUsuario() {
  return localStorage.removeItem(usuarioKey)
}

export function getSelectedTeam() {
  return localStorage.getItem(selectedTeamKey)
}

export function setSelectedTeam(selectedTeam) {
  return localStorage.setItem(selectedTeamKey, selectedTeam)
}

export function removeSelectedTeam() {
  return localStorage.removeItem(selectedTeamKey)
}
