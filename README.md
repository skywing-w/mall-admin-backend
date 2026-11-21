# 电商后台管理系统 · 后端服务

面向电商运营场景的后台管理 API，覆盖操作员与权限、商品与分类、订单、营销（轮播图 / 优惠券）、会员、评价、数据统计等模块。

## 技术栈

| 层 | 选型 |
|---|---|
| 语言 | Java 17 |
| 框架 | Spring Boot 3.5.0、Spring MVC、Spring AOP |
| 持久层 | MyBatis-Plus 3.5（分页插件 + 条件构造器），XML 手写复杂 SQL |
| 数据库 | MySQL 8 |
| 鉴权 | JWT（HS256，密钥与有效期走配置） |
| 工具 | Hutool、Lombok |
| 构建 | Maven 多模块（mall-common / mall-entity / mall-admin） |

## 功能模块

| 模块 | 接口前缀 | 说明 |
|---|---|---|
| 登录 | `/login` | 账号密码登录，签发 JWT |
| 操作员 | `/users` | 分页、详情、增删改、当前登录用户信息 |
| 角色 / 部门 | `/roles`、`/depts` | RBAC 基础数据维护 |
| 商品 | `/products` | 分页检索、增删改、上下架 |
| 商品分类 | `/categories` | 树形分类维护 |
| 订单 | `/orders` | 条件分页、详情（含明细）、发货 / 取消 / 退款 |
| 购物车 | `/cart-items` | 会员购物车数据 |
| 会员 | `/cusers` | 会员信息与状态 |
| 收货地址 | `/addresses` | 会员地址 |
| 评价 | `/reviews` | 商品评价与审核 |
| 轮播图 | `/banners` | 首页轮播维护 |
| 优惠券 | `/coupons` | 优惠券定义 |
| 数据统计 | `/stats` | 销售与订单统计 |
| 操作日志 | `/operate-logs` | AOP 自动记录，可按条件查询 |
| 文件上传 | `/upload` | 图片与文档上传，返回可访问路径 |

## 目录结构

```
mall-admin
├── pom.xml                     # 父 POM（依赖版本统一管理）
├── mall-common                   # 通用模块：Result / PageResult / 异常 / JWT / 上传 / 拦截器
├── mall-entity                   # 实体与 DTO/VO
├── mall-admin                    # 主应用：Controller / Service / Mapper / 配置 / 切面
│   └── src/main/resources
│       ├── application.yml          # 公共配置
│       ├── application-dev.yml      # 开发环境：数据源、上传目录、JWT 配置
│       ├── logback.xml
│       └── mappers/*.xml            # MyBatis XML
├── db                          # 建表与种子数据脚本
│   ├── 01-schema.sql
│   └── 02-seed.sql
└── docs                        # 接口文档（OpenAPI 3.0 + Markdown）
```

## 快速开始

### 1. 初始化数据库

```bash
# 必须带 --default-character-set=utf8mb4，否则中文会因客户端默认编码被截断
# （报错示例：Data too long for column 'name'）
mysql -uroot -p --default-character-set=utf8mb4 -e "create database if not exists mall_admin default charset utf8mb4;"
mysql -uroot -p --default-character-set=utf8mb4 mall_admin < db/01-schema.sql
mysql -uroot -p --default-character-set=utf8mb4 mall_admin < db/02-seed.sql
```

种子账号：`admin / 123456`（另有 `zhangsan / 123456`）。

### 2. 修改配置

`mall-admin/src/main/resources/application-dev.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mall_admin?...
    username: root
    password: 你的密码

mall:
  upload-dir: ./uploads/                                   # 上传目录，相对路径便于迁移
  jwt-secret: 至少32字节的随机字符串                          # 生产环境请用环境变量注入
  jwt-expire-hours: 12
```

### 3. 启动

```bash
mvn clean package -DskipTests
mvn -pl mall-admin spring-boot:run
```

服务地址 `http://localhost:8080/api`（`context-path` 为 `/api`）。

## 接口约定

- 统一响应：`{ "code": 1, "msg": "success", "data": ... }`，`code=0` 为失败
- 鉴权：除 `/login`、`/error`、`/uploads/**` 外，所有接口需在请求头携带 `token: <JWT>`
- 未登录返回 HTTP 401，响应体同为上述结构
- 分页：`page`（默认 1）、`pageSize`（默认 10，上限 100），返回 `{ total, rows }`
- 上传文件访问地址：`/api/uploads/{yyyy}/{MM}/{dd}/{uuid}.{ext}`

完整接口说明见 `docs/完整的项目功能API接口.md` 与 `docs/mall-admin.openapi.json`（可导入 Apifox / Postman）。

## 说明

本项目为个人项目，用于演示 Spring Boot 分层架构、MyBatis-Plus 复杂查询、JWT 鉴权以及 AOP 操作日志的落地方式。
