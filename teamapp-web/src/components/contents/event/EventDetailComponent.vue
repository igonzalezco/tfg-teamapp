<template>
  <div class="event-detail-page">
    <el-page-header class="event-detail-header" title=" " @back="goBack">
      <template #content>
        <h1 class="page__title">{{ t('event.detail.title') }}</h1>
      </template>

      <template #extra>
        <el-button
          v-if="canManageEvent"
          type="danger"
          plain
          class="event-detail-header__delete-button"
          @click="openDeleteDialog"
        >
          {{ t('event.actions.delete') }}
        </el-button>
      </template>
    </el-page-header>

    <section class="app-surface event-detail-page__section">
      <div v-if="loading" class="event-detail-page__state">
        {{ t('common.loading') }}
      </div>

      <div v-else-if="!eventData" class="event-detail-page__state">
        {{ t('event.detail.empty') }}
      </div>

      <div v-else class="event-detail-grid">
        <article class="event-detail-card event-detail-card--full event-detail-card--title">
          <span v-if="eventData.allDay" class="event-detail-card__ribbon">
            {{ t('event.detail.allDayEnabled') }}
          </span>

          <span class="event-detail-card__label">{{ t('event.fields.title') }}</span>

          <span class="event-detail-card__value event-detail-card__value--title">
            {{ eventData.titulo }}
          </span>
        </article>

        <article class="event-detail-card">
          <span class="event-detail-card__label">{{ t('event.fields.startDate') }}</span>
          <span class="event-detail-card__value">
            {{ formatDate(eventData.fechaInicio) }}
          </span>
        </article>

        <article class="event-detail-card">
          <span class="event-detail-card__label">{{ t('event.fields.endDate') }}</span>
          <span class="event-detail-card__value">
            {{ formatDate(eventData.fechaFin) }}
          </span>
        </article>

        <article class="event-detail-card">
          <span class="event-detail-card__label">{{ t('event.fields.location') }}</span>
          <span class="event-detail-card__value">
            {{ eventData.ubicacion || '-' }}
          </span>
        </article>
      </div>
    </section>

    <el-dialog
      v-model="deleteDialogVisible"
      :title="t('event.delete.confirmTitle')"
      width="420px"
      destroy-on-close
    >
      <p>{{ t('event.delete.confirmText') }}</p>

      <template #footer>
        <div class="event-delete-dialog__actions">
          <el-button @click="deleteDialogVisible = false">
            {{ t('common.cancel') }}
          </el-button>

          <el-button type="danger" :loading="deleting" @click="confirmDelete">
            {{ t('common.delete') }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
  import { computed, onMounted, ref } from 'vue'
  import dayjs from 'dayjs'
  import { useRouter } from 'vue-router'
  import { useI18n } from 'vue-i18n'
  import services from '@/services/services'
  import { hasTeamPermission } from '@/utils/permissions'

  const MANAGE_EVENT_PERMISSIONS = ['ADMINISTRADOR', 'STAFF']

  const props = defineProps({
    teamId: {
      type: [String, Number],
      required: true,
    },
    eventId: {
      type: [String, Number],
      required: true,
    },
  })

  const router = useRouter()
  const { t } = useI18n()

  const loading = ref(false)
  const deleting = ref(false)
  const deleteDialogVisible = ref(false)
  const eventData = ref(null)

  const canManageEvent = computed(() => hasTeamPermission(MANAGE_EVENT_PERMISSIONS, props.teamId))

  const formatDate = (value) => {
    if (!value) {
      return ''
    }

    return dayjs(value).format('DD/MM/YYYY HH:mm')
  }

  const goBack = () => {
    router.push({
      name: 'events',
      params: {
        id: props.teamId,
      },
    })
  }

  const loadEventDetail = async () => {
    loading.value = true

    try {
      const response = await services.eventService.getEventDetail(props.teamId, props.eventId)
      eventData.value = response?.data || null
    } finally {
      loading.value = false
    }
  }

  const openDeleteDialog = () => {
    deleteDialogVisible.value = true
  }

  const confirmDelete = async () => {
    if (deleting.value) {
      return
    }

    deleting.value = true

    try {
      await services.eventService.deleteEvent(props.teamId, props.eventId)
      deleteDialogVisible.value = false
      goBack()
    } catch (err) {
      console.error(err)
    } finally {
      deleting.value = false
    }
  }

  onMounted(() => {
    loadEventDetail()
  })
</script>
