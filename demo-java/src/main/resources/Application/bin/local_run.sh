#!/bin/sh
base_path=$(cd `dirname $0`;pwd)

nohup ${base_path}/run.sh > /dev/null 2>&1 &