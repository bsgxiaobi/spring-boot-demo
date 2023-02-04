# spring-boot-demo
spring-boot下各种中间件、功能实现的demo

# 各Module介绍
## demo-mongodb
mongodb增删改查

## demo-redis
redis相关demo
### 1 redis消息队列
#### publish/subscribe 操作
参考文档：
1. [redis消息队列-redis整合SpringBoot实现消息队列_诸般世界的博客-CSDN博客](https://blog.csdn.net/zxylwj/article/details/94648433)
2. [redis消息队列-springboot redis 与发布订阅 - zhy_learn - 博客园](https://www.cnblogs.com/2020-zhy-jzoj/p/13165523.html)
3. [redis消息队列-Redis6.0系列(8)-Redis发布订阅及Spring Boot集成Redis实现发布订阅消息_云烟成雨TD的博客-CSDN博客](https://blog.csdn.net/qq_43437874/article/details/116701245)

Redis提供了发布订阅功能，可以用于消息的传输，Redis的发布订阅机制包括三个部分：发布者（Publisher），订阅者（Subscriber）和频道（Channel）
Redis发布订阅 提供了简单的消息队列功能，但实际应用较少，因为其模式单一、消息丢失性高，但其简单易用，常用于日志等不重要的数据传输
1. 消息无法持久化，存在丢失风险；
2. 没有类似 RabbitMQ的ACK机制；
3. 由于是广播机制，无法通过添加worker 提升消费能力

RedisTemplate类用于消息生成。对于类似于Java EE的消息驱动bean样式的异步接收，Spring Data提供了一个专用的消息侦听器容器，
该容器用于创建消息驱动的POJO（MDP），并用于同步接收RedisConnection

在接收方，可以通过直接命名一个频道或多个频道或使用模式匹配来订阅一个或多个频道
由于其阻塞性质，低级订阅并不吸引人，因为它需要每个侦听器都进行连接和线程管理。为了减轻这个问题，
Spring Data提供了RedisMessageListenerContainer，它可以完成所有繁重的工作。

RedisMessageListenerContainer充当消息侦听器容器。它用于接收来自Redis通道的消息并驱动MessageListener注入到该通道中的实例。
侦听器容器负责消息接收的所有线程，并分派到侦听器中进行处理。消息侦听器容器是MDP与消息传递提供程序之间的中介，并负责注册接收消息，资源获取和释放，异常转换等

#### stream

## demo-qps
接口qps统计