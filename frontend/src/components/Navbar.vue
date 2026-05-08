<template>
  <nav class="navbar">
    <div class="navbar-inner">
      <div class="navbar-left">
        <div class="logo">
          <div class="logo-icon">
            <svg viewBox="0 0 24 24" width="20" height="20" fill="white">
              <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
            </svg>
          </div>
          <span class="logo-text">健身打卡助手</span>
        </div>
      </div>
      <div class="navbar-center">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          :class="{ active: $route.path === item.path }"
        >
          {{ item.label }}
        </router-link>
      </div>
    </div>
  </nav>
</template>

<script setup>
const navItems = [
  { path: '/', label: '首页' },
  { path: '/quick-check', label: '快速打卡' },
  { path: '/history', label: '运动历史' },
  { path: '/goals', label: '我的目标' },
  { path: '/calendar', label: '运动日历' }
]
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  z-index: 1000;
}

.navbar-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.navbar-left {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background: #F4672A;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  color: #67C23A;
}

.navbar-center {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-item {
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  text-decoration: none;
  transition: all 0.2s;
}

.nav-item:hover {
  color: #F4672A;
}

.nav-item.active {
  background: #F4672A;
  color: #fff;
}
</style>
