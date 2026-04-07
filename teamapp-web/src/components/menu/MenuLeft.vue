<template>
  <div class="sidebar-wrapper">
    <div class="sidebar-top">
      <el-button text @click="$emit('toggle-collapse')">
        <el-icon size="20">
          <Expand v-if="collapsed" />
          <Fold v-else />
        </el-icon>
      </el-button>
    </div>

    <el-menu
      :default-active="activeMenu"
      :collapse="collapsed"
      :collapse-transition="false"
      router
      class="sidebar-menu"
    >
      <el-menu-item index="/content/initContent">
        <el-icon><House /></el-icon>
        <template #title>Inicio</template>
      </el-menu-item>

      <el-menu-item index="/content/dashboard">
        <el-icon><DataBoard /></el-icon>
        <template #title>Dashboard</template>
      </el-menu-item>

      <el-menu-item index="/profile">
        <el-icon><User /></el-icon>
        <template #title>Mi perfil</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
  import { computed } from 'vue'
  import { useRoute } from 'vue-router'
  import { Expand, Fold, House, DataBoard, User } from '@element-plus/icons-vue'

  defineProps({
    collapsed: {
      type: Boolean,
      default: false,
    },
  })

  defineEmits(['toggle-collapse'])

  const route = useRoute()

  const activeMenu = computed(() => route.path)
</script>
