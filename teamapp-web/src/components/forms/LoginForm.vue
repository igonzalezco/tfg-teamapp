<template>
  <el-form ref="loginFormRef" @submit.prevent="submitLogin" :model="usuario" :rules="rules" label-position="top">
    <el-form-item label="Email" prop="email">
      <el-input v-model="usuario.email" type="email" placeholder="Introduce tu email" />
    </el-form-item>

    <el-form-item label="Contraseña" prop="password">
      <el-input v-model="usuario.password" type="password" placeholder="Introduce tu contraseña" show-password />
    </el-form-item>

    <el-form-item>
      <el-button :disabled="disableEntrar" type="primary" @click="submitLogin">Iniciar sesión</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { authStore } from '@/stores/auth';

export default {
  name: 'LoginForm',

  data() {
    return {
      usuario : {
        email: '',
        password: '',
      },
      rules: {
        email: [{ required: true, message: 'El email es obligatorio', trigger: 'blur' }],
        password: [{ required: true, message: 'La contraseña es obligatorio', trigger: 'blur' }],
      },
      disableEntrar: false,
      auth: null,
    }
  },
  created() {
    this.auth = authStore();
  },
  methods: {
    submitLogin() {
      var self = this;
      this.$refs.loginFormRef.validate((valid) => {
        if (valid) {
            this.disableEntrar = true;
            this.auth.login(this.usuario)
            .then((response) => {
              self.disableEntrar = false;
              this.$router.push("/content/initContent");
            })
        }
      });
    }
  }
}
</script>