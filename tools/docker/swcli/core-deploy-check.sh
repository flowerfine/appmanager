    while true
    do
        CORE=$(/root/swcli --config /swcli/swcli.yaml deployment list --app-id=${CORE_APP_ID} --page=1 --page-size=1 -j|awk -F 'deployStatus":"' '{print $2}'|awk -F '"' '{print $1}')
        if [[ "$CORE" == "SUCCESS" ]] ; then
            echo "check flycore deploy success!"
            break
        else
            echo "wait flycore deploy, current: "$CORE
            sleep 5
        fi
    done