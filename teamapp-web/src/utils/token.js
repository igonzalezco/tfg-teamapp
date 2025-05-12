import { authStore } from "@/stores/auth"

function decodeJWT(token) {
  try {
    const payload = token.split('.')[1]
    return JSON.parse(atob(payload))
  } catch (e) {
    console.error('Token inválido', e)
    return null
  }
}

export function isTokenExpired() {
  const auth = new authStore();
  const token = auth.token;
  const decoded = decodeJWT(token)
  if (!decoded || !decoded.exp) return true

  const now = Math.floor(Date.now() / 1000) // en segundos
  return decoded.exp < now
}