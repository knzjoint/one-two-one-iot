export const data = {
  "key": "v-8daa1a0e",
  "path": "/",
  "title": "",
  "lang": "en-US",
  "frontmatter": {
    "home": true,
    "heroImage": "/spider.png",
    "heroText": "一二一物联",
    "tagline": "物联设备管理",
    "actions": [
      {
        "text": "快速上手",
        "link": "/guide/introduction",
        "type": "primary"
      },
      {
        "text": "项目介绍",
        "link": "/guide/introduction",
        "type": "secondary"
      }
    ],
    "features": [
      {
        "title": "约定优于配置",
        "details": "约定常用传感器数据模型，规范硬件设备接口和协议，实现自动生成数据模型。"
      },
      {
        "title": "分层设计",
        "details": "产品端用于管理产品和设备，应用端处理使用者和设备交互，设备端通过规范硬件接口自动生成数据模型。"
      },
      {
        "title": "高性能",
        "details": "开发采用了Webflux响应式Web应用框架，大提高了消息的处理性能。"
      }
    ],
    "footer": "Copyright © 2019-present 联系方式：1461706290@qq.com"
  },
  "excerpt": "",
  "headers": [],
  "git": {},
  "filePathRelative": "README.md"
}

if (import.meta.webpackHot) {
  import.meta.webpackHot.accept()
  if (__VUE_HMR_RUNTIME__.updatePageData) {
    __VUE_HMR_RUNTIME__.updatePageData(data)
  }
}

if (import.meta.hot) {
  import.meta.hot.accept(({ data }) => {
    __VUE_HMR_RUNTIME__.updatePageData(data)
  })
}
