<template>
  <transition name="fade">
    <div 
      v-if="isVisible" 
      class="back-to-top"
      @click="backToTop"
      :style="{ bottom: `${bottom}px`, right: `${right}px` }"
    >
      <el-button type="primary" circle>
        <el-icon>
          <Top />
        </el-icon>
      </el-button>
    </div>
  </transition>
</template>

<script>
import { Top } from '@element-plus/icons-vue'

export default {
  name: 'BackToTop',
  components: {
    Top
  },
  props: {
    visibilityHeight: {
      type: Number,
      default: 300
    },
    bottom: {
      type: Number,
      default: 120
    },
    right: {
      type: Number,
      default: 150
    },
    duration: {
      type: Number,
      default: 300
    }
  },
  data() {
    return {
      isVisible: false,
      timer: null
    }
  },
  mounted() {
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeUnmount() {
    window.removeEventListener('scroll', this.handleScroll)
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    handleScroll() {
      if (window.scrollY >= this.visibilityHeight) {
        this.isVisible = true
      } else {
        this.isVisible = false
      }
    },
    backToTop() {
      const start = window.scrollY
      const end = 0
      const duration = this.duration
      const startTime = Date.now()

      const easeInOutQuad = (t, b, c, d) => {
        t /= d / 2
        if (t < 1) return c / 2 * t * t + b
        t--
        return -c / 2 * (t * (t - 2) - 1) + b
      }

      const scrollToTop = () => {
        const currentTime = Date.now()
        const elapsed = currentTime - startTime
        const value = easeInOutQuad(elapsed, start, end - start, duration)
        window.scrollTo(0, value)
        
        if (elapsed < duration) {
          this.timer = requestAnimationFrame(scrollToTop)
        } else {
          window.scrollTo(0, end)
        }
      }

      this.timer = requestAnimationFrame(scrollToTop)
    }
  }
}
</script>

<style scoped lang="scss">
.back-to-top {
  position: fixed;
  z-index: 1000;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 768px) {
  .back-to-top {
    bottom: 20px !important;
    right: 20px !important;
  }
}
</style>
