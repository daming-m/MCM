/**
 * Vue Router 路由配置 — 前端页面导航的核心
 *
 * 本文件定义了整个前端应用的 URL 与页面组件的映射关系。
 *
 * ============================
 * 两种路由模式的区别
 * ============================
 * createWebHashHistory()：URL 带 # 号
 *   例：http://localhost:5173/#/drone/3/edit
 *   优点：不需要后端配置，兼容性好（本项目使用此模式）
 *   原理：# 后面的部分不会发送给服务器，由浏览器端 JS 处理
 *
 * createWebHistory()：URL 不带 # 号（需要后端配合）
 *   例：http://localhost:5173/drone/3/edit
 *   优点：URL 更美观
 *   缺点：需要后端配置 fallback（所有路径返回 index.html）
 *
 * ============================
 * 路由配置说明
 * ============================
 * path: URL 路径
 *   - '/': 根路径，显示无人机列表
 *   - '/drone/new': 新增无人机页面
 *   - '/drone/:id/edit': 编辑无人机页面（:id 是动态参数，如 /drone/3/edit）
 *
 * component: 使用懒加载（() => import(...)）
 *   懒加载的好处：访问哪个页面才加载哪个页面的 JS，减少首屏加载时间
 *   打包后每个 import() 会生成独立的 chunk 文件
 *
 * props: true 表示将路由参数（如 :id）作为 props 传递给组件
 *   这样 DroneEdit.vue 可以直接用 props.id 获取无人机 ID
 */
import { createRouter, createWebHashHistory } from 'vue-router'

// 路由表：定义 URL 路径到页面组件的映射
const routes = [
  {
    // 首页：无人机列表
    path: '/',
    name: 'DroneList', // 路由名称，可用于编程式导航（router.push({name: 'DroneList'})）
    // 懒加载：只在首次访问时加载 DroneList.vue 及其依赖
    component: () => import('@/views/DroneList.vue'),
  },
  {
    // 新增无人机页面
    path: '/drone/new',
    name: 'DroneCreate',
    component: () => import('@/views/DroneEdit.vue'),
    // 注意：新增和编辑共用一个 DroneEdit.vue 组件
    // 组件内部通过判断 route.params.id 是否存在来区分新增/编辑模式
  },
  {
    // 编辑无人机页面
    // :id 是动态路由参数，匹配任意内容（如 /drone/1/edit、/drone/99/edit）
    path: '/drone/:id/edit',
    name: 'DroneEdit',
    component: () => import('@/views/DroneEdit.vue'),
    // props: true → 将 :id 作为组件的 prop 传入，组件中通过 defineProps 接收
    props: true,
  },
]

// 创建路由实例
const router = createRouter({
  // 使用 Hash 模式（URL 带 #），无需后端额外配置
  history: createWebHashHistory(),
  routes, // ES6 简写，等价于 routes: routes
})

export default router
