# 推送服务

#### 概述

* ##### 消息推送
  在移动端，如果希望实现向某些终端发送消息，通常做法有两种,**定时轮询**或者**推送服务**。
  
* ##### 定时轮询
  **定时轮询** 的核心思路是通过周期任务定时向服务器pull数据。这种方式优点在于实现简单，缺点是时效性较差，资源消耗较大,在iOS平台还可能受到平台限制（苹果也倾向于引导使用APNS而不是轮询）。如果要做就需要根据网络环境、成功率动态改变周期。

* ##### 推送服务
**推送服务** 的核心思路是App与Server之间通过TCP长连接建立通信管道。推送服务会分配给设备一个deviceId,通过deviceId可以定向推送服务，但是这种方式会需要维护clientId与应用帐号之间的关系。除此之外推送还支持订阅-发布模式，通过这种模式可以完成推送。推送服务优点在于实时性高。


* ##### iOS推送

  **iOS App** 如果希望在app store中发布，那么苹果会要求必须使用APNs服务。iOS的推送是由苹果内置的服务统一管理分发的，这个好处就在于所有应用可以**共享推送服务**,能量消耗更少，这也就是为什么苹果的推送达到率又高，耗能又低的原因。消息所属App 如果


* ##### Android推送
  **针对** Android App，Google提供了类似APNs的C2DM、[GCM](https://developers.google.com/cloud-messaging/)的推送服务。GCM竟然还兼容对iOS的APNs的兼容。但是由于某些原因，在国内无法很好的使用，所以就导致所有的应用需要自己去实现自己的推送Push,没有了**共享推送服务**，导致耗能一下子就提升了。

   >**PS:** 目前有一些手机厂商也在做自己的共享推送服务来替代GCM，比如小米、华为，一定程度上可以减少耗能，又可以一定程度上保证推送服务的稳定，各大国内推送平台一般都针对进行兼容。

---
#### 场景分析
 第三方推送服务会提供通知、消息两种推送方式，可以理解通知推送格式化消息推送。一般的APP大致的推送场景如下：
 * ##### ~~按 DeviceId 推送~~
 
  **DeviceId** 是推送服务分配给设备的id标识, 推送服务根据deviceId来管理推送连接。这种推送方式需要维护设备与应用帐号的关系,通常用户推送服务调试。
 * ##### 按帐号(别名)推送
 
  **帐号(别名)** 推送通常用来进行定向推送。一个设备同一时间只能对应一个帐号（别名），而一个帐号（别名）同时可以对应多个设备。
 
 * ##### 按照标签推送
 
 **标签** 推送用来对设备、帐号、别名进行归类。通常用于批量下发消息。
 
--- 
 
#### 消息类型

根据消息的不同使用方式，可以分为以下几类：
   1. ##### 提醒消息
    
    提醒消息是最常用的推送消息，他的标准动作是创建一个Notification或者一个Dialog,引导用户进入相关页面。提醒消息是按照固定协议实现在Native内部的。
   2. ##### URI消息
   
    URI消息是动态发布消息的手段，他的标准动作是创建一个Notification或者一个Dialog,引导远程访问网站或者下载网络资源。
   3. ##### 消息命令
   
   消息命令是一种格式化的消息，通过对消息的解析，完成一些命令，和前面两种消息的区别在于，消息命令通常是用户不可知的,除了android客户端单次定位场景以外，更多的是进行远程运维。在平台实现方面,Android更适合这种消息命令，iOS由于机制的问题，无法保证实时性，要区分平台使用。
 
---

####移动端框架设计
#####设计方向：
1. 构建的推送服务与具体推送服务SDK无关(可以在多个服务提供商之间任意切换)
>这么设计一方面是这样的设计更加符合面向高层、抽象设计的原则；
>另一方面，为不同的设备提供不同推送服务的提供可能性（具体请参考概述-Android推送）
2. 使用消息队列处理推送消息(Android)
3. 消息解析器设计灵活（如增加消息本地持久化逻辑）、易扩展、职责单一
4. 设计要适用于iOS、Android双平台

##### 框架及流转图：


![Push Message Flow](http://doulala.oss-cn-qingdao.aliyuncs.com/image/PushMessageFlow.jpg)
  1. **PushInterface**, 由 IPusher 和 MessageQueue 组成,通过PushInterfaceBuilder创建。是应用开发人员操作Push服务的统一入口。
  2. **IPusher**, 定义的第三方推送服务接口,第三方推送平台按照接口实现，即可加入该平台推送能力。
  3. **Dispatcher**,用来完成对推送消息的第一次过滤、封装,并添加到消息队列。比如可以在这里加入消息持久化逻辑。
  4. **MessageQueue**,维护一个消息队列,管理消息生命周期,消息处理交给PushMessageHandlerBus。
  5. **MessageHandler**,是用户自定义的Handler,每个handler可以仅处理一种消息,并实现独立的逻辑，结构也会比较清晰。
  6. **DefaultMessageHandler**， 是MessageHandler的实现，他是最后统一处理未匹配消息的地方，一个MessageQueue可以有最多一个DefaultMessageHandler。

##### 创建 PushInterface

```java
 private void initPush() {
        pushInterface = PushBuilder.builder()
                .pusher(new JPusher(CtsiApplication.this))
                .defaultMessageHandler(null)
                .addMessageHandler(messageHandlerA)
                .callback(messageCallback)
                .callback(registerCallback)
                .callback(connectionCallback)
                .build();

        pushInterface.start();
    }
```


---
#### 服务器端消息协议设计

通过PushMessageBuilder建立通用的Message：
```java
public class PushMessageBuilder{

   public  static PushServiceBuilder builder();

   public static PushServiceBuilder alias(String alias);//设置别名

   public static PushServiceBuilder tags(Set<String>tags);//设置标签

   public static PushServiceBuilder platform(PlatForm platform);//设置平台
   
   public PushServiceBuilder noticeMessage(NoticeMessage message);//通知消息
 
   public static PushServiceBuilder urlMessage(UrlMessage message);//Url消息
   
   public static PushServiceBuilder androidCommandMessage(AndroidCommandMessage message);//消息命令
  
   public PushMessage build();
}

```
通过不同推送服务可以实现PushService接口,用来发送PushMessage:

```java
public interface PushService{
   void sendMessage(PushMessage message);
   ...
} 
```
一共设计了3种推送消息：

1. **NoticeMessage**.用来推送自定义消息：
```java
public class NoticeMessage{
   //TO-DO
} 
```
2. **UrlMessage**.用来推送Url页面：
```java
public class UrlMessage{
   //TO-DO
} 
```
3. **AndroidCommandMessage**.主要用来针对Android的命令消息进行推送,如单次定位
```java
public class AndroidCommandMessage{
   //TO-DO
} 
```
