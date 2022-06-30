const path = require('path')
const CompressionWebpackPlugin = require('compression-webpack-plugin')
const {
  baseURL,
  publicPath,
  assetsDir,
  outputDir,
  lintOnSave,
  transpileDependencies,
  title,
  abbreviation,
  devPort,
  providePlugin,
  build7z,
  donation,
} = require('./src/config')
const { webpackBarName, webpackBanner, donationConsole } = require('vab-config')

if (donation) donationConsole()
const { version, author } = require('./package.json')
const Webpack = require('webpack')
const WebpackBar = require('webpackbar')
const FileManagerPlugin = require('filemanager-webpack-plugin')
const dayjs = require('dayjs')
const date = dayjs().format('YYYY_M_D')
const time = dayjs().format('YYYY-M-D HH:mm:ss')
process.env.VUE_APP_UPDATE_TIME = time
process.env.VUE_APP_VERSION = version

const IS_PROD =['production', 'prod'].includes(process.env.NODE_ENV)

const resolve = (dir) => {
  return path.join(__dirname, dir)
}

// const mockServer = () => {
//   if (process.env.NODE_ENV === 'development') {
//     return require('./mock/mockServer.js')
//   } else {
//     return ''
//   }
// }

module.exports = {
  publicPath,
  assetsDir,
  outputDir,
  lintOnSave,
  transpileDependencies,
  devServer: {
    hot: true,
    port: devPort,
    open: true,
    noInfo: true,
    overlay: {
      warnings: true,
      errors: true,
    },
    // 注释掉的地方是前端配置代理访问后端的示例
    proxy: {
      [baseURL]: {
        target: process.env.VUE_APP_API_PROXY_TARGET,
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          "^/api": ""
        },
      },
    },
    // after: mockServer(),
  },
  configureWebpack() {

    const productionGzipExtensions = ['html', 'js', 'css']

    // // 生产环境相关配置
    // if (process.env.NODE_ENV === 'development') {
    //   //gzip压缩
    //   const productionGzipExtensions = ['html', 'js', 'css']
    //   config.plugins.push(
    //     new CompressionWebpackPlugin({
    //       filename: '[path].gz[query]',
    //       algorithm: 'gzip',
    //       test: new RegExp(
    //         '\\.(' + productionGzipExtensions.join('|') + ')$'
    //       ),
    //       threshold: 10240, // 只有大小大于该值的资源会被处理 10240
    //       minRatio: 0.8, // 只有压缩率小于这个值的资源才会被处理
    //       deleteOriginalAssets: false // 删除原文件
    //     })
    //   )
    // }

    return {
      devtool: process.env.NODE_ENV === 'development' ? 'source-map' : undefined,
      resolve: {
        alias: {
          '@': resolve('src'),
          '*': resolve(''),
        },
      },
      plugins: [
        new Webpack.ProvidePlugin(providePlugin),
        new WebpackBar({
          name: '一二一物联平台',
        }),
      ],
    }
  },
  chainWebpack(config) {
    config.resolve.symlinks(true)
    config.module.rule('svg').exclude.add(resolve('src/icon/remixIcon')).end()

    // 开启js、css压缩
    if (IS_PROD) {
      config.plugin("compressionPlugin").use(
        new CompressionWebpackPlugin({
          filename: '[path].gz[query]',
          algorithm: 'gzip',
          test: new RegExp(
            '\\.(' + productionGzipExtensions.join('|') + ')$'
          ),
          threshold: 10240, // 只有大小大于该值的资源会被处理 10240
          minRatio: 0.8, // 只有压缩率小于这个值的资源才会被处理
          deleteOriginalAssets: false // 删除原文件
        })
      )
    }

    config.module
      .rule('remixIcon')
      .test(/\.svg$/)
      .include.add(resolve('src/icon/remixIcon'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({ symbolId: 'remix-icon-[name]' })
      .end()

    config.when(process.env.NODE_ENV === 'development', (config) => {
      config.devtool('source-map')
    })

    config.when(process.env.NODE_ENV !== 'development', (config) => {
      config.performance.set('hints', false)
      config.devtool('none')
      config.optimization.splitChunks({
        chunks: 'all',
        cacheGroups: {
          libs: {
            name: 'vue-admin-beautiful-libs',
            test: /[\\/]node_modules[\\/]/,
            priority: 10,
            chunks: 'initial',
          },
        },
      })
      // config
      //   .plugin('banner')
      //   .use(Webpack.BannerPlugin, [`${webpackBanner}${time}`])
      //   .end()
      config.module
        .rule('images')
        .use('image-webpack-loader')
        .loader('image-webpack-loader')
        .options({
          bypassOnDebug: true,
        })
        .end()
    })

    if (build7z) {
      config.when(process.env.NODE_ENV === 'production', (config) => {
        config
          .plugin('fileManager')
          .use(FileManagerPlugin, [
            {
              onEnd: {
                delete: [`./${outputDir}/video`, `./${outputDir}/data`],
                archive: [
                  {
                    source: `./${outputDir}`,
                    destination: `./${outputDir}/${abbreviation}_${outputDir}_${date}.7z`,
                  },
                ],
              },
            },
          ])
          .end()
      })
    }
  },
  runtimeCompiler: true,
  productionSourceMap: process.env.NODE_ENV === 'development',
  css: {
    requireModuleExtension: true,
    sourceMap: true,
    loaderOptions: {
      less: {
        lessOptions: {
          javascriptEnabled: true,
          modifyVars: {
            'vab-color-blue': '#1890ff',
            'vab-margin': '20px',
            'vab-padding': '20px',
            'vab-header-height': '65px',
          },
        },
      },
    },
  },
}
