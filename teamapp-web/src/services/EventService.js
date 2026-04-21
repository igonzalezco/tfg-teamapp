import request from '@/utils/request'

class EventService {
  baseUrl

  constructor(baseUrl) {
    this.baseUrl = baseUrl
  }

  getTeamEvents(teamId, query) {
    var _self = this
    return request.post(`${_self.baseUrl}api/equipos/${teamId}/eventos/search`, query, {
      headers: { 'Content-Type': 'application/json' },
    })
  }
}

export default EventService
