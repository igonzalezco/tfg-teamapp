<template>
  <FullCalendar :options="calendarOptions" />
</template>

<script setup>
  import { computed } from 'vue'
  import FullCalendar from '@fullcalendar/vue3'
  import dayGridPlugin from '@fullcalendar/daygrid'
  import timeGridPlugin from '@fullcalendar/timegrid'
  import interactionPlugin from '@fullcalendar/interaction'
  import esLocale from '@fullcalendar/core/locales/es'

  const props = defineProps({
    events: {
      type: Array,
      default: () => [],
    },
  })

  const emit = defineEmits(['select-event'])

  const calendarOptions = computed(() => ({
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    locale: esLocale,
    height: 'auto',
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay',
    },
    buttonText: {
      today: 'Hoy',
      month: 'Mes',
      week: 'Semana',
      day: 'Día',
    },
    firstDay: 1,
    weekends: true,
    events: props.events,
    eventClick(info) {
      emit('select-event', {
        id: info.event.id,
      })
    },
  }))
</script>
