# 部署

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
# 替换NODE_IP为某个节点的浏览器可访问IP。如果是本地 minikube，不能使用 localhost，需换成 ip 地址
# 如果是 k3s 或 minikube 自带 storageClass，k3s: local-path，其他需用 kubectl get storageClass 获取
# 如果是用 helm 卸载重新安装，会有异常
helm install sreworks ./ \
    --create-namespace --namespace sreworks \
    --set global.accessMode="nodePort" \
    --set global.images.tag="v1.5" \
    --set appmanager.home.url="http://{ip}:30767" \
    --set appmanager.server.jwtSecretKey="1234567" \
    --set global.storageClass="{storageclass}" \ 
    --set saas.onlyBase=true \
    --set localPathProvisioner=false

# 卸载
helm uninstall sreworks -n sreworks
```


### 部署介绍

参考文档：

* [快速安装](https://sreworks.cn/docs/rr5g10)。
* [离线安装](https://sreworks.cn/docs/vswz3aa0td7os7i3)。
* [在K3s下安装SREWorks](https://sreworks.cn/docs/hwchqke3tibvlg7t)。