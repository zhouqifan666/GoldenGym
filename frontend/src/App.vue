<template>
  <div id="app">
    <div class="light-rays-bg">
      <LightRays
        raysOrigin="top-center"
        raysColor="#F4672A"
        :raysSpeed="1.5"
        :lightSpread="0.8"
        :rayLength="1.2"
        :followMouse="true"
        :mouseInfluence="0.1"
        :noiseAmount="0.1"
        :distortion="0.05"
      />
    </div>
    <Navbar />
    <main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="page-fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

<script setup>
import Navbar from './components/Navbar.vue'
import LightRays from './components/LightRays.vue'
</script>

<style>
:root {
  --bg-primary: #0a0a0f;
  --bg-secondary: #12121a;
  --bg-card: rgba(255, 255, 255, 0.04);
  --bg-card-hover: rgba(255, 255, 255, 0.07);
  --bg-card-solid: #16161f;
  --border-subtle: rgba(255, 255, 255, 0.06);
  --border-medium: rgba(255, 255, 255, 0.1);

  --text-primary: #f0f0f5;
  --text-secondary: #8a8a9a;
  --text-muted: #55556a;

  --accent: #f59e0b;
  --accent-bright: #fbbf24;
  --accent-dim: #b45309;
  --accent-glow: rgba(245, 158, 11, 0.15);
  --accent-glow-strong: rgba(245, 158, 11, 0.3);

  --success: #10b981;
  --success-glow: rgba(16, 185, 129, 0.15);
  --danger: #ef4444;
  --danger-glow: rgba(239, 68, 68, 0.15);
  --info: #6366f1;
  --info-glow: rgba(99, 102, 241, 0.15);

  --font-display: 'Outfit', sans-serif;
  --font-body: 'DM Sans', sans-serif;

  --radius-sm: 8px;
  --radius-md: 12px;
  --radius-lg: 16px;
  --radius-xl: 20px;
  --radius-full: 9999px;

  --shadow-card: 0 1px 2px rgba(0, 0, 0, 0.3), 0 4px 16px rgba(0, 0, 0, 0.2);
  --shadow-elevated: 0 4px 24px rgba(0, 0, 0, 0.4), 0 0 60px rgba(245, 158, 11, 0.03);
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html {
  background: var(--bg-primary);
}

body {
  font-family: var(--font-body);
  background: var(--bg-primary);
  color: var(--text-primary);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  overflow-x: hidden;
}

h1, h2, h3, h4, h5, h6 {
  font-family: var(--font-display);
}

#app {
  position: relative;
  min-height: 100vh;
}

.light-rays-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  pointer-events: none;
  z-index: 0;
}

.main-content {
  padding-top: 72px;
  min-height: 100vh;
  position: relative;
  z-index: 1;
}

/* Page transitions */
.page-fade-enter-active {
  transition: opacity 0.25s ease, transform 0.25s ease;
}
.page-fade-leave-active {
  transition: opacity 0.15s ease;
}
.page-fade-enter-from {
  opacity: 0;
  transform: translateY(8px);
}
.page-fade-leave-to {
  opacity: 0;
}

/* Element Plus dark overrides */
.el-dialog {
  background: var(--bg-card-solid) !important;
  border: 1px solid var(--border-subtle) !important;
  border-radius: var(--radius-lg) !important;
  box-shadow: var(--shadow-elevated) !important;
}

.el-dialog__title {
  color: var(--text-primary) !important;
  font-family: var(--font-display) !important;
  font-weight: 600 !important;
}

.el-dialog__body {
  color: var(--text-secondary) !important;
}

.el-form-item__label {
  color: var(--text-secondary) !important;
  font-family: var(--font-body) !important;
}

.el-input__wrapper,
.el-textarea__inner {
  background: var(--bg-secondary) !important;
  border-color: var(--border-medium) !important;
  box-shadow: none !important;
  color: var(--text-primary) !important;
  border-radius: var(--radius-sm) !important;
}

.el-input__wrapper:focus-within,
.el-textarea__inner:focus {
  border-color: var(--accent) !important;
  box-shadow: 0 0 0 2px var(--accent-glow) !important;
}

.el-input__inner {
  color: var(--text-primary) !important;
}

.el-input__inner::placeholder {
  color: var(--text-muted) !important;
}

