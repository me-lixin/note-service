# 在线笔记系统（Node-Note）文档

## 一、项目概览

### 1. 项目名称
- 在线笔记系统（Node-Note）

### 2. 项目目标
- 提供用户笔记的创建、编辑、分类、搜索、分享功能
- 支持 Markdown、Word 文本导入
- 提供基于全文检索的快速查询功能
- 可生成分享链接，允许有限期内公开访问

### 3. 技术栈
- **后端**：Java 17 / Spring Boot 2.6 / MyBatis-Plus / MySQL
- **前端**：Vue 3 + Markdown 编辑器
- **安全**：JWT 鉴权
- **全文搜索**：MySQL FULLTEXT 索引

---

## 二、功能说明

### 1. 用户管理
- 登录 / 登出（JWT 方式）
- 用户信息获取

### 2. 笔记管理
- CRUD（创建、读取、更新、删除）
- 分类管理
- 笔记分享(包含下载)
- 导入文件（Markdown、Word、文本文件）
- 全文检索（支持 title 和 content）

### 3. 分享功能
- 生成分享链接（可设置过期时间）
- 访问分享链接时自动统计访问量
- 分享内容只读，不允许修改

### 4. 前端操作
- 编辑器支持 Markdown 格式
- 页面列表支持分页、搜索
- 文件导入可直接转笔记内容

---

## 三、接口文档

### 1. 用户登录
```
POST /api/auth/login
Request:
{
"username": "test",
"password": "123456"
}

Response:
{
"code": 200,
"msg": "success",
"data": "JWT_TOKEN"
}
```


### 2. 创建笔记
```
POST /api/note
Request:
{
"title": "笔记标题",
"content": "笔记内容",
"categoryId": 1
}

Response:
{
"code": 200,
"msg": "success",
"data": 1001 // 笔记ID
}
```


### 3. 分页查询笔记
```
GET /api/note/list
Params:

current: 当前页

size: 每页条数

categoryId: 分类ID（可选）

Response:
{
"code": 200,
"msg": "success",
"data": {
"records": [...],
"total": 125,
"current": 1,
"size": 10,
"pages": 13
}
}
```


### 4. 搜索笔记（全文检索）
```
GET /api/note/search
Params:

keyword: 搜索关键字

current: 当前页

size: 每页条数

Response:
{
"code": 200,
"msg": "success",
"data": {
"records": [...],
"total": 50,
"current": 1,
"size": 10,
"pages": 5
}
}
```

### 5. 分享笔记
```
POST /api/note/share
Request:
{
"noteId": 1001,
"expireTime": "2026-01-10T00:00:00"
}

Response:
{
"code": 200,
"msg": "success",
"data": "shareCode_abc123"
}
```

### 6. 访问分享笔记
```
GET /api/note/share/{shareCode}

Response:
{
"code": 200,
"msg": "success",
"data": {
"id": 1001,
"title": "笔记标题",
"content": "笔记内容",
"viewCount": 3
}
}
```

---

## 四、数据结构

### note 表
| 字段          | 类型           | 说明   | 备注                     |
| ----------- | ------------ | ---- | ---------------------- |
| id          | bigint       | 笔记ID | 主键，自增                  |
| user_id     | bigint       | 所属用户 | 建立索引 `idx_user_id`     |
| category_id | bigint       | 所属分类 | 建立索引 `idx_category_id` |
| title       | varchar(255) | 标题   | 支持 FULLTEXT 检索         |
| content     | text         | 内容   | 支持 FULLTEXT 检索         |
| create_time | datetime     | 创建时间 | 默认当前时间                 |
| update_time | datetime     | 更新时间 | 默认当前时间，更新时自动修改         |

### note_share 表
| 字段               | 类型           | 说明     | 备注                |
| ---------------- | ------------ | ------ | ----------------- |
| id               | bigint       | 分享ID   | 主键，自增             |
| note_id          | bigint       | 对应笔记ID | -                 |
| share_code       | varchar(64)  | 分享码    | 唯一索引 `share_code` |
| title_snapshot   | varchar(255) | 笔记标题快照 | 可为空               |
| content_snapshot | longtext     | 笔记内容快照 | 不为空               |
| expire_time      | datetime     | 分享过期时间 | 可为空               |
| view_count       | int          | 浏览次数   | 默认 0              |
| create_time      | datetime     | 创建时间   | -                 |

### note_category 表（笔记分类表）
| 字段          | 类型          | 说明                | 备注                                     |
| ----------- | ----------- | ----------------- | -------------------------------------- |
| id          | bigint      | 分类ID              | 主键，自增                                  |
| user_id     | bigint      | 所属用户ID            | -                                      |
| parent_id   | bigint      | 父级ID，0表示一级分类      | 建立索引 `idx_parent`                      |
| level       | tinyint     | 层级：1-一级，2-二级，3-三级 | 建立索引 `idx_level`                       |
| name        | varchar(64) | 分类名称              | parent_id + name 唯一约束 `uk_parent_name` |
| create_time | datetime    | 创建时间              | 默认当前时间                                 |
| update_time | datetime    | 更新时间              | 默认当前时间，更新时自动修改                         |

### user 表（用户表）
| 字段          | 类型           | 说明       | 备注              |
| ----------- | ------------ | -------- | --------------- |
| id          | bigint       | 用户ID     | 主键，自增           |
| nickname    | varchar(64)  | 昵称       | 可为空             |
| username    | varchar(64)  | 用户名      | 唯一索引 `username` |
| password    | varchar(128) | 密码（加密存储） | 不为空             |
| create_time | datetime     | 创建时间     | 默认当前时间          |


---

## 五、前端页面说明

1. 首页笔记列表：分页 + 搜索
2. 编辑页面：Markdown 编辑器，支持导入文件
3. 分享页面：生成链接，显示剩余有效时间
4. 文件导入：支持 `.md` / `.txt` / `.docx`

---

## 六、运维和注意事项

- **JWT Token** 有效期控制
- **MySQL FULLTEXT** 索引需维护
- 分享链接过期后自动不可访问
- 文件导入注意文件大小限制

---

## 七、下一步可扩展功能

1. 搜索高亮和权重排序
2. Elasticsearch 替代 MySQL FULLTEXT
3. 笔记版本管理（快照）
4. 在线协作（多人编辑同一笔记）
5. 标签系统、收藏夹、提醒功能  