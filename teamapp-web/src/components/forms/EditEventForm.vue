<template>
  <div class="event-edit-page">
    <el-page-header class="event-edit-header" title=" " @back="goBack">
      <template #content>
        <h1 class="page__title">{{ t('event.edit.title') }}</h1>
      </template>
    </el-page-header>

    <section class="app-surface event-edit-page__section">
      <div v-if="loadingDetail" class="event-edit-page__state">
        {{ t('common.loading') }}
      </div>

      <el-form
        v-else
        ref="formRef"
        :model="form"
        :rules="rules"
        label-position="top"
        class="event-edit-form"
      >
        <div class="event-edit-form__grid">
          <el-form-item prop="titulo" :label="t('event.fields.title')">
            <el-input
              v-model="form.titulo"
              :placeholder="t('event.fields.titlePlaceholder')"
              clearable
            />
          </el-form-item>

          <el-form-item prop="allDay" :label="t('event.fields.allDay')">
            <el-switch v-model="form.allDay" />
          </el-form-item>

          <el-form-item prop="fechaInicio" :label="t('event.fields.startDate')">
            <el-date-picker
              v-model="form.fechaInicio"
              class="event-edit-form__control"
              :type="form.allDay ? 'date' : 'datetime'"
              :format="form.allDay ? 'DD/MM/YYYY' : 'DD/MM/YYYY HH:mm:ss'"
              :value-format="form.allDay ? 'YYYY-MM-DD' : 'YYYY-MM-DDTHH:mm:ss'"
              :placeholder="t('event.fields.startDatePlaceholder')"
              clearable
            />
          </el-form-item>

          <el-form-item prop="fechaFin" :label="t('event.fields.endDate')">
            <el-date-picker
              v-model="form.fechaFin"
              class="event-edit-form__control"
              :type="form.allDay ? 'date' : 'datetime'"
              :format="form.allDay ? 'DD/MM/YYYY' : 'DD/MM/YYYY HH:mm:ss'"
              :value-format="form.allDay ? 'YYYY-MM-DD' : 'YYYY-MM-DDTHH:mm:ss'"
              :placeholder="t('event.fields.endDatePlaceholder')"
              clearable
            />
          </el-form-item>

          <el-form-item prop="ubicacion" :label="t('event.fields.location')">
            <el-input
              v-model="form.ubicacion"
              :placeholder="t('event.fields.locationPlaceholder')"
              clearable
            />
          </el-form-item>
        </div>

        <div class="event-edit-form__actions">
          <el-button @click="goBack">
            {{ t('common.cancel') }}
          </el-button>

          <el-button type="primary" :loading="saving" @click="submitForm">
            {{ t('common.save') }}
          </el-button>
        </div>
      </el-form>
    </section>
  </div>
</template>

<script setup>
  import { onMounted, reactive, ref, watch } from 'vue'
  import dayjs from 'dayjs'
  import { useRoute, useRouter } from 'vue-router'
  import { useI18n } from 'vue-i18n'
  import services from '@/services/services'

  const props = defineProps({
    teamId: {
      type: [String, Number],
      required: true,
    },
    eventId: {
      type: [String, Number],
      required: true,
    },
  })

  const route = useRoute()
  const router = useRouter()
  const { t } = useI18n()

  const formRef = ref(null)
  const loadingDetail = ref(false)
  const saving = ref(false)

  const form = reactive({
    titulo: '',
    fechaInicio: null,
    fechaFin: null,
    allDay: false,
    ubicacion: '',
  })

  const normalizeStartValue = (value, allDay) => {
    if (!value) {
      return null
    }

    if (!allDay) {
      return value
    }

    return dayjs(value).startOf('day').format('YYYY-MM-DDTHH:mm:ss')
  }

  const normalizeEndValue = (value, allDay) => {
    if (!value) {
      return null
    }

    if (!allDay) {
      return value
    }

    return dayjs(value).endOf('day').format('YYYY-MM-DDTHH:mm:ss')
  }

  const validateEndDate = (_rule, value, callback) => {
    if (!value || !form.fechaInicio) {
      callback()
      return
    }

    const start = dayjs(normalizeStartValue(form.fechaInicio, form.allDay))
    const end = dayjs(normalizeEndValue(value, form.allDay))

    if (end.isBefore(start)) {
      callback(new Error(t('event.validation.endDateAfterStart')))
      return
    }

    callback()
  }

  const rules = {
    titulo: [
      {
        required: true,
        message: t('event.validation.titleRequired'),
        trigger: 'blur',
      },
    ],
    fechaInicio: [
      {
        required: true,
        message: t('event.validation.startDateRequired'),
        trigger: 'change',
      },
    ],
    fechaFin: [
      {
        required: true,
        message: t('event.validation.endDateRequired'),
        trigger: 'change',
      },
      {
        validator: validateEndDate,
        trigger: 'change',
      },
    ],
  }

  const buildPayload = () => ({
    titulo: form.titulo?.trim(),
    fechaInicio: normalizeStartValue(form.fechaInicio, form.allDay),
    fechaFin: normalizeEndValue(form.fechaFin, form.allDay),
    allDay: form.allDay,
    ubicacion: form.ubicacion?.trim() || null,
  })

  const getBackRoute = () => {
    if (route.query.returnTo === 'list') {
      return {
        name: 'events',
        params: {
          id: props.teamId,
        },
      }
    }

    return {
      name: 'eventDetail',
      params: {
        id: props.teamId,
        eventId: props.eventId,
      },
    }
  }

  const goBack = () => {
    router.push(getBackRoute())
  }

  const loadEvent = async () => {
    loadingDetail.value = true

    try {
      const response = await services.eventService.getEventDetail(props.teamId, props.eventId)
      const event = response?.data

      if (!event) {
        return
      }

      form.titulo = event.titulo || ''
      form.allDay = !!event.allDay
      form.ubicacion = event.ubicacion || ''

      if (form.allDay) {
        form.fechaInicio = dayjs(event.fechaInicio).format('YYYY-MM-DD')
        form.fechaFin = dayjs(event.fechaFin).format('YYYY-MM-DD')
      } else {
        form.fechaInicio = dayjs(event.fechaInicio).format('YYYY-MM-DDTHH:mm:ss')
        form.fechaFin = dayjs(event.fechaFin).format('YYYY-MM-DDTHH:mm:ss')
      }
    } finally {
      loadingDetail.value = false
    }
  }

  const submitForm = async () => {
    const valid = await formRef.value.validate().catch(() => false)

    if (!valid) {
      return
    }

    saving.value = true

    try {
      await services.eventService.updateEvent(props.teamId, props.eventId, buildPayload())
      router.push(getBackRoute())
    } finally {
      saving.value = false
    }
  }

  watch(
    () => form.allDay,
    (newValue, oldValue) => {
      if (newValue === oldValue || !form.fechaInicio || !form.fechaFin) {
        return
      }

      if (newValue) {
        form.fechaInicio = dayjs(form.fechaInicio).format('YYYY-MM-DD')
        form.fechaFin = dayjs(form.fechaFin).format('YYYY-MM-DD')
        return
      }

      form.fechaInicio = dayjs(form.fechaInicio).startOf('day').format('YYYY-MM-DDTHH:mm:ss')
      form.fechaFin = dayjs(form.fechaFin).endOf('day').format('YYYY-MM-DDTHH:mm:ss')
    }
  )

  onMounted(() => {
    loadEvent()
  })
</script>
