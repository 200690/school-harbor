const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: process.env.VUE_APP_API_URL || 'http://localhost:8090',  // 从环境变量读取
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/api'  // 保留 /api 前缀
        }
      }
    }
  }
})