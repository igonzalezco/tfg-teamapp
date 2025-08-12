<template>
    <el-form
      ref="registerFormRef"
      :model="form"
      :rules="rules"
      label-position="top"
      @submit.prevent="submitRegister"
    >
      <el-form-item :label="$t('register.email')" prop="email">
        <el-input v-model="form.email" type="email" :placeholder="$t('register.placeholder.email')" />
      </el-form-item>
  
      <el-form-item :label="$t('register.password')" prop="password">
        <el-input v-model="form.password" type="password" :placeholder="$t('register.placeholder.password')" show-password />
      </el-form-item>
  
      <el-form-item :label="$t('register.passwordRepeat')" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" type="password" :placeholder="$t('register.placeholder.passwordRepeat')" show-password />
      </el-form-item>
  
      <el-form-item>
        <el-button type="success" @click="submitRegister">{{ $t('register.register') }}</el-button>
      </el-form-item>
    </el-form>
  </template>
  
  <script>
  export default {
    name: 'RegisterForm',
  
    data() {
      const validatePassword = (rule, value, callback) => {
        if (!value) {
          callback(new Error(this.$t('register.rules.password')))
        } else {
          const strongRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[\W\d])[A-Za-z\d\W]{8,}$/
          if (!strongRegex.test(value)) {
            callback(
              new Error(this.$t('register.rules.passwordFormat'))
            )
          } else {
            callback()
          }
        }
      }
  
      const validateConfirmPassword = (rule, value, callback) => {
        if (!value) {
          callback(new Error(this.$t('register.rules.passwordConfirm')))
        } else if (value !== this.form.password) {
          callback(new Error(this.$t('register.rules.passwordCompare')))
        } else {
          callback()
        }
      }
  
      return {
        form: {
          email: '',
          password: '',
          confirmPassword: ''
        },
        rules: {
          email: [{ required: true, message: this.$t('register.rules.email'), trigger: 'blur' }],
          password: [{ validator: validatePassword, trigger: 'blur' }],
          confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }]
        }
      }
    },
  
    methods: {
      submitRegister() {
        this.$refs.registerFormRef.validate((valid) => {
          if (valid) {
            this.$emit('register', {
              email: this.form.email,
              password: this.form.password
            })
          }
        })
      }
    }
  }
  </script>