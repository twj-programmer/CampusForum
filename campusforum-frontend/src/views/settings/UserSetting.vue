<script setup>
import Card from "@/components/Card.vue";
import {Message, Refresh, Select, User} from "@element-plus/icons-vue";
import {useStore} from "@/store/index.js";
import {computed} from "vue";
import {reactive}
  from "vue";

const store = useStore()
const registerTime = computed(() => new Date(store.user.registerTime).toLocaleString())

const baseForm = reactive({
  username: '',
  gender: 1,
  phone: '',
  qq: '',
  wx: '',
  desc: ''
})
const emailForm = reactive({
  email: '',
  code: ''
})
const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名只能包含字母、数字和汉字'))
  } else {
    callback()
  }
}
const rules = {
  username: [
    {validator: validateUsername, trigger: ['blur', 'change']}
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
</script>

<template>
  <div style="display: flex">
    <div class="settings-left">
      <card :icon="User" title="账号信息设置" desc="在这里编辑您的个人信息，您可以在隐私设置中选择是否展示这些信息">
        <el-form :model="baseForm" :rules="rules" label-position="top" style="margin: 5px 10px 10px 10px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="baseForm.username"/>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="baseForm.gender">
              <el-radio :label="0">男</el-radio>
              <el-radio :label="1">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="baseForm.phone" maxlength="11"/>
          </el-form-item>
          <el-form-item label="QQ" prop="qq">
            <el-input v-model="baseForm.qq" maxlength="13"/>
          </el-form-item>
          <el-form-item label="微信" prop="wx">
            <el-input v-model="baseForm.wx" maxlength="20"/>
          </el-form-item>
          <el-form-item label="个人简介" prop="desc">
            <el-input v-model="baseForm.desc" type="textarea" :rows="6" maxlength="200"/>
          </el-form-item>
          <div>
            <el-button :icon="Select" type="success">保存用户信息</el-button>
          </div>
        </el-form>
      </card>
      <card style="margin-top: 10px" :icon="Message" title="邮箱设置" desc="您可以在这里修改默认绑定的电子邮件地址">
        <el-form :rules="rules" :model="emailForm" label-position="top" style="margin: 5px 10px 10px 10px">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="emailForm.email"/>
          </el-form-item>
          <el-form-item prop="code">
            <el-row style="width: 100%" :gutter="10">
              <el-col :span="18">
                <el-input placeholder="请获取验证码" v-model="emailForm.code"/>
              </el-col>
              <el-col :span="6">
                <el-button style="width: 100%" type="success" plain>获取验证码</el-button>
              </el-col>
            </el-row>
          </el-form-item>
          <div>
            <el-button :icon="Refresh" type="success">更换邮箱</el-button>
          </div>
        </el-form>
      </card>
    </div>
    <div class="settings-right">
      <div style="position: sticky; top: 20px">
        <card>
          <div style="text-align: center; padding: 5px 15px 0 15px">
            <el-avatar :size="70" src="/avatar.jpg"/>
            <div style="font-weight: bold">你好, {{ store.user.username }}</div>
          </div>
          <el-divider style="margin: 10px 0"/>
          <div style="font-size: 14px; color: grey; padding: 10px">
            大家好！我是撸管大王，欢迎来到我的个人主页！
          </div>
        </card>
        <card style="margin-top: 10px; font-size: 14px">
          <div>账号注册时间: {{ registerTime }}</div>
          <div style="color: grey">欢迎加入撸管交流社区！</div>
        </card>
      </div>
    </div>
  </div>
</template>

<style scoped>
.settings-left {
  flex: 1;
  margin: 20px;
}

.settings-right {
  width: 300px;
  margin: 20px 25px 20px 0;
}
</style>