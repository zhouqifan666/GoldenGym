# GoldenGYM 待办事项

## 已完成

- [x] 项目初始化：搭建前后端分离项目结构
- [x] 数据库设计：user / exercise_record / exercise_goal 三张表
- [x] 后端四层架构：Controller → Service → ServiceImpl → Mapper
- [x] 统一响应体 Result<T> + 全局异常处理
- [x] CORS 跨域配置 + MyBatis-Plus 分页插件
- [x] 运动记录 CRUD + 统计查询（周/月/总）
- [x] 目标管理 CRUD + 完成状态切换
- [x] 日历打点查询（按月获取有记录的日期）
- [x] 前端 5 个页面：首页 / 快速打卡 / 运动历史 / 我的目标 / 运动日历
- [x] 前端组件：Navbar / StatCard / ExerciseCard / GoalCard
- [x] Axios 封装 + 统一错误提示
- [x] Code Review：修复 MyBatis 类型别名 / 异常信息泄露 / 未使用导入 / SQL 索引

## 待完成

### 功能增强

- [ ] 用户登录注册（JWT 鉴权，替代硬编码 userId=1）
- [ ] 运动类型自定义（字典表替代 ENUM 硬编码）
- [ ] 数据导出（CSV / Excel 格式导出运动记录）
- [ ] 趋势图表（ECharts 展示周/月运动趋势折线图）
- [ ] 日历性能优化：前端缓存已加载月份的打点数据
- [ ] 移动端适配：导航栏小屏汉堡菜单 + 响应式布局

### 代码质量

- [ ] 后端单元测试（Service 层）
- [ ] 前端单元测试（组件 / API）
- [ ] 前端 Loading 状态：全局请求加载遮罩
- [ ] Swagger / SpringDoc 接口文档自动生成
- [ ] application.yml 区分 dev / prod 环境配置
- [ ] 数据库密码加密（不使用明文 root/root）

### 部署

- [ ] Docker Compose 一键部署（MySQL + 后端 + Nginx 前端）
- [ ] 前端生产构建 + Nginx 反向代理配置
- [ ] CI/CD 流水线（GitHub Actions 或 Jenkins）
