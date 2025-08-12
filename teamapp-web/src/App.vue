
<template>
  <div id="app" class="flex_div">
    <notifications />
    <RouterView />
  </div>
</template>

<script>
  import { RouterView } from 'vue-router'
  
  export default {
    name: 'App'
  }
</script>

<style>
</style>
