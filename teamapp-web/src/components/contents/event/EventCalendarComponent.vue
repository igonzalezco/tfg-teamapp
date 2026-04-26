<template>
  <div class="event-calendar">
    <div v-if="loading" class="event-calendar__state">
      {{ t('common.loading') }}
    </div>

    <FullCalendar :options="calendarOptions" />
  </div>
</template>

<script setup>
  import { computed } from 'vue'
  import { useI18n } from 'vue-i18n'
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
    loading: {
      type: Boolean,
      default: false,
    },
  })

  const emit = defineEmits(['range-change', 'event-click', 'date-click'])

  const { t } = useI18n()

  const calendarOptions = computed(() => ({
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
    locale: esLocale,
    initialView: 'dayGridMonth',
    height: 'auto',
    firstDay: 1,
    weekends: true,
    events: props.events,
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay',
    },
    buttonText: {
      today: t('calendar.today'),
      month: t('calendar.month'),
      week: t('calendar.week'),
      day: t('calendar.day'),
    },
    datesSet(info) {
      emit('range-change', {
        start: info.startStr,
        end: info.endStr,
        viewType: info.view.type,
      })
    },
    eventClick(info) {
      emit('event-click', info.event.id)
    },
    dateClick(info) {
      emit('date-click', {
        date: info.date,
        dateStr: info.dateStr,
        allDay: info.allDay,
        viewType: info.view.type,
      })
    },
  }))
</script>
