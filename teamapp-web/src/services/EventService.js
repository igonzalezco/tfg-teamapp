import request from '@/utils/request'

class EventService {
  baseUrl

  constructor(baseUrl) {
    this.baseUrl = baseUrl
  }

  getTeamEvents(teamId) {
    var _self = this
    return request.get(_self.baseUrl + `/equipos/${teamId}/events`)
  }
  createTeam(team) {
    var _self = this
    return request.post(_self.baseUrl + 'api/equipos', team, {
      headers: { 'Content-Type': 'application/json' },
    })
  }

  deleteTeam(id) {
    var _self = this
    return request.delete(_self.baseUrl + 'api/equipos/' + id)
  }
}

export default EventService
