import { Vuepress } from '@vuepress/client/lib/components/Vuepress'

const routeItems = [
  ["v-8daa1a0e","/",{"title":""},["/index.html","/README.md"]],
  ["v-1c7b2593","/guide/introduction.html",{"title":"智能家居工农业数据采集开篇"},["/guide/introduction","/guide/introduction.md"]],
  ["v-68b8c234","/note/EMQ%20X%20MySQL%208%E8%AE%A4%E8%AF%81.html",{"title":"EMQ X MySQL 8认证"},["/note/EMQ X MySQL 8认证.html","/note/EMQ%20X%20MySQL%208%E8%AE%A4%E8%AF%81","/note/EMQ X MySQL 8认证.md","/note/EMQ%20X%20MySQL%208%E8%AE%A4%E8%AF%81.md"]],
  ["v-2056d71a","/note/EMQ%20X%E4%BB%8B%E7%BB%8D.html",{"title":"EMQ X介绍及安装"},["/note/EMQ X介绍.html","/note/EMQ%20X%E4%BB%8B%E7%BB%8D","/note/EMQ X介绍.md","/note/EMQ%20X%E4%BB%8B%E7%BB%8D.md"]],
  ["v-70ac7aa2","/note/%E4%B8%BB%E9%A2%98%E6%9D%83%E9%99%90.html",{"title":"EMQ X MySQL 8.0主题发布/订阅鉴权"},["/note/主题权限.html","/note/%E4%B8%BB%E9%A2%98%E6%9D%83%E9%99%90","/note/主题权限.md","/note/%E4%B8%BB%E9%A2%98%E6%9D%83%E9%99%90.md"]],
  ["v-2d1ef36d","/note/%E5%88%9B%E5%BB%BA%E9%A1%B9%E7%9B%AE.html",{"title":"初始化项目"},["/note/创建项目.html","/note/%E5%88%9B%E5%BB%BA%E9%A1%B9%E7%9B%AE","/note/创建项目.md","/note/%E5%88%9B%E5%BB%BA%E9%A1%B9%E7%9B%AE.md"]],
  ["v-760aadda","/note/%E5%A2%9E%E5%8A%A0swager.html",{"title":""},["/note/增加swager.html","/note/%E5%A2%9E%E5%8A%A0swager","/note/增加swager.md","/note/%E5%A2%9E%E5%8A%A0swager.md"]],
  ["v-45140334","/note/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E8%AE%A1.html",{"title":"产品管理服务--数据库设计"},["/note/数据库设计.html","/note/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E8%AE%A1","/note/数据库设计.md","/note/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E8%AE%A1.md"]],
  ["v-1af4daf8","/note/%E6%B3%A8%E5%86%8C%E3%80%81%E7%99%BB%E5%BD%95%E5%92%8C%E9%89%B4%E6%9D%83.html",{"title":"注册、登录和鉴权"},["/note/注册、登录和鉴权.html","/note/%E6%B3%A8%E5%86%8C%E3%80%81%E7%99%BB%E5%BD%95%E5%92%8C%E9%89%B4%E6%9D%83","/note/注册、登录和鉴权.md","/note/%E6%B3%A8%E5%86%8C%E3%80%81%E7%99%BB%E5%BD%95%E5%92%8C%E9%89%B4%E6%9D%83.md"]],
  ["v-c3de352e","/note/%E8%BF%9E%E6%8E%A5EMQX.html",{"title":"连接EMQ X"},["/note/连接EMQX.html","/note/%E8%BF%9E%E6%8E%A5EMQX","/note/连接EMQX.md","/note/%E8%BF%9E%E6%8E%A5EMQX.md"]],
  ["v-8b4be08a","/note/%E8%BF%9E%E6%8E%A5pulsar.html",{"title":"连接Pulsar"},["/note/连接pulsar.html","/note/%E8%BF%9E%E6%8E%A5pulsar","/note/连接pulsar.md","/note/%E8%BF%9E%E6%8E%A5pulsar.md"]],
  ["v-3706649a","/404.html",{"title":""},["/404"]],
]

export const pagesRoutes = routeItems.reduce(
  (result, [name, path, meta, redirects]) => {
    result.push(
      {
        name,
        path,
        component: Vuepress,
        meta,
      },
      ...redirects.map((item) => ({
        path: item,
        redirect: path,
      }))
    )
    return result
  },
  [
    {
      name: "404",
      path: "/:catchAll(.*)",
      component: Vuepress,
    }
  ]
)
