#!/bin/bash - 
#===============================================================================
#
#          FILE: easysqoop_config.sh
# 
#         USAGE: ./easysqoop_config.sh
# 
#   DESCRIPTION: 
# 
#       OPTIONS: ---
#  REQUIREMENTS: ---
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: xiangtao (), xiangtao@letv.com
#  ORGANIZATION: 
#       CREATED: 2015年05月07日 18:47
#      REVISION:  ---
#===============================================================================
add_to_classpath(){
  dir=$1
  for f in $dir/*.jar; do
    EASYSQOOP_CLASSPATH=${EASYSQOOP_CLASSPATH}:$f;
  done
  export EASYSQOOP_CLASSPATH
}

EASYSQOOP_CLASSPATH=""
if [ -d "`pwd`/../lib" ]; then
 # old_dir=`pwd`
  #cd ..
  #h_dir=`pwd`
  add_to_classpath `pwd`/../lib
  add_to_classpath `pwd`/..
  cd $old_dir
fi

export EASYSQOOP_CLASSPATH

