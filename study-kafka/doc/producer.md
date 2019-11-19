# kafka 生产者客户端的知识

[kafka 官方文档](http://kafka.apachecn.org/documentation.html)

生产者客户端是线程安全的。

## 发送消息的方式

kafka 消息发送有同步（sync）、异步（async）两种，以及三种消息确认方式。

> 同步方式，直接将请求发送到分区，不会加入缓冲区。但是新版本的 kafka-producer 客户端，已经基本移除了这个配置。同步通过 future.get 实现。

kafka 的客户端，异步发送时，会将消息 producerRecord 先放入到缓存与队列中，缓存到某个数量或者占用内存大小才会发送，所以不需要特别进行批量处理，性能是有保证的。
这些 kafka 客户端已经帮助封装好了。

发送的时候如果不指定分区，或者未设置 key，将会放到 topic 下随机的分区下。如果设置了 key，将会把 key 进行 hash 后，按规则放到某个分区下。
所以如果分区数不变的话，消息进入的分区是不变的。建议如果想放到某个固定分区的话，设置自定义的分区器。

## 提高吞吐率

为了提高吞吐率，可以提高 broker 和 topic 的数量。

如果不追求消息百分百投递成功，可以设置 `producer.acks=0 或 1`。

> 吞吐率与消息的完整率是互斥的，提高吞吐率的同时，必然会导致消息丢失可能性的增加。

## 百分百保证准确投递

### 服务端

- kafka服务端`unclean.leader.election.enable=false`。关闭unclean leader选举，即不允许非ISR中的副本被选举为leader，以避免数据丢失

### topic

- `replication.factor`：这个值必须大于 1，要求每个 partition 必须有至少 2 个副本。
- `min.insync.replicas`：这个值必须大于 1，
这个是要求一个 leader 至少感知到有至少一个 follower 还跟自己保持联系，没掉队，这样才能确保 leader 挂了还有一个 follower。
- `replication.factor > min.insync.replicas` 如果两者相等，当一个副本挂掉了分区也就没法正常工作了。通常设置 `replication.factor = min.insync.replicas + 1` 即可

### producer

- `producer.acks=-1 或 all`。表示一定得分区的全部 replicas 都同步完成后，才确认记录。
- `max.in.flight.requests.per.connection=1`。限制客户端在单个连接上能够发送的未响应请求的个数。设置此值是1表示kafka broker在响应请求之前client不能再向同一个broker发送请求。注意：设置此参数是为了避免消息乱序
- 了解每条写入数据的大小，避免数据过大超过限制。
- `block.on.buffer.full = true`。异步发送消息，如果 ack 一直没有或者数据过多，导致缓冲池满了，会丢失消息。可使用同步或者当缓冲区满了后设置阻塞。
该字段，新版文档已不在，可用 `max.block.ms=Long.MAX_VALUE` 替代。
- `retries`，重试次数增大。

## 异步触发发送

- batch-size 满了
- linger.ms 时间限制

## spring-kafka

- 如果未设置 topic 的分区等信息，默认自动通知 kafka 服务端，生成一个 1 分区，1 副本的 topic；