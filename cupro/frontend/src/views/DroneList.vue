<script setup>
/**
 * 无人机数据面板 — 列表主页
 *
 * 本文件是应用的核心页面，负责：
 * 1. 展示无人机统计卡片（总数、空闲、充电、维护）
 * 2. 提供搜索筛选功能（序列号、型号、状态）
 * 3. 展示无人机数据表格（含编辑/删除操作）
 * 4. 提供新增无人机入口
 *
 * ============================
 * Vue 3 Composition API 核心概念
 * ============================
 * <script setup> 是 Vue 3 的"语法糖"，相比传统 Options API 更简洁：
 *   - 不需要 return，顶层变量自动暴露给模板
 *   - 不需要 components 注册，import 后直接使用
 *   - 编译后性能更好（静态分析优化）
 *
 * ref()  vs reactive()：
 *   - ref(): 包装基本类型（字符串、数字），通过 .value 访问
 *   - reactive(): 包装对象类型，直接访问属性（不需要 .value）
 *   - 模板中使用时，ref 会自动解包（不需要 .value）
 *
 * computed(): 计算属性
 *   - 依赖其他响应式数据，依赖变化时自动重新计算
 *   - 有缓存：依赖不变时返回上次的结果，不会重复执行
 *
 * onMounted(): 生命周期钩子
 *   - 组件挂载到 DOM 后执行，适合发 API 请求获取初始数据
 *   - 等价于 Vue 2 的 mounted()
 *
 * watch() vs computed()：
 *   - computed: 同步计算并返回值（用于模板绑定）
 *   - watch: 监听变化并执行副作用（如保存到 localStorage、发请求）
 */
