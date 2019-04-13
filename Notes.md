# 笔记
## Hadoop 安装
1. 安装 Java
   安装文件并自动配置
   ```bash
   $ sudo add-apt-repository ppa:webupd8team/java
   $ sudo apt-get update
   $ sudo apt-get install oracle-java8-installer
   $ sudo apt-get install oracle-java8-set-default   
   ```
2. 安装 Hadoop  
   为 Hadoop 创建文件夹并将权限赋予相关账户：  
   ```bash
   $ sudo mkdir /opt/software/hadoop
   $ sudo chown -R <user>:<group> /opt/software/hadoop
   ```
   添加环境变量：在`~/.profile`加入以下内容：
   ```bash
   export HADOOP_HOME="/opt/software/hadoop"
   ```
   将 Hadoop 文件解压缩至`$HADOOP_HOME`
## 配置 Hadoop
1. 添加环境变量：在`etc/hadoop/hadoop-env.sh`中修改以下内容：
   ```bash
   export JAVA_HOME=/usr
   ```
### 配置伪分布式：
1. 修改`etc/hadoop/core-site.xml`：
    ```xml
    <configuration>
        <property>
            <name>fs.defaultFS</name>
            <value>hdfs://localhost:9000</value>
        </property>
        <property>
            <name>hadoop.tmp.dir</name>
            <value>/opt/software/hadoop/workspace/tmp</value>
        </property>
    </configuration>
    ```
2. 创建临时文件夹：  
    ```bash
    $ mkdir -p workspace/tmp
    ```
3. 修改`etc/hadoop/hdfs-site.xml`，设置文件重复数量：  
    ```xml
    <configuration>
        <property>
            <name>dfs.replication</name>
            <value>1</value>
        </property>
    </configuration>
    ```
4. 配置 SSH 免密码访问：   
    ```bash
    $ ssh localhost
    ```
    如果需要输入密码，则生成密钥：   
    ```bash
    $ ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
    $ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
    $ chmod 0600 ~/.ssh/authorized_keys
    ```
5. 格式化并启动HDFS：   
    ```bash
    $ bin/hdfs namenode -format
    $ sbin/start-dfs.sh
    ```
    现在应当可以从`http://localhost:9870/`访问NameNode的Web客户端
6. 在HDFS中创建工作路径：
    ```bash
    $ bin/hdfs dfs -mkdir -p /user/<user>
    ```
7. 修改`etc/hadoop/mapred-site.xml`，为MapReduce配置YARN和环境：
    ```xml
    <configuration>
        <property>
                <name>mapreduce.framework.name</name>
                <value>yarn</value>
        </property>
        <property>
                <name>mapreduce.application.classpath</name>
                <value>${HADOOP_HOME}/share/hadoop/mapreduce/*:${HADOOP_HOME}/share/hadoop/mapreduce/lib/*</value>
        </property>
    </configuration>
    ```
8. 修改`etc/hadoop/yarn-site.xml`，配置YARN：  
    ```xml
    <configuration>
        <property>
                <name>yarn.nodemanager.aux-services</name>
                <value>mapreduce_shuffle</value>
        </property>
        <property>
                <name>yarn.resourcemanager.hostname</name>
                <value>the-hostname</value>
        </property>
    </configuration>
    ```
9. 启动 YARN：  
    ```bash
    $ sbin/start-yarn.sh
    ```
    现在应当可以从`http://localhost:8088/`访问ResourceManager的Web端。
10. 启动历史记录服务：
    ```bash
    $ bin/mapred --daemon start historyserver
    ```
## 使用MapReduce
1. 查看正在运行的服务：
    ```bash
    $ jps
    ```
    应当显示：  
    ```bash
    3296 Jps
    3201 JobHistoryServer
    29556 NameNode
    31428 NodeManager
    30148 SecondaryNameNode
    29782 DataNode
    30984 ResourceManager
    ```
3. 准备输入输出文件夹：
    ```bash
    $ bin/hdfs dfs -mkdir -p /work/wordcount/input
    $ bin/hdfs dfs -mkdir -p /work/wordcount/output
    ```
2. 准备输入文件：
    从古腾堡计划下载《爱丽丝漫游仙境》的纯文本文档，清除头尾说明、数字和标点符号，并通过SCP传入服务器：
    ```bash
    $ scp 11-0.txt user@hostname:~/Downloads
    ```
    将文件放入HDFS：
    ```bash
    $ bin/hdfs dfs -put ~/Downloads/cleaned-11-0.txt /work/wordcount/input
    ```
3. 运行单词统计程序：
    ```bash
    $ bin/yarn jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.2.0.jar wordcount /work/wordcount/input /work/wordcount/output
    ```
4. 检查结果文件：
    ```bash
    $ bin/hdfs dfs -ls /work/wordcount/output
    ```
    应当有用于标志成功的`_SUCCESS`和储存输出结果的`part-r-00000`文件。
5. 获取并查看输出文件
    ```bash
    $ bin/hdfs dfs -get /work/wordcount/output/part-r-00000 ${HADOOP_HOME}/
    $ vim part-r-00000
    ```

## MapReduce开发
