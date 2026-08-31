# 源码

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