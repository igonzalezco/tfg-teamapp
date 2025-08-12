<template>
  <el-form ref="loginFormRef" @submit.prevent="submitLogin" :model="usuario" :rules="rules" label-position="top">
    <el-form-item :label="$t('login.email')" prop="email">
      <el-input v-model="usuario.username" type="email" :placeholder="$t('login.placeholder.email')" />
    </el-form-item>

    <el-form-item :label="$t('login.password')" prop="password">
      <el-input v-model="usuario.password" type="password" :placeholder="$t('login.placeholder.password')" show-password />
    </el-form-item>

    <el-form-item>
      <el-button :disabled="disableEntrar" type="primary" @click="submitLogin">{{ $t('login.login') }}</el-button>
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
        username: '',
        password: '',
      },
      rules: {
        username: [{ required: true, message: this.$t('login.rules.email'), trigger: 'blur' }],
        password: [{ required: true, message: this.$t('login.rules.password'), trigger: 'blur' }],
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