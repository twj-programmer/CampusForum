<script setup>
import {computed, ref} from 'vue'
import { reactive } from 'vue'
import {EditPen, Lock, Message} from "@element-plus/icons-vue";
import {get} from "@/net/net.js";
import {ElMessage} from "element-plus";
import {post} from "@/net/net.js";
import router from "@/router/router.js";

const active = ref(0)
const coldTime = ref(0)
const formRef = ref()

const form = reactive({
  email: '',
  code: '',
  password: '',
  password_repeat: ''
})

function askCode() {
  if (isEmailValid.value) {
    coldTime.value = 60
    get(`/api/auth/ask-code?email=${form.email}&type=register`, () => {
      ElMessage.success(`验证码已发送到${form.email}，请注意查收`)
      const interval = setInterval(() => {
        if (coldTime.value > 0) {
          coldTime.value--;
        } else {
          clearInterval(interval);
        }
      }, 1000)
    }, (message) => {
      ElMessage.warning(message)
      coldTime.value = 0
    })
  } else {
    ElMessage.warning('请输入正确的邮箱')
  }
}

const isEmailValid = computed(() => {
  return /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(form.email)
})

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rule = {
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码必须在6-20个字符之间', trigger: ['blur', 'change'] }
  ],
  password_repeat: [
    { validator: validatePassword, trigger: ['blur', 'change'] }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱', trigger: ['blur', 'change'] }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度必须为6位', trigger: ['blur', 'change'] }
  ]
}

function confirmReset() {
  formRef.value.validate((valid) => {
    if (valid) {
      post('/api/auth/forget-confirm', {
        email: form.email,
        code: form.code
      }, () => active.value++)
    }
  })
}

function doRest() {
  formRef.value.validate((valid) => {
    if (valid) {
      post('/api/auth/forget-password', {...form}, () => {
        ElMessage.success('密码重置成功')
        router.push('/')
      })
    }
  })
}
</script>

<template>
  <div style="text-align: center">
    <div style="margin-top: 30px">
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="验证邮箱"></el-step>
        <el-step title="重置密码"></el-step>
      </el-steps>
    </div>
    <div v-if="active === 0" style="margin: 0 20px">
      <div style="margin-top: 80px">
        <div style="font-size: 25px; font-weight: bold">验证邮箱</div>
        <div style="font-size: 14px; color: grey; margin-top: 5px">请输入邮箱</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rule" ref="formRef">
          <el-form-item prop="email">
            <el-input v-model="form.email" type="text" placeholder="邮箱">
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-row :gutter="10" style="width: 100%">
              <el-col :span="17">
                <el-input v-model="form.code" maxlength="6" minlength="6" type="text" placeholder="请输入验证码">
                  <template #prefix>
                    <el-icon><EditPen /></el-icon>
                  </template>
                </el-input>
              </el-col>
              <el-col :span="5">
                <el-button @click="askCode()" :disabled="!isEmailValid || coldTime > 0" type="success">
                  {{ coldTime > 0 ? `请稍后 ${coldTime} 秒` : '获取验证码' }}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 40px">
        <el-button style="width: 270px" @click="confirmReset" type="warning" plain>验证邮箱</el-button>
      </div>
    </div>
    <div v-if="active === 1" style="margin: 0 20px">
      <div style="margin-top: 80px">
        <div style="font-size: 25px; font-weight: bold">重置密码</div>
        <div style="font-size: 14px; color: grey; margin-top: 5px">请输入新密码</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rule" ref="formRef">
          <el-form-item prop="password">
            <el-input v-model="form.password" maxlength="20" minlength="6" type="password" placeholder="密码">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password_repeat">
            <el-input v-model="form.password_repeat" maxlength="20" minlength="6" type="password" placeholder="重复密码">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 40px">
        <el-button style="width: 270px" @click="doRest" type="danger" plain>重置密码</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>