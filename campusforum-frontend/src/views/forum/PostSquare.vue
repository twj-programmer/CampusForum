<script setup>
import LightCard from "@/components/LightCard.vue";
import {Calendar, CollectionTag, EditPen, Link} from "@element-plus/icons-vue";
import Weather from "@/components/Weather.vue";
import {computed, reactive} from "vue";
import {get} from "@/net/net.js";
import {ElMessage} from "element-plus";

const weather = reactive({
  location: {},
  now: {},
  hourly: [],
  success: false
})

const today = computed(() => {
  const date = new Date();
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`;
})

navigator.geolocation.getCurrentPosition(position => {
  const longitude = position.coords.longitude;
  const latitude = position.coords.latitude;
  console.log(longitude, latitude);
  get(`/api/forum/weather?longtitude=${longitude}&latitude=${latitude}`, data => {
    console.log(data);
    Object.assign(weather, data);
    weather.success = true;
  })
}, error => {
  console.log(error);
  ElMessage.warning("位置信息和获取超时，请检查网络设置");
  get(`/api/forum/weather?longtitude=116.40529&latitude=39.90499`, data => {
    Object.assign(weather, data);
    weather.success = true;
  })
}, {
  enableHighAccuracy: true,
  timeout: 3000
})
</script>

<template>
  <div style="display: flex; margin: 20px auto; gap: 20px; max-width: 900px">
    <div style="flex: 1">
      <light-card>
        <div class="create-topic">
          <el-icon><EditPen/></el-icon>
          <span style="color: grey"> 点击发表新话题...</span>
        </div>
      </light-card>
      <light-card style="margin-top: 10px; height: 30px">

      </light-card>
      <div style="margin-top: 10px; display: flex; flex-direction: column; gap: 10px">
        <light-card style="height: 150px" v-for="item in 10">

        </light-card>
      </div>
    </div>
    <div style="width: 280px">
      <div style="position: sticky; top: 20px">
        <light-card>
          <div style="font-weight: bold">
            <el-icon><CollectionTag/></el-icon>
            论坛公告
          </div>
          <el-divider style="margin: 10px 0"/>
          <div style="font-size: 14px; margin: 10px; color: grey">
            <p>本论坛允许黄、赌、毒。</p>
          </div>
        </light-card>
        <light-card style="margin-top: 10px">
          <div style="font-weight: bold">
            <el-icon><Calendar/></el-icon>
            当地天气
          </div>
          <el-divider style="margin: 10px 0"/>
          <weather :data="weather"/>
        </light-card>
        <light-card style="margin-top: 10px">
          <div class="info-text">
            <div>当前日期</div>
            <div>{{ today }}</div>
          </div>
          <div class="info-text" style="margin-top: 3px">
            <div>当前ip地址</div>
            <div>192.168.174.128</div>
          </div>
        </light-card>
        <div style="font-size: 14px; margin-top: 10px; color: grey">
          <el-icon><Link/></el-icon>
          友情链接
          <el-divider style="margin: 10px 0"/>
        </div>
        <div style="display: grid; grid-template-columns: repeat(2, 1fr); grid-gap: 10px; margin-top: 10px">
          <div style="background-color: var(--el-bg-color); border-radius: 5px; padding: 10px">
            <div>Vue.js</div>
            <div>https://vuejs.org/</div>
          </div>
          <div style="background-color: var(--el-bg-color); border-radius: 5px; padding: 10px">
            <div>Element Plus</div>
            <div>https://element-plus.gitee.io/#/zh-CN</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
.info-text {
  display: flex;
  justify-content: space-between;
  color: grey;
  font-size: 14px
}
.create-topic {
  background-color: #efefef;
  border-radius: 5px;
  height: 40px;
  font-size: 14px;
  line-height: 40px;
  padding: 0 10px;
  color: grey;

  &:hover {
    cursor: pointer;
  }
}
</style>