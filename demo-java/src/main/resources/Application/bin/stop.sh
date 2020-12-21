#!/bin/sh
base_path=$(cd `dirname $0`;pwd)

deploypackage=$(dirname ${base_path})

echo "获取pid：ps -ef | grep ${base_path} | grep -v grep | awk '{print \$2}'"
pid=$(ps -ef | grep ${base_path} | grep -v grep | awk '{print $2}')

if [ -n "${pid}" ]; then
  kill ${pid}
  echo "进程：${pid} 终止成功"
  exit 0
else
  echo "服务停止失败，未找到对应pid"
  exit 1
fi
