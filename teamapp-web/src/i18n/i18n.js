import { createI18n } from 'vue-i18n'
import es from '@/lang/CustomMessages_es.json';

export const i18n = createI18n({
  legacy: false,
  locale: 'es',
  fallbackLocale: 'en',
  messages: { es }
})

export function setLocale(locale, i18nInstance = 118n) {
  i18nInstance.global.locale.value = locale
  document.documentElement.setAttribute('lang', locale)
}