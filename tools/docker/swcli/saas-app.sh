    set -e
    set -x

    sh /app/core-deploy-check.sh

    cd /root

    export NAMESPACE_ID=$(cat /run/secrets/kubernetes.io/serviceaccount/namespace)
    envsubst < /root/saas/app/api/build.yaml.tpl > /root/saas/app/api/build.yaml
    envsubst < /root/saas/app/build/launch.yaml.tpl > /root/saas/app/launch.yaml
    envsubst < /root/saas/app/build/launch-backend.yaml.tpl > /root/saas/app/launch-backend.yaml
    envsubst < /root/saas/app/build/launch-frontend.yaml.tpl > /root/saas/app/launch-frontend.yaml
    envsubst < /root/saas/app/build/launch-frontend-dev.yaml.tpl > /root/saas/app/launch-frontend-dev.yaml
    python /app/pack.py --src /root/saas/app/build --dest /root/saas/app/build.zip


    if [[ "$IMAGE_BUILD_ENABLE" == "true" ]] ; then

        cd /root/saas/app/api/
        /root/swcli --config /swcli/swcli.yaml app-package oneflow --app-id=app --stage=prod --namespace=${NAMESPACE_ID} --tags="release=sreworks/x86_64" --arch=x86 --cluster=master --path=../launch-backend.yaml --disable-dir-check

        if [[ "$IMPORT_FRONTEND" == "true" ]] ; then

            sh /app/import.sh app /root/saas/app/build.zip /root/saas/app/launch-frontend.yaml /root/saas/app/launch-frontend-dev.yaml

        fi

    else

      sh /app/import.sh app /root/saas/app/build.zip /root/saas/app/launch.yaml /root/saas/app/launch-frontend-dev.yaml

    fi