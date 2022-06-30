export const themeData = {
  "logo": "/logo.png",
  "navbar": [
    {
      "text": "概述",
      "link": "/"
    },
    {
      "text": "参考",
      "children": [
        {
          "text": "项目介绍",
          "link": "/guide/introduction.md"
        }
      ]
    },
    {
      "text": "笔记",
      "children": [
        {
          "text": "笔记",
          "link": "/note/创建项目.md"
        },
        {
          "text": "其它链接",
          "link": "https://blog.csdn.net/maker_knz?spm=1019.2139.3001.5343"
        }
      ]
    },
    {
      "text": "控制台",
      "link": "http://tot.makerknz.cn"
    }
  ],
  "sidebar": {
    "/note/": [
      {
        "text": "一二一物联笔记",
        "children": [
          "/note/创建项目.md",
          "/note/EMQ X介绍.md",
          "/note/EMQ X MySQL 8认证.md",
          "/note/主题权限.md",
          "/note/数据库设计.md",
          "/note/注册、登录和鉴权.md",
          "/note/连接EMQX.md",
          "/note/连接pulsar.md"
        ]
      }
    ],
    "/": [
      ""
    ]
  },
  "locales": {
    "/": {
      "selectLanguageName": "English"
    }
  },
  "darkMode": true,
  "repo": null,
  "selectLanguageText": "Languages",
  "selectLanguageAriaLabel": "Select language",
  "sidebarDepth": 2,
  "editLink": true,
  "editLinkText": "Edit this page",
  "lastUpdated": true,
  "lastUpdatedText": "Last Updated",
  "contributors": true,
  "contributorsText": "Contributors",
  "notFound": [
    "There's nothing here.",
    "How did we get here?",
    "That's a Four-Oh-Four.",
    "Looks like we've got some broken links."
  ],
  "backToHome": "Take me home",
  "openInNewWindow": "open in new window",
  "toggleDarkMode": "toggle dark mode",
  "toggleSidebar": "toggle sidebar"
}

if (import.meta.webpackHot) {
  import.meta.webpackHot.accept()
  if (__VUE_HMR_RUNTIME__.updateThemeData) {
    __VUE_HMR_RUNTIME__.updateThemeData(themeData)
  }
}

if (import.meta.hot) {
  import.meta.hot.accept(({ themeData }) => {
    __VUE_HMR_RUNTIME__.updateThemeData(themeData)
  })
}
