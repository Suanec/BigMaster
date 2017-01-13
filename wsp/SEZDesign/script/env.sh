export SUANEC_HOME=/home/yantu/suanec
export LIB_HOME=$SUANEC_HOME/libs
export ANACONDA_HOME=$LIB_HOME/anaconda2
export JAVA_HOME=$LIB_HOME/jdk1.8.0_60
export SCALA_HOME=$LIB_HOME/scala-2.11.8
export HADOOP_HOME=$LIB_HOME/hadoop-2.7.3
export SPARK_HOME=$LIB_HOME/spark-2.0.1-bin-hadoop2.4
export CLASSPATH=.:$JAVA_HOME/lib:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar 
export SBT_HOME=$LIB_HOME/sbt
export MVN_HOME=$LIB_HOME/apache-maven-3.3.3


export PATH=$ANACONDA_HOME/bin:$SUANEC_HOME:$LIB_HOME:$JAVA_HOME/bin:$SCALA_HOME/bin:$HADOOP_HOME/bin:$SPARK_HOME/bin:$SBT_HOME/bin:$MVN_HOME/bin:$PATH 


alias hadoop-start='$HADOOP_HOME/sbin/start-all.sh'
alias fs='hadoop fs'


hadoop-start
