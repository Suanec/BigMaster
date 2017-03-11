alias suanec='source /home/yantu/suanec/libs/env.sh'


export SUANEC_HOME=/home/yantu/suanec
export LIB_HOME=$SUANEC_HOME/libs
export ANACONDA_HOME=$LIB_HOME/anaconda2
export MXNET_HOME=$LIB_HOME/mxnet-mkl
export JAVA_HOME=$LIB_HOME/jdk1.8.0_60
export SCALA_HOME=$LIB_HOME/scala-2.11.8
export HADOOP_HOME=$LIB_HOME/hadoop-2.7.3
export HIVE_HOME=$LIB_HOME/apache-hive-2.1.1-bin
export SPARK_HOME=$LIB_HOME/spark-2.0.1-bin-hadoop2.4
export CLASSPATH=.:$JAVA_HOME/lib:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar 
export SBT_HOME=$LIB_HOME/sbt
export MVN_HOME=$LIB_HOME/apache-maven-3.3.3
export mxlib=/home/yantu/suanec/libs/mxnet-full_2.10-linux-x86_64-cpu-0.1.1/mxnet-full-scala.jar


export PATH=$ANACONDA_HOME/bin:$MXNET_HOME:$MXNET_HOME/lib:$SUANEC_HOME:$LIB_HOME:$JAVA_HOME/bin:$SCALA_HOME/bin:$HADOOP_HOME/bin:$SPARK_HOME/bin:$SBT_HOME/bin:$MVN_HOME/bin:$PATH 


alias hadoop-start='$HADOOP_HOME/sbin/start-all.sh'
alias hadoop-stop='$HADOOP_HOME/sbin/stop-all.sh'
alias fs='hadoop fs'
alias scala-mxnet='scala -cp /home/yantu/suanec/libs/mxnet-full_2.10-linux-x86_64-cpu-0.1.1/mxnet-full-scala.jar -J-Xmx1024m'
alias spark-mxnet='spark-shell --driver-memory 4g -cp /home/yantu/suanec/libs/mxnet-full_2.10-linux-x86_64-cpu-0.1.1/mxnet-full-scala.jar'
alias suanecFTP='ftp 192.168.199.188'
alias spark-ml='spark-shell -cp /home/yantu/suanec/wsp/spark-ml-framework/spark-ml-desktop.jar'


#hadoop-start
