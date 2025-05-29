    while true
    do
        curl prod-flycore-paas-action
        if [[ "$?" == "0" ]]; then
           echo "action service is ok"
           break
        else
           echo "wait action service ready"
           sleep 5
        fi
    done