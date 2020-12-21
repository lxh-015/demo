#!/bin/sh
base_path=$(cd `dirname $0`;pwd)

deploypackage=$(dirname ${base_path})

echo "获取pid：ps -ef | grep ${base_path} | grep -v grep | awk '{print \$2}'"
pid=$(ps -ef | grep ${base_path} | grep -v grep | awk '{print $2}')

if [ -n "${pid}" ]; then
  kill -9 ${pid}
  echo "进程：${pid} 终止成功"
  echo "清理文件包：${deploypackage}"
  rm -rf ${deploypackage}
  exit 0
else
  echo "服务停止失败，未找到对应pid"
  echo "清理文件包：${deploypackage}"
  rm -rf ${deploypackage}
  exit 0
fi
