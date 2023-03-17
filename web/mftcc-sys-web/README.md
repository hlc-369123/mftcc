# 设置模块前端页面项目


-----



# vue_elementUI

## 介绍
新架构前端项目

## 软件架构
**vue+elementUI**


## 前端项目环境搭建

#### NodeJs开发环境安装

1.  nodejs官网下载并安装(http://nodejs.cn/download/)
2.  在命令行中输入 node -v 查看是否安装成功
3.  查看环境变量中是否存在nodejs,如没有请手动添加
4.  安装淘宝镜像 npm install -g cnpm --registry=https://registry.npm.taobao.org

#### 搭建vue-cli脚手架

1.  cnpm install --global vue-cli
2.  在命令行中输入 vue -V 查看是否安装成功

#### 安装webpack

1.  cnpm install webpack@3.12.0 -g

#### 项目准备

1.  访问 https://gitee.com/guo_hanchen/vue_elementUI 并 下载项目
2.  在项目根目录运行 cnpm install 安装依赖


#### 项目运行

    运行npm run dev 或 npm start 启动项目

#### 项目打包并在tomcat中运行

1.  修改 config/index.js 中的 productName 修改项目名称 （默认为 'pltweb' ）
2.  运行 npm run build 编译项目
3.  打包好的项目生成在 dist 目录中
4.	将 dist 中的打包后的项目 copy到 tomcat/webapp/ 中
5.	运行 tomcat 并访问 （例：127.0.0.1:8070/pltweb）










## 关于powershall 不识别vue命令的解决方案

1.  以管理员运行powershall
2.  输入：set-ExecutionPolicy RemoteSigned , 然后输入A






#### 码云特技

1.  使用 Readme\_XXX.md 来支持不同的语言,例如 Readme\_en.md, Readme\_zh.md
2.  码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目,是码云综合评定出的优秀开源项目
5.  码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
