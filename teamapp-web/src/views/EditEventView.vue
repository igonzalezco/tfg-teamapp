<template>
  <EditEventForm :team-id="id" :event-id="eventId" />
</template>

<script setup>
  import EditEventForm from '@/components/forms/EditEventForm.vue'

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
