# Sreworks 安装

本文通过分析 sreworks helm chart 内容，分析 sreworks 项目启动流程，了解 sreworks 部分运行机理。

## 架构层级

参考 sreworks 的产品架构，结合 sreworks 相关文档，可以得出 sreworks 的架构层级。

![产品架构](https://static001.geekbang.org/infoq/30/30c991b345e3f585392bba5ed7c90a36.png)

架构层级：

* AppManager。核心部分，提供 devops 的基础能力。
* 应用。按照 **用途** 分类，3 者只是用途的不同，都可归纳为 运维应用
* 企业应用（App）。开源。主要是**应用管理**部分
  * 应用开发
  * 应用市场
  * 应用实例
* 运维应用（FlyAdmin）。开源。sreworks 中的所有平台能力均由不同运维应用提供，企业应用（App）也是一款运维应用
  * 参考：[功能列表](https://sreworks.cn/docs/tcpxav)
* 大数据应用（RMS）。阿里云内部专用，不开源

在 sreworks 中，AppManager 位于底层，提供基础的运维能力。在之上构建了一系列运维应用（微服务），每个运维应用承担不同的运维能力，每个运维应用作为一个微服务，研发、发布、运维也可通过 AppManager 发布，实现我自己运维我自己的效果。

对于一个产品，如果能实现套娃式管理，可以充分说明自身能力的强大，相关案例如：

* docker in docker。在 docker 中部署 linux，在 linux 容器中安装 docker，在 linux 容器中用 docker 部署 linux，持续套娃
* devops 领域的多云管理或者多集群管理。在业务部署范围包含多个 kubernetes 集群，以及设计多个云提供商，如国内阿里云，国外 AWS，多云管理不一致就会有概率出现。
  * [统一管控方案](https://sreworks.cn/column/cylt0vx8qc1g3973#%E7%BB%9F%E4%B8%80%E7%AE%A1%E6%8E%A7%E6%96%B9%E6%A1%88)
  * [Spinnaker终极形态-Spinnaker On Spinnaker](https://yejingtao.blog.csdn.net/article/details/103789813)

## 启动流程

在启动流程上也体现了 sreworks 的架构流程，启动流程：

* 初始化环境。启动 mysql、redis、minio、zookeeper
* 启动 appmanager。
  * 初始化集群。将 appmanager 运行的集群，作为默认集群注册到 appmanager
  * 初始化各种插件、definition
* 启动 authproxy、gateway、frontend
* 启动运维应用
  * 企业应用
  * 运维应用

`paas/swcli` 是 sreworks 提供的 cli 接口，通过 http 操作 appmanager 服务。helm chart 通过 `paas/swcli` 在 appmanager 启动后，启动所有的运维应用。

### swcli

helm chart 中提供了 swcli 的默认配置信息，用以访问 appmanager。swcli 通过 http 与 appmanager 通信。

```yaml
# Source: sreworks/templates/swcli.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: init-swcli
data:
  swcli.yaml: |
    endpoint: http://sreworks-appmanager
    username: superuser
    password: yJfIYmjAiCL0ondV3kY7e5x6kVTpvC3h
    client-id: superclient
    client-secret: stLCjCPKbWmki65DsAj2jPoeBLPimpJa
```

通过 `job` 运行运维应用的启动，以 `saas/swcore` 为例，`swcore` 项目启动 authproxy、gateway、frontend 等模块

在启动 `saas/swcore` 的 `job` 中，用到了 3 个 configmap，分别是上面的 `init-swcli`，还有 `init-configmap` 和 `init-run`。其中 `init-configmap` 中是一堆的环境变量，`init-run` 是每个 `saas` 运维应用启动脚本，从 `sreworks-core-init-job` job 中可以看到 `saas/swcore` 的启动脚本为 `core.sh`。

```yaml
apiVersion: batch/v1
kind: Job
metadata: 
  name: sreworks-core-init-job
  annotations:
spec:
  backoffLimit: 100000
  ttlSecondsAfterFinished: 600
  template:
    spec:
      restartPolicy: OnFailure 
      containers:
      - image: sreworks-registry.cn-beijing.cr.aliyuncs.com/hub/swcli-builtin-package:v1.5
        name: job
        command: ["sh"]
        args: ["/app/core.sh"]
        imagePullPolicy: Always
        workingDir: /root
        envFrom:
          - configMapRef:
              name: init-configmap
        volumeMounts:
        - name: run-volume
          mountPath: /app
        - name: swcli-volume
          mountPath: /swcli
 
      volumes:
      - name: run-volume
        configMap:
          name: init-run
      - name: swcli-volume
        configMap:
          name: init-swcli
```

查看对应的 `core.sh` 脚本内容：

```shell
apiVersion: v1
kind: ConfigMap
metadata:
  name: init-run
data:
  core.sh: |
    set -e
    set -x
    
    cat /swcli/swcli.yaml
    cd /root/saas/swcore/api/core/

    export NAMESPACE_ID=$(cat /run/secrets/kubernetes.io/serviceaccount/namespace)

    if [[ "$IMAGE_BUILD_ENABLE" == "true" ]] ; then
        # build&launch paas
        envsubst < /root/saas/swcore/api/core/build.yaml.tpl > build.yaml

        if [[ "$ACCESS_MODE" == "ingress" ]] ; then
           envsubst < /root/saas/swcore/api/core/launch.yaml.tpl > launch.yaml
        else
           envsubst < /root/saas/swcore/api/core/launch-nodeport.yaml.tpl > launch.yaml
        fi

        cat build.yaml
        cat launch.yaml
        /root/swcli --config /swcli/swcli.yaml app-package oneflow --app-id=${CORE_APP_ID} --stage=prod --namespace=${NAMESPACE_ID} --tags="release=sreworks/x86_64" --arch=x86 --cluster=master --path=./launch.yaml --disable-dir-check 

    else
        python /app/patch_meta_yaml.py /root/saas/swcore/build
        python /app/pack.py --src /root/saas/swcore/build --dest /root/saas/swcore/build.zip

        if [[ "$ACCESS_MODE" == "ingress" ]] ; then
            envsubst < /root/saas/swcore/build/launch.yaml.tpl > /root/saas/swcore/launch.yaml
        else
            envsubst < /root/saas/swcore/build/launch-nodeport.yaml.tpl > /root/saas/swcore/launch.yaml
        fi
        
        #result=$(/root/swcli --config /swcli/swcli.yaml app-package import --app-id=${CORE_APP_ID} --filepath /root/saas/swcore/build.zip --print-only-app-package-id=true --reset-version=true)
        result=$(/root/swcli --config /swcli/swcli.yaml app-package import --app-id=${CORE_APP_ID} --filepath /root/saas/swcore/build.zip --print-only-app-package-id=true)
        /root/swcli --config /swcli/swcli.yaml deployment launch --wait-max-seconds 400 --app-id=${CORE_APP_ID} --stage=prod --namespace=${NAMESPACE_ID} --path /root/saas/swcore/launch.yaml --app-package-id $result --arch x86 --wait=true --cluster master
    fi


    if [[ "$IMPORT_FRONTEND" == "true" ]] ; then

        sh /app/core-action-service-check.sh
        # import&launch saas desktop
        envsubst < /root/saas/desktop/build/launch-frontend.yaml.tpl > /root/saas/desktop/launch-frontend.yaml
        envsubst < /root/saas/desktop/build/launch-frontend-dev.yaml.tpl > /root/saas/desktop/launch-frontend-dev.yaml 

        if [[ "$ONLY_BASE" == "true" ]] ; then
           sed -i 's/searchConfig\\":true/searchConfig\\":false/g' /root/saas/desktop/build/INTERNAL_ADDON_productopsv2.zip.dir/content.json
        fi

        python /app/pack.py --src /root/saas/desktop/build --dest /root/saas/desktop/build.zip
   
        sh /app/import.sh desktop /root/saas/desktop/build.zip /root/saas/desktop/launch-frontend.yaml /root/saas/desktop/launch-frontend-dev.yaml
        
        # import&launch saas swadmin
        python /app/pack.py --src /root/saas/swadmin/build --dest /root/saas/swadmin/build.zip
        envsubst < /root/saas/swadmin/build/launch-frontend.yaml.tpl > /root/saas/swadmin/launch-frontend.yaml
        envsubst < /root/saas/swadmin/build/launch-frontend-dev.yaml.tpl > /root/saas/swadmin/launch-frontend-dev.yaml
        
        sh /app/import.sh swadmin /root/saas/swadmin/build.zip /root/saas/swadmin/launch-frontend.yaml /root/saas/swadmin/launch-frontend-dev.yaml
 
        # import&launch saas template
        python /app/pack.py --src /root/saas/template/build --dest /root/saas/template/build.zip
        envsubst < /root/saas/template/build/launch-frontend.yaml.tpl > /root/saas/template/launch-frontend.yaml
        envsubst < /root/saas/template/build/launch-frontend-dev.yaml.tpl > /root/saas/template/launch-frontend-dev.yaml
        
        sh /app/import.sh template /root/saas/template/build.zip /root/saas/template/launch-frontend.yaml /root/saas/template/launch-frontend-dev.yaml
    fi
```

内容比较多，默认的核心流程即是：

* 填充环境变量
* 启动应用

在所有的 `saas` 目录下的运维应用都有 1 个目录：`build` 目录，里面包含预定义的 OAM `ApplicationConfiguration` 定义。

通过渲染 `build` 目录下的模板，获取到运行配置，即可提交应用





