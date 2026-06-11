<!--
  无人机信息管理系统 — Vue 3 根组件

  本文件是整个前端 SPA（单页面应用）的根组件。

  核心机制：<router-view />
  Vue Router 会根据当前浏览器 URL 动态渲染对应的页面组件：
  - 访问 /                → 渲染 DroneList.vue（无人机列表页）
  - 访问 /drone/new       → 渲染 DroneEdit.vue（新增无人机页）
  - 访问 /drone/:id/edit  → 渲染 DroneEdit.vue（编辑无人机页）

  这种模式称为"路由出口"——App.vue 只提供布局框架，实际内容由路由决定。
  这也是为什么 App.vue 非常简洁：所有页面逻辑分散在各自的视图组件中。

  渲染流程：
  浏览器 URL 变化 → router/index.js 匹配路由 → 加载对应 .vue 组件 → 替换 <router-view />
-->
<template>
  <!--
    router-view：Vue Router 的占位组件
    它本身不渲染任何可见内容，而是被当前路由匹配到的组件替换
    例如当前路由是 /，这里就会被替换为 DroneList.vue 的内容
  -->
  <router-view />
</template>
