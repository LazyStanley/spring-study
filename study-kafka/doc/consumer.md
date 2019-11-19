# kafka 消费者客户端的知识

[kafka 官方文档](http://kafka.apachecn.org/documentation.html)

消费者客户端不是线程安全的。

## 消费注意事项

消费者是定时拉取 kafka 数据的。根据 `max.poll.records` 设置每次拉取的数量。

`max.poll.interval.ms` 是两次 poll 之间的最大时间间隔。如果 poll 后，业务逻辑处理的时间过长，大于 `max.poll.interval.ms`，
会导致 kafka 服务端认为这个消费者异常，排除了这个消费者。这样会导致消费者的 offset 提交失败，然后导致重复消费。
所以处理消息的业务逻辑得估算好大致的消费时间。

## 提交 offset

提交分为自动提交与手动提交，手动提交又分为同步与异步。

### 自动提交

- `enable.auto.commit` 决定是否自动提交；
- `auto.commit.interval.ms` 决定自动提交之间的时间间隔。

offset 会先记录在 listener 的线程中，当 `auto.commit.interval.ms` 到了后，再提交到 kafka 的服务端。

### 手动提交

- 同步提交会阻塞；
- 异步提交需要自己记录失败的记录并处理。

## 消费者数量

消费者的数量不能大于分区，因为一个分区，同时只能与一个消费者链接。
spring-kafka 支持设置多线程，形成多个消费者。

## 不丢失或重复消息

- 手动 commit；
- 建立去重表。


