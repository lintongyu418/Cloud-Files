module.exports = {
  proxy: {
    '/api': {
      target: 'http://161.35.224.251:10000',
      ws: true, // 是否跨域
      changeOrigin: true,
      pathRewrite: {
        '^/api':'/',
      },
    },
  },
}
