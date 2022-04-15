# team

# **$\textcolor{Magenta}{技术选型} $**

前期：Servlet+HTML+JS+CSS+JDBC

中期：

1、Servlet+HTML+JS+CSS+Vue+JDBC

2、Springboot+HTML+JS+CSS+Vue

3、Servlet+Vue+JDBC(困难)

4、Springboot+Vue（困难）



# $\textcolor{Magenta}{工具版本}$

Ubuntu 20

Tomcat 8 —— Tomcat 8.5.78 (待定)

JDK 17 （待定）

Vue 2 —— Vue2.96



# $\textcolor{Magenta}{组内分工}$

前期：混合

中期：前端、后端



# $\textcolor{Magenta}{任务目标}$

## $\textcolor{RedOrange}{基本功能}$

### $\textcolor{Green}{注册}$

将用户提交的username、email、password存入数据库

### $\textcolor{Green}{登录}$

从数据库查询用户数据并判定是否登录成功

### $\textcolor{Green}{上传、下载、删除文件}$

允许用户上传、下载、删除自己网盘中的文件



## $\textcolor{RedOrange}{防守}$

### $\textcolor{Green}{防止SQL注入（（前）后端）}$



### $\textcolor{Green}{限制数据（前后端）}$

1、数据长度限制（过长/过短）

2、数据类型限制

### $\textcolor{Green}{限制权限（后端）}$

1、sessionID校验

2、session.name校验

###$\textcolor{Green}{限制访问（后端）}$

1、限制来源链接

2、限制浏览器



## $\textcolor{RedOrange}{进攻}$

### $\textcolor{Green}{SQL注入}$

### $\textcolor{Green}{DDos}$

### $\textcolor{Green}{写入非常规数据}$

1、过长过短数据

2、提交其他类型数据

### $\textcolor{Green}{跨越权限}$

1、使session.name与post请求不一致

2、直接访问地址

### $\textcolor{Green}{反反爬}$



# $\textcolor{Magenta}{项目结构}$

## $\textcolor{RedOrange}{前端}$

### $\textcolor{Green}{HTML}$

#### $\textcolor{SkyBlue}{index.html  菜单页（导航页）}$

1、可跳转用户登录页、用户注册页



#### $\textcolor{SkyBlue}{register.html (SignUpSuccess.html)  用户注册页}$

1、可跳转用户登录页、菜单页

2、可向register.class提交post请求表单，提交用户注册信息，注册成功后重定向到SignUpSuccess.html

3、自动去除输入信息中空格

4、用户体验优化

（1）页面焦点依次聚焦于账号输入框、密码输入框

（2）回车提交表单



#### $\textcolor{SkyBlue}{login.html  用户登录页}$

1、可跳转用户登录页、菜单页

2、可向login.class提交post请求表单，提交用户登录信息

3、自动去除输入信息中空格

4、用户体验优化

（1）页面焦点依次聚焦于账号输入框、密码输入框

（2）回车提交表单



#### $\textcolor{SkyBlue}{filepage.html  文件操作页}$

1、显示用户账号信息

2、以表格显示用户网盘文件名并允许勾选

3、提供文件下载按钮

4、提供文件上传按钮

5、提供文件删除按钮

6、提供登出按钮，可向logout.class发出请求

#### $\textcolor{SkyBlue}{404.html 403.html (error.html*n)  错误页}$

1、提供404、403错误返回页面，并允许返回菜单页

2、提供多种错误页面

（1）服务器内部错误

（2）注册失败

（3）用户名已存在

（4）账号错误

（5）密码错误

### $\textcolor{Green}{CSS}$

对应页面的CSS

### $\textcolor{Green}{JS}$

#### $\textcolor{SkyBlue}{register.js}$

前端验证用户输入账号密码长度是否符合要求

#### $\textcolor{SkyBlue}{login.js}$

前端验证用户输入账号密码长度是否符合要求

## $\textcolor{RedOrange}{后端}$

### $\textcolor{Green}{过滤器}$

#### $\textcolor{SkyBlue}{EncodingFilter.class}$

将除了json, js, css, ico, jpg, png, html之外的请求设置为UTF-8编码



### $\textcolor{Green}{数据库操作}$

#### $\textcolor{SkyBlue}{DB_Connect.class}$

1、填写数据库信息（可使用properties文件替代）

2、获取连接

#### $\textcolor{SkyBlue}{DB_Write.class}$

1、调用Username_Check.class查询账号是否已被注册

2、向数据库内写入用户注册信息并返回注册状态（注册成功、账号已存在、注册失败）

3、防止sql注入

####  $\textcolor{SkyBlue}{DB_Delete.class}$

1、调用Username_Check.class查询账号是否存在

2、向数据库发出删除请求并返回删除状态（删除成功、删除失败、账号不存在）

3、防止sql注入



### $\textcolor{Green}{注册}$

#### $\textcolor{SkyBlue}{Register.class}$

1、调用User_Agent_Check.class检查请求的user-agent字段，验证是否来自浏览器的请求（可被轻松绕过）

2、调用Referer_Check.class检查请求的referer字段，验证是否来自register.html的请求（可被轻松绕过）

3、后端验证用户输入账号、密码长度

4、调用DB_Write.class写入数据



### $\textcolor{Green}{登录}$

#### $\textcolor{SkyBlue}{Login.class}$

1、调用User_Agent_Check.class检查请求的user-agent字段，验证是否来自浏览器的请求（可被轻松绕过）

2、调用Referer_Check.class检查请求的referer字段，验证是否来自login.html的请求（可被轻松绕过）

3、查询数据库验证账号密码是否正确

4、防sql注入

5、验证是否已有sessionID，如果没有则创建，如果有则删除旧sessionID再创建

6、将用户名传入session

7、客户端重定向到Showfilepage.class



### $\textcolor{Green}{登出}$

#### $\textcolor{SkyBlue}{Logout.class}$

1、销毁session

2、重定向到index.html



### $\textcolor{Green}{文件展示}$

#### $\textcolor{SkyBlue}{Showfilepage.class}$

1、判断是否存在session

2、判断session是否为新

3、调用Referer_Check.class检查请求的referer字段，验证是否来自login.html的请求（可被轻松绕过）

4、创建对应用户的文件夹

5、将文件夹下所有文件名传入session

6、渲染filepage.html



### $\textcolor{Green}{文件上传}$

#### $\textcolor{SkyBlue}{FileUpload.class}$

1、判断是否存在session，session.name

2、调用Referer_Check.class检查请求的referer字段，验证是否来自filepage.html的请求（可被轻松绕过）

3、创建用户对应文件夹

4、判断是否有重名文件

5、将文件以所选名称保存（没有自动识别后缀）



### $\textcolor{Green}{文件下载}$

#### $\textcolor{SkyBlue}{FileDownload.class}$





### $\textcolor{Green}{文件删除}$

#### $\textcolor{SkyBlue}{FileDelete.class}$





### $\textcolor{Green}{文件查询(可选)}$

#### $\textcolor{SkyBlue}{FileSearch.class}$

