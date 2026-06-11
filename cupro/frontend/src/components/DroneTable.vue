<script setup>
/**
 * 无人机数据表格组件
 *
 * 本组件负责渲染无人机数据表格，包括：
 * 1. 展示无人机列表（ID、序列号、型号、电量、最大飞行时长、状态）
 * 2. 电量以进度条可视化展示
 * 3. 状态以彩色标签展示
 * 4. 每行提供编辑和删除操作按钮
 *
 * ============================
 * Vue 3 组件通信
 * ============================
 * defineProps(): 声明父组件传入的属性（只读）
 *   - data: 要展示的无人机数组
 *   - loading: 是否显示加载动画
 *
 * defineEmits(): 声明组件可以向外触发的事件
 *   - edit: 点击编辑按钮 → 父组件处理页面跳转
 *   - delete: 点击删除按钮（确认后） → 父组件调用 API 删除
 */

// defineProps 接收父组件传入的数据
// 在模板中可以直接使用 data 和 loading
defineProps({
  // data 是必填的数组类型
  data: { type: Array, required: true },
  // loading 有默认值 false（非必填）
  loading: { type: Boolean, default: false },
})

// defineEmits 声明事件，返回 emit 函数
const emit = defineEmits(['edit', 'delete'])

// ============================
// 工具函数
// ============================

/**
 * 根据状态返回 Element Plus Tag 组件的颜色类型
 *
 * Element Plus Tag type 可选值：
 * - success: 绿色
 * - warning: 橙色
 * - danger: 红色
 * - info: 灰色
 *
 * @param {string} status - 无人机状态（IDLE/CHARGING/IN_MAINTENANCE）
 * @returns {string} Element Plus Tag 的 type 值
 */
function statusType(status) {
  switch (status) {
    case 'IDLE': return 'success'           // 空闲 → 绿色标签
    case 'CHARGING': return 'warning'       // 充电 → 橙色标签
    case 'IN_MAINTENANCE': return 'danger'  // 维护 → 红色标签
    default: return 'info'                  // 未知状态 → 灰色标签
  }
}

/**
 * 状态值 → 中文显示映射表
 * 用于在表格中显示中文状态名
 */
const statusLabelMap = {
  IDLE: '空闲',
  CHARGING: '充电中',
  IN_MAINTENANCE: '维护中',
}

/**
 * 根据电量百分比返回进度条颜色
 * - ≥80% → 绿色（电量充足）
 * - 30-79% → 橙色（电量中等）
 * - <30% → 红色（电量不足）
 *
 * @param {number} percent - 电量百分比
 * @returns {string} CSS 颜色值
 */
function batteryColor(percent) {
  if (percent >= 80) return '#67c23a' // 绿色
  if (percent >= 30) return '#e6a23c' // 橙色
  return '#f56c6c' // 红色
}

/**
 * 根据电量百分比返回进度条背景色
 * 与 batteryColor 搭配使用，保持视觉一致性
 */
function batteryStroke(percent) {
  if (percent >= 80) return '#e1f3d8' // 浅绿
  if (percent >= 30) return '#faecd8' // 浅橙
  return '#fde2e2' // 浅红
}
</script>

<template>
  <!--
    el-table：Element Plus 表格组件
    :data="data"          → 绑定数据源
    v-loading="loading"   → 加载中显示遮罩动画
    stripe                → 斑马纹（隔行变色）
    empty-text            → 数据为空时显示的提示文字
  -->
  <el-table :data="data" v-loading="loading" stripe empty-text="暂无数据" style="width: 100%">
    <!-- ID 列：宽度 80px，内容居中 -->
    <el-table-column prop="id" label="ID" width="80" align="center" />

    <!--
      序列号列：
      min-width="160"         → 最小宽度 160px（列宽自适应时不会小于此值）
      show-overflow-tooltip   → 内容过长时显示省略号并悬浮显示完整内容
    -->
    <el-table-column prop="serialNumber" label="序列号" min-width="160" show-overflow-tooltip />

    <!-- 型号列 -->
    <el-table-column prop="model" label="型号" min-width="140" show-overflow-tooltip />

    <!--
      电量列：使用自定义模板（非 prop 直接渲染）
      #default="{ row }" 是 Vue 3 的插槽语法，{ row } 解构获取当前行数据
      el-progress：Element Plus 进度条组件
        :percentage  → 进度百分比
        :color       → 进度条颜色（动态计算）
        :stroke-width → 进度条高度
    -->
    <el-table-column label="电量" width="180" align="center">
      <template #default="{ row }">
        <div class="battery-cell">
          <el-progress
            :percentage="row.batteryPercent"
            :color="batteryColor(row.batteryPercent)"
            :stroke-width="14"
            :bgcolor="batteryStroke(row.batteryPercent)"
          />
        </div>
      </template>
    </el-table-column>

    <!--
      最大飞行时长列：自定义模板添加"分钟"单位
    -->
    <el-table-column prop="maxFlightMinutes" label="最大飞行时长" width="130" align="center">
      <template #default="{ row }">
        {{ row.maxFlightMinutes }} 分钟
      </template>
    </el-table-column>

    <!--
      状态列：使用 el-tag 彩色标签 + el-tooltip 悬浮提示
      el-tooltip：鼠标悬浮时显示原始英文状态值
      el-tag：根据状态类型动态切换颜色
    -->
    <el-table-column label="状态" width="120" align="center">
      <template #default="{ row }">
        <!-- placement="top" → 提示框出现在元素上方 -->
        <el-tooltip :content="row.status" placement="top">
          <!-- :type 动态绑定颜色类型 -->
          <el-tag :type="statusType(row.status)" size="default">
            <!-- 优先显示中文映射，没有映射则显示原始值 -->
            {{ statusLabelMap[row.status] || row.status }}
          </el-tag>
        </el-tooltip>
      </template>
    </el-table-column>

    <!--
      操作列：编辑 + 删除按钮
      fixed="right" → 列固定在右侧（水平滚动时保持可见）
    -->
    <el-table-column label="操作" width="160" align="center" fixed="right">
      <template #default="{ row }">
        <!-- link 属性：按钮显示为链接样式（无边框） -->
        <el-button type="primary" size="small" link @click="emit('edit', row)">编辑</el-button>

        <!--
          el-popconfirm：气泡确认框
          点击删除按钮 → 弹出确认气泡 → 用户点击"确认"才真正触发删除
          防止误操作删除数据
        -->
        <el-popconfirm
          title="确认删除该记录吗？"
          confirm-button-text="确认"
          cancel-button-text="取消"
          @confirm="emit('delete', row.id)"  <!-- 确认后触发 delete 事件 -->
        >
          <!--
            #reference 插槽：触发气泡的元素
            这里是一个"删除"链接按钮
          -->
          <template #reference>
            <el-button type="danger" size="small" link>删除</el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
</template>

<style scoped>
/* 电量单元格上下留白 */
.battery-cell { padding: 4px 0; }
</style>
