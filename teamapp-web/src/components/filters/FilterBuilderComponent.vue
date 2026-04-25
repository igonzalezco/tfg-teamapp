<template>
  <div class="event-filters">
    <div class="event-filters__header">
      <h2 class="event-filters__title">{{ resolvedTitle }}</h2>
    </div>

    <el-form label-position="top" class="event-filters__form" @submit.prevent>
      <div class="event-filters__grid">
        <div v-for="field in config" :key="field.key" class="event-filters__field">
          <template v-if="field.component === 'input'">
            <el-form-item :label="resolveText(field.label)">
              <el-input
                v-model="form[field.key]"
                :type="field.type || 'text'"
                :placeholder="resolveText(field.placeholder)"
                :clearable="field.clearable !== false"
                @keyup.enter="submitSearch"
              />
            </el-form-item>
          </template>

          <template v-else-if="field.component === 'textarea'">
            <el-form-item :label="resolveText(field.label)">
              <el-input
                v-model="form[field.key]"
                type="textarea"
                :rows="field.rows || 3"
                :placeholder="resolveText(field.placeholder)"
                :clearable="field.clearable !== false"
              />
            </el-form-item>
          </template>

          <template v-else-if="field.component === 'select'">
            <el-form-item :label="resolveText(field.label)">
              <el-select
                v-model="form[field.key]"
                class="event-filters__control"
                :placeholder="resolveText(field.placeholder)"
                :clearable="field.clearable !== false"
                :multiple="!!field.multiple"
                collapse-tags
                collapse-tags-tooltip
              >
                <el-option
                  v-for="option in field.options || []"
                  :key="option.value"
                  :label="resolveText(option.label)"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </template>

          <template v-else-if="field.component === 'checkbox'">
            <el-form-item :label="resolveText(field.label)">
              <el-checkbox v-model="form[field.key]">
                {{ resolveText(field.checkboxLabel || field.label) }}
              </el-checkbox>
            </el-form-item>
          </template>

          <template v-else-if="field.component === 'number'">
            <el-form-item :label="resolveText(field.label)">
              <el-input-number
                v-model="form[field.key]"
                class="event-filters__control"
                :min="field.min"
                :max="field.max"
                :step="field.step || 1"
                controls-position="right"
              />
            </el-form-item>
          </template>

          <template v-else-if="field.component === 'date'">
            <el-form-item :label="resolveText(field.label)">
              <el-date-picker
                v-model="form[field.key]"
                class="event-filters__control"
                type="date"
                :placeholder="resolveText(field.placeholder)"
                :value-format="field.valueFormat || 'YYYY-MM-DD'"
                :format="field.format || 'DD/MM/YYYY'"
                clearable
              />
            </el-form-item>
          </template>

          <template v-else-if="field.component === 'daterange'">
            <el-form-item :label="resolveText(field.label)">
              <el-date-picker
                v-model="form[field.key]"
                class="event-filters__control"
                type="daterange"
                :start-placeholder="resolveText(field.startPlaceholder)"
                :end-placeholder="resolveText(field.endPlaceholder)"
                :value-format="field.valueFormat || 'YYYY-MM-DD'"
                :format="field.format || 'DD/MM/YYYY'"
                range-separator="-"
                clearable
              />
            </el-form-item>
          </template>
        </div>
      </div>

      <div class="event-filters__actions">
        <el-button type="primary" :loading="loading" @click="submitSearch">
          {{ resolveText(searchLabel) }}
        </el-button>

        <el-button :disabled="loading" @click="resetFilters">
          {{ resolveText(resetLabel) }}
        </el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
  import { computed, reactive, watch } from 'vue'
  import { useI18n } from 'vue-i18n'

  const props = defineProps({
    config: {
      type: Array,
      required: true,
    },
    loading: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: 'event.filters.title',
    },
    searchLabel: {
      type: String,
      default: 'common.search',
    },
    resetLabel: {
      type: String,
      default: 'common.reset',
    },
    initialFiltersDto: {
      type: Object,
      default: () => ({
        page: 0,
        limit: 10,
        ordenaciones: [],
        filtros: [],
        identificador: null,
        identificadores: [],
      }),
    },
  })

  const emit = defineEmits(['search', 'reset'])

  const { t, te } = useI18n()

  const resolveText = (value) => {
    if (!value) {
      return ''
    }

    return te(value) ? t(value) : value
  }

  const resolvedTitle = computed(() => resolveText(props.title))

  const createInitialForm = () =>
    props.config.reduce((acc, field) => {
      if (field.component === 'checkbox') {
        acc[field.key] = field.defaultValue ?? false
        return acc
      }

      if (field.multiple) {
        acc[field.key] = field.defaultValue ?? []
        return acc
      }

      acc[field.key] = field.defaultValue ?? null
      return acc
    }, {})

  const form = reactive(createInitialForm())

  const applyInitialForm = () => {
    const initialForm = createInitialForm()

    Object.keys(form).forEach((key) => {
      delete form[key]
    })

    Object.entries(initialForm).forEach(([key, value]) => {
      form[key] = value
    })
  }

  watch(
    () => props.config,
    () => {
      applyInitialForm()
    },
    { deep: true }
  )

  const shouldIncludeValue = (field, value) => {
    if (typeof field.includeWhen === 'function') {
      return field.includeWhen(value, form)
    }

    if (field.component === 'checkbox') {
      return value === true
    }

    if (Array.isArray(value)) {
      return value.length > 0
    }

    return value !== null && value !== undefined && value !== ''
  }

  const normalizeFilter = (field, value) => ({
    campo: field.filter?.campo ?? field.key,
    valor: field.valueFormatter ? field.valueFormatter(value, form) : String(value),
    tipo: field.filter?.tipo ?? 'STRING',
    expresion: field.filter?.expresion ?? null,
    comparacion: field.filter?.comparacion ?? 'EQUAL',
    joinType: field.filter?.joinType ?? null,
    entityJoinName: field.filter?.entityJoinName ?? null,
    operador: field.filter?.operador ?? 'AND',
    prefiltrado: field.filter?.prefiltrado ?? false,
    filedNameCompareColumn: field.filter?.filedNameCompareColumn ?? null,
  })

  const buildFilters = () => {
    const filters = []

    props.config.forEach((field) => {
      const value = form[field.key]

      if (!shouldIncludeValue(field, value)) {
        return
      }

      if (typeof field.buildFilters === 'function') {
        const builtFilters = field.buildFilters(value, form) || []
        filters.push(...builtFilters)
        return
      }

      filters.push(normalizeFilter(field, value))
    })

    return filters
  }

  const VALID_COMPARISONS = [
    'CONTAINS',
    'EQUALS',
    'NOT_EQUALS',
    'GT',
    'LT',
    'GTE',
    'LTE',
    'LIKE',
    'IN',
    'NOT_IN',
    'IS_NULL',
    'IS_NOT_NULL',
    'BETWEEN',
  ]

  const validateConfig = () => {
    props.config.forEach((field) => {
      if (!field.filter) {
        return
      }

      const comparison = field.filter.comparacion

      if (!comparison) {
        throw new Error(`El filtro "${field.key}" no tiene definida la comparación.`)
      }

      if (!VALID_COMPARISONS.includes(comparison)) {
        throw new Error(`La comparación "${comparison}" del filtro "${field.key}" no es válida.`)
      }
    })
  }

  const validateBuiltFilters = (filters) => {
    filters.forEach((filter, index) => {
      if (!filter.comparacion) {
        throw new Error(`El filtro en posición ${index} no tiene comparación.`)
      }

      if (!VALID_COMPARISONS.includes(filter.comparacion)) {
        throw new Error(
          `La comparación "${filter.comparacion}" del filtro en posición ${index} no es válida.`
        )
      }
    })
  }

  const buildFiltersDto = () => {
    const filters = buildFilters()

    validateBuiltFilters(filters)

    return {
      page: props.initialFiltersDto.page ?? 0,
      limit: props.initialFiltersDto.limit ?? 10,
      ordenaciones: Array.isArray(props.initialFiltersDto.ordenaciones)
        ? [...props.initialFiltersDto.ordenaciones]
        : [],
      filtros: filters,
      identificador: props.initialFiltersDto.identificador ?? null,
      identificadores: Array.isArray(props.initialFiltersDto.identificadores)
        ? [...props.initialFiltersDto.identificadores]
        : [],
    }
  }

  const submitSearch = () => {
    validateConfig()
    emit('search', buildFiltersDto())
  }

  const resetFilters = () => {
    applyInitialForm()
    validateConfig()
    emit('reset', buildFiltersDto())
  }
</script>
