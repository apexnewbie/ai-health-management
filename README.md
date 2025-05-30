
---

### API文档使用

项目启动后访问 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) 即可查看 API 文档。

### 已完成的功能

- **基础架构与依赖配置**  
  - 已设计并搭建了项目整体架构，明确了各业务模块和文件层级。
  - 配置了 Spring Boot 核心依赖（Web、Redis、MyBatis、DevTools、JWT、统一响应与异常处理等），并使用 Java 17。

- **用户管理与认证**  
  - 实现了用户注册和登录功能，包括密码加密、用户数据持久化。
  - 生成并返回 JWT，实现了无状态的认证机制。
  - 配置了 Spring Security，通过 JWT 认证过滤器来检测用户登录状态。
  - 建立了统一的响应格式（ServiceResponse）和全局异常处理（ServiceException、GlobalExceptionHandler），确保前后端返回数据格式统一。

- **运动与饮食前端静态页面初步搭建**
  - ![img.png](img.png)
  - 使用react，搭建包含问题建议，聊天框
---

### 待完成的功能

- **扩展健康管理业务模块**  
  - **心理健康模块（聊天接口）**：集成 LLM 接口，提供基于 AI 的聊天和心理支持功能。
  - **饮食推荐模块**：结合食品营养数据库与 AI 模型，提供个性化的饮食建议。
  - **运动计划模块**：记录用户运动数据，生成和调整健身计划。
  - **健康数据分析与可视化**：汇总用户各方面健康数据，生成报告和个性化建议。
  - **提醒通知模块**：实现定时消息推送和提醒服务。

- **Web3/NFT 集成**  
  - 开发与区块链交互的模块，生成用户虚拟形象与 NFT，管理用户数字资产。

- **输入校验与接口文档**  
  - 引入 Bean Validation 完善输入数据校验，提升数据安全性与完整性。
  - 配置 Swagger 或 SpringDoc OpenAPI，实现前端接口文档的自动化生成。

- **测试与自动化**  
  - 完善单元测试与集成测试，确保各模块稳定可靠。
  - 配置持续集成（CI）工具，自动化验证代码质量和功能正确性。

---

---
AI 健康管理系统 的饮食和运动推荐模块
- 技术路线
  - 调用API
  - Prompt工程
  - 数据存储，用到客户自己的饮食习惯
  - 数据分析

---

