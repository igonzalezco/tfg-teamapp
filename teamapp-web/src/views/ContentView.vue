<template>
  <div class="app-shell">
    <header class="app-shell__header">
      <MenuTop />
    </header>

    <div class="app-shell__body">
      <aside class="app-shell__sidebar" :class="{ 'is-collapsed': collapsed }">
        <MenuLeft :collapsed="collapsed" @toggle-collapse="toggleCollapse" />
      </aside>

      <main class="app-shell__main">
        <div class="app-shell__content">
          <router-view />
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
  import { ref } from 'vue'
  import MenuTop from '@/components/menu/MenuTop.vue'
  import MenuLeft from '@/components/menu/MenuLeft.vue'

  const collapsed = ref(false)

  const toggleCollapse = () => {
    collapsed.value = !collapsed.value
  }
</script>
