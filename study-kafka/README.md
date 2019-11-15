# kafka 安装

安装流程见下，生产者和消费者使用的注意事项见跳转。

- [producer](doc/producer.md)
- [consumer](doc/consumer.md)

## 安装 jdk 环境

通过 yum 安装

### 安装流程

```shell script
# 检查是否已经安装
yum list installed | grep java

# 卸载旧版本
yum -y remove java-1.8.0-openjdk*

# 查看可安装的 jdk
yum -y list java*

# 下载安装
yum install java-11-openjdk.x86_64

# 验证是否安装成功
java -version
```

### 设置环境变量

#### 全体用户

```shell script
vi /etc/profile

#set java environment  
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-11.0.5.10-0.el7_7.x86_64

export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/jre/lib/rt.jar

export PATH=$PATH:$JAVA_HOME/bin

:wq

# 使配置生效
. /etc/profile
```

#### 局部用户

```shell script
vi ~/.bashrc

export  JAVA_HOME=/usr/lib/jvm/java-11-openjdk-11.0.5.10-0.el7_7.x86_64

:wq
```

#### 验证

```shell script
echo $JAVA_HOME
```

## 安装 kafka

以下都是官网抄录，建议直接看官网。部署在阿里云，然后外网访问的话，需要针对 kafka 的 server.properties 进行设置。

```shell script
advertised.listeners=PLAINTEXT://阿里云外网映射地址:9092    # 注册到zookeeper的地址和端口
```

参考 [官方文档](http://kafka.apache.org/quickstart)

### 下载

```shell script
wget http://mirrors.tuna.tsinghua.edu.cn/apache/kafka/2.3.0/kafka_2.12-2.3.0.tgz

tar -xzf kafka_2.12-2.3.0.tgz

cd kafka_2.12-2.3.0
```

### 启动

```shell script
bin/zookeeper-server-start.sh config/zookeeper.properties

bin/kafka-server-start.sh config/server.properties
```

### 创建 topic

```shell script
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test

bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

### 发送一些消息

```shell script
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test

> message a
> message b
```

### 创建消费者接收消息

```shell script
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
```
