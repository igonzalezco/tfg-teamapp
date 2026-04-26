<template>
  <EventComponent :team-id="id" />
</template>

<script setup>
  import EventComponent from '@/components/contents/event/EventComponent.vue'

  defineProps({
    id: {
      type: [String, Number],
      required: true,
    },
  })
</script>
