<template>
  <div class="calendar-page">
    <div class="page__header">
      <div>
        <h1 class="page__title">Calendario</h1>
        <p class="page__description">
          Consulta los eventos del equipo en formato calendario y listado.
        </p>
      </div>
    </div>

    <div class="calendar-layout">
      <section class="calendar-layout__main app-surface">
        <CalendarComponent :events="calendarEvents" @select-event="handleSelectEvent" />
      </section>

      <aside class="calendar-layout__sidebar app-surface">
        <EventListComponent
          :events="filteredEvents"
          :selected-event-id="selectedEvent?.id"
          :loading="loading"
          @select="handleSelectEvent"
        />
      </aside>
    </div>
  </div>
</template>

<script setup>
  import { computed, onMounted, ref, watch } from 'vue'
  import { useRoute } from 'vue-router'
  import CalendarComponent from '@/components/contents/CalendarComponent.vue'
  import EventListComponent from '@/components/contents/EventListComponent.vue'
  import eventService from '@/services/eventService'

  const props = defineProps({
    id: {
      type: [String, Number],
      required: true,
    },
  })

  const route = useRoute()
  const loading = ref(false)
  const events = ref([])
  const selectedEvent = ref(null)

  const loadEvents = async () => {
    loading.value = true

    try {
      const response = await eventService.getTeamEvents(props.id)
      events.value = response.data
    } finally {
      loading.value = false
    }
  }

  const calendarEvents = computed(() =>
    events.value.map((event) => ({
      id: event.id,
      title: event.titulo,
      start: event.fechaInicio,
      end: event.fechaFin,
      allDay: event.allDay,
      extendedProps: {
        descripcion: event.descripcion,
        ubicacion: event.ubicacion,
      },
    }))
  )

  const filteredEvents = computed(() => events.value)

  const handleSelectEvent = (event) => {
    const normalizedId = Number(event.id)
    selectedEvent.value = events.value.find((item) => Number(item.id) === normalizedId) || null
  }

  watch(
    () => route.params.id,
    () => {
      loadEvents()
    }
  )

  onMounted(() => {
    loadEvents()
  })
</script>
