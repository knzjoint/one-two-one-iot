import { defineAsyncComponent } from 'vue'

export const layoutComponents = {
  "404": defineAsyncComponent(() => import("E:/document/knz-device/knz-device-manager/document/node_modules/@vuepress/theme-default/lib/client/layouts/404.vue")),
  "Layout": defineAsyncComponent(() => import("E:/document/knz-device/knz-device-manager/document/node_modules/@vuepress/theme-default/lib/client/layouts/Layout.vue")),
}
