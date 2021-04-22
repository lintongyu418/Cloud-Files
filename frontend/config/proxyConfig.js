module.exports = {
  proxy: {
    '/api': {
      target: 'http://localhost:10000',
      ws: true, // 是否跨域
      changeOrigin: true,
      pathRewrite: {
        '^/api':'/',
      },
    },
  },
}
