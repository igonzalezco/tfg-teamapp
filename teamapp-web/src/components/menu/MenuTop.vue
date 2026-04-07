<template>
  <div class="app-header">
    <div class="app-header__brand" @click="goHome">
      <router-link :to="{ name: 'initContent' }">
        <img class="app-header__logo" src="@/assets/images/logo-full.png" alt />
      </router-link>
    </div>

    <div class="app-header__actions">
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="user-trigger">
          <span class="user-trigger__name"> {{ user.email }} </span>
          <img class="app-header__avatar" src="@/assets/images/avatar.png" alt />
          <el-icon><ArrowDown /></el-icon>
        </div>

        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              {{ $t('lang.mi-perfil') }}
            </el-dropdown-item>
            <el-dropdown-item command="logout" divided>
              {{ $t('lang.cerrar-sesion') }}
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
  import { computed } from 'vue'
  import { useRouter } from 'vue-router'
  import { ArrowDown } from '@element-plus/icons-vue'
  import { authStore } from '@/stores/auth'

  const router = useRouter()
  const auth = authStore()

  const user = computed(() => auth.usuario || {})

  const handleCommand = async (command) => {
    if (command === 'profile') {
      router.push({ name: 'profile' })
    }

    if (command === 'logout') {
      auth.logout()
      router.push({ name: 'auth' })
    }
  }
</script>
