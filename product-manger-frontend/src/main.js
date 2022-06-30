import { createApp } from 'vue'
import Antd from 'ant-design-vue'
import App from './App'
import router from './router'
import store from './store'
import 'ant-design-vue/dist/antd.css'
import '@/vab'
import { bus } from '@/utils/bus'
import contextmenu from 'vue3-contextmenu'
import 'vue3-contextmenu/dist/vue3-contextmenu.css'

// 引入vue-amap
import VueAMap, {initAMapApiLoader} from '@vuemap/vue-amap'
import '@vuemap/vue-amap/dist/style.css'
// 初始化vue-amap
initAMapApiLoader({
  // 高德的key
  key: '8c180d16acd06cb0dd636b316c901328',
})

/**
 * @author chuzhixin 1204505056@qq.com
 * @description 正式环境默认使用mock，正式项目记得注释后再打包
 */
// if (process.env.NODE_ENV === 'production') {
//   const { mockXHR } = require('@/utils/static')
//   mockXHR()
// }

const app = createApp(App)
app.use(store).use(router).use(Antd).mount('#app')
app.config.globalProperties.$bus = bus
app.use(contextmenu)
app.use(VueAmap)
