<template>
  <section class="team-view">
    <div class="team-view__container">
      <div class="team-dashboard">
        <div class="team-dashboard__header">
          <div class="team-dashboard__header-main">
            <span class="page__eyebrow">Equipo</span>
            <h1 class="team-dashboard__title">Dashboard del equipo</h1>
            <p class="team-dashboard__description">
              Esta será la vista principal del equipo, desde la que se podrán consultar accesos
              rápidos, métricas y actividad reciente.
            </p>
          </div>

          <div v-if="canDeleteTeam" class="team-dashboard__header-actions">
            <el-button type="danger" plain @click="openDeleteDialog"> Eliminar equipo </el-button>
          </div>
        </div>

        <div class="team-stat-grid">
          <div class="team-stat">
            <span class="team-stat__label">Jugadores</span>
            <span class="team-stat__value">0</span>
          </div>

          <div class="team-stat">
            <span class="team-stat__label">Eventos</span>
            <span class="team-stat__value">0</span>
          </div>

          <div class="team-stat">
            <span class="team-stat__label">Noticias</span>
            <span class="team-stat__value">0</span>
          </div>
        </div>

        <div class="team-dashboard__grid">
          <div class="team-dashboard__stack">
            <div class="team-panel">
              <div class="team-panel__header">
                <div>
                  <h2 class="team-panel__title">Resumen general</h2>
                  <p class="team-panel__subtitle">Estado inicial del equipo</p>
                </div>
              </div>

              <div class="team-empty">
                <div class="team-empty__title">Sin información todavía</div>
                <p class="team-empty__text">
                  Aquí podrás mostrar actividad reciente, próximos eventos o indicadores relevantes
                  cuando estén desarrollados.
                </p>
              </div>
            </div>
          </div>

          <div class="team-dashboard__stack">
            <div class="team-panel">
              <div class="team-panel__header">
                <div>
                  <h2 class="team-panel__title">Datos técnicos</h2>
                  <p class="team-panel__subtitle">Información temporal</p>
                </div>
              </div>

              <div class="team-panel__content">
                <p class="team-empty__text">ID del equipo: {{ id }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <el-dialog
        v-model="deleteDialogVisible"
        title="Eliminar equipo"
        width="460px"
        destroy-on-close
      >
        <div class="team-delete-dialog">
          <p class="team-delete-dialog__text">
            Vas a eliminar el equipo
            <strong>{{ selectedTeamName }}</strong
            >.
          </p>

          <p class="team-delete-dialog__warning">Esta acción no se puede deshacer.</p>
        </div>

        <template #footer>
          <div class="team-delete-dialog__actions">
            <el-button @click="closeDeleteDialog"> Cancelar </el-button>

            <el-button type="danger" :loading="deleteLoading" @click="confirmDeleteTeam">
              Eliminar
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </section>
</template>

<script setup>
  import { onMounted, watch, computed, ref } from 'vue'
  import { authStore } from '@/stores/auth'
  import { useRouter } from 'vue-router'
  import { ElMessage } from 'element-plus'

  const props = defineProps({
    id: {
      type: [String, Number],
      required: true,
    },
  })

  const router = useRouter()
  const auth = authStore()

  const deleteDialogVisible = ref(false)
  const deleteLoading = ref(false)

  const selectedTeamName = computed(() => {
    return auth.getSelectedTeam?.equipo?.nombre || 'este equipo'
  })

  const canDeleteTeam = computed(() => {
    const permissionCode = auth.getSelectedTeam?.permiso?.codigo
    return permissionCode === 'ADMINISTRADOR'
  })

  function openDeleteDialog() {
    deleteDialogVisible.value = true
  }

  function closeDeleteDialog() {
    if (deleteLoading.value) {
      return
    }

    deleteDialogVisible.value = false
  }

  async function confirmDeleteTeam() {
    try {
      deleteLoading.value = true

      await auth.deleteTeamAction()

      ElMessage.success('Equipo eliminado correctamente')
      deleteDialogVisible.value = false

      const nextTeamId = auth.getSelectedTeam?.equipo?.id

      if (nextTeamId) {
        router.push({
          name: 'dashboard',
          params: { id: nextTeamId },
        })
        return
      }

      router.push({ name: 'initContent' })
    } catch (error) {
      const backendMessage = error?.response?.data?.message || 'No se ha podido eliminar el equipo'

      ElMessage.error(backendMessage)
    } finally {
      deleteLoading.value = false
    }
  }

  function syncSelectedTeam() {
    const teamId = Number(props.id)

    const selectedTeam = auth.getUserTeams.find((team) => team?.equipo?.id === teamId)

    if (selectedTeam) {
      auth.setSelectedTeamAction(selectedTeam)
    }
  }

  onMounted(() => {
    syncSelectedTeam()
  })

  watch(
    () => props.id,
    () => {
      syncSelectedTeam()
    }
  )
</script>
