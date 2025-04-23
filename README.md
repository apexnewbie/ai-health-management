# 健康管理系统前端部分

## 主要功能

1. **智能健身咨询**
   - chatbot连接
   - 运动数据展示
   - 添加假数据进行测试test-api，需要在ExerciseAIChatBox文件中消除注释行

2. **健康数据追踪**
    - 待完成
  

3. **饮食管理**
    - 待完成

4. **用户个性化**
   - 个人档案设置

5. **登录注册界面**
   - 已存在
   - 但需要优化


- **Markdown渲染问题**: 尝试不同版本，但是依赖问题没有解决

## 项目结构

```
health-management-system/
├── public/                # 静态资源
├── src/                   # 源代码
│   ├── assets/            # 图片、图标等资源
│   ├── components/        # 可复用组件
│   │   ├── ExerciseAIChatBox.tsx       # 健身AI助手组件
│   │   └── ExerciseAIChatBox.css       # 健身AI助手样式
│   ├── layouts/           # 布局组件
│   ├── pages/             # 页面组件
│   ├── services/          # API服务
│   │   └── api.ts         # API请求处理
│   ├── styles/            # 全局样式
│   ├── utils/             # 工具函数
│   ├── App.tsx            # 应用主组件
│   └── index.tsx          # 应用入口
├── package.json           # 依赖包配置
└── tsconfig.json          # TypeScript配置
```

### 需安装依赖

```bash
npm install
```

### 开发环境运行

```bash
npm start
```

### 构建生产环境

```bash
npm run build
``
