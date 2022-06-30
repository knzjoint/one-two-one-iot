import { createRouter, createWebHashHistory } from 'vue-router'
import Layout from '@/layout'

import FlowDesigner from '@/components/flow/designer/FlowDesigner'
import WorkFlow from '@/components/work-flow/index'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true,
  },
  {
    path: '/403',
    name: '403',
    component: () => import('@/views/403'),
    hidden: true,
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/404'),
    hidden: true,
  },
]
export const asyncRoutes = [
  {
    path: '/',
    component: Layout,
    redirect: '/index',
    meta: {
      title: '首页',
      icon: 'home-4-line',
      affix: true,
      roles: ['ADMIN', 'PERSONAL', 'FIRM'],
    },
    children: [
      {
        path: 'index',
        name: 'Index',
        component: () => import('@/views/index'),
        meta: {
          title: '首页',
          icon: 'home-4-line',
          affix: true,
          roles: ['ADMIN', 'PERSONAL', 'FIRM'],
        },
      },
    ],
  },
  {
    path: '/product',
    component: Layout,
    redirect: '/product/table',
    alwaysShow: true,
    meta: {
      title: '产品',
      icon: 'product-hunt-fill',
      roles: ['ADMIN', 'PERSONAL', 'FIRM'],
    },
    children: [
      {
        path: 'table',
        name: 'productTable',
        component: () => import('@/views/product/table'),
        meta: {
          title: '产品列表',
          icon: 'table-2',
          roles: ['ADMIN', 'PERSONAL', 'FIRM'],
        },
      },
      {
        path: 'topic/:id',
        name: 'productTopics',
        hidden: true,
        component: () => import('@/views/product/topic'),
        meta: {
          title: '产品主题',
          icon: 'table-2',
          roles: ['ADMIN', 'PERSONAL', 'FIRM'],
        },
      },
    ],
  },
  {
    path: '/device',
    component: Layout,
    redirect: '/device/table',
    alwaysShow: true,
    meta: {
      title: '设备',
      icon: 'device-fill',
      roles: ['ADMIN', 'PERSONAL', 'FIRM'],
    },
    children: [
      {
        path: 'table',
        name: 'deviceTable',
        component: () => import('@/views/device/table'),
        meta: {
          title: '设备列表',
          icon: 'table-2',
          roles: ['ADMIN', 'PERSONAL', 'FIRM'],
        },
      },
      {
        path: 'topic/:id',
        name: 'DeviceTopics',
        hidden: true,
        component: () => import('@/views/device/topic'),
        meta: {
          title: '设备主题',
          icon: 'table-2',
          roles: ['ADMIN', 'PERSONAL', 'FIRM'],
          keepAlive: true,
        },
      },
      {
        path: 'things',
        name: 'ThingsTable',
        hidden: true,
        component: () => import('@/views/device/things'),
        meta: {
          title: '物',
          icon: 'table-2',
          roles: ['ADMIN', 'PERSONAL', 'FIRM'],
        },
      },
    ],
  },
  {
    path: '/venues',
    component: Layout,
    redirect: '/venues/show',
    alwaysShow: true,
    meta: {
      title: '房间/场地',
      icon: 'archive-drawer-fill',
      roles: ['ADMIN', 'PERSONAL', 'FIRM'],
    },
    children: [
      {
        path: 'show',
        name: 'venuesShow',
        component: () => import('@/views/venues/show'),
        meta: {
          title: '房间/场地',
          icon: 'table-2',
          roles: ['ADMIN', 'PERSONAL', 'FIRM'],
        },
      },
      {
        path: 'work-flow',
        name: 'WorkFlow',
        component: WorkFlow,
        hidden: true,
        meta: {
          title: '场景联动编辑',
          icon: 'table-2',
          roles: ['ADMIN', 'PERSONAL', 'FIRM'],
        },
      },
      {
        path: 'things-linkage',
        name: 'ThingsLinkage',
        component: () => import('@/views/venues/things-linkage'),
        meta: {
          title: '联动列表',
          icon: 'table-2',
          roles: ['ADMIN', 'PERSONAL', 'FIRM'],
        },
      },
    ],
  },
  {
    path: '/data-view',
    component: Layout,
    redirect: '/data-view/rules-engine',
    alwaysShow: true,
    meta: {
      title: '数据大屏',
      icon: 'database-line',
      roles: ['ADMIN', 'PERSONAL', 'FIRM'],
    },
    children: [
      {
        path: 'rules-engine',
        name: 'rulesEngine',
        component: FlowDesigner,
        meta: {
          title: '规则引擎 流程图',
          icon: 'table-2',
          roles: ['ADMIN', 'PERSONAL', 'FIRM'],
        },
      },
    ],
  },
  {
    path: '/application',
    component: Layout,
    redirect: '/application/table',
    alwaysShow: true,
    meta: {
      title: '应用',
      icon: 'apps-line',
      roles: ['ADMIN', 'PERSONAL', 'FIRM'],
    },
    children: [
      {
        path: 'table',
        name: 'applicationTabe',
        component: () => import('@/views/application/table'),
        meta: {
          title: '应用列表',
          icon: 'table-2',
          roles: ['ADMIN', 'PERSONAL', 'FIRM'],
        },
      },
    ],
  },
  {
    path: '/tools',
    component: Layout,
    redirect: '/tools/mqtt',
    alwaysShow: true,
    meta: {
      title: '工具',
      icon: 'tools-fill',
      roles: ['ADMIN', 'PERSONAL', 'FIRM'],
    },
    children: [
      {
        path: 'mqtt',
        name: 'mqttTool',
        component: () => import('@/views/tools/mqtt'),
        meta: {
          title: 'MQTT测试工具',
          icon: 'table-2',
          roles: ['ADMIN', 'PERSONAL', 'FIRM'],
        },
      },
    ],
  },
  {
    path: '/error',
    name: 'Error',
    component: Layout,
    redirect: '/error/403',
    meta: {
      title: '错误页',
      icon: 'error-warning-line',
    },
    children: [
      {
        path: '403',
        name: 'Error403',
        component: () => import('@/views/403'),
        meta: {
          title: '403',
          icon: 'error-warning-line',
        },
      },
      {
        path: '404',
        name: 'Error404',
        component: () => import('@/views/404'),
        meta: {
          title: '404',
          icon: 'error-warning-line',
        },
      },
    ],
  },
  {
    path: '/*',
    redirect: '/404',
    hidden: true,
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes,
})

export default router
