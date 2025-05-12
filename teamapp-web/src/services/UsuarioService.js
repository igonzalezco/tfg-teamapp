import request from "@/utils/request";

class UsuarioService {
    baseUrl;

    constructor(baseUrl) {
      this.baseUrl = baseUrl;
    }

    login(usuario) {
      var _self = this;
      return request.post(_self.baseUrl + 'login', usuario, {headers: {'Content-Type': 'application/json' }});
    }

    register(usuario) {
      var _self = this;
      return request.post(_self.baseUrl + 'registro', usuario, {headers: {'Content-Type': 'application/json' }});
    }

    getSessionData(username) {
      var _self = this;
      return request.get(_self.baseUrl + 'sessionData', {
        params: {
          username: username
        }
      });
    }
    
}

export default UsuarioService;