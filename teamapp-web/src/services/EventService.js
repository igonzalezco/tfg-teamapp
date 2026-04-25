import request from '@/utils/request'

class EventService {
  baseUrl

  constructor(baseUrl) {
    this.baseUrl = baseUrl
  }

  getTeamEvents(teamId, query) {
    var _self = this
    return request.post(`${_self.baseUrl}api/equipos/${teamId}/eventos/search`, query)
  }

  createEvent(teamId, eventData) {
    return request.post(`${this.baseUrl}api/equipos/${teamId}/eventos`, eventData)
  }
}

export default EventService
