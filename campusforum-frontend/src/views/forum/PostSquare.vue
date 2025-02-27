<script setup>
import LightCard from "@/components/LightCard.vue";
import {Calendar, CollectionTag, Link} from "@element-plus/icons-vue";
import Weather from "@/components/Weather.vue";
import {computed} from "vue";

const today = computed(() => {
  const date = new Date();
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`;
})
</script>

<template>
  <div style="display: flex; margin: 20px auto; gap: 20px; max-width: 900px">
    <div style="flex: 1">
      <light-card style="height: 2000px"/>
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
          <weather/>
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

<style scoped>
.info-text {
  display: flex;
  justify-content: space-between;
  color: grey;
  font-size: 14px
}
</style>