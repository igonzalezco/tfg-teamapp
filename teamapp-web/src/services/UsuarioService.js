import request from '@/utils/request'

class UsuarioService {
  baseUrl

  constructor(baseUrl) {
    this.baseUrl = baseUrl
  }

  login(usuario) {
    return request.post(`${this.baseUrl}login`, usuario)
  }

  register(usuario) {
    return request.post(`${this.baseUrl}registro`, usuario)
  }
}

export default UsuarioService
