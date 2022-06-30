module.exports = {
  title: '一二一物联', // 设置网站标题
  dest: 'docSynopsis',
  port: 9000,
  head: [ // 注入到当前页面的 HTML <head> 中的标签
    ['link', {rel: 'icon', href: '/logo.png'}], // 增加一个自定义的 favicon(网页标签的图标)
    ['link', {rel: 'apple-touch-icon', href: '/logo.jpg'}],
    // PWA单页
    ['link', {rel: 'manifest', href: '/manifest.webmanifest'}],
    ['meta', {name: 'theme-color', content: '#3eaf7c'}],
  ],
  description: '描述：我的技术站',
  // base: '/', //默认路径
  themeConfig: { // 主题设置
    logo: '/logo.png',  // 左上角logo
    navbar: [
      { // 右上导航航条 docs/.vuepress/README.md
        text: '概述',
        link: '/'
      },
      {
        text: '参考',
        children: [
          {text: '项目介绍', link: '/guide/introduction.md'}, // 可不写后缀 .md
        ]
      },
      {
        text: '笔记',
        children: [
          {text: '笔记', link: '/note/创建项目.md'},// 以 ‘/’结束，默认读取 README.md
          {text: '其它链接', link: 'https://blog.csdn.net/maker_knz?spm=1019.2139.3001.5343'} // 外部链接
        ]
      },
      {
        text: '控制台', link: 'http://tot.makerknz.cn',
      }
    ],
    sidebar: { // 左侧列表
      '/note/': [
        {
          text: '一二一物联笔记',
          children: [
            '/note/创建项目.md',
            '/note/EMQ X介绍.md',
            '/note/EMQ X MySQL 8认证.md',
            '/note/主题权限.md',
            '/note/数据库设计.md',
            '/note/注册、登录和鉴权.md',
            '/note/连接EMQX.md',
            '/note/连接pulsar.md'
          ]
        }
      ],
      // fallback 侧边栏被最后定义
      '/': [''], //不能放在数组第一个，否则会导致右侧栏无法使用
    }
  },
  plugins: [
    [
      '@vuepress/pwa',
      {
        skipWaiting: true,
      },
    ],
    [
      '@vuepress/plugin-search',
      {
        locales: {
          '/': {
            placeholder: 'Search',
          },
          '/zh/': {
            placeholder: '搜索',
          },
        },
      },
    ],
  ],
}
