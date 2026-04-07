import request from '@/utils/request'

class TeamService {
  baseUrl

  constructor(baseUrl) {
    this.baseUrl = baseUrl
  }

  createTeam(team) {
    var _self = this
    return request.post(_self.baseUrl + 'api/equipos', team, {
      headers: { 'Content-Type': 'application/json' },
    })
  }
}

export default TeamService
