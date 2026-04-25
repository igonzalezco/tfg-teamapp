import request from '@/utils/request'

class TeamService {
  baseUrl

  constructor(baseUrl) {
    this.baseUrl = baseUrl
  }

  createTeam(team) {
    var _self = this
    return request.post(_self.baseUrl + 'api/equipos', team)
  }

  deleteTeam(id) {
    var _self = this
    return request.delete(_self.baseUrl + 'api/equipos/' + id)
  }
}

export default TeamService
