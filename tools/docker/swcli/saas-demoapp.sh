    set -e
    set -x

    sh /app/core-deploy-check.sh

    cd /root/saas/app/
    checkDemoApp=$(python /root/saas/app/initDemoApp.py)
    echo $checkDemoApp
    if [[ "$checkDemoApp" == "NO" ]] ; then
       result=$(/root/swcli --config /swcli/swcli.yaml app-package import --app-id sreworks1 --filepath /root/saas/app/demoApp.zip --print-only-app-package-id=true --reset-version=true)
       /root/swcli --config /swcli/swcli.yaml deployment launch  --namespace sreworks --stage prod --app-id sreworks1 --path /root/saas/app/launch-demoApp.yaml --app-package-id $result --arch x86 --wait=true --cluster master
    fi