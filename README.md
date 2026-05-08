GoldenGYM 健身打卡系统

项目概述

面向个人用户的运动打卡与目标管理 Web 应用，前后端分离架构。支持运动记录、统计分析、目标设定与日历可视化。

当前为单用户演示模式（默认 userId = 1），后续可接入 JWT 登录体系。

技术栈

  层   	技术                                      
  前端  	Vue 3 (Composition API) + Element Plus + Vite + Vue Router 4 + Pinia + Axios
  后端  	Java 1.8 + Spring Boot 2.7 + MyBatis-Plus 3.5 + MySQL 8.x
  构建  	Maven (后端) / npm (前端)                   

目录结构

    goldenGYM-mimo/
    ├── sql/init.sql                  # 数据库初始化（建表 + 默认用户 + 索引）
    ├── backend/                      # Spring Boot 后端
    │   ├── pom.xml
    │   └── src/main/java/com/goldengym/
    │       ├── GoldenGymApplication.java
    │       ├── common/Result.java           # 统一响应体 {code, message, data}
    │       ├── config/
    │       │   ├── CorsConfig.java          # 全局 CORS
    │       │   ├── MyBatisPlusConfig.java   # 分页插件
    │       │   └── GlobalExceptionHandler.java
    │       ├── entity/               # User, ExerciseRecord, ExerciseGoal
    │       ├── dto/                  # ExerciseRecordDTO, GoalDTO (javax.validation)
    │       ├── vo/                   # StatsVO, ExerciseRecordVO, GoalVO
    │       ├── mapper/               # BaseMapper + 自定义聚合 SQL
    │       ├── service/              # 接口
    │       ├── service/impl/         # 业务实现
    │       └── controller/           # RESTful API
    └── frontend/                     # Vue 3 前端
        ├── package.json
        ├── vite.config.js            # 开发代理 /api → localhost:8080
        └── src/
            ├── main.js
            ├── App.vue
            ├── router/index.js       # 5 个路由
            ├── stores/exercise.js    # Pinia store
            ├── api/                  # Axios 封装 + 接口函数
            ├── components/           # Navbar, StatCard, ExerciseCard, GoalCard
            └── views/                # Home, QuickCheck, ExerciseHistory, MyGoals, Calendar

启动方式

    # 1. 数据库
    mysql -u root -p < sql/init.sql
    
    # 2. 后端
    cd backend
    mvn spring-boot:run
    
    # 3. 前端
    cd frontend
    npm install
    npm run dev

前端: http://localhost:3000  |  后端: http://localhost:8080

API 接口

  方法    	路径                                   	说明           
  POST  	/api/exercise/record                 	新增打卡记录       
  GET   	/api/exercise/records                	分页查询记录（可筛选类型）
  DELETE	/api/exercise/record/{id}            	删除记录         
  GET   	/api/exercise/stats/week             	本周统计         
  GET   	/api/exercise/stats/month            	本月统计         
  GET   	/api/exercise/stats/total            	全部统计         
  GET   	/api/exercise/calendar/{year}/{month}	日历有记录日期列表    
  GET   	/api/exercise/records/{date}         	某天详细记录       
  POST  	/api/goal/create                     	创建目标         
  GET   	/api/goal/list                       	目标列表         
  PUT   	/api/goal/{id}/complete              	切换完成状态       
  DELETE	/api/goal/{id}                       	删除目标         

页面路由

  路径          	页面             	功能                       
  /           	Home           	首页（本周/本月统计 + 活跃目标 + 最近运动）
  /quick-check	QuickCheck     	快速打卡（选类型 → 填表单）          
  /history    	ExerciseHistory	运动历史（统计 + 筛选 + 分页 + 删除）  
  /goals      	MyGoals        	我的目标（创建/完成/删除目标）         
  /calendar   	Calendar       	运动日历（月视图打点 + 日期详情）       

视觉规范

  元素  	规范               
  主色  	橙色 #F4672A       
  辅助色 	绿色 #67C23A       
  背景  	浅灰 #F5F5F5 / 白色卡片
  圆角  	卡片 12px，按钮 6px   
  导航  	白色固定顶部，激活项橙色填充   

数据库表

- user — 用户表（id, username, password, email, create_time）
- exercise_record — 运动记录（id, user_id, type, date, duration, calories, avg_heart_rate, note）
- exercise_goal — 目标表（id, user_id, period, target_days, target_duration, target_calories, completed）

代码规范

- 后端四层架构：Controller → Service 接口 → ServiceImpl → Mapper
- 统一响应体 Result<T>，code=200 成功，其他为失败
- 前端 Axios 拦截器统一处理错误，ElMessage 提示
- Vue 3 Composition API（<script setup>）
