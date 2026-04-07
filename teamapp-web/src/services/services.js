import UsuarioService from './UsuarioService'
import TeamService from './TeamService'

export default {
  usuarioService: new UsuarioService(import.meta.env.VITE_TEAMAPP_BASE_URL),
  teamService: new TeamService(import.meta.env.VITE_TEAMAPP_BASE_URL),
}
