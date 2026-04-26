<template>
  <div class="event-list">
    <div class="event-list__header">
      <h2 class="event-list__title">{{ t('event.list.title') }}</h2>
      <span class="app-badge">{{ total }}</span>
    </div>

    <div v-if="loading" class="event-list__state">
      {{ t('common.loading') }}
    </div>

    <div v-else-if="!items.length" class="event-list__state">
      {{ t('event.list.empty') }}
    </div>

    <div v-else class="event-list__cards">
      <article v-for="item in items" :key="item.id" class="event-card">
        <div class="event-card__content">
          <div class="event-card__header">
            <h3 class="event-card__title">{{ item.titulo }}</h3>
          </div>

          <div class="event-card__meta">
            <span>{{ formatDate(item.fechaInicio) }}</span>
            <span v-if="item.fechaFin">{{ formatDate(item.fechaFin) }}</span>
            <span v-if="item.ubicacion">{{ item.ubicacion }}</span>
          </div>
        </div>

        <div class="event-card__actions">
          <el-button class="event-card__action-button" @click="$emit('detail', item.id)">
            <el-icon><Postcard /></el-icon>
            <span>{{ t('event.actions.detail') }}</span>
          </el-button>

          <el-button
            v-if="canManageEvents"
            class="event-card__action-button"
            @click="$emit('edit', item.id)"
          >
            <el-icon><EditPen /></el-icon>
            <span>{{ t('event.actions.edit') }}</span>
          </el-button>
        </div>
      </article>
    </div>

    <div class="event-list__pagination">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next"
        :total="total"
        :current-page="page + 1"
        :page-size="limit"
        :page-sizes="[5, 10, 20, 50]"
        @current-change="onCurrentChange"
        @size-change="onSizeChange"
      />
    </div>
  </div>
</template>

<script setup>
  import dayjs from 'dayjs'
  import { useI18n } from 'vue-i18n'
  import { EditPen, Postcard } from '@element-plus/icons-vue'

  defineProps({
    items: {
      type: Array,
      default: () => [],
    },
    page: {
      type: Number,
      default: 0,
    },
    limit: {
      type: Number,
      default: 10,
    },
    total: {
      type: Number,
      default: 0,
    },
    loading: {
      type: Boolean,
      default: false,
    },
    canManageEvents: {
      type: Boolean,
      default: false,
    },
  })

  const emit = defineEmits(['page-change', 'limit-change', 'detail', 'edit'])

  const { t } = useI18n()

  const onCurrentChange = (currentPage) => {
    emit('page-change', currentPage - 1)
  }

  const onSizeChange = (pageSize) => {
    emit('limit-change', pageSize)
  }

  const formatDate = (value) => {
    if (!value) {
      return ''
    }

    return dayjs(value).format('DD/MM/YYYY HH:mm')
  }
</script>
