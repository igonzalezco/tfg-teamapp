<template>
  <div class="event-page">
    <div class="page__header event-page__header">
      <div>
        <h1 class="page__title">{{ t('event.title') }}</h1>
        <p class="page__description">{{ t('event.description') }}</p>
      </div>

      <div class="event-page__header-actions">
        <el-button v-if="canManageEvents" type="primary" @click="goToCreateEvent">
          {{ t('event.actions.create') }}
        </el-button>
      </div>
    </div>

    <div class="event-page__content">
      <section class="app-surface event-page__section">
        <FilterBuilderComponent
          :config="eventFiltersConfig"
          :loading="loading"
          title="event.filters.title"
          @search="handleSearch"
          @reset="handleReset"
        />
      </section>

      <section class="app-surface event-page__section">
        <EventListComponent
          :items="pagedItems"
          :page="page"
          :limit="limit"
          :total="total"
          :loading="loading"
          @page-change="handlePageChange"
          @limit-change="handleLimitChange"
          @detail="goToEventDetail"
        />
      </section>

      <section class="app-surface event-page__section">
        <EventCalendarComponent
          :events="calendarEvents"
          :loading="loading"
          @range-change="handleRangeChange"
          @event-click="goToEventDetail"
          @date-click="handleCalendarDateClick"
        />
      </section>
    </div>
  </div>
</template>

<script setup>
  import { computed, reactive, ref } from 'vue'
  import { useI18n } from 'vue-i18n'
  import { useRouter } from 'vue-router'
  import FilterBuilderComponent from '@/components/filters/FilterBuilderComponent.vue'
  import EventListComponent from '@/components/contents/event/EventListComponent.vue'
  import EventCalendarComponent from '@/components/contents/event/EventCalendarComponent.vue'
  import eventFiltersConfig from '@/components/filters/configs/eventFilters.config'
  import services from '@/services/services'
  import { hasTeamPermission } from '@/utils/permissions'

  const MANAGE_EVENT_PERMISSIONS = ['ADMINISTRADOR', 'STAFF']

  const props = defineProps({
    teamId: {
      type: [String, Number],
      required: true,
    },
  })

  const { t } = useI18n()
  const router = useRouter()

  const loading = ref(false)
  const initialized = ref(false)

  const page = ref(0)
  const limit = ref(10)
  const total = ref(0)

  const pagedItems = ref([])
  const calendarItems = ref([])

  const state = reactive({
    ordenaciones: [
      {
        campo: 'fechaInicio',
        orden: 'ASC',
      },
    ],
    optionalFilters: [],
    calendarRange: {
      start: null,
      end: null,
      viewType: null,
    },
  })

  const canManageEvents = computed(() => hasTeamPermission(MANAGE_EVENT_PERMISSIONS, props.teamId))

  const calendarEvents = computed(() =>
    calendarItems.value.map((event) => ({
      id: event.id,
      title: event.titulo,
      start: event.fechaInicio,
      end: event.fechaFin,
      allDay: !!event.allDay,
    }))
  )

  const goToCreateEvent = () => {
    router.push({
      name: 'createEvent',
      params: {
        id: props.teamId,
      },
    })
  }

  const goToEventDetail = (eventId) => {
    router.push({
      name: 'eventDetail',
      params: {
        id: props.teamId,
        eventId,
      },
    })
  }

  const handleCalendarDateClick = ({ dateStr, allDay }) => {
    if (!canManageEvents.value) {
      return
    }

    router.push({
      name: 'createEvent',
      params: {
        id: props.teamId,
      },
      query: {
        startDate: dateStr,
        allDay: String(allDay),
      },
    })
  }

  const buildCalendarFilters = () => {
    if (!state.calendarRange.start || !state.calendarRange.end) {
      return []
    }

    return [
      {
        campo: 'fechaInicio',
        valor: state.calendarRange.end,
        tipo: 'date',
        expresion: null,
        comparacion: 'LT',
        joinType: null,
        entityJoinName: null,
        operador: 'AND',
        prefiltrado: true,
        filedNameCompareColumn: null,
      },
      {
        campo: 'fechaFin',
        valor: state.calendarRange.start,
        tipo: 'date',
        expresion: null,
        comparacion: 'GT',
        joinType: null,
        entityJoinName: null,
        operador: 'AND',
        prefiltrado: true,
        filedNameCompareColumn: null,
      },
    ]
  }

  const buildFiltersDto = () => ({
    page: page.value,
    limit: limit.value,
    ordenaciones: state.ordenaciones,
    filtros: [...buildCalendarFilters(), ...state.optionalFilters],
    identificador: Number(props.teamId),
    identificadores: [],
  })

  const loadEvents = async () => {
    if (!state.calendarRange.start || !state.calendarRange.end) {
      return
    }

    loading.value = true

    try {
      const response = await services.eventService.getTeamEvents(props.teamId, buildFiltersDto())
      const data = response?.data || {}

      pagedItems.value = data.content || data.items || []
      calendarItems.value = data.calendarEvents || data.content || data.items || []
      total.value = data.totalElements || data.total || 0
      page.value = data.number ?? page.value
      limit.value = data.size ?? limit.value
      initialized.value = true
    } finally {
      loading.value = false
    }
  }

  const handleSearch = async (filtersDto) => {
    state.optionalFilters = filtersDto?.filtros || []
    page.value = 0
    await loadEvents()
  }

  const handleReset = async () => {
    state.optionalFilters = []
    page.value = 0
    await loadEvents()
  }

  const handlePageChange = async (newPage) => {
    page.value = newPage
    await loadEvents()
  }

  const handleLimitChange = async (newLimit) => {
    limit.value = newLimit
    page.value = 0
    await loadEvents()
  }

  const handleRangeChange = async (range) => {
    const changed =
      state.calendarRange.start !== range.start ||
      state.calendarRange.end !== range.end ||
      state.calendarRange.viewType !== range.viewType

    state.calendarRange.start = range.start
    state.calendarRange.end = range.end
    state.calendarRange.viewType = range.viewType

    if (!initialized.value || changed) {
      page.value = 0
      await loadEvents()
    }
  }
</script>
