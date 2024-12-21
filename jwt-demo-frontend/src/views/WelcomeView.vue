<script setup>

</script>

<template>
  <div style="width: 100vw; height: 100vh; overflow: hidden; display: flex">
    <div style="flex: 1">
      <el-image style="width: 100%; height: 100%" fit="cover" src="/loginBackground.png"/>
    </div>
    <div class="welcome-title">
      <h1>全球第二大PC平台</h1>
    </div>
    <div class="right-card">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component"/>
        </transition>
      </router-view>
    </div>
  </div>
</template>

<style scoped>
.right-card {
  width: 400px;
  z-index: 1;
  background-color: var(--el-bg-color);
}
.welcome-title {
  position: absolute;
  bottom: 50px;
  left: 50px;
  color: white;
  text-shadow: 0 0 10px black;
}
</style>