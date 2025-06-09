# 苍穹外卖项目

<!-- TOC -->
* [苍穹外卖项目](#苍穹外卖项目)
  * [项目介绍](#项目介绍)
  * [业务流程](#业务流程)
  * [技术选型](#技术选型)
    * [技术架构分层](#技术架构分层)
    * [前端技术](#前端技术)
    * [后端技术](#后端技术)
    * [数据库](#数据库)
    * [部署](#部署)
  * [项目架构](#项目架构)
    * [1. sky-common](#1-sky-common)
    * [2. sky-pojo](#2-sky-pojo)
    * [3. sky-server](#3-sky-server)
    * [4. sql](#4-sql)
    * [5. Web](#5-web)
    * [6. WeiXin-sky](#6-weixin-sky)
  * [接口文档](#接口文档)
  * [项目心得](#项目心得)
    * [1. 常量集中管理](#1-常量集中管理)
    * [2. 线程安全的用户上下文](#2-线程安全的用户上下文)
    * [3. 自定义JSON序列化](#3-自定义json序列化)
    * [4. 配置属性对象化](#4-配置属性对象化)
    * [5. 统一返回结构和分页封装](#5-统一返回结构和分页封装)
    * [6. 常用工具类封装](#6-常用工具类封装)
    * [7. DTO/Entity/VO分层清楚](#7-dtoentityvo分层清楚)
    * [8. 自动填充公共字段](#8-自动填充公共字段)
    * [9. 配置类分层三方依赖解耦](#9-配置类分层三方依赖解耦)
    * [10. Web层配置和拦截器注册](#10-web层配置和拦截器注册)
    * [11. 控制器层职责单一接口注释规范](#11-控制器层职责单一接口注释规范)
    * [12. 全局异常处理](#12-全局异常处理)
    * [13. 请求拦截器](#13-请求拦截器)
    * [14. mapper层](#14-mapper层)
    * [15. service层](#15-service层)
    * [16. 定时任务](#16-定时任务)
    * [17. WebSocket即时通讯服务](#17-websocket即时通讯服务)
    * [18. Spring Boot主类](#18-spring-boot主类)
<!-- TOC -->

## 项目介绍

苍穹外卖是一套面向中小型餐饮门店的外卖业务系统，采用前后端分离架构，支持微信小程序、PC管理端等多端接入。系统涵盖了从用户下单、商家接单、配送、支付到数据统计的完整业务流程，适用于线上点餐、外卖配送等场景。

系统分为管理端和用户端两大部分：

- **管理端**：用于门店员工、管理人员进行日常运营管理，包括员工管理、菜品管理、套餐管理、订单管理、数据统计、来单提醒等。
- **用户端**：面向消费者，支持微信小程序下单、商品浏览、购物车、历史订单、地址管理、微信支付、催单等功能。

---

## 业务流程

整个点餐系统主要包括：

1. 管理员可以管理员工，新增菜品、管理套餐。
2. 用户登录后，可以使用小程序进行点餐、加入购物车、下单、支付、催单。
3. 管理员可以接收来单提醒和用户的催单。
4. 管理员可以在后台统计分析、导出营业数据。

**功能模块：**

- **管理端**
    - 员工管理
    - 分类管理
    - 菜品管理
    - 套餐管理
    - 订单管理
    - 工作台
    - 数据统计
    - 来单提醒

- **用户端**
    - 微信登录
    - 商品浏览
    - 购物车
    - 用户下单
    - 微信支付
    - 历史订单
    - 地址管理
    - 用户催单

---

## 技术选型

### 技术架构分层

- **用户层**：node.js、Vue.js、Element UI、微信小程序、Apache Echarts
- **网关层**：Nginx
- **应用层**：Spring Boot、Spring MVC、Spring Task、HttpClient、Spring Cache、JWT、阿里云OSS、Swagger、POI、WebSocket
- **数据层**：MySQL、Redis、MyBatis、PageHelper、Spring Data Redis
- **工具**：Git、Maven、Junit、Postman、Apifox

### 前端技术

- Vue.js
- Axios 请求库
- Element UI
- Apache Echarts
- 微信小程序开发

### 后端技术

- Spring Boot + SSM
- MyBatis Plus
- Spring Cache + Redis 缓存
- Spring Task 定时任务
- JWT 认证
- WebSocket 实时通信
- POI 表格操作
- 阿里云 OSS 文件存储
- Swagger 接口文档
- Sharding JDBC 读写分离（如有）

### 数据库

- MySQL：关系型数据库，存储业务数据

### 部署

- 前后端分离部署，Nginx 作为网关

---

## 项目架构

项目采用前后端分离的架构，主要分为以下几个模块：

### 1. sky-common

公共模块，包含项目中公用的工具类、常量、异常处理等。

#### 主要包结构：

- `constant`: 常量类
    - `JwtClaimsConstant`: JWT相关常量，定义JWT令牌中的字段名
    - `MessageConstant`: 消息常量，定义系统提示信息
    - `StatusConstant`: 状态常量，定义业务状态码
    - `AutoFillConstant`: 自动填充相关常量，定义公共字段名
    - `PasswordConstant`: 密码相关常量

- `context`: 上下文类
    - `BaseContext`: 基于ThreadLocal的上下文管理类，用于存储当前线程的用户信息，实现用户信息的线程隔离

- `enumeration`: 枚举类
    - `OperationType`: 操作类型枚举（INSERT/UPDATE），用于标识数据库操作类型

- `exception`: 自定义异常类
    - `BaseException`: 基础异常类，所有自定义异常的父类
    - `BusinessException`: 业务异常类，处理业务逻辑异常
    - `LoginFailedException`: 登录失败异常，处理登录相关异常
    - `OrderBusinessException`: 订单相关异常，处理订单业务异常
    - `AccountLockedException`: 账号被锁定异常
    - `AccountNotFoundException`: 账号未找到异常
    - `AddressBookBusinessException`: 地址簿相关业务异常
    - `DeletionNotAllowedException`: 删除操作不允许异常
    - `PasswordEditFailedException`: 密码修改失败异常
    - `PasswordErrorException`: 密码错误异常
    - `SetmealEnableFailedException`: 套餐启用失败异常
    - `ShoppingCartBusinessException`: 购物车相关业务异常
    - `UserNotLoginException`: 用户未登录异常

- `json`: JSON处理工具类
    - `JacksonObjectMapper`: 自定义Jackson对象映射器，处理日期时间格式化和特殊字符转义

- `properties`: 属性配置类
    - `JwtProperties`: JWT配置属性，管理JWT相关配置
    - `AliOssProperties`: 阿里云OSS配置属性，管理文件上传相关配置
    - `WeChatProperties`: 微信相关配置属性

- `result`: 统一返回结果封装类
    - `Result`: 统一返回结果类，封装接口返回数据
    - `PageResult`: 分页查询结果类，封装分页数据

- `utils`: 工具类
    - `JwtUtil`: JWT工具类，处理令牌的生成和解析
    - `AliOssUtil`: 阿里云OSS工具类，处理文件上传
    - `HttpClientUtil`: HTTP客户端工具类，处理HTTP请求
    - `WeChatPayUtil`: 微信支付工具类，处理支付相关功能

### 2. sky-pojo

实体类模块，包含项目中使用的数据传输对象（DTO）、实体类（Entity）和视图对象（VO）。

#### 主要包结构：

- `dto`：数据传输对象（Data Transfer Object，DTO），用于接收前端请求参数，封装业务操作所需数据。每个DTO通常对应一次业务操作（如新增、修改、登录、下单等）。
    - `CategoryDTO`：新增/修改菜品分类时使用的数据对象。
    - `CategoryPageQueryDTO`：分页查询菜品分类时的查询参数。
    - `DataOverViewQueryDTO`：数据总览统计查询参数。
    - `DishDTO`：新增/修改菜品时使用的数据对象。
    - `DishPageQueryDTO`：分页查询菜品时的查询参数。
    - `EmployeeDTO`：新增/修改员工时使用的数据对象。
    - `EmployeeLoginDTO`：员工登录时使用的数据对象。
    - `EmployeePageQueryDTO`：分页查询员工时的查询参数。
    - `GoodsSalesDTO`：商品销量统计查询参数。
    - `OrdersCancelDTO`：订单取消操作的数据对象。
    - `OrdersConfirmDTO`：订单确认操作的数据对象。
    - `OrdersDTO`：订单相关操作的通用数据对象。
    - `OrdersPageQueryDTO`：分页查询订单时的查询参数。
    - `OrdersPaymentDTO`：订单支付操作的数据对象。
    - `OrdersRejectionDTO`：订单拒绝操作的数据对象。
    - `OrdersSubmitDTO`：用户提交订单时的数据对象。
    - `PasswordEditDTO`：修改密码时的数据对象。
    - `SetmealDTO`：新增/修改套餐时使用的数据对象。
    - `SetmealPageQueryDTO`：分页查询套餐时的查询参数。
    - `ShoppingCartDTO`：购物车相关操作的数据对象。
    - `UserLoginDTO`：用户登录时使用的数据对象。

- `entity`：实体类（Entity），与数据库表结构一一对应，反映数据库中的数据结构。用于ORM框架（如MyBatis）进行数据持久化。
    - `AddressBook`：收货地址表实体。
    - `Category`：菜品分类表实体。
    - `Dish`：菜品表实体。
    - `DishFlavor`：菜品口味表实体。
    - `Employee`：员工表实体。
    - `OrderDetail`：订单明细表实体。
    - `Orders`：订单表实体。
    - `Setmeal`：套餐表实体。
    - `SetmealDish`：套餐与菜品关系表实体。
    - `ShoppingCart`：购物车表实体。
    - `User`：用户表实体。

- `vo`：视图对象（View Object，VO），用于返回给前端的数据封装，通常是接口返回的数据结构。VO可以包含多个实体类的数据组合，也可以只包含部分字段。
    - `BusinessDataVO`：经营数据统计视图对象。
    - `DishItemVO`：套餐内菜品项视图对象。
    - `DishOverViewVO`：菜品总览统计视图对象。
    - `DishVO`：菜品详情视图对象。
    - `EmployeeLoginVO`：员工登录返回视图对象。
    - `OrderOverViewVO`：订单总览统计视图对象。
    - `OrderPaymentVO`：订单支付返回视图对象。
    - `OrderReportVO`：订单统计报表视图对象。
    - `OrderStatisticsVO`：订单状态统计视图对象。
    - `OrderSubmitVO`：下单成功返回视图对象。
    - `OrderVO`：订单详情视图对象。
    - `SalesTop10ReportVO`：销量TOP10统计视图对象。
    - `SetmealOverViewVO`：套餐总览统计视图对象。
    - `SetmealVO`：套餐详情视图对象。
    - `TurnoverReportVO`：营业额统计视图对象。
    - `UserLoginVO`：用户登录返回视图对象。
    - `UserReportVO`：用户统计报表视图对象。

### 3. sky-server

核心业务模块，包含项目的服务层、控制层和配置文件等。

#### 主要包结构：

- `annotation`: 自定义注解类
    - `AutoFill`: 用于标记Mapper层方法，指示该方法需要自动填充如创建人、创建时间等公共字段，配合切面实现自动赋值。

- `aspect`: 切面类
    - `AutoFillAspect`: AOP切面，拦截带有`@AutoFill`注解的方法，在插入/更新数据库前自动填充公共字段，提升代码复用性和一致性。

- `config`: 配置类
    - `OssConfiguration`: 读取阿里云OSS配置，创建`AliOssUtil` Bean，实现文件上传等功能的依赖注入。
    - `RedisConfiguration`: 自定义RedisTemplate的序列化器，优化Redis操作，提升缓存读写效率。
    - `WebMvcConfiguration`: 注册JWT拦截器、静态资源映射、消息转换器、Swagger/Knife4j接口文档等，统一管理Web层相关配置。
    - `WebSocketConfiguration`: 注册WebSocket所需的`ServerEndpointExporter` Bean，支持WebSocket服务端点。

- `handler`: 全局异常处理类
    - `GlobalExceptionHandler`: 统一处理项目中的业务异常、SQL异常等，避免异常信息直接暴露给前端，返回统一的错误响应结构。

- `interceptor`: 拦截器类
    - `JwtTokenAdminInterceptor`: 管理端JWT令牌校验拦截器，校验管理员身份，保障接口安全。
    - `JwtTokenUserInterceptor`: 用户端JWT令牌校验拦截器，校验用户登录状态，保障用户接口安全。

- `mapper`：数据访问层，MyBatis的Mapper接口及其XML映射文件。
  - `AddressBookMapper`：收货地址数据库操作
  - `CategoryMapper`：菜品分类数据库操作
  - `DishFlavorMapper`：菜品口味数据库操作
  - `DishMapper`：菜品数据库操作
  - `EmployeeMapper`：员工数据库操作
  - `OrderDetailMapper`：订单明细数据库操作
  - `OrderMapper`：订单数据库操作
  - `SetmealDishMapper`：套餐与菜品关系数据库操作
  - `SetmealMapper`：套餐数据库操作
  - `ShoppingCartMapper`：购物车数据库操作
  - `UserMapper`：用户数据库操作
  
- `service`：服务层，包含业务逻辑接口及其实现类（impl）。
    - `AddressBookService`：收货地址相关业务逻辑
    - `CategoryService`：菜品分类相关业务逻辑
    - `DishService`：菜品相关业务逻辑
    - `EmployeeService`：员工相关业务逻辑
    - `OrderService`：订单相关业务逻辑
    - `ReportService`：数据统计相关业务逻辑
    - `SetmealService`：套餐相关业务逻辑
    - `ShoppingCartService`：购物车相关业务逻辑
    - `UserService`：用户相关业务逻辑

- `task`: 定时任务类
    - `OrderTask`: 订单相关定时任务，定时处理超时未支付订单、派送中超时订单等，提升系统自动化和健壮性。

- `websocket`: WebSocket服务类
    - `WebSocketServer`: WebSocket服务端，管理客户端连接、消息收发、群发等，实现来单提醒、催单等实时通信功能。

- `SkyApplication`: Spring Boot应用主类
    - `SkyApplication`: 项目全局入口，包含main方法，负责项目启动，并通过注解开启事务、缓存、定时任务等Spring特性。


### 4. sql

包含了项目的数据库设计和SQL脚本。
- `sky.sql`：包含了项目的数据库设计。
- `数据库设计文档.md`：包含了项目的数据库设计文档。

### 5. Web

包含了项目的Web配置和前端静态文件。
- `nginx-1.20.2`：包含了Nginx的配置文件和前端静态文件。

### 6. WeiXin-sky

包含了项目的微信小程序配置。
- `mp-weixin`：包含了苍穹外卖的用户端的配置文件和启动界面。


## 接口文档

项目使用Swagger/Knife4j生成接口文档，启动项目后访问：

- 商家端接口文档：http://localhost:8080/doc.html
- 用户端接口文档：http://localhost:8080/doc.html


## 项目心得

### 1. 常量集中管理
- 项目里所有用到的常量都集中写在常量类里，比如默认密码就写在`PasswordConstant`，这样以后要改只用动一个地方。
   
   ```java
   /**
    * 密码常量
    */
   public class PasswordConstant {
       public static final String DEFAULT_PASSWORD = "123456";
   }
   ```

### 2. 线程安全的用户上下文
- 用`BaseContext`配合`ThreadLocal`，每个请求线程都能独立存自己的用户id，互不干扰，保证并发安全。
   
   ```java
   public class BaseContext {
       public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
       public static void setCurrentId(Long id) { threadLocal.set(id); }
       public static Long getCurrentId() { return threadLocal.get(); }
       public static void removeCurrentId() { threadLocal.remove(); }
   }
   ```

### 3. 自定义JSON序列化
- 自己写了`JacksonObjectMapper`，统一时间格式，前后端不用担心时间解析出错。
   
   ```java
   public class JacksonObjectMapper extends ObjectMapper {
       public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
       //public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
       public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
       public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

       public JacksonObjectMapper() {
           super();
           //收到未知属性时不报异常
           this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
           //反序列化时，属性不存在的兼容处理
           this.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
           SimpleModule simpleModule = new SimpleModule()
                   .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                   .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                   .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
                   .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                   .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                   .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
           //注册功能模块 例如，可以添加自定义序列化器和反序列化器
           this.registerModule(simpleModule);
       }
   }
   ```

### 4. 配置属性对象化
- 用`@ConfigurationProperties`把yml里的配置直接映射成Java对象，代码里用起来很方便。
   
   ```java
   @Component
   @ConfigurationProperties(prefix = "sky.wechat")
   @Data
   public class WeChatProperties {
       private String appid; //小程序的appid
       private String secret; //小程序的秘钥
       private String mchid; //商户号
       private String mchSerialNo; //商户API证书的证书序列号
       private String privateKeyFilePath; //商户私钥文件
       private String apiV3Key; //证书解密的密钥
       private String weChatPayCertFilePath; //平台证书
       private String notifyUrl; //支付成功的回调地址
       private String refundNotifyUrl; //退款成功的回调地址
   }
   ```

### 5. 统一返回结构和分页封装
- 所有接口都用`Result`和`PageResult`统一返回，前后端交互很清晰。
   
   ```java
   /**
    * 后端统一返回结果
    * @param <T>
    */
   @Data
   public class Result<T> implements Serializable {
       private Integer code; //编码：1成功，0和其它数字为失败
       private String msg; //错误信息
       private T data; //数据
       public static <T> Result<T> success() {
           Result<T> result = new Result<T>();
           result.code = 1;
           return result;
       }
       ...
   }
   /**
    * 封装分页查询结果
    */
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class PageResult implements Serializable {
       private long total; //总记录数
       private List records; //当前页数据集合
   }
   ```

### 6. 常用工具类封装
- 定义各种公用的工具类，提高编写代码的效率。
- 比如`HttpClientUtil`，定义了发送 get、post 请求的方法，把http请求常用方法都封装好了，直接用。

   ```java
   public class HttpClientUtil {
       static final  int TIMEOUT_MSEC = 5 * 1000;
       /**
        * 发送GET方式请求
        * @param url
        * @param paramMap
        * @return
        */
       public static String doGet(String url,Map<String,String> paramMap){
           // 创建Httpclient对象
           CloseableHttpClient httpClient = HttpClients.createDefault();
           String result = "";
           CloseableHttpResponse response = null;
           try{
               URIBuilder builder = new URIBuilder(url);
               if(paramMap != null){
                   for (String key : paramMap.keySet()) {
                       builder.addParameter(key,paramMap.get(key));
                   }
               }
               URI uri = builder.build();
               //创建GET请求
               HttpGet httpGet = new HttpGet(uri);
               //发送请求
               response = httpClient.execute(httpGet);
               //判断响应状态
               if(response.getStatusLine().getStatusCode() == 200){
                   result = EntityUtils.toString(response.getEntity(),"UTF-8");
               }
           }catch (Exception e){
               e.printStackTrace();
           }finally {
               try {
                   response.close();
                   httpClient.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
           return result;
       }
   }
   ```

### 7. DTO/Entity/VO分层清楚
- DTO存放用于在程序处理过程中传输数据的类。 一般来说，DTO 类比较灵活，可能是多个不同表的数据的组合、或者是一个表的部分字段，为了特定的业务逻辑而定义。 DTO 对象一般被广泛用于 Controller 和 Service 之间、Service 和 Service 之间传输数据。
- Entity存放用于接受数据库中数据的类(实体类)一般 entity 类的属性名称、类型和数据库表的字段名称、类型一致。 存放用于返回给前端信息的类。 
- vo 类是为了满足前端页面需要的数据而定制的，通常是对实体类的包装和组合。 比如菜品包装类 DishVo，不仅返回了 Dish 的菜品信息，还额外返回了菜品的口味信息 flavors。
  

### 8. 自动填充公共字段
- 用自定义注解`@AutoFill`和AOP切面`AutoFillAspect`，插入或更新数据时自动填充创建人、创建时间等字段，省了很多重复代码。
- 所谓切面，就是能够在某个方法的执行前后，增加一些额外的处理。
  本项目中定义了 AutoFilAspect 切面，对所有带有 AutoFil 注解的 mapper 方法生效，用于在操作数据库的方法执行前为方法接受的参数填充默认值。比如从上下文中获取当前登录用户id，然后填充给要插入数据的 createUser 字段，然后再插入到数据库中。

   ```java
   /**
    * 自定义切面，实现公共字段自动填充处理逻辑
    */
   @Aspect
   @Component
   @Slf4j
   public class AutoFillAspect {
   
       /**
        * 切入点（定义切面对哪些类、方法生效）
        */
       @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
       public void autoFillPointCut(){}
   
       /**
        * 前置通知，在通知中进行公共字段的赋值
        */
       @Before("autoFillPointCut()")
       public void autoFill(JoinPoint joinPoint){
           log.info("开始进行公共字段自动填充...");
   
           //获取到当前被拦截的方法上的数据库操作类型
           MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
           AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
           OperationType operationType = autoFill.value();//获得数据库操作类型
   
           //获取到当前被拦截的方法的参数--实体对象
           Object[] args = joinPoint.getArgs();
           if(args == null || args.length == 0){
               return;
           }
   
           Object entity = args[0];
   
           //准备赋值的数据
           LocalDateTime now = LocalDateTime.now();
           Long currentId = BaseContext.getCurrentId();
   
           //根据当前不同的操作类型，为对应的属性通过反射来赋值
           if(operationType == OperationType.INSERT){
               //为4个公共字段赋值
               try {
                   Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                   Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                   Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                   Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
   
                   //通过反射为对象属性赋值
                   setCreateTime.invoke(entity,now);
                   setCreateUser.invoke(entity,currentId);
                   setUpdateTime.invoke(entity,now);
                   setUpdateUser.invoke(entity,currentId);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }
   }
   ```

### 9. 配置类分层，三方依赖解耦
- config目录用于创建项目中用到的第三方依赖的配置对象。它和 common 模块的 properties 目录看起来有些相似，但作用不同。properties 的作用是用于读取和接受配置文件，把配置保存到对象中;而 config 目录中的类基本上都会使用到 @Configuration 注解，用于根据配置初始化对象 Bean。 比如 OssConfiguration 类，实现了读取配置文件并且创建一个 Ali0ssUtil 工具 Bean，可以让其他类直接引用。
- 比如`OssConfiguration`、`RedisConfiguration`，统一管理三方依赖Bean的创建和初始化，后续扩展很方便。
   
   ```java
   /**
    * 配置类，用于创建AliOssUtil对象
    */
   @Configuration
   @Slf4j
   public class OssConfiguration {
       @Bean
       @ConditionalOnMissingBean
       public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
           log.info("开始创建阿里云文件上传工具类对象：{}",aliOssProperties);
           return new AliOssUtil(aliOssProperties.getEndpoint(),
                   aliOssProperties.getAccessKeyId(),
                   aliOssProperties.getAccessKeySecret(),
                   aliOssProperties.getBucketName());
       }
   }
   ```

   - Spring Boot 项目中，经常使用带有 @Confiquration 注解的配置类来覆盖第三方依赖中提供的默认 Bean，
   比如 Redisconfiguration 类中，自定义了 RedisTemplate 对象的初始化过程、自定义了 Redis key 的序列化器。
   
   ```java
   @Configuration
   @Slf4j
   public class RedisConfiguration {
       @Bean
       public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
           log.info("开始创建redis模板对象...");
           RedisTemplate redisTemplate = new RedisTemplate();
           //设置redis的连接工厂对象
           redisTemplate.setConnectionFactory(redisConnectionFactory);
           //设置redis key的序列化器
           redisTemplate.setKeySerializer(new StringRedisSerializer());
           return redisTemplate;
       }
   }
   ```

### 10. Web层配置和拦截器注册
- `WebMvcConfiguration`统一注册了JWT拦截器、静态资源映射、消息转换器等，保证接口安全、文档可访问、数据格式统一。
    
    ```java
    /**
     * 配置类，注册web层相关组件
     */
    @Configuration
    @Slf4j
    public class WebMvcConfiguration extends WebMvcConfigurationSupport {
        @Autowired
        private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
        @Autowired
        private JwtTokenUserInterceptor jwtTokenUserInterceptor;
        /**
         * 注册自定义拦截器
         * @param registry
         */
        protected void addInterceptors(InterceptorRegistry registry) {
            log.info("开始注册自定义拦截器...");
            registry.addInterceptor(jwtTokenAdminInterceptor)
                    .addPathPatterns("/admin/**")
                    .excludePathPatterns("/admin/employee/login");
            registry.addInterceptor(jwtTokenUserInterceptor)
                    .addPathPatterns("/user/**")
                    .excludePathPatterns("/user/user/login")
                    .excludePathPatterns("/user/shop/status");
        }
        @Bean
        public Docket docket1(){
            log.info("准备生成接口文档...");
            ApiInfo apiInfo = new ApiInfoBuilder()
                    .title("苍穹外卖项目接口文档")
                    .version("2.0")
                    .description("苍穹外卖项目接口文档")
                    .build();
    
            Docket docket = new Docket(DocumentationType.SWAGGER_2)
                    .groupName("管理端接口")
                    .apiInfo(apiInfo)
                    .select()
                    //指定生成接口需要扫描的包
                    .apis(RequestHandlerSelectors.basePackage("com.sky.controller.admin"))
                    .paths(PathSelectors.any())
                    .build();
    
            return docket;
        }
    
        @Bean
        public Docket docket2(){
            log.info("准备生成接口文档...");
            ApiInfo apiInfo = new ApiInfoBuilder()
                    .title("苍穹外卖项目接口文档")
                    .version("2.0")
                    .description("苍穹外卖项目接口文档")
                    .build();
    
            Docket docket = new Docket(DocumentationType.SWAGGER_2)
                    .groupName("用户端接口")
                    .apiInfo(apiInfo)
                    .select()
                    //指定生成接口需要扫描的包
                    .apis(RequestHandlerSelectors.basePackage("com.sky.controller.user"))
                    .paths(PathSelectors.any())
                    .build();
    
            return docket;
        }
    
        /**
         * 设置静态资源映射，主要是访问接口文档（html、js、css）
         * @param registry
         */
        protected void addResourceHandlers(ResourceHandlerRegistry registry) {
            log.info("开始设置静态资源映射...");
            registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    
        /**
         * 扩展Spring MVC框架的消息转化器
         * @param converters
         */
        protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            log.info("扩展消息转换器...");
            //创建一个消息转换器对象
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            //需要为消息转换器设置一个对象转换器，对象转换器可以将Java对象序列化为json数据
            converter.setObjectMapper(new JacksonObjectMapper());
            //将自己的消息转化器加入容器中
            converters.add(0,converter);
        }
    }
    ```

### 11. 控制器层职责单一，接口注释规范
- Controller负责提供后端接口，接受前端(或其他客户端)的请求，并在业务处理完成后返回响应数据。注意，controller 层最好不要处理太复杂的逻辑，尽量保证精简清晰， 复杂的逻辑交给其调用的 service 层处理
- 使用@Api等注解自动生成API文档很方便。
    
    ```java
    /**
     * 菜品管理
     */
    @RestController
    @RequestMapping("/admin/dish")
    @Api(tags = "菜品相关接口")
    @Slf4j
    public class DishController {
        @Autowired
        private DishService dishService;
        @Autowired
        private RedisTemplate redisTemplate;
        /**
         * 新增菜品
         *
         * @param dishDTO
         * @return
         */
        @PostMapping
        @ApiOperation("新增菜品")
        public Result save(@RequestBody DishDTO dishDTO) {
            log.info("新增菜品：{}", dishDTO);
            dishService.saveWithFlavor(dishDTO);
            //清理缓存数据
            String key = "dish_" + dishDTO.getCategoryId();
            cleanCache(key);
            return Result.success();
        }
    }
    ```

### 12. 全局异常处理
- 用于定义一些处理器。处理器通常是在某个条件触发时执行的，比如本项目唯一的 Handler -GlobalExceptionHandler全局异常处理器，就是在整个项目代码抛出业务异常时，能够集中捕获和处理，而不是直接把,Spring Boot 内置的错误信息暴露给客户端(可能会泄露业务代码信息)。
    
    ```java
    /**
     * 全局异常处理器，处理项目中抛出的业务异常
     */
    @RestControllerAdvice
    @Slf4j
    public class GlobalExceptionHandler {
        /**
         * 捕获业务异常
         * @param ex
         * @return
         */
        @ExceptionHandler
        public Result exceptionHandler(BaseException ex){
            log.error("异常信息：{}", ex.getMessage());
            return Result.error(ex.getMessage());
        }
        /**
         * 处理SQL异常
         * @param ex
         * @return
         */
        @ExceptionHandler
        public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
            //Duplicate entry 'zhangsan' for key 'employee.idx_username'
            String message = ex.getMessage();
            if(message.contains("Duplicate entry")){
                String[] split = message.split(" ");
                String username = split[2];
                String msg = username + MessageConstant.ALREADY_EXISTS;
                return Result.error(msg);
            }else{
                return Result.error(MessageConstant.UNKNOWN_ERROR);
            }
        }
    }
    ```

### 13. 请求拦截器
- interceptor目录，存放请求拦截器，用于在Controller层处理请求前执行逻辑
- 项目里定义了两个拦截器，分别用于校验管理员和用户的登录状态。原理就是从请求头里拿到token，然后用JwtUtil解析，判断是否合法。
    
    ```java
    /**
     * jwt令牌校验的拦截器
     */
    @Component
    @Slf4j
    public class JwtTokenAdminInterceptor implements HandlerInterceptor {
    
        @Autowired
        private JwtProperties jwtProperties;
    
        /**
         * 校验jwt
         */
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            //判断当前拦截到的是Controller的方法还是其他资源
            if (!(handler instanceof HandlerMethod)) {
                //当前拦截到的不是动态方法，直接放行
                return true;
            }
    
            //1、从请求头中获取令牌
            String token = request.getHeader(jwtProperties.getAdminTokenName());
    
            //2、校验令牌
            try {
                log.info("jwt校验:{}", token);
                Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
                Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
                log.info("当前员工id：", empId);
                BaseContext.setCurrentId(empId);
                //3、通过，放行
                return true;
            } catch (Exception ex) {
                //4、不通过，响应401状态码
                response.setStatus(401);
                return false;
            }
        }
    }
    //两个拦截器的区别在于解析令牌时，使用的 secretkey 不同
    
    // JwtTokenAdminInterceptor 获取管理员密钥和用户 id
    Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
    Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
    // JwtTokenUserInterceptor 获取用户密钥和用户 id
    Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
    Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
    
    //管理员有自己的密钥，从而能够区分出用户是否具有管理员权限，不用再编写if else 来判断。
    ```

### 14. mapper层
- mapper目录下存放操作数据库方法的类，也叫dao层。一般Mapper类中绝大多数简单的增删改查方法都是可以由插件自动生成的，也可以通过注解或Mapper.xml自定义SQL。
    
    ```java
    @Mapper
    public interface OrderMapper {
    
        /**
         * 插入订单数据
         * @param orders
         */
        void insert(Orders orders);
    
        /**
         * 根据订单号和用户id查询订单
         * @param orderNumber
         * @param userId
         */
        @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
        Orders getByNumberAndUserId(String orderNumber, Long userId);
    
        /**
         * 修改订单信息
         * @param orders
         */
        void update(Orders orders);
    }
    ```

### 15. service层
- service才是后端项目的主体，请求响应、业务逻辑、数据读写都在该模块.
- 主要负责处理业务逻辑，是整个后端项目中需要自己编写最多的地方。service目录下会有很多Service接口文件，还有impl目录来存放每个接口对应的实现类文件，从而实现了定义和逻辑的分离。service层的代码"花样很多"，逻辑可能很简单，也可能很复杂，但基本上service层的事情可以概括为：为Controller提供服务，调用Mapper（Dao）和其他Service完成服务。


### 16. 定时任务
- task目录下存放定时任务。比如OrderTask中，定义了每分钟处理超时订单的方法，使用Spring Task的@Scheduled注解实现。
- @Scheduled 注解接受一个正则表达式，用于定义方法执行的周期，不用去背正则表达式的规则，需要用到的时候，访问网址使用正则表达式工具自动生成即可"https://cron.qqe2.com/"
    
    ```java
    /**
     * 定时任务类，定时处理订单状态
     */
    @Component
    @Slf4j
    public class OrderTask {
    
        @Autowired
        private OrderMapper orderMapper;
    
        /**
         * 处理超时订单的方法
         */
        @Scheduled(cron = "0 * * * * ? ") //每分钟触发一次
        public void processTimeoutOrder(){
            LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
            // 获取超时订单
            List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
            if(ordersList != null && ordersList.size() > 0){
                for (Orders orders : ordersList) {
                    orders.setStatus(Orders.CANCELLED);
                    orders.setCancelReason("订单超时，自动取消");
                    orders.setCancelTime(LocalDateTime.now());
                    orderMapper.update(orders);
                }
            }
        }
    }
    ```

### 17. WebSocket即时通讯服务
- websocket目录下定义了WebSocket服务。可以让客户端和服务端双向通信。
- WebSocket 理解为可以让客户端和服务端双向通信的管道。一般来说，创建 WebSocket 服务器要做4件事:
  - 和客户端建立连接
  - 从客户端接受消息
  - 向客户端发送消息
  - 关闭客户端的连接

    ```java
    /**
     * WebSocket服务
     */
    @Component
    @ServerEndpoint("/ws/{sid}")
    public class WebSocketServer {
    
        //存放会话对象
        private static Map<String, Session> sessionMap = new HashMap();
    
        /**
         * 连接建立成功调用的方法
         */
        @OnOpen
        public void onOpen(Session session, @PathParam("sid") String sid) {
            System.out.println("客户端：" + sid + "建立连接");
            sessionMap.put(sid, session);
        }
    
        /**
         * 收到客户端消息后调用的方法
         *
         * @param message 客户端发送过来的消息
         */
        @OnMessage
        public void onMessage(String message, @PathParam("sid") String sid) {
            System.out.println("收到来自客户端：" + sid + "的信息:" + message);
        }
    
        /**
         * 连接关闭调用的方法
         *
         * @param sid
         */
        @OnClose
        public void onClose(@PathParam("sid") String sid) {
            System.out.println("连接断开:" + sid);
            sessionMap.remove(sid);
        }
    
        /**
         * 群发
         *
         * @param message
         */
        public void sendToAllClient(String message) {
            Collection<Session> sessions = sessionMap.values();
            for (Session session : sessions) {
                try {
                    //服务器向客户端发送消息
                    session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    ```
    - 上述代码中，使用一个 HashMap 来存放连接到服务器的客户端会话信息(可以理解为加入聊天室的用户)。当连接成功时，把该客户端的信息放到 map 中;然后持续通过 onMessage 方法获取客户端的消息、通过 session.getBasicRemote0.sendText 方法向某个客户端推送消息;最后当连接关闭时，再把该客户端的信息从 map 中移除，防止资源泄露。
    

### 18. Spring Boot主类
- SkyApplication是Spring Boot项目的全局入口类，包含了项目启动的main方法和一些开启Spring Boot特性的注解。
    
    ```java
    @SpringBootApplication
    @EnableTransactionManagement //开启注解方式的事务管理
    @Slf4j
    @EnableCaching//开发缓存注解功能
    @EnableScheduling //开启任务调度
    public class SkyApplication {
        public static void main(String[] args) {
            SpringApplication.run(SkyApplication.class, args);
            log.info("server started");
        }
    }
    ```



