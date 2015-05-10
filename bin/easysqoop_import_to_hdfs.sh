#!/bin/bash 
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

parseArgs(){
    args=($@)
    arglen=$#
    for((i=0;i <$arglen;i+=2));do 
        arg=${args[$i]}
        if [ $arg == "-h" ];then
            _db_host=${args[$i+1]}
        elif [ $arg == "-d" ];then
            _db=${args[$i+1]}
        elif [ $arg == "-t" ];then
            _tbl=${args[$i+1]}
        elif [ $arg == "-u" ];then
            _username=${args[$i+1]}
        elif [ $arg == "-p" ];then
            _password=${args[$i+1]}
        elif [ $arg == "-dir" ];then
            hdfs_dir=${args[$i+1]}
        elif [ $arg == "-fmt" ];then
            _isseqfile=${args[$i+1]}
        elif [ $arg == "-hdb" ];then
            hive_db=${args[$i+1]}
        elif [ $arg == "-htb" ];then
            hive_tbl=${args[$i+1]}
        elif [ $arg == "-datetime" ];then
            hive_dt_partition=${args[$i+1]}
        elif [ $arg == "-help" ];then
            _help=${args[$i+1]}
        elif [ $arg == "-overwrite" ];then
            _overwrite=${args[$i+1]}
        elif [ $arg == "-cols" ];then
            _columns=${args[$i+1]}
        fi
    done
}
usage(){
   echo "usage:`basename $0` -h <host> -d <dbname> -t <tablename> -u <username> -p <password> -dir <hdfsdir> -fmt <0,1> -hdb <hiveDB> -htb <hivetable> -help -overwrite <0,1> -cols <col1,col2,col3>"
}
printVal(){
   echo $_db_host
   echo $_db
   echo $_tbl 
   echo $_username
   echo $_password
   echo $hdfs_dir
}

_db_host=""
_db=""
_tbl=""
_username=""
_password=""
hdfs_dir=""
_isseqfile=0
_fileformat="--as-sequencefile"

hive_db=""
hive_tbl=""
hive_dt_partition=""

_overwrite=0
_overwritedir=""

_help=""
parseArgs $@

if [ -z $_help ];then
  echo ""
else
   usage
   exit 1
fi

if [ -z $_db_host ] || [ -z $_db ] || [ -z $_tbl ] || [ -z $_username ] || [ -z $_password ] || [ -z $hdfs_dir ];then
   usage 
   printVal
   exit 1;
fi

if [ $_isseqfile -eq 0 ];then
    _fileformat="--as-textfile"
fi
if [ $_overwrite -eq 1 ];then
   _overwritestr="--delete-target-dir"
fi
if [ "$_columns" != "" ];then
   colstr="--columns $_columns"
   echo $colstr
fi

option_par="--fields-terminated-by \001 --lines-terminated-by \n --hive-drop-import-delims"

sqoop import --connect jdbc:mysql://${_db_host}/${_db} --username ${_username} --password ${_password} --table ${_tbl} --target-dir ${hdfs_dir}  ${_fileformat} ${_overwritestr} ${colstr} ${option_par}
if [ $? -ne 0 ];then
   echo "sqoop import failed..."
   exit 1
fi

add_to_hive=true
if [ -z $hive_db ] || [ -z $hive_tbl ] || [ -z $hive_dt_partition ];then
  add_to_hive=false
fi

if [ $add_to_hive == true ];then
    hive -e "use ${hive_db}; ALTER TABLE ${hive_tbl} DROP IF EXISTS PARTITION (dt='$hive_dt_partition');"
    [ $? -eq 0 ] || exit 1   
    hive -e "use ${hive_db}; ALTER TABLE ${hive_tbl} ADD PARTITION (dt='$hive_dt_partition') LOCATION '${hdfs_dir}';"
    [ $? -eq 0 ] || exit 1   
fi




