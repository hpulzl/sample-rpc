# sample-parent
## 包名说明
* **sample-common 公共模块** util和公用方法
* **sample-rpc 远程通信模块** 抽象各种协议，以及动态代理，只包含一对一的调用，不关心集群的管理。
* **sample-cluster 集群模块** 将多个服务提供者伪装成一个，包括负载均衡，容错，路由等
* **sample-registry 注册中心模块** 基于注册中心下发地址的集群方式，以及对各种注册中心的抽象
* **sample-config 配置模块** 对外API，用户通过配置文件使用rpc调用，隐藏内部实现细节
* **sample-container 容器模块：** 是一个 Standlone 的容器，以简单的 Main 加载 Spring 启动，因为服务通常不需要 Tomcat/JBoss 等 Web 容器的特性，没必要用 Web 容器去加载服务
* **sample-serialization 序列化模块：** 提供不同序列化方式
## 从零开始写RPC
**目标：** 通过重复造轮子的方式，了解dubbo的整个设计理念。重点了解以下细节：
1. 基于rmi的远程调用 ok
2. 注册中心的逻辑
3. dubbo和spring整合
4. 自定义远程调用的实现，包括序列化和反序列化协议等
### 方法提供者
1. 向注册中心注册接口信息（IP+端口+接口名）
### 方法调用者
2. 订阅注册中心感兴趣的接口
### 服务注册
1. RegistryService类定义服务注册和订阅的方法
### 动态代理
1. ProxyFactory定义获取代理对象，发起调用的方法
### 远程调用
1. 实现方式有很多种比如：rmi、netty、http等

## 通过RMI的方式实现一个rpc

## 通过zk实现服务注册

## 实现SPI机制
## 使用spring-hessian实现远程调用
1. 实现spring-hessian的远程调用方法
1. 实现spring文件配置化



