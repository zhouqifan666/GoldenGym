<template>
  <nav class="navbar">
    <div class="navbar-inner">
      <div class="navbar-left">
        <router-link to="/" class="logo">
          <div class="logo-icon">
            <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
              <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2" />
            </svg>
          </div>
          <span class="logo-text">Golden<span class="logo-accent">GYM</span></span>
        </router-link>
      </div>
      <div class="navbar-center">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          :class="{ active: $route.path === item.path }"
        >
          <span class="nav-icon">{{ item.icon }}</span>
          <span class="nav-label">{{ item.label }}</span>
        </router-link>
      </div>
    </div>
    <div class="navbar-glow"></div>
  </nav>
</template>

<script setup>
const navItems = [
  { path: '/', label: '首页', icon: '◆' },
  { path: '/quick-check', label: '打卡', icon: '⚡' },
  { path: '/history', label: '历史', icon: '◈' },
  { path: '/goals', label: '目标', icon: '◎' },
  { path: '/calendar', label: '日历', icon: '▦' }
]
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 64px;
  background: transparent;
  backdrop-filter: none;
  -webkit-backdrop-filter: none;
  border-bottom: none;
  z-index: 1000;
}

.navbar-glow {
  display: none;
}

.navbar-inner {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 28px;
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
  gap: 12px;
  text-decoration: none;
  cursor: pointer;
}

.logo-icon {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, var(--accent), var(--accent-dim));
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #000;
  box-shadow: 0 2px 12px var(--accent-glow-strong);
}

.logo-text {
  font-family: var(--font-display);
  font-size: 20px;
  font-weight: 700;
  color: var(--text-primary);
  letter-spacing: -0.5px;
}

.logo-accent {
  color: var(--accent);
}

.navbar-center {
  display: flex;
  align-items: center;
  gap: 2px;
  background: var(--bg-card);
  border-radius: var(--radius-full);
  padding: 4px;
  border: 1px solid var(--border-subtle);
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 7px 16px;
  border-radius: var(--radius-full);
  font-size: 13px;
  font-weight: 500;
  color: var(--text-muted);
  text-decoration: none;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
}

.nav-item:hover {
  color: var(--text-secondary);
  background: rgba(255, 255, 255, 0.03);
}

.nav-item.active {
  background: var(--accent);
  color: #000;
  box-shadow: 0 2px 8px var(--accent-glow-strong);
}

.nav-icon {
  font-size: 10px;
  line-height: 1;
}

.nav-label {
  letter-spacing: 0.3px;
}
</style>
