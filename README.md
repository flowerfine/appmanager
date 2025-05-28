# AppManager

AppManager 为 [SREWorks](https://github.com/alibaba/SREWorks) 项目中的 `paas/appmanager` 模块。

## 开发文档

### 开发环境

* JDK。11
* 中间件。在 `tools/docker/local` 目录下执行 `docker compose --profile env up -d` 命令启动数据库
  * mysql。通过 `db-migration` 创建数据库。用户名密码查看 `docker-compose.yaml`
    * 参考：[paas/migrate](https://github.com/alibaba/SREWorks/tree/main/paas/migrate)
    * 参考：[Dockerfile_db_migration](https://github.com/alibaba/SREWorks/tree/main/paas/appmanager/Dockerfile_db_migration)
    * 参考：[entrypoint.sh](https://github.com/alibaba/SREWorks/tree/main/paas/migrate/entrypoint.sh)
    * 参考：[images.txt](https://github.com/alibaba/SREWorks/blob/master/images.txt)
  * redis。密码查看 `docker-compose.yaml`
  * minio。用户名密码查看 `docker-compose.yaml`

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



