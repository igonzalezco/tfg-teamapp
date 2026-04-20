<template>
  <div class="event-list">
    <div class="event-list__header">
      <h2 class="event-list__title">Eventos</h2>
    </div>

    <div v-if="loading" class="event-list__state">Cargando eventos...</div>

    <div v-else-if="!events.length" class="event-list__state">No hay eventos para este equipo.</div>

    <div v-else class="event-list__items">
      <button
        v-for="event in events"
        :key="event.id"
        type="button"
        class="event-list__item"
        :class="{ 'event-list__item--active': Number(selectedEventId) === Number(event.id) }"
        @click="$emit('select', event)"
      >
        <div class="event-list__item-main">
          <span class="event-list__item-title">{{ event.titulo }}</span>
          <span class="event-list__item-date">
            {{ event.fechaInicio }}
          </span>
        </div>
      </button>
    </div>
  </div>
</template>

<script setup>
  defineProps({
    events: {
      type: Array,
      default: () => [],
    },
    loading: {
      type: Boolean,
      default: false,
    },
    selectedEventId: {
      type: [String, Number, null],
      default: null,
    },
  })

  defineEmits(['select'])
</script>
