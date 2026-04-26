import { authStore } from '@/stores/auth'

export const hasTeamPermission = (allowedPermissions = [], teamId) => {
  const auth = authStore()

  if (!Array.isArray(allowedPermissions) || !allowedPermissions.length) {
    return false
  }

  return (auth.getUserTeams || []).some(
    (usuarioEquipo) =>
      Number(usuarioEquipo?.equipo?.id) === Number(teamId) &&
      allowedPermissions.includes(usuarioEquipo?.permiso?.codigo)
  )
}