.el-select .el-input__wrapper {
  background: var(--bg-secondary) !important;
}

.el-select-dropdown {
  background: var(--bg-card-solid) !important;
  border: 1px solid var(--border-subtle) !important;
}

.el-select-dropdown__item {
  color: var(--text-secondary) !important;
}

.el-select-dropdown__item.hover,
.el-select-dropdown__item:hover {
  background: var(--accent-glow) !important;
  color: var(--accent) !important;
}

.el-select-dropdown__item.selected {
  color: var(--accent) !important;
  font-weight: 600 !important;
}

.el-input-number {
  --el-input-number-step-btn-bg: var(--bg-secondary);
}

.el-input-number .el-input__wrapper {
  background: var(--bg-secondary) !important;
}

.el-date-editor .el-input__wrapper {
  background: var(--bg-secondary) !important;
}

.el-picker-panel {
  background: var(--bg-card-solid) !important;
  border: 1px solid var(--border-subtle) !important;
  color: var(--text-primary) !important;
}

.el-date-table th {
  color: var(--text-muted) !important;
}

.el-date-table td .el-date-table-cell__text {
  color: var(--text-secondary) !important;
}

.el-date-table td.today .el-date-table-cell__text {
  color: var(--accent) !important;
}

.el-date-table td.current:not(.disabled) .el-date-table-cell__text {
  background: var(--accent) !important;
  color: #000 !important;
}

.el-button {
  font-family: var(--font-body) !important;
  font-weight: 500 !important;
  border-radius: var(--radius-sm) !important;
}

.el-button--warning {
  background: var(--accent) !important;
  border-color: var(--accent) !important;
  color: #000 !important;
  font-weight: 600 !important;
}

.el-button--warning:hover {
  background: var(--accent-bright) !important;
  border-color: var(--accent-bright) !important;
}

.el-button--warning.is-plain {
  background: var(--accent-glow) !important;
  border-color: var(--accent-dim) !important;
  color: var(--accent) !important;
}

.el-button--warning.is-plain:hover {
  background: var(--accent-glow-strong) !important;
  border-color: var(--accent) !important;
}

.el-button--primary {
  background: var(--info) !important;
  border-color: var(--info) !important;
}

.el-button--danger.is-link {
  color: var(--text-muted) !important;
}

.el-button--danger.is-link:hover {
  color: var(--danger) !important;
}

.el-tag {
  border-radius: var(--radius-full) !important;
  font-family: var(--font-body) !important;
  font-weight: 500 !important;
  border: none !important;
}

.el-tag--dark.el-tag--success {
  background: var(--success-glow) !important;
  color: var(--success) !important;
}

.el-tag--dark.el-tag--warning {
  background: var(--accent-glow) !important;
  color: var(--accent) !important;
}

.el-tag--dark.el-tag--danger {
  background: var(--danger-glow) !important;
  color: var(--danger) !important;
}

.el-tag--plain {
  background: var(--bg-card) !important;
  color: var(--text-secondary) !important;
  border: 1px solid var(--border-subtle) !important;
}

.el-checkbox__inner {
  background: var(--bg-secondary) !important;
  border-color: var(--border-medium) !important;
}

.el-checkbox__input.is-checked .el-checkbox__inner {
  background: var(--accent) !important;
  border-color: var(--accent) !important;
}

.el-pagination {
  --el-pagination-bg-color: transparent;
  --el-pagination-text-color: var(--text-muted);
  --el-pagination-button-bg-color: var(--bg-card);
  --el-pagination-hover-color: var(--accent);
}

.el-pagination button {
  background: var(--bg-card) !important;
  color: var(--text-muted) !important;
}

.el-pager li {
  background: var(--bg-card) !important;
  color: var(--text-muted) !important;
}

.el-pager li.is-active {
  color: var(--accent) !important;
}

.el-message-box {
  background: var(--bg-card-solid) !important;
  border: 1px solid var(--border-subtle) !important;
}

.el-message-box__title {
  color: var(--text-primary) !important;
}

.el-message-box__message {
  color: var(--text-secondary) !important;
}

/* Scrollbar */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: var(--border-medium);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: var(--text-muted);
}

/* Stagger animation utility */
@keyframes slide-up-fade {
  from {
    opacity: 0;
    transform: translateY(16px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-in {
  animation: slide-up-fade 0.4s ease-out both;
}
</style>
