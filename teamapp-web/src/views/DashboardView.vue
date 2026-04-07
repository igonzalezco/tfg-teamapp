<template>
  <DashboardContent :id="id" />
</template>

<script setup>
  import DashboardContent from '@/components/contents/DashboardContent.vue'

  defineProps({
    id: {
      type: [String, Number],
      required: true,
    },
  })
</script>
