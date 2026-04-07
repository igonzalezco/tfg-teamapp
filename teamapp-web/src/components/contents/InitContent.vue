<template>
  <section class="team-view">
    <div class="team-view__container">
      <div class="team-welcome">
        <div class="team-welcome__content">
          <div class="team-welcome__badge">
            <span class="app-badge">Primeros pasos</span>
          </div>

          <h1 class="team-welcome__title">Bienvenido</h1>

          <p class="team-welcome__description">
            Aún no formas parte de ningún equipo. Puedes crear uno nuevo o unirte a uno existente
            para comenzar a gestionar su actividad.
          </p>

          <div class="team-welcome__actions">
            <el-button type="primary" @click="goToCreateTeam"> Crear equipo </el-button>

            <el-button @click="goToJoinTeam"> Unirse a equipo </el-button>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
  import { useRouter } from 'vue-router'

  const router = useRouter()

  function goToCreateTeam() {
    router.push({ name: 'createTeam' })
  }

  function goToJoinTeam() {
    // Pendiente de implementar
  }
</script>
