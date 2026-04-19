<template>
  <div v-if="auth.hasTeams" class="sidebar-team-selector">
    <div class="sidebar-team-card">
      <div class="sidebar-team-card__label">Equipo actual</div>

      <div class="sidebar-team-card__name">
        {{ auth.getSelectedTeam?.equipo?.nombre || 'Sin equipo seleccionado' }}
      </div>
    </div>

    <div v-if="auth.getUserTeams.length > 1" class="sidebar-team-card">
      <div class="sidebar-team-card__label">Selecciona otro equipo</div>

      <el-select
        :model-value="selectedTeamId"
        class="sidebar-team-card__select"
        placeholder="Selecciona un equipo"
        @change="onChangeTeam"
      >
        <el-option
          v-for="team in auth.getUserTeams"
          :key="team.id"
          :label="team.equipo.nombre"
          :value="team.id"
        />
      </el-select>
    </div>
  </div>
</template>

<script setup>
  import { computed } from 'vue'
  import { useRouter } from 'vue-router'
  import { authStore } from '@/stores/auth'

  const router = useRouter()
  const auth = authStore()

  const selectedTeamId = computed(() => {
    return auth.getSelectedTeam?.id || null
  })

  function onChangeTeam(userTeamId) {
    const selectedTeam = auth.getUserTeams.find((team) => team?.id === userTeamId)

    if (!selectedTeam) {
      return
    }

    auth.setSelectedTeamAction(selectedTeam)

    router.push({
      name: 'dashboard',
      params: { id: selectedTeam.equipo.id },
    })
  }
</script>
