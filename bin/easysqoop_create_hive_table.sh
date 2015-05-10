#!/bin/bash - 
#===============================================================================
#
#          FILE: sqoop_import_to_hdfs.sh
# 
#         USAGE: ./sqoop_import_to_hdfs.sh 
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
. ./easysqoop_config.sh
echo $EASYSQOOP_CLASSPATH

java -classpath $EASYSQOOP_CLASSPATH com.hardthing.easysqoop.Main $@ 

#java -classpath $EASYSQOOP_CLASSPATH com.hardthing.easysqoop.Main -dc com.mysql.jdbc.Driver -conn jdbc:mysql://192.168.73.130:3306/test -u root -p root -inpt user -outt user -columns id  -hdb source -partk dt,pf -loc /user/root/ -external 