import { ref, reactive, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { listDrones, deleteDrone } from '@/api/drone'
import SearchBar from '@/components/SearchBar.vue'
import DroneTable from '@/components/DroneTable.vue'

// useRouter() 获取路由实例，用于编程式导航（跳转页面）
const router = useRouter()

// ============================
// 防止组件卸载后更新状态
// ============================
// 当用户快速切换页面时，异步请求可能在组件销毁后才返回
// 此时如果更新组件状态，Vue 会报警告。用 isMounted 标记防止此问题。
let isMounted = true
onBeforeUnmount(() => {
  isMounted = false // 组件即将销毁，标记为 false
})

// ============================
// 响应式数据
// ============================

// ref([])：存储从后端获取的无人机列表（原始数据，不变）
const drones = ref([])

// 加载状态：true 时表格显示加载动画
const loading = ref(false)

// 筛选后的数据列表（展示用）
// 初始化为空数组，fetchDrones 成功后填充
const filteredDrones = ref([])

// reactive({})：筛选条件对象
// 使用 reactive 而非 ref 是因为筛选条件是一个对象（包含多个字段）
// 修改 filters.serialNumber 会自动触发依赖它的 computed 和 watch
const filters = reactive({
  serialNumber: '', // 序列号筛选（模糊匹配）
  model: '',        // 型号筛选（模糊匹配）
  status: '',       // 状态筛选（精确匹配，空字符串 = 全部）
})

// ============================
// 统计卡片计算属性
// ============================
// computed 会根据 drones.value 的变化自动重新计算
// 模板中直接使用 {{ stats.total }} 即可
const stats = computed(() => {
  const total = drones.value.length
  // filter() 是数组方法，返回满足条件的元素组成的新数组
  const idle = drones.value.filter((d) => d.status === 'IDLE').length
  const charging = drones.value.filter((d) => d.status === 'CHARGING').length
  const maintenance = drones.value.filter((d) => d.status === 'IN_MAINTENANCE').length
  return { total, idle, charging, maintenance }
})

// ============================
// 筛选逻辑
// ============================

/**
 * 应用筛选条件
 * 将 filters 中的条件应用到 drones 原始列表上，结果存入 filteredDrones
 *
 * 筛选规则：
 * - serialNumber: 不区分大小写的模糊匹配（includes）
 * - model: 不区分大小写的模糊匹配
 * - status: 精确匹配，空字符串表示不过滤
 */
function applyFilters() {
  let result = drones.value
  const sn = filters.serialNumber.trim().toLowerCase()
  const model = filters.model.trim().toLowerCase()
  const status = filters.status

  if (sn) {
    // 序列号包含搜索关键字（忽略大小写）
    result = result.filter((d) => (d.serialNumber || '').toLowerCase().includes(sn))
  }
  if (model) {
    // 型号包含搜索关键字
    result = result.filter((d) => d.model.toLowerCase().includes(model))
  }
  if (status) {
    // 状态精确匹配
    result = result.filter((d) => d.status === status)
  }
  filteredDrones.value = result
}

/**
 * 重置筛选条件
 * 清空所有筛选输入并恢复显示全部数据
 */
function resetFilters() {
  filters.serialNumber = ''
  filters.model = ''
  filters.status = ''
  // 展开运算符 ... 创建数组的浅拷贝
  filteredDrones.value = [...drones.value]
}

// ============================
// API 操作
// ============================

/**
 * 从后端加载无人机列表
 * async/await 是 Promise 的语法糖，让异步代码看起来像同步代码
 * try/catch 捕获可能的异常（网络错误、后端业务异常等）
 */
async function fetchDrones() {
  loading.value = true // 显示加载动画
  try {
    // await 等待 listDrones() 返回的 Promise 完成
    const data = await listDrones()
    // 组件可能已经卸载（用户切换了页面），此时不应更新状态
    if (!isMounted) return
    drones.value = data
    applyFilters() // 加载后应用当前筛选条件
    ElMessage.success(`加载完成，共 ${data.length} 条`) // 右上角成功提示
  } catch (e) {
    if (!isMounted) return
    ElMessage.error('加载失败：' + e.message) // 右上角错误提示
  } finally {
    // finally 块无论成功或失败都会执行
    if (isMounted) loading.value = false
  }
}

/**
 * 跳转到新增无人机页面
 */
function openCreate() {
  router.push('/drone/new')
}

/**
 * 跳转到编辑无人机页面
 * @param {Object} drone - 无人机对象
 */
function openEdit(drone) {
  // 模板字符串 `...${变量}...` 是 ES6 语法
  router.push(`/drone/${drone.id}/edit`)
}

/**
 * 删除无人机并刷新列表
 * @param {number} id - 要删除的无人机 ID
 */
async function handleDelete(id) {
  try {
    await deleteDrone(id)
    ElMessage.success('删除成功')
    // 删除后重新加载列表（后端会自动重排 ID）
    await fetchDrones()
  } catch (e) {
    ElMessage.error('删除失败：' + e.message)
  }
}

// ============================
// 生命周期
// ============================
// onMounted：组件挂载完成时执行（DOM 已渲染）
// 在这里发起初始数据请求
onMounted(() => {
  fetchDrones()
})
</script>

<!--
  ============================
  模板 (Template)
  ============================
  Vue 模板使用声明式语法描述界面：
  - {{ }} 插值表达式，显示响应式数据
  - v-model 双向绑定（输入框 ↔ 数据）
  - @click 事件监听器
  - :prop 动态属性绑定（冒号是 v-bind: 的简写）

  Element Plus 组件前缀说明：
  - el-card: 卡片容器（带边框和阴影的面板）
  - el-button: 按钮
  - el-input: 输入框
  - el-table: 数据表格
-->
<template>
  <!-- 页面标题卡片 -->
  <el-card class="header-card">
    <h1 style="text-align: center;">无人机数据面板</h1>
  </el-card>

  <!--
    统计卡片行
    CSS Grid 布局：4 列等宽，gap: 16px 间距
    每个卡片显示：标签文字 + 数字（来自 computed stats）
  -->
  <div class="stats-row">
    <el-card class="stat-card stat-total">
      <div class="stat-label">无人机总数</div>
      <div class="stat-value">{{ stats.total }}</div>
    </el-card>
    <el-card class="stat-card stat-idle">
      <div class="stat-label">空闲</div>
      <div class="stat-value">{{ stats.idle }}</div>
    </el-card>
    <el-card class="stat-card stat-charging">
      <div class="stat-label">充电中</div>
      <div class="stat-value">{{ stats.charging }}</div>
    </el-card>
    <el-card class="stat-card stat-maintenance">
      <div class="stat-label">维护中</div>
      <div class="stat-value">{{ stats.maintenance }}</div>
    </el-card>
  </div>

  <!-- 搜索筛选栏 -->
  <el-card class="section-card">
    <!--
      SearchBar 组件：
      :filters="filters"          → 将 filters 对象传给子组件（单向数据流）
      @update:filters="..."       → 子组件通过 emit 通知父组件更新 filters
      @search / @reset / @create  → 子组件发出的事件，父组件用对应方法处理
    -->
    <SearchBar
      :filters="filters"
      @update:filters="Object.assign(filters, $event)"
      @search="applyFilters"
      @reset="resetFilters"
      @create="openCreate"
    />
  </el-card>

  <!-- 数据表格 -->
  <el-card class="section-card">
    <!--
      DroneTable 组件：
      :data="filteredDrones"   → 传入筛选后的数据
      :loading="loading"       → 控制表格加载动画
      @edit / @delete          → 表格中的编辑/删除按钮事件
    -->
    <DroneTable
      :data="filteredDrones"
      :loading="loading"
      @edit="openEdit"
      @delete="handleDelete"
    />
  </el-card>
</template>

<style scoped>
/*
 * scoped 属性表示以下 CSS 只作用于当前组件，不会影响其他组件。
 * Vue 通过给元素添加 data-v-xxxxx 属性实现样式隔离。
 */

/* 标题卡片：底部留白 16px */
.header-card { margin-bottom: 16px; }
.header-card h1 { font-size: 20px; font-weight: 600; }

/* 通用区块卡片间距 */
.section-card { margin-bottom: 16px; }

/*
 * CSS Grid 布局：统计卡片 4 列
 * repeat(4, 1fr) = 4 列等分容器宽度
 * gap: 16px = 列间距和行间距都是 16px
 */
.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}

/* 统计卡片通用样式 */
.stat-card {
  text-align: center;
  padding: 8px 0;
  border-left: 4px solid; /* 左侧彩色边框作为视觉标识 */
}

/* 各统计卡片左侧边框颜色 */
.stat-total { border-left-color: #409eff; }       /* 蓝色 — 总数 */
.stat-idle { border-left-color: #67c23a; }        /* 绿色 — 空闲 */
.stat-charging { border-left-color: #e6a23c; }    /* 橙色 — 充电 */
.stat-maintenance { border-left-color: #f56c6c; } /* 红色 — 维护 */

/* 统计标签：灰色小字 */
.stat-label { font-size: 14px; color: #909399; margin-bottom: 8px; }

/* 统计数值：大号加粗 */
.stat-value { font-size: 28px; font-weight: 700; color: #303133; }
</style>
