# team

*   [技术选型](#技术选型)

*   [工具版本](#工具版本)

*   [组内分工](#组内分工)

*   [任务目标](#任务目标)

    *   [基本功能](#基本功能)

        *   [注册](#注册)
        *   [登录](#登录)
        *   [上传、下载、删除文件](#上传下载删除文件)

    *   [防守](#防守)

        *   [防止SQL注入（（前）后端）](#防止sql注入前后端)
        *   [限制数据（前后端）](#限制数据前后端)
        *   [限制权限（后端）](#限制权限后端)
        *   [限制访问（后端）](#限制访问后端)

    *   [进攻](#进攻)

        *   [SQL注入](#sql注入)
        *   [DDos](#ddos)
        *   [写入非常规数据](#写入非常规数据)
        *   [跨越权限](#跨越权限)
        *   [反反爬](#反反爬)

*   [项目结构](#项目结构)

    *   [前端](#前端)

        *   [HTML](#html)

            *   [index.html  菜单页（导航页）](#indexhtml--菜单页导航页)
            *   [register.html (SignUpSuccess.html)  用户注册页 ](#registerhtml-signupsuccesshtml--用户注册页-)
            *   [login.html  用户登录页 ](#loginhtml--用户登录页-)
            *   [filepage.html  文件操作页 ](#filepagehtml--文件操作页-)
            *   [404.html 403.html (error.html\*n)  错误页 ](#404html-403html-errorhtmln--错误页-)

        *   [CSS](#css)

        *   [JS](#js)

            *   [register.js ](#registerjs-)
            *   [login.js](#loginjs)

    *   [后端](#后端)

        *   [过滤器](#过滤器)

            *   [EncodingFilter.class](#encodingfilterclass)

        *   [数据库操作](#数据库操作)

            *   [DB\_Connect.class](#db_connectclass)
            *   [DB\_Write.class](#db_writeclass)
            *   [DB\_Delete.class （可选）](#db_deleteclass-可选)

        *   [注册](#注册-1)

            *   [Register.class](#registerclass)

        *   [登录](#登录-1)

            *   [Login.class](#loginclass)

        *   [登出](#登出)

            *   [Logout.class](#logoutclass)

        *   [文件展示](#文件展示)

            *   [Showfilepage.class](#showfilepageclass)

        *   [文件上传](#文件上传)

            *   [FileUpload.class](#fileuploadclass)

        *   [文件下载](#文件下载)

            *   [FileDownload.class](#filedownloadclass)

        *   [文件删除](#文件删除)

            *   [FileDelete.class](#filedeleteclass)

        *   [文件查询(可选)](#文件查询可选)

            *   [FileSearch.class](#filesearchclass)

# 技术选型

前期：Servlet+HTML+JS+CSS+JDBC

中期：

1、Servlet+HTML+JS+CSS+Vue+JDBC

2、Springboot+HTML+JS+CSS+Vue

3、Servlet+Vue+JDBC(困难)

4、Springboot+Vue（困难）

# 工具版本

Ubuntu 20

Tomcat 8 —— Tomcat 8.5.78 (待定)

JDK 17 （待定）

Vue 2 —— Vue2.96

# 组内分工

前期：混合

中期：前端、后端

# 任务目标

## 基本功能

### 注册

将用户提交的username、email、password存入数据库

### 登录

从数据库查询用户数据并判定是否登录成功

### 上传、下载、删除文件

允许用户上传、下载、删除自己网盘中的文件

## 防守

### 防止SQL注入（（前）后端）

### 限制数据（前后端）

1、数据长度限制（过长/过短）

2、数据类型限制

### 限制权限（后端）

1、sessionID校验

2、session.name校验

### 限制访问（后端）

1、限制来源链接

2、限制浏览器

## 进攻

### SQL注入

### DDos

### 写入非常规数据

1、过长过短数据

2、提交其他类型数据

### 跨越权限

1、使session.name与post请求不一致

2、直接访问地址

### 反反爬

# 项目结构

## 前端

### HTML

#### index.html  菜单页（导航页）

1、可跳转用户登录页、用户注册页

#### register.html (SignUpSuccess.html)  用户注册页&#x20;

1、可跳转用户登录页、菜单页

2、可向register.class提交post请求表单，提交用户注册信息，注册成功后重定向到SignUpSuccess.html

3、自动去除输入信息中空格

4、用户体验优化

（1）页面焦点依次聚焦于账号输入框、密码输入框

（2）回车提交表单

#### login.html  用户登录页&#x20;

1、可跳转用户登录页、菜单页

2、可向login.class提交post请求表单，提交用户登录信息

3、自动去除输入信息中空格

4、用户体验优化

（1）页面焦点依次聚焦于账号输入框、密码输入框

（2）回车提交表单

#### filepage.html  文件操作页&#x20;

1、显示用户账号信息

2、以表格显示用户网盘文件名并允许勾选

3、提供文件下载按钮

4、提供文件上传按钮

5、提供文件删除按钮

6、提供登出按钮，可向logout.class发出请求

#### 404.html 403.html (error.html\*n)  错误页&#x20;

1、提供404、403错误返回页面，并允许返回菜单页

2、提供多种错误页面

（1）服务器内部错误

（2）注册失败

（3）用户名已存在

（4）账号错误

（5）密码错误

### CSS

对应页面的CSS

### JS

#### register.js&#x20;

前端验证用户输入账号密码长度是否符合要求

#### login.js

前端验证用户输入账号密码长度是否符合要求

## 后端

### 过滤器

#### EncodingFilter.class

将除了json, js, css, ico, jpg, png, html之外的请求设置为UTF-8编码

### 数据库操作

#### DB\_Connect.class

1、填写数据库信息（可使用properties文件替代）

2、获取连接

#### DB\_Write.class

1、调用Username\_Check.class查询账号是否已被注册

2、向数据库内写入用户注册信息并返回注册状态（注册成功、账号已存在、注册失败）

3、防止sql注入

#### DB\_Delete.class （可选）

1、调用Username\_Check.class查询账号是否存在

2、向数据库发出删除请求并返回删除状态（删除成功、删除失败、账号不存在）

3、防止sql注入

### 注册

#### Register.class

1、调用User\_Agent\_Check.class检查请求的user-agent字段，验证是否来自浏览器的请求（可被轻松绕过）

2、调用Referer\_Check.class检查请求的referer字段，验证是否来自register.html的请求（可被轻松绕过）

3、后端验证用户输入账号、密码长度

4、调用DB\_Write.class写入数据

### 登录

#### Login.class

1、调用User\_Agent\_Check.class检查请求的user-agent字段，验证是否来自浏览器的请求（可被轻松绕过）

2、调用Referer\_Check.class检查请求的referer字段，验证是否来自login.html的请求（可被轻松绕过）

3、查询数据库验证账号密码是否正确

4、防sql注入

5、验证是否已有sessionID，如果没有则创建，如果有则删除旧sessionID再创建

6、将用户名传入session

7、客户端重定向到Showfilepage.class

### 登出

#### Logout.class

1、销毁session

2、重定向到index.html

### 文件展示

#### Showfilepage.class

1、判断是否存在session

2、判断session是否为新

3、调用Referer\_Check.class检查请求的referer字段，验证是否来自login.html的请求（可被轻松绕过）

4、创建对应用户的文件夹

5、将文件夹下所有文件名传入session

6、渲染filepage.html

### 文件上传

#### FileUpload.class

1、判断是否存在session，session.name

2、调用Referer\_Check.class检查请求的referer字段，验证是否来自filepage.html的请求（可被轻松绕过）

3、创建用户对应文件夹

4、判断是否有重名文件

5、将文件以所选名称保存（没有自动识别后缀）

### 文件下载

#### FileDownload.class

### 文件删除

#### FileDelete.class

### 文件查询(可选)

#### FileSearch.class
