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