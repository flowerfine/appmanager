# AppManager

AppManager 为 [SREWorks](https://github.com/alibaba/SREWorks) 项目中的 `paas/appmanager` 模块。

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
* 初始化数据库。在 `tools/docker/db-migration` 目录下执行 `docker compose up -d` 命令初始化数据库
* 部署 flycore。启动 appmanager 后，在 `tools/docker/init` 目录下执行 `docker compose up -d` 命令部署 flycore。
  * appmanager 需开启鉴权


### 启动代码

* 新增 `application-local.properties` 文件，配置本地环境。在 `tesla-appmanager-start-standalone` 新增。注意在 `.gitignore` 默认添加了
  * 修改日志文件。修改 `tesla-appmanager-start-standalone` 模块的 `logback-spring-local.xml` 和 `logback-spring.xml` 日志文件。默认使用 `logback-spring.xml`，在 idea 中启动会报错，需调整成 `logback-spring-local.xml` 文件。
  * 在 `application-local.properties` 中新增配置：`logging.config=classpath:logback-spring-local.xml`
  * 新增环境变量配置
* 启动类新增环境变量。
  * `ABM_CLUSTER=daily`
* 启动：`com.alibaba.tesla.appmanager.start.Application`
* 初始化应用。appmanager 有 2 个初始化操作，`docker compose --profile postrun up -d`
  * 初始化集群。将 appmanager 部署的 kubernetes 作为默认集群更新到 appmanager 中
  * 初始化 definition
  * 鉴权问题。在启动 appmanager 时，默认是没有开启鉴权的，初始化脚本都以开启鉴权为前提，因此使用 sreworks 官方镜像执行初始化操作，需要先开启 appmanager 鉴权启动，初始化后视需要关闭鉴权重启 appmanager 即可

* 查看 swagger：http://localhost:7001/doc.html

## SREWorks 介绍

### 项目结构

* paas
  * appmanager。核心
  * tesla-authproxy。
  * tesla-gateway
  * sreworks-base。
    * 是 appmanager 部分接口的对接部分（通过 http 对接 appmanager）。
* plugins。sreworks 将 trait 和 component 插件抽离出来，放到了此目录下，原属于 appmanager
* saas
  * app。应用管理
    * appdev-server。应用管理 -> 开发
    * appcenter-server。应用管理 -> 应用（我的应用）
    * appmarket-server。应用管理 -> 市场
    * 无插件。应用管理 -> 插件 是直接调用的 appmanager 的接口
  * system
    * 阿里云 kubernetes 集群管理
    * 阿里云 rds 管理
    * 阿里云 redis 管理
    * 阿里云 ecs 管理
  * cluster。多 kubernetes 集群管理
  * team。团队管理
  * upload。文件上传

### 部署介绍

参考文档：

* [快速安装](https://sreworks.cn/docs/rr5g10)。
* [离线安装](https://sreworks.cn/docs/vswz3aa0td7os7i3)。
* [在K3s下安装SREWorks](https://sreworks.cn/docs/hwchqke3tibvlg7t)。

安装流程：

* 准备环境
  * kubernetes。需准备 kuernetes，快速体验可采用 minikube、k3s、kubesphere（其实是通过 kubekey 快速部署 kubernetes 和 kubesphere），docker desktop。
    * sreworks 部署时依赖 `StorageClass`，为了让对 kubernetes 了解不足的用户快速部署 sreworks，sreworks helm charts 添加了 `StorageClass`，对于 minikube、k3s 等自带 `StorageClass` 的场景，需修改 helm charts 参数禁止 sreworks 添加 `StorageClass`，否则会出问题。详情参考
  * helm。sreworks 提供了 helm 的分发，可以不用 helm 官方自己的安装脚本，用 sreworks 的就可以
  * 源码。clone sreworks 指定分支源码。如果 github clone 的比较慢，可以用一些 github 下载代理网站下载。
    * [GitHub 文件加速](https://github.abskoop.workers.dev/)
    * [Github 文件加速](https://www.7ed.net/gitmirror/hub.html)
  * 其他。sreworks 运行需要如下中间件或数据库，sreworks helm charts 内会安装这些以快速部署，可以通过参数控制是否部署，使用外部的 mysql、minio 等
    * mysql
    * redis
    * minio
    * zookeeper
* 启动。

启动脚本如下：

```shell
cd sreworks/chart/sreworks-chart
# 安装SREWorks
# 替换NODE_IP为某个节点的浏览器可访问IP
# 如果是用 helm 卸载重新安装，会有异常
helm install sreworks ./ \
    --create-namespace --namespace sreworks \
    --set global.accessMode="nodePort" \
    --set global.images.tag="v1.5" \
    --set appmanager.home.url="http://43.143.73.169:30767" \
    --set appmanager.server.jwtSecretKey="1234567" \
    --set global.storageClass="local-path" \
    --set saas.onlyBase=true \
    --set localPathProvisioner=false
```

### 源码分析

#### AppManager

##### 业务隔离

阿里云面临复杂的业务场景：

- **专有云**：将 ABM 交付到各个客户现场环境中，依赖统一的阿里专有云 K8s 底座。大部分的客户环境是网络隔离的，不出差到现场的情况下，只能拍照一来一回解决问题。
- **公共云**：交付各个阿里大数据产品到公共云 ACK 集群上(资源集群 or 业务集群)，多 Region，为云上客户提供服务。
- **集团内部**：部署各个大数据产品到集团内部 K8S 集群上，多 Region，为集团内部各业务方提供服务；另外还需要将我们自身交付到 OXS(阿里云内核区域)K8s 公共集群中 (权限受限)。
- **开源社区**：交付 SREWorks 到各类用户自建的集群下以及各大厂商公共云。

AppManager 提供了多集群管理功能：

![统一管控](https://static001.geekbang.org/infoq/9e/9e322802e6c0774d5c5e70fd86ddf690.png)

AppManager 在中心环境部署一个，作为整体的管控。

* unit（UnitProvider）。在阿里云中，单元（unit）表示一个网络隔离或用途隔离的环境，单元自称一体，对外无任何依赖。单元间网络隔离。每个单元需要一个 AppManager 实例进行管控。一个单元可包括多个 Cluster。
* cluster（ClusterProvider）。一个独立 K8S 集群，集群间网络可达。集群直接注册 kubeconfig 到当前单元下的 AppManager 即可使用。
* namespace（NamespaceProvider）。对应 K8S 的 Namespace 概念，用于一个 Cluster 下的资源及应用的隔离，一个 Namespace 对应了一个信息孤岛。
* stage（StageProvider）。阶段/环境。一个 Namespace 可包含多个 Stage，每个 Stage 共享了当前 Namespace 下的所有资源。

##### OAM模型

* Application
* Policy
* Component。AppManager 将 Component 和 Trait 统一以插件形式处理。
* Trait。AppManager 将 Component 和 Trait 统一以插件形式处理。
* Workflow

##### 应用构建

在 OAM 模型中，一个 Application 包含：Policy、Component、Trait、Workflow。

其中研发角色只需要关注 Component 即可，而 Component 也是研发产出，需要打包的东西。应用构建即对应用进行打包，即对 Component 打包，产出 Component 信息如 image、helm 等。

* AppPackageTaskProvider。打包任务
* ComponentPackageProvider。Component 包
* AppPackageProvider。应用包

##### 应用部署

todo


## 参考链接

- 理论相关
  - [阿里云发布 OAM Kubernetes 标准实现与核心依赖库](https://mp.weixin.qq.com/s/YWPgNgr-WHd-ORvtJXp62A)
  - [OAM 深入解读：OAM 为云原生应用带来哪些价值？](https://developer.aliyun.com/article/744417)
  - 服务编排
    - [Kubernetes资源编排系列之一: Pod YAML篇](https://mp.weixin.qq.com/s?__biz=MzUwOTkwNzQxMg==&mid=2247485027&idx=1&sn=6d1371244d5c05203c0d5be78a27cd32&chksm=f90a5ed8ce7dd7cefcf19974f0e8d6c1cdeddf1e1c6ca45cdbefbeab669a30511cfa7e6da334&mpshare=1&scene=1&srcid=0319uWVYWokg3J4TuImH6imU&sharer_shareinfo=7286f71d67c3da15cd9ced0b5a609ec3&sharer_shareinfo_first=7286f71d67c3da15cd9ced0b5a609ec3&version=4.1.10.99312&platform=mac#rd)
    - [Kubernetes资源编排系列之二: Helm篇](https://mp.weixin.qq.com/s?__biz=MzUwOTkwNzQxMg==&mid=2247485073&idx=1&sn=ba1ced10c1941ee859632b55c3b8fcaa&chksm=f90a5e2ace7dd73c501318ce4358c58f651c06ccccc2eda2301b3374c6795f3ca8e936a09734&mpshare=1&scene=1&srcid=03190G17UN1VLeSiAAanZc7C&sharer_shareinfo=2092d771e8d85123066a653e44b782d9&sharer_shareinfo_first=2092d771e8d85123066a653e44b782d9&version=4.1.10.99312&platform=mac#rd)
    - [Kubernetes资源编排系列之三: Kustomize篇](https://mp.weixin.qq.com/s?__biz=MzUwOTkwNzQxMg==&mid=2247485126&idx=1&sn=2b28ab0bc1c6d93ba2181f0c11e1bc42&chksm=f90a5e7dce7dd76b581aa8aa2830ddb0f6a94b54ef662dff97b5b1b9bcf7015f9adfbac0a916&mpshare=1&scene=1&srcid=0319I8Yzen2kbpBmzD8Y9jZ5&sharer_shareinfo=4e494dfcc3c0bb48029f95ebe198cf38&sharer_shareinfo_first=4e494dfcc3c0bb48029f95ebe198cf38&version=4.1.10.99312&platform=mac#rd)
    - [Kubernetes资源编排系列之四: CRD+Operator篇](https://mp.weixin.qq.com/s?__biz=MzUwOTkwNzQxMg==&mid=2247485151&idx=1&sn=e2ec08bc1ccbe27ba4b9cbb14c841d40&chksm=f90a5e64ce7dd772849d149a371ce87d5226582ea726d79127fee0657264ad164ac9921b9c1e&mpshare=1&scene=1&srcid=0319mte6jyvOIYzrnul334bp&sharer_shareinfo=80e53b5f453ee5108d5e4d726ab254ee&sharer_shareinfo_first=80e53b5f453ee5108d5e4d726ab254ee&version=4.1.10.99312&platform=mac#rd)
    - [Kubernetes资源编排系列之五: OAM篇](https://mp.weixin.qq.com/s?__biz=MzUwOTkwNzQxMg==&mid=2247485175&idx=1&sn=61ed0bdc1141e4fe98ca53c959c3cbed&chksm=f90a5e4cce7dd75a3ba75d3ed530ad6f234b6c9b979054f45222da8df2106d67ad420c020a02&mpshare=1&scene=1&srcid=03193XfA6ej728rsvMFKSDq4&sharer_shareinfo=f825f7f093cc53f55ba215542765d8c1&sharer_shareinfo_first=f825f7f093cc53f55ba215542765d8c1&version=4.1.10.99312&platform=mac&poc_token=HEkH-WWjNxmvh6z3OXltOPzth0U4RSVtVYs42ygx)
- [SREWorks](https://github.com/alibaba/SREWorks)。[sreworks.cn](https://sreworks.cn/)。阿里巴巴大数据SRE团队云原生运维平台 SREWorks，沉淀了团队近10年经过内部业务锤炼的 SRE 工程实践，秉承“数据化、智能化”运维思想，帮助运维行业更多的从业者采用“数智”思想做好高效运维。
  - [QCon 演讲实录（上）：多云环境下应用管理与交付实践](https://xie.infoq.cn/article/330fa3e9327c0836f193ba9b0)
  - [QCon 演讲实录（下）：多云管理关键能力实现与解析 -AppManager](https://xie.infoq.cn/article/ccf591830b980db73d0e5af9c)
- [kubevela](https://github.com/kubevela/kubevela)
- [kubernetes-data-platform](https://github.com/linktimecloud/kubernetes-data-platform)。KDP(Kubernetes Data Platform) 提供了一个基于 Kubernetes 的现代化混合云原生数据平台。它能够利用 Kubernetes 的云原生能力来有效地管理数据平台。
  - 基于 kubevela 实现
