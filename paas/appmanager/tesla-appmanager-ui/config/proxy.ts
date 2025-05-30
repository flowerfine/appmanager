/**
 * @name 代理的配置
 * @see 在生产环境 代理是无法生效的，所以这里没有生产环境的配置
 * -------------------------------
 * The agent cannot take effect in the production environment
 * so there is no configuration of the production environment
 * For details, please see
 * https://pro.ant.design/docs/deploy
 *
 * @doc https://umijs.org/docs/guides/proxy
 */
export default {
  /**
   * @name 详细的代理配置
   * @doc https://github.com/chimurai/http-proxy-middleware
   */
  dev: {
    '/appmanager': {
      target: 'http://localhost:7001',
      changeOrigin: true,
      pathRewrite: {'^/appmanager' : ''}
    },
    '/plugins': {
      target: 'http://localhost:7001',
      changeOrigin: true
    },
    '/dynamic-script': {
      target: 'http://localhost:7001',
      changeOrigin: true
    },
    '/definition-schemas': {
      target: 'http://localhost:7001',
      changeOrigin: true
    },
    '/addon': {
      target: 'http://localhost:7001',
      changeOrigin: true
    },
    '/traits': {
      target: 'http://localhost:7001',
      changeOrigin: true
    },
  }
};
