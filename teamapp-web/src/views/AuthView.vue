<template>
    <body class="auth flex_div">
      <div class="auth-placeholder">
        <img src="@/assets/logo-full.png" class="logo"/>
      </div>
      <div class="auth-form">
        <login-form/>
        <p style="margin-top: 1rem;">
          ¿No tienes cuenta?
          <el-link type="primary" @click="showRegister = !showRegister">Mostrar Registro</el-link>
        </p>
        <register-form v-if="showRegister" style="margin-top: 2rem;" />
      </div>
    </body>
  </template>
  
  <script>
  import LoginForm from '@/components/forms/LoginForm.vue'
  import RegisterForm from '@/components/forms/RegisterForm.vue'
  
  export default {
    name: 'AuthView',
  
    components: {
      LoginForm,
      RegisterForm
    },
  
    data() {
      return {
        showRegister: false
      }
    },
  }
  </script>