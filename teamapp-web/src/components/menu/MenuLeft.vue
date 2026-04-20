<template>
  <div class="sidebar-wrapper">
    <div class="sidebar-top">
      <el-button text @click="$emit('toggle-collapse')">
        <el-icon size="20">
          <component :is="collapsed ? ElementPlusIconsVue.Expand : ElementPlusIconsVue.Fold" />
        </el-icon>
      </el-button>
    </div>

    <div v-if="!collapsed" class="sidebar-team-block">
      <OwnTeamSelect @team-changed="onTeamChanged" />
    </div>

    <el-menu
      :default-active="activeMenu"
      :collapse="collapsed"
      :collapse-transition="false"
      router
      class="sidebar-menu"
    >
      <el-menu-item
        v-for="item in menuItems"
        :key="item.name"
        :index="item.index"
        :disabled="item.disabled"
      >
        <el-icon>
          <component :is="item.icon" />
        </el-icon>
        <template #title>{{ $t(item.label) }}</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
  import { computed } from 'vue'
  import { useRoute } from 'vue-router'
  import * as ElementPlusIconsVue from '@element-plus/icons-vue'
  import OwnTeamSelect from '../contents/OwnTeamSelect.vue'
  import { authStore } from '@/stores/auth'
  import routes from '@/router/routes'
  import { useI18n } from 'vue-i18n'

  defineProps({
    collapsed: {
      type: Boolean,
      default: false,
    },
  })

  defineEmits(['toggle-collapse'])

  const auth = authStore()
  const { t } = useI18n()
  const route = useRoute()

  function joinPaths(parentPath, childPath) {
    if (!parentPath) {
      return childPath || ''
    }

    if (!childPath) {
      return parentPath
    }

    if (childPath.startsWith('/')) {
      return childPath
    }

    const normalizedParent = parentPath.endsWith('/') ? parentPath.slice(0, -1) : parentPath

    return `${normalizedParent}/${childPath}`
  }

  function buildFlatMenuItems(routeList, parentPath = '', teamId = null) {
    return routeList.flatMap((route) => {
      const fullPath = joinPaths(parentPath, route.path)
      const needsTeam = fullPath.includes(':id')

      let index = fullPath

      if (needsTeam && teamId) {
        index = index.replace(':id', String(teamId))
      }

      const currentItem =
        route.visible === true
          ? [
              {
                path: route.path,
                name: t(route.name) || route.name,
                label: route.label,
                icon: ElementPlusIconsVue[route.icon] || ElementPlusIconsVue.Menu,
                index,
                disabled: needsTeam && !teamId,
              },
            ]
          : []

      const childrenItems = buildFlatMenuItems(route.children || [], fullPath, teamId)

      return [...currentItem, ...childrenItems]
    })
  }

  const menuItems = computed(() => {
    const teamId = auth.getTeamId
    return buildFlatMenuItems(routes, '', teamId)
  })

  function onTeamChanged(userTeam) {
    // De momento no hace falta hacer nada aquí.
    // Más adelante puedes usar esto para recargar dashboard,
    // cambiar de ruta o reconstruir el menú dinámico.
  }
</script>
