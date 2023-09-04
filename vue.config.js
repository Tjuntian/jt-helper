const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/api/': {
        target: 'http://127.0.0.1:520/',
        pathRewrite: { '^/api/': '' },
        changeOrigin: true, //开启代理
        ['/api/']: ''
      },
    },
  },
})
