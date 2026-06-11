/**
 * 无人机信息管理系统 — 前端应用入口
 *
 * 本文件是 Vue 3 应用的启动入口，相当于整个前端的 "main 函数"。
 *
 * ============================
 * 启动流程
 * ============================
 * 1. import 引入 Vue、插件、路由、根组件
 * 2. createApp(App) 创建 Vue 应用实例
 * 3. app.use(ElementPlus) 全局注册 Element Plus UI 组件库
 * 4. app.use(router) 全局注册 Vue Router 路由
 * 5. app.mount('#app') 将应用挂载到 index.html 的 <div id="app"> 上
 *
 * ============================
 * 依赖关系（组件树）
 * ============================
 * main.js → App.vue → router-view
 *                      ├── DroneList.vue → SearchBar + DroneTable
 *                      └── DroneEdit.vue
 *
 * ============================
 * Vue 3 与 Vue 2 的关键区别
 * ============================
 * - createApp() 替代 new Vue()
 * - app.use() 链式调用替代 Vue.use() 全局注册
 * - 每个插件/库的作用域限制在当前 app 实例（多应用隔离）
 */

// 从 'vue' 包导入 createApp 函数（Vue 3 的工厂函数）
import { createApp } from 'vue'

// Element Plus：基于 Vue 3 的企业级 UI 组件库
// 提供了 el-button、el-table、el-input 等常用组件
import ElementPlus from 'element-plus'
// Element Plus 的全局 CSS 样式（必须引入，否则组件无样式）
import 'element-plus/dist/index.css'

// Vue Router：前端路由库，管理页面跳转
import router from './router'

// 根组件
import App from './App.vue'

// 全局自定义样式（body 背景色、字体等）
import './style.css'

// ============================
// 创建并配置 Vue 应用
// ============================

// createApp(App)：以 App.vue 为根组件创建应用实例
const app = createApp(App)

// app.use() 安装插件：
// 1. ElementPlus → 注册所有 Element Plus 组件为全局组件
//    之后在任何 .vue 文件中都可以直接使用 <el-button>、<el-table> 等
app.use(ElementPlus)

// 2. router → 注入路由功能
//    之后可以使用 <router-view>、useRouter()、useRoute() 等
app.use(router)

// ============================
// 挂载应用
// ============================

// mount('#app') 将 Vue 应用挂载到 DOM 容器
// '#app' 对应 index.html 中的 <div id="app"></div>
// 挂载后，Vue 会接管该容器的内容渲染
app.mount('#app')
