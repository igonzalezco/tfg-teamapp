<template>
    <el-form
      ref="registerFormRef"
      :model="form"
      :rules="rules"
      label-position="top"
      @submit.prevent="submitRegister"
    >
      <el-form-item label="Nombre" prop="name">
        <el-input v-model="form.name" placeholder="Introduce tu nombre" />
      </el-form-item>
  
      <el-form-item label="Email" prop="email">
        <el-input v-model="form.email" type="email" placeholder="Introduce tu email" />
      </el-form-item>
  
      <el-form-item label="Contraseña" prop="password">
        <el-input v-model="form.password" type="password" placeholder="Introduce tu contraseña" show-password />
      </el-form-item>
  
      <el-form-item label="Confirmar Contraseña" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" type="password" placeholder="Repite tu contraseña" show-password />
      </el-form-item>
  
      <el-form-item>
        <el-button type="success" @click="submitRegister">Registrarse</el-button>
      </el-form-item>
    </el-form>
  </template>
  
  <script>
  export default {
    name: 'RegisterForm',
  
    data() {
      const validatePassword = (rule, value, callback) => {
        if (!value) {
          callback(new Error('La contraseña es obligatoria'))
        } else {
          const strongRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[\W\d])[A-Za-z\d\W]{8,}$/
          if (!strongRegex.test(value)) {
            callback(
              new Error(
                'La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas y un número o carácter especial'
              )
            )
          } else {
            callback()
          }
        }
      }
  
      const validateConfirmPassword = (rule, value, callback) => {
        if (!value) {
          callback(new Error('Por favor, confirma tu contraseña'))
        } else if (value !== this.form.password) {
          callback(new Error('Las contraseñas no coinciden'))
        } else {
          callback()
        }
      }
  
      return {
        form: {
          name: '',
          email: '',
          password: '',
          confirmPassword: ''
        },
        rules: {
          name: [{ required: true, message: 'El nombre es obligatorio', trigger: 'blur' }],
          email: [{ required: true, message: 'El email es obligatorio', trigger: 'blur' }],
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
              name: this.form.name,
              email: this.form.email,
              password: this.form.password
            })
          }
        })
      }
    }
  }
  </script>