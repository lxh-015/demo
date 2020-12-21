#!/bin/sh
base_path=$(cd `dirname $0`;pwd)

appLog=${base_path}/../log
appConf=${base_path}/../conf
appLib=${base_path}/../lib

java ${JAVA_OPTS} -cp "$appConf/.:$appLib/*" \
    -Dlogpath=${appLog} \
    com.linxh.paas.demo.PaasMain