# 笔记 - mongoDB
## mongoDB 安装
本文将描述于 Ubuntu 18.04 LTS (bionic) 安装 mongoDB。另外由于众所周知的原因，从国内镜像下载往往更快，本文将从[清华大学开源软件镜像站](https://mirror.tuna.tsinghua.edu.cn/help/mongodb/)下载安装。
1. 设置公钥和源  
   信任 GPG 公钥：  
   ```bash
   $ sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927
   ```
   创建并写入 list 文件：  
   ```bash
   $ echo "deb [ arch=amd64 ] https://mirrors.tuna.tsinghua.edu.cn/mongodb/apt/ubuntu bionic/mongodb-org/4.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-4.0.list
   ```
2. 安装 mongoDB
   ```bash
   $ sudo apt-get update
   $ sudo apt-get install -y mongodb-org
   ```
## 启动 mongoDB
1. 创建相关目录：
    数据库目录：  
    ```bash
    $ sudo mkdir -p /data/db
    $ sudo chown -R <user>:<group> /data/db
    ```
    日志目录：  
    ```bash
    $ sudo mkdir /opt/software/mongodb
    $ sudo chown -R <user>:<group> /opt/software/mongodb
    ```
2. 安装 `numactl`
    如果服务器为[非统一内存访问架构（NUMA）](https://zh.wikipedia.org/wiki/非均匀访存模型)，那么需要安装 `numactl` 以防止其导致的性能问题：  
    ```bash
    $ sudo apt-get install numactl
    ```
3. 启动 mongoDB
    如果为 NUMA，则通过 `numactl` 运行：  
    ```bash
    $ numactl --interleave=all mongod --fork --logpath /opt/software/mongodb/mongod.log --rest
    ```
    否则直接运行：  
    ```bash
    $ mongod --fork --logpath /opt/software/mongodb/mongod.log --rest
    ```
    可以通过 `localhost:28017` 访问其 Web 页面。
