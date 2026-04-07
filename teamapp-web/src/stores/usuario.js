const tokenKey = 'Token'
const usuarioKey = 'Usuario'

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
