<template>
  <EventDetailComponent :team-id="id" :event-id="eventId" />
</template>

<script setup>
  import EventDetailComponent from '@/components/contents/event/EventDetailComponent.vue'

  defineProps({
    id: {
      type: [String, Number],
      required: true,
    },
    eventId: {
      type: [String, Number],
      required: true,
    },
  })
</script>
