#!/bin/sh
base_path=$(cd `dirname $0`;pwd)

deploypackage=$(dirname ${base_path})

echo "获取pid：ps -ef | grep ${base_path} | grep -v grep | awk '{print \$2}'"
pid=$(ps -ef | grep ${base_path} | grep -v grep | awk '{print $2}')

if [ ! -n "${pid}" ]; then
  echo "服务不存在或已经停止"
  exit 1
fi

echo "获取port：netstat -ntlp |grep ${pid} | grep -v grep | awk '{print \$4}' | awk  -F : '{print \$4}'"
port=$(netstat -ntlp |grep ${pid} | grep -v grep | awk '{print $4}' | awk  -F : '{print $4}')
echo "测试接口：localhost:${port}"
result=$(curl -I -m 10 -o /dev/null -s -w %{http_code} localhost:${port})

if [[ 200 -eq "${result}" ]]; then
  echo "服务正常返回状态码:${result}"
  exit 0
 else
  echo "服务停止失败"
  exit 2
fi
