import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    sidebar: {
      opened: localStorage.getItem('sidebarStatus') ? !!+localStorage.getItem('sidebarStatus') : true,
      withoutAnimation: false
    },
    device: 'desktop',
    size: localStorage.getItem('size') || 'default'
  }),

  getters: {
    isDesktop: (state) => state.device === 'desktop',
    isMobile: (state) => state.device === 'mobile'
  },

  actions: {
    toggleSidebar() {
      this.sidebar.opened = !this.sidebar.opened
      this.sidebar.withoutAnimation = false
      if (this.sidebar.opened) {
        localStorage.setItem('sidebarStatus', '1')
      } else {
        localStorage.setItem('sidebarStatus', '0')
      }
    },

    closeSidebar(withoutAnimation) {
      this.sidebar.opened = false
      this.sidebar.withoutAnimation = withoutAnimation
      localStorage.setItem('sidebarStatus', '0')
    },

    toggleDevice(device) {
      this.device = device
    },

    setSize(size) {
      this.size = size
      localStorage.setItem('size', size)
    }
  }
})
