import request from '@/utils/request'

class UsuarioService {
  baseUrl

  constructor(baseUrl) {
    this.baseUrl = baseUrl
  }

  login(usuario) {
    var _self = this
    return request.post(_self.baseUrl + 'login', usuario)
  }

  register(usuario) {
    var _self = this
    return request.post(_self.baseUrl + 'registro', usuario)
  }
}

export default UsuarioService
