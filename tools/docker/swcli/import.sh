sh /app/core-action-service-check.sh

result=$(/root/swcli --config /swcli/swcli.yaml app-package import --app-id $1 --filepath $2 --print-only-app-package-id=true --reset-version=true)

/root/swcli --config /swcli/swcli.yaml deployment launch  --namespace ${NAMESPACE_ID} --stage prod --app-id $1 --path $3 --app-package-id $result --arch x86 --wait=true --cluster master
/root/swcli --config /swcli/swcli.yaml deployment launch  --namespace ${NAMESPACE_ID} --stage dev --app-id $1 --path $4 --app-package-id $result --arch x86 --wait=true --cluster master