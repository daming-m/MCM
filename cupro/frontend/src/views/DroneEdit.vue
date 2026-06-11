<script setup>
/**
 * 无人机表单页面 — 新增 / 编辑共用
 *
 * 本组件通过路由参数区分两种模式：
 * - 新增模式：URL = /drone/new，route.params.id 为 undefined
 * - 编辑模式：URL = /drone/:id/edit，route.params.id 有值
 *
 * ============================
 * 组件设计要点
 * ============================
 * 1. 新增和编辑共用同一个表单组件，减少重复代码
 * 2. 所有字段均为可选：未填写的字段后端会自动生成默认值
 * 3. 编辑模式下先通过 getDrone(id) 加载现有数据回填表单
 * 4. Element Plus 表单校验：batteryPercent(0-100)、maxFlightMinutes(1-300)
 */

import { reactive, ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createDrone, updateDrone, getDrone } from '@/api/drone'

// ============================
// 路由初始化
// ============================
// useRouter() → 编程式导航（跳转页面）
// useRoute()  → 获取当前路由信息（路径、参数、查询字符串等）
const router = useRouter()
const route = useRoute()

// 判断当前是编辑还是新增模式
// computed 确保 route.params.id 变化时自动更新
// !! 是双重否定运算符，将值转为布尔类型：
//   route.params.id 有值 → true（编辑模式）
//   route.params.id 为 undefined → false（新增模式）
const isEdit = computed(() => !!route.params.id)

// 根据模式显示不同的标题
const title = computed(() => (isEdit.value ? '编辑无人机' : '新增无人机'))

// ============================
// 表单数据与校验
// ============================

// 表单组件引用（用于触发表单校验）
const formRef = ref(null)

// 加载状态（编辑模式下加载已有数据时显示 loading）
const loading = ref(false)

// reactive 创建响应式表单对象
// 使用 reactive 而非多个 ref 是因为表单字段是一个整体
// 注意：batteryPercent 和 maxFlightMinutes 初始为 undefined（不是 0）
//       undefined 表示"未填写"，后端会自动生成默认值
//       0 表示"用户明确填了 0"
const form = reactive({
  serialNumber: '',
  model: '',
  batteryPercent: undefined,  // undefined ≠ 0，表示用户未填
  maxFlightMinutes: undefined,
  status: '',
})

/**
 * Element Plus 表单校验规则
 *
 * 每个字段可以定义多条规则，按顺序执行：
 * - type: 校验的数据类型
 * - min/max: 数值范围
 * - message: 校验失败时的提示文字
 * - trigger: 触发校验的事件（blur = 失去焦点时校验）
 *
 * 注意：serialNumber、model、status 没有强制必填，
 * 因为后端会自动生成默认值（fillByAiStrategy）
 */
const rules = {
  batteryPercent: [
    { type: 'number', min: 0, max: 100, message: '电量范围 0-100', trigger: 'blur' },
  ],
  maxFlightMinutes: [
    { type: 'number', min: 1, max: 300, message: '飞行时长范围 1-300', trigger: 'blur' },
  ],
}

// ============================
// 生命周期
// ============================

onMounted(async () => {
  // 编辑模式：从后端加载现有数据回填表单
  if (isEdit.value) {
    loading.value = true
    try {
      // 通过 ID 查询无人机详情
      const drone = await getDrone(Number(route.params.id))
      // 将查询结果逐一赋值到表单（不能直接 form = drone，那样会断开响应式）
      form.serialNumber = drone.serialNumber
      form.model = drone.model
      form.batteryPercent = drone.batteryPercent
      form.maxFlightMinutes = drone.maxFlightMinutes
      form.status = drone.status
    } catch (e) {
      ElMessage.error('加载失败：' + e.message)
      // 加载失败时跳回列表页
      router.push('/')
    } finally {
      loading.value = false
    }
  }
})

// ============================
// 表单提交
// ============================

/**
 * 提交表单：新增或更新无人机
 *
 * 流程：
 * 1. 触发表单校验 → 不通过则停止
 * 2. 组装请求参数（空字符串转 undefined，让后端自动填充）
 * 3. 根据模式调用 createDrone 或 updateDrone
 * 4. 成功后跳回列表页
 */
