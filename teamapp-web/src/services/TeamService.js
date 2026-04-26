import request from '@/utils/request'

class TeamService {
  baseUrl

  constructor(baseUrl) {
    this.baseUrl = baseUrl
  }

  createTeam(team) {
    return request.post(`${this.baseUrl}api/equipos`, team)
  }

  deleteTeam(id) {
    return request.delete(`${this.baseUrl}api/equipos/${id}`)
  }
}

export default TeamService
