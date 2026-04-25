<template>
  <CreateEventForm :team-id="id" />
</template>

<script setup>
  import CreateEventForm from '@/components/forms/CreateEventForm.vue'

  defineProps({
    id: {
      type: [String, Number],
      required: true,
    },
  })
</script>
