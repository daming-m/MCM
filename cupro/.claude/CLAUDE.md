# Java 工程规范

本规则在 Claude 对话中持续生效，约束工作区内所有 Spring Boot 子项目。

## 四层架构约束

- 合法依赖：`controller -> service`、`service -> domain`、`service -> repository`、`repository -> domain`
- 禁止跨层：controller 直接调 repository、repository 反调 service、domain 依赖上层
- 生成代码前必须检查包路径和依赖方向，不合规时拒绝生成并给出替代方案

## 包结构约定

- Java 类必须落在合法层级包：`controller | service | domain | repository | config | common | exception`
- 新建包时同步维护 `package-info.java`

## 编码规范

- 公共方法必须补齐 Javadoc（`@param`、`@return`、`@throws`）
- 禁止字段注入 `@Autowired`，统一构造器注入（可用 Lombok `@RequiredArgsConstructor`）
- 类名、方法名、常量名遵循标准命名约定

---

# 测试与质量规范

## 测试分层策略

- Service 层：`@ExtendWith(MockitoExtension.class)`，外部依赖必须 mock
- Controller 层：`@WebMvcTest` + `@MockBean`，校验状态码/响应体/参数校验
- Repository 层：JPA 使用 `@DataJpaTest`，关注自定义查询正确性
- 集成测试：`@SpringBootTest`，数量少于单元测试，独立于切片测试

## 命名与映射

- 测试方法统一：`should_[预期行为]_when_[条件]`
- 测试类与被测类保持相同包路径，位于 `src/test/java`
- 业务类变更时同步补齐测试类与测试用例

## 覆盖率门禁

- `harness-new`：行覆盖率 >= 80%
- `harness-legacy`：仅出报告，不设阈值
- 完成编码后提示执行：`mvn clean verify -Pharness-new`

---

# API 文档同步规范

## 触发规则

- 新增/修改公共 API 时，必须同步更新 `harness-collab/04-api-docs/` 与 `harness-collab/func.md`
- 删除 API 前先 `@Deprecated`，文档标注废弃说明与替代方案

## Controller 注解要求

- Controller 类必须有 `@Tag`
- `@RequestMapping`/`@GetMapping` 等方法必须有 `@Operation`

## 交付要求

- 每次代码交付时明确标记"文档同步：已同步/待同步"
- 若存在 API 变更但未更新文档，优先提醒并要求补齐

---

# AI 协作协议

## 代码生成前置检查

- 业务代码前：确认 `harness-collab/01-product-specs/` 存在需求文档
- 架构代码前：确认 `harness-collab/02-design-docs/` 存在设计文档
- 文档缺失时拒绝生成业务代码，并引导使用模板补齐

## Lifecycle Gate

- 不得跳过 Gate-1 到 Gate-6 检查点
- 发现缺失项时列出清单、给出修复步骤，并在后续会话持续跟进

## 同步生成与交付

- 生成业务类时必须同步生成对应测试类
- 交付后输出标准摘要：修改文件、测试状态、文档同步、下一步建议
- 对分层违规请求必须拒绝并提供合规替代方案

---

# Dev_Lifecycle 项目生命周期规范

## 六阶段流程

1. 需求分析
2. 技术设计
3. 编码实现
4. 测试验证
5. 文档同步
6. CI 发布

## 阶段要求

- 阶段 1：需求文档存在且已确认
- 阶段 2：设计文档存在且已确认
- 阶段 3：代码分层合规并同步测试
- 阶段 4：`mvn clean verify -Pharness-new` 通过，覆盖率达标
- 阶段 5：API 文档和 `func.md` 已同步
- 阶段 6：CI 门禁通过后再推进 PR 合并

## 违规处理

- 任一 Gate 未通过时，禁止进入下一阶段
- 输出缺失项与修复指引，问题解决前持续提醒
