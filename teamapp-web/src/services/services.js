import UsuarioService from './UsuarioService'


export default {
    usuarioService: new UsuarioService(import.meta.env.VITE_TEAMAPP_BASE_URL),
}