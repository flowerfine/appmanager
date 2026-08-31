# 开发

## 开发文档

### 开发环境

* JDK。11
* 中间件。在 `tools/docker/local` 目录下执行 `docker compose up -d` 命令启动数据库
    * mysql。通过 `db-migration` 创建数据库。用户名密码查看 `docker-compose.yaml`
        * 参考：[paas/migrate](https://github.com/alibaba/SREWorks/tree/main/paas/migrate)
        * 参考：[Dockerfile_db_migration](https://github.com/alibaba/SREWorks/tree/main/paas/appmanager/Dockerfile_db_migration)
        * 参考：[entrypoint.sh](https://github.com/alibaba/SREWorks/tree/main/paas/migrate/entrypoint.sh)
        * 参考：[images.txt](https://github.com/alibaba/SREWorks/blob/master/images.txt)
    * redis。密码查看 `docker-compose.yaml`
    * minio。用户名密码查看 `docker-compose.yaml`
* 初始化数据库。在 `tools/docker/local/db-migration` 目录下执行 `docker compose up -d` 命令初始化数据库

### 启动代码

#### 启动服务端

* 新增 `application-local.properties` 文件，配置本地环境。在 `tesla-appmanager-start-standalone` 新增。注意在 `.gitignore` 默认添加了
    * 修改日志文件。修改 `tesla-appmanager-start-standalone` 模块的 `logback-spring-local.xml` 和 `logback-spring.xml` 日志文件。默认使用 `logback-spring.xml`，在 idea 中启动会报错，需调整成 `logback-spring-local.xml` 文件。
    * 在 `application-local.properties` 中新增配置：`logging.config=classpath:logback-spring-local.xml`
    * 新增环境变量配置
* 启动类新增环境变量。
    * `ABM_CLUSTER=daily`
* 启动：`com.alibaba.tesla.appmanager.start.Application`
* 初始化应用。appmanager 有 2 个初始化操作，在 `tools/docker/local/postrun`  目录下执行 `docker compose up -d`
    * 初始化集群。将 appmanager 部署的 kubernetes 作为默认集群更新到 appmanager 中
    * 初始化 definition
    * 部署 flycore。启动 appmanager 后，在 `tools/docker/local/init` 目录下执行 `docker compose up -d` 命令部署 flycore。
    * 鉴权问题。在启动 appmanager 时，默认是没有开启鉴权的，初始化脚本都以开启鉴权为前提，因此使用 sreworks 官方镜像执行初始化操作，需要先开启 appmanager 鉴权启动，初始化后视需要关闭鉴权重启 appmanager 即可

* 查看 swagger：http://localhost:7001/doc.html

#### 启动前端

本项目未复制 SREWorks 项目的前端部分到本项目中，需 clone SREWorks 项目并 `cd SREWorks/paas/frontend` 执行。

* 前置需求，安装 yarn。`npm install yarn --global`
* 安装依赖。`yarn install`
* 构建项目。`yarn build:all`
* 启动项目。`yarn start`。必须在构建项目之后才可以启动项目
* 查看web。http://localhost:8080