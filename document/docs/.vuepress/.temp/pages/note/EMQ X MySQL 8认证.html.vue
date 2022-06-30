<template><h1 id="emq-x-mysql-8认证" tabindex="-1"><a class="header-anchor" href="#emq-x-mysql-8认证" aria-hidden="true">#</a> EMQ X MySQL 8认证</h1>
<p>[toc]</p>
<h2 id="emq-x-客户端登陆认证介绍" tabindex="-1"><a class="header-anchor" href="#emq-x-客户端登陆认证介绍" aria-hidden="true">#</a> <a href="">EMQ X 客户端登陆认证介绍</a></h2>
<p>身份认证是大多数应用的重要组成部分，MQTT 协议支持用户名密码认证，启用身份认证能有效阻止非法客户端的连接。</p>
<p>EMQ X 中的认证指的是当一个客户端连接到 EMQ X 的时候，通过服务器端的配置来控制客户端连接服务器的权限。</p>
<p>EMQ X 的认证支持包括两个层面：</p>
<ul>
<li>
<p>MQTT 协议本身在 CONNECT 报文中指定用户名和密码，EMQ X 以插件形式支持基于 Username、ClientID、HTTP、JWT、LDAP 及各类数据库如 MongoDB、MySQL、PostgreSQL、Redis 等多种形式的认证。</p>
</li>
<li>
<p>在传输层上，TLS 可以保证使用客户端证书的客户端到服务器的身份验证，并确保服务器向客户端验证服务器证书。也支持基于 PSK 的 TLS/DTLS 认证。</p>
<p><img src="https://oscimg.oschina.net/oscnet/up-5526c37b0f7c5f06ad02b3eb6368921d485.png" alt="img"></p>
</li>
</ul>
<h2 id="关于mysql认证" tabindex="-1"><a class="header-anchor" href="#关于mysql认证" aria-hidden="true">#</a> 关于MySQL认证</h2>
<p>MySQL 认证使用外部 MySQL 数据库作为认证数据源，可以存储大量数据，同时方便与外部设备管理系统集成。</p>
<h2 id="环境准备" tabindex="-1"><a class="header-anchor" href="#环境准备" aria-hidden="true">#</a> 环境准备</h2>
<h3 id="安装mysql" tabindex="-1"><a class="header-anchor" href="#安装mysql" aria-hidden="true">#</a> 安装MySQL</h3>
<p><a href="https://blog.csdn.net/maker_knz/article/details/120720454?spm=1001.2014.3001.5501" target="_blank" rel="noopener noreferrer">MySQL 8.0 Docker安装<ExternalLinkIcon/></a></p>
<h3 id="创建数据库" tabindex="-1"><a class="header-anchor" href="#创建数据库" aria-hidden="true">#</a> 创建数据库</h3>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>CREATE USER emqx@'%' IDENTIFIED  WITH mysql_native_password BY 'public';
-- 创建数据库
create DATABASE IF NOT EXISTS emqx DEFAULT charset utf8mb4;
-- 授权
GRANT create,insert,select,update,delete,drop ON emqx.* TO emqx@'%';
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br></div></div><h3 id="创建表" tabindex="-1"><a class="header-anchor" href="#创建表" aria-hidden="true">#</a> 创建表</h3>
<p>表名mqtt_user是默认配置。</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>CREATE TABLE `mqtt_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `client_id` varchar(64) DEFAULT NULL COMMENT 'client ID',
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `salt` varchar(35) DEFAULT NULL,
  `is_superuser` tinyint(1) DEFAULT 0,
  `created` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mqtt_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br></div></div><p><img src="http://note.makerknz.cn/image-20211012145447030.png" alt="image-20211012145447030"></p>
<h2 id="emq-x-mysql认证配置" tabindex="-1"><a class="header-anchor" href="#emq-x-mysql认证配置" aria-hidden="true">#</a> EMQ X MySQL认证配置</h2>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>$ docker ps | grep emqx
c46cb71330f1        emqx/emqx:4.3.6                        "/usr/bin/docker-ent…"   10 days ago         Up 4 hours          4369-4370/tcp, 5369/tcp, 0.0.0.0:1883->1883/tcp, 0.0.0.0:8081->8081/tcp, 0.0.0.0:8083-8084->8083-8084/tcp, 6369-6370/tcp, 0.0.0.0:8883->8883/tcp, 0.0.0.0:18083->18083/tcp, 11883/tcp   emqx
$ docker exec -it c46cb71330f1 bash
$ cd /opt/emqx/etc/plugins
$ vi emqx_auth_mysql.conf
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br></div></div><h3 id="mysql连接信息" tabindex="-1"><a class="header-anchor" href="#mysql连接信息" aria-hidden="true">#</a> MySQL连接信息</h3>
<p>配置 MySQL 服务相关的连接地址，用户名密码和数据库：</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>## 服务器地址
auth.mysql.server = 127.0.0.1:3306

## 连接池大小
auth.mysql.pool = 8
## 登录名
auth.mysql.username = emqx
## 登录密码
auth.mysql.password = public
## 数据库名
auth.mysql.database = emqx

auth.mysql.query_timeout = 5s

auth.mysql.password_hash = sha256,salt
auth.mysql.auth_query = select password,salt from mqtt_user where username = '%u' limit 1
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br><span class="line-number">12</span><br><span class="line-number">13</span><br><span class="line-number">14</span><br><span class="line-number">15</span><br><span class="line-number">16</span><br></div></div><h3 id="密码加盐规则与哈希方法" tabindex="-1"><a class="header-anchor" href="#密码加盐规则与哈希方法" aria-hidden="true">#</a> 密码加盐规则与哈希方法</h3>
<p>EMQ X 多数认证插件中可以启用哈希方法，数据源中仅保存密码密文，保证数据安全。</p>
<p>启用哈希方法时，用户可以为每个客户端都指定一个 salt（盐）并配置加盐规则，数据库中存储的密码是按照加盐规则与哈希方法处理后的密文。</p>
<p>以 MySQL 认证为例：</p>
<p>加盐规则与哈希方法配置：</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code># etc/plugins/emqx_auth_mysql.conf

## 不加盐，仅做哈希处理
auth.mysql.password_hash = sha256

## salt 前缀：使用 sha256 加密 salt + 密码 拼接的字符串
auth.mysql.password_hash = salt,sha256

## salt 后缀：使用 sha256 加密 密码 + salt 拼接的字符串
auth.mysql.password_hash = sha256,salt

## pbkdf2 with macfun iterations dklen
## macfun: md4, md5, ripemd160, sha, sha224, sha256, sha384, sha512
## auth.mysql.password_hash = pbkdf2,sha256,1000,20
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br><span class="line-number">5</span><br><span class="line-number">6</span><br><span class="line-number">7</span><br><span class="line-number">8</span><br><span class="line-number">9</span><br><span class="line-number">10</span><br><span class="line-number">11</span><br><span class="line-number">12</span><br><span class="line-number">13</span><br><span class="line-number">14</span><br></div></div><p>我这里配置的是</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>auth.mysql.password_hash = sha256,salt
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br></div></div><p>数据库中存储的密码是一个密文信息，是sha256(密码 + salt)得到的值 。</p>
<p>如果添加salt，在sql语句中必须要将slat返回</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>auth.mysql.auth_query = select password,salt from mqtt_user where username = '%u' limit 1
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br></div></div><h2 id="如何生成认证信息" tabindex="-1"><a class="header-anchor" href="#如何生成认证信息" aria-hidden="true">#</a> 如何生成认证信息</h2>
<p>为每个客户端分用户名、Client ID、密码以及 salt（盐）等信息
使用与 MySQL 认证相同加盐规则与哈希方法处理客户端信息得到密文
将客户端信息写入数据库，客户端的密码应当为密文信息</p>
<p>password: 123456   salt: makerKnz</p>
<p>sha256(密码 + salt)生成密文</p>
<p>工具(https://www.cmd5.com/hash.aspx)</p>
<p><img src="http://note.makerknz.cn/image-20211012165228640.png" alt="image-20211012165228640"></p>
<p>在数据库中插入</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>INSERT INTO `mqtt_user` ( `username`, `password`, `salt`)
VALUES
	('emqx', 'a053f6a9786c2eb1bf5b7278a6beab2c2409a41855cc2da68e784b853e5bbe3f', 'maker_Knz');

</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br></div></div><h2 id="启动mysql认证插件" tabindex="-1"><a class="header-anchor" href="#启动mysql认证插件" aria-hidden="true">#</a> 启动MySQL认证插件</h2>
<p>在emq的控制台开启插件</p>
<p><img src="http://note.makerknz.cn/image-20211012101300157.png" alt="image-20211012101300157"></p>
<h2 id="关闭匿名认证" tabindex="-1"><a class="header-anchor" href="#关闭匿名认证" aria-hidden="true">#</a> 关闭匿名认证</h2>
<p>EMQ X 默认配置中启用了匿名认证，任何客户端都能接入 EMQ X。没有启用认证插件或认证插件没有显式允许/拒绝（ignore）连接请求时，EMQ X 将根据匿名认证启用情况决定是否允许客户端连接。</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>$ vi /opt/emqx/etc/emqx.conf
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br></div></div><div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code># etc/emqx.conf

## Value: true | false
allow_anonymous = true
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br><span class="line-number">3</span><br><span class="line-number">4</span><br></div></div><h2 id="重启客户端" tabindex="-1"><a class="header-anchor" href="#重启客户端" aria-hidden="true">#</a> 重启客户端</h2>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>$ exit
$ docker restart c46cb71330f1
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br><span class="line-number">2</span><br></div></div><h2 id="测试" tabindex="-1"><a class="header-anchor" href="#测试" aria-hidden="true">#</a> 测试</h2>
<p><img src="https://api2.mubu.com/v3/document_image/17731294-be3e-4f82-ae8f-510ab7423d4e-5604983.jpg" alt="img"></p>
<h2 id="问题" tabindex="-1"><a class="header-anchor" href="#问题" aria-hidden="true">#</a> 问题</h2>
<p>启动mysql插件时出现的问题</p>
<div class="language-text ext-text line-numbers-mode"><pre v-pre class="language-text"><code>{emqx_auth_mysql,{bad_return,{{emqx_auth_mysql_app,start,[normal,[]]},{'EXIT',{{badmatch,{error,{shutdown,{failed_to_start_child,emqx_auth_mysql,{shutdown,{failed_to_start_child,worker_sup,{shutdown,{failed_to_start_child,{worker,1},{{badmatch,{error,econnrefused}},[{mysql_conn,connect_socket,1,[{file,"mysql_conn.erl"},{line,160}]},{mysql_conn,'-connect/1-fun-0-',2,[{file,"mysql_conn.erl"},{line,136}]}]}}}}}}}}},[{emqx_auth_mysql_app,start,2,[{file,"emqx_auth_mysql_app.erl"},{line,38}]},{application_master,start_it_old,4,[{file,"application_master.erl"},{line,277}]}]}}}}}
</code></pre><div class="line-numbers" aria-hidden="true"><span class="line-number">1</span><br></div></div><p>原因是数据库配置错误</p>
</template>
