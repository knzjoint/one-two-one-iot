# 智能家居工农业数据采集开篇

从零搭建智能家居/工农业数据收集教程，该教程涉及EMQ X、Python Flask、Vue、Flutter、Spring Cloud以及ESP32的应用。

EMQ X 是消息服务器，是设备直接连接的服务器，用来流转设备上传的数据以及对设备进行控制的平台。

Python Flask 用为服务端管理平台的后台服务

Vue 服务端管理平台的后台管理

Flutter 作为客户端

Spring Cloud 用户

ESP32 设备

![image-20211002104451821](http://note.makerknz.cn/image-20211002104451821.png)

项目结构

![image-20211002142725383](http://note.makerknz.cn/image-20211002142725383.png)

device 包含设备的代码

device-client 是用户的使用端

device-server 是设备拥有者开发的设备服务

product-manager-backend 产品管理的后台页面

product-manager-server 产品管理的后台服务