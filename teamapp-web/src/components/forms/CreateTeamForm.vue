<template>
  <section class="team-view">
    <div class="team-view__container">
      <div class="team-form-page">
        <div class="page__header">
          <span class="page__eyebrow">Equipos</span>
          <h1 class="page__title">Crear equipo</h1>
          <p class="page__description">
            Completa los datos básicos para dar de alta un nuevo equipo dentro de la aplicación.
          </p>
        </div>

        <div class="team-form-page__container">
          <el-card class="team-form-card">
            <template #header>
              <div class="team-form-card__header">
                <h2 class="team-form-card__title">Datos del equipo</h2>
                <p class="team-form-card__description">
                  Estos datos se usarán como base de la configuración inicial.
                </p>
              </div>
            </template>

            <el-form
              ref="formRef"
              :model="form"
              :rules="rules"
              label-position="top"
              class="team-form"
              @submit.prevent
            >
              <div class="team-form__grid">
                <el-form-item label="Nombre del equipo" prop="name">
                  <el-input
                    v-model="form.name"
                    maxlength="100"
                    show-word-limit
                    placeholder="Introduce el nombre del equipo"
                  />
                </el-form-item>

                <el-form-item label="Descripción" prop="description">
                  <el-input
                    v-model="form.description"
                    type="textarea"
                    :rows="4"
                    maxlength="500"
                    show-word-limit
                    placeholder="Introduce una descripción breve del equipo"
                  />
                </el-form-item>
              </div>

              <div class="team-form__actions">
                <el-button @click="goBack">Cancelar</el-button>
                <el-button type="primary" :loading="loading" @click="submitForm">
                  Crear equipo
                </el-button>
              </div>
            </el-form>
          </el-card>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
  import { reactive, ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { ElMessage } from 'element-plus'
  import { authStore } from '@/stores/auth'

  const router = useRouter()
  const auth = authStore()

  const formRef = ref()
  const loading = ref(false)

  const form = reactive({
    name: '',
    description: '',
  })

  const rules = {
    name: [
      { required: true, message: 'El nombre es obligatorio', trigger: 'blur' },
      {
        min: 3,
        max: 100,
        message: 'Debe tener entre 3 y 100 caracteres',
        trigger: 'blur',
      },
    ],
    description: [
      {
        max: 500,
        message: 'La descripción no puede superar 500 caracteres',
        trigger: 'blur',
      },
    ],
  }

  function goBack() {
    router.push({ name: 'initContent' })
  }

  async function submitForm() {
    if (!formRef.value) {
      return
    }

    try {
      await formRef.value.validate()
      loading.value = true

      await auth.createTeamAction({
        nombre: form.name,
        descripcion: form.description,
      })

      ElMessage.success('Equipo creado correctamente')

      if (auth.getSelectedTeam) {
        router.push({
          name: 'dashboard',
          params: { id: auth.getSelectedTeam.equipo.id },
        })
        return
      }

      router.push({ name: 'initContent' })
    } catch (error) {
      const backendMessage = error?.response?.data?.message || 'No se ha podido crear el equipo'

      ElMessage.error(backendMessage)
    } finally {
      loading.value = false
    }
  }
</script>
