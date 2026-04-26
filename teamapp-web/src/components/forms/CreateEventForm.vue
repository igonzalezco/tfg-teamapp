<template>
  <div class="event-create-page">
    <el-page-header class="event-create-header" title=" " @back="goBack">
      <template #content>
        <h1 class="page__title">{{ t('event.create.title') }}</h1>
      </template>
    </el-page-header>

    <section class="app-surface event-create-page__section">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-position="top"
        class="event-create-form"
      >
        <div class="event-create-form__grid">
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
              class="event-create-form__control"
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
              class="event-create-form__control"
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

        <div class="event-create-form__actions">
          <el-button @click="goBack">
            {{ t('common.cancel') }}
          </el-button>

          <el-button type="primary" :loading="loading" @click="submitForm">
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
  })

  const route = useRoute()
  const router = useRouter()
  const { t } = useI18n()

  const formRef = ref(null)
  const loading = ref(false)

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

  const goBack = () => {
    router.push({
      name: 'events',
      params: {
        id: props.teamId,
      },
    })
  }

  const applyStartDateFromQuery = () => {
    const rawStartDate = route.query.startDate
    const rawAllDay = route.query.allDay

    if (!rawStartDate || Array.isArray(rawStartDate)) {
      return
    }

    const isAllDay = rawAllDay === 'true' || (!rawAllDay && !rawStartDate.includes('T'))

    form.allDay = isAllDay

    if (isAllDay) {
      const baseDate = dayjs(rawStartDate)

      form.fechaInicio = baseDate.format('YYYY-MM-DD')
      form.fechaFin = baseDate.format('YYYY-MM-DD')
      return
    }

    const baseDateTime = dayjs(rawStartDate)

    form.fechaInicio = baseDateTime.format('YYYY-MM-DDTHH:mm:ss')
    form.fechaFin = baseDateTime.add(1, 'hour').format('YYYY-MM-DDTHH:mm:ss')
  }

  const submitForm = async () => {
    const valid = await formRef.value.validate().catch(() => false)

    if (!valid) {
      return
    }

    loading.value = true

    try {
      await services.eventService.createEvent(props.teamId, buildPayload())
      goBack()
    } finally {
      loading.value = false
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
    applyStartDateFromQuery()
  })
</script>
