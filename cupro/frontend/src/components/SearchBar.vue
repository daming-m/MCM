<script setup>
/**
 * 搜索筛选栏组件
 *
 * 提供无人机列表的搜索筛选 + 新增入口，包含：
 * 1. 序列号输入框（模糊搜索）
 * 2. 型号输入框（模糊搜索）
 * 3. 状态下拉框（精确筛选）
 * 4. 搜索按钮（触发筛选）
 * 5. 重置按钮（清空条件）
 * 6. 新增无人机按钮（跳转到新增页面）
 *
 * ============================
 * 组件通信模式：v-model 的替代方案
 * ============================
 * 这里使用 :filters + @update:filters 实现"双向绑定"效果：
 * - 父组件通过 :filters="filters" 传入筛选对象
 * - 子组件通过 emit('update:filters', newValue) 通知父组件更新
 * - 父组件中 Object.assign(filters, $event) 合并更新
 *
 * 为什么不直接用 v-model？
 * 因为 filters 是一个对象，需要多个输入框分别修改不同字段。
 * v-model 更适合单个值的场景。
 */

// 接收父组件传入的筛选条件对象
const props = defineProps({
  filters: { type: Object, required: true },
})

// 声明可以向外触发的事件
const emit = defineEmits(['update:filters', 'search', 'reset', 'create'])

/**
 * 单个字段变更处理
 * 使用展开运算符 {...props.filters, [field]: value} 创建新对象
 * 这保证了每次更新都是新对象，Vue 能正确检测到变化
 *
 * @param {string} field - 变更的字段名
 * @param {string} value - 新的值
 */
function onFieldChange(field, value) {
  // 展开原对象 + 覆盖变更字段 = 新对象
  emit('update:filters', { ...props.filters, [field]: value })
}

// 状态选项列表（下拉框使用）
// value 为空字符串表示"全部"（不筛选）
const statusOptions = [
  { label: '全部', value: '' },
  { label: 'IDLE', value: 'IDLE' },
  { label: 'CHARGING', value: 'CHARGING' },
  { label: 'IN_MAINTENANCE', value: 'IN_MAINTENANCE' },
]
</script>

<template>
  <!-- flex 布局：所有子元素水平排列，gap: 12px 元素间距 -->
  <div class="search-bar">
    <!--
      序列号输入框
      :model-value="props.filters.serialNumber" → 单向绑定父组件传入的值
      @input → 用户输入时通知父组件更新（通过 onFieldChange 包装 emit）
      @keyup.enter → 按回车键直接触发搜索
      clearable → 显示一键清空按钮
    -->
    <el-input
      :model-value="props.filters.serialNumber"
      placeholder="序列号"
      clearable
      style="width: 180px"
      @input="onFieldChange('serialNumber', $event)"
      @clear="onFieldChange('serialNumber', '')"
      @keyup.enter="emit('search')"
    />

    <!-- 型号输入框（逻辑同序列号） -->
    <el-input
      :model-value="props.filters.model"
      placeholder="型号"
      clearable
      style="width: 180px"
      @input="onFieldChange('model', $event)"
      @clear="onFieldChange('model', '')"
      @keyup.enter="emit('search')"
    />

    <!--
      状态下拉框
      @change 事件：选中值变化时触发
      清空时 value 为 undefined 或 ''，统一处理为空字符串
    -->
    <el-select
      :model-value="props.filters.status"
      placeholder="状态"
      clearable
      style="width: 160px"
      @change="onFieldChange('status', $event || '')"
    >
      <!--
        v-for 循环渲染下拉选项
        :key 是 Vue 的列表渲染 key（必须唯一，帮助 Vue 高效更新 DOM）
      -->
      <el-option
        v-for="opt in statusOptions"
        :key="opt.value"
        :label="opt.label"
        :value="opt.value"
      />
    </el-select>

    <!-- 搜索按钮：type="primary" → Element Plus 主题色（蓝色） -->
    <el-button type="primary" @click="emit('search')">搜索</el-button>

    <!-- 重置按钮：清空所有筛选条件 -->
    <el-button @click="emit('reset')">重置</el-button>

    <!--
      新增按钮：
      type="success" → 绿色按钮，视觉上与其他按钮区分
      style="margin-left: auto" → 推到最右边（flex 布局中 auto margin 会占据剩余空间）
    -->
    <el-button type="success" @click="emit('create')" style="margin-left: auto">新增无人机</el-button>
  </div>
</template>

<style scoped>
/*
 * 搜索栏布局：Flexbox
 * display: flex → 子元素水平排列
 * gap: 12px → 元素间距
 * align-items: center → 垂直居中
 * flex-wrap: wrap → 内容过多时自动换行（响应式）
 */
.search-bar {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}
</style>
