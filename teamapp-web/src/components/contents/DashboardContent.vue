<template>
  <section class="team-view">
    <div class="team-view__container">
      <div class="team-dashboard">
        <div class="team-dashboard__header">
          <div class="team-dashboard__header-main">
            <span class="page__eyebrow">Equipo</span>
            <h1 class="team-dashboard__title">Dashboard del equipo</h1>
            <p class="team-dashboard__description">
              Esta será la vista principal del equipo, desde la que se podrán consultar accesos
              rápidos, métricas y actividad reciente.
            </p>
          </div>
        </div>

        <div class="team-stat-grid">
          <div class="team-stat">
            <span class="team-stat__label">Jugadores</span>
            <span class="team-stat__value">0</span>
          </div>

          <div class="team-stat">
            <span class="team-stat__label">Eventos</span>
            <span class="team-stat__value">0</span>
          </div>

          <div class="team-stat">
            <span class="team-stat__label">Noticias</span>
            <span class="team-stat__value">0</span>
          </div>
        </div>

        <div class="team-dashboard__grid">
          <div class="team-dashboard__stack">
            <div class="team-panel">
              <div class="team-panel__header">
                <div>
                  <h2 class="team-panel__title">Resumen general</h2>
                  <p class="team-panel__subtitle">Estado inicial del equipo</p>
                </div>
              </div>

              <div class="team-empty">
                <div class="team-empty__title">Sin información todavía</div>
                <p class="team-empty__text">
                  Aquí podrás mostrar actividad reciente, próximos eventos o indicadores relevantes
                  cuando estén desarrollados.
                </p>
              </div>
            </div>
          </div>

          <div class="team-dashboard__stack">
            <div class="team-panel">
              <div class="team-panel__header">
                <div>
                  <h2 class="team-panel__title">Datos técnicos</h2>
                  <p class="team-panel__subtitle">Información temporal</p>
                </div>
              </div>

              <div class="team-panel__content">
                <p class="team-empty__text">ID del equipo: {{ id }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
  defineProps({
    id: {
      type: [String, Number],
      required: true,
    },
  })
</script>