async function submit() {
  // formRef.value.validate() 返回 Promise
  // 校验通过 → resolve
  // 校验失败 → reject（这里用 .catch(() => false) 捕获，让 valid = false）
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return // 校验失败，停止提交

  // 组装 payload：空字符串转为 undefined
  // 后端 fillByAiStrategy 会对 null/空字符串的字段自动生成默认值
  // || 运算符：空字符串 '' 是 falsy，会被转为 undefined
  const payload = {
    serialNumber: form.serialNumber || undefined,
    model: form.model || undefined,
    batteryPercent: form.batteryPercent,   // 数字不需要转换
    maxFlightMinutes: form.maxFlightMinutes,
    status: form.status || undefined,
  }

  try {
    if (isEdit.value) {
      // 编辑模式：调用 PUT /api/drones/{id}
      await updateDrone(Number(route.params.id), payload)
      ElMessage.success('更新成功')
    } else {
      // 新增模式：调用 POST /api/drones
      await createDrone(payload)
      ElMessage.success('创建成功')
    }
    // 成功后返回列表页
    router.push('/')
  } catch (e) {
    ElMessage.error((isEdit.value ? '更新' : '创建') + '失败：' + e.message)
  }
}

/**
 * 返回列表页
 */
function goBack() {
  router.push('/')
}
</script>

<template>
  <!--
    表单页面容器
    max-width: 640px + margin: 24px auto → 居中显示，最大宽度 640px
  -->
  <el-card class="form-page">
    <!--
      卡片头部：#header 是 el-card 的具名插槽
      包含返回按钮和页面标题
    -->
    <template #header>
      <el-button :icon="'ArrowLeft'" @click="goBack">返回</el-button>
      <span style="margin-left: 12px; font-weight: 600">{{ title }}</span>
    </template>

    <!--
      Element Plus 表单
      ref="formRef"     → 用于在 JS 中调用 validate() 校验
      :model="form"     → 绑定表单数据对象
      :rules="rules"    → 绑定校验规则
      label-width       → 标签栏宽度
      v-loading="loading" → 加载中遮罩（编辑模式下初始加载时显示）
    -->
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="110px"
      style="max-width: 500px"
      v-loading="loading"
    >
      <!--
        序列号输入框
        v-model="form.serialNumber" → 双向绑定：输入框内容 ↔ form.serialNumber
        maxlength="64"              → 对应后端 @Size(max=64) 校验
        placeholder                 → 占位提示文字
      -->
      <el-form-item label="序列号">
        <el-input v-model="form.serialNumber" maxlength="64" placeholder="留空将自动生成" />
      </el-form-item>

      <!-- 型号输入框 -->
      <el-form-item label="型号">
        <el-input v-model="form.model" maxlength="64" placeholder="留空将自动生成" />
      </el-form-item>

      <!--
        电量数字输入框
        prop="batteryPercent" → 关联 rules.batteryPercent 校验规则
        :min="0" :max="100"   → 限制输入范围
      -->
      <el-form-item label="电量" prop="batteryPercent">
        <el-input-number
          v-model="form.batteryPercent" :min="0" :max="100"
          placeholder="留空将自动生成" style="width: 100%"
        />
      </el-form-item>

      <!-- 最大飞行时长数字输入框 -->
      <el-form-item label="最大飞行时长" prop="maxFlightMinutes">
        <el-input-number
          v-model="form.maxFlightMinutes" :min="1" :max="300"
          placeholder="留空将自动生成" style="width: 100%"
        />
      </el-form-item>

      <!--
        状态下拉选择框
        clearable → 显示清除按钮（可清空选择）
        el-option 循环生成下拉选项
      -->
      <el-form-item label="状态">
        <el-select v-model="form.status" placeholder="留空将自动生成" clearable style="width: 100%">
          <el-option label="IDLE" value="IDLE" />
          <el-option label="CHARGING" value="CHARGING" />
          <el-option label="IN_MAINTENANCE" value="IN_MAINTENANCE" />
        </el-select>
      </el-form-item>

      <!-- 提示信息：告知用户留空字段会自动填充 -->
      <el-form-item>
        <el-alert type="info" :closable="false" show-icon title="未填写的字段系统将自动生成默认值" />
      </el-form-item>

      <!-- 操作按钮 -->
      <el-form-item>
        <el-button @click="goBack">取消</el-button>
        <!-- type="primary" → Element Plus 主题色按钮（蓝色） -->
        <el-button type="primary" @click="submit">确认</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<style scoped>
/* 表单页面居中，限制最大宽度 */
.form-page { max-width: 640px; margin: 24px auto; }
</style>
