const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/member': {
        target: 'http://localhost:8000', // 指向网关服务的地址
        changeOrigin: true, // 是否改变请求头中的 host 信息
        pathRewrite: { '^/member': '/member' } // 保持路径一致
      }
    }
  }
});