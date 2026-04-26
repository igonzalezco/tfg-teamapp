import request from '@/utils/request'

class EventService {
  baseUrl

  constructor(baseUrl) {
    this.baseUrl = baseUrl
  }

  getTeamEvents(teamId, query) {
    return request.post(`${this.baseUrl}api/equipos/${teamId}/eventos/search`, query)
  }

  createEvent(teamId, eventData) {
    return request.post(`${this.baseUrl}api/equipos/${teamId}/eventos`, eventData)
  }

  getEventDetail(teamId, eventId) {
    return request.get(`${this.baseUrl}api/equipos/${teamId}/eventos/${eventId}`)
  }

  deleteEvent(teamId, eventId) {
    return request.delete(`${this.baseUrl}api/equipos/${teamId}/eventos/${eventId}`)
  }

  updateEvent(teamId, eventId, eventData) {
    return request.put(`${this.baseUrl}api/equipos/${teamId}/eventos/${eventId}`, eventData)
  }
}

export default EventService
