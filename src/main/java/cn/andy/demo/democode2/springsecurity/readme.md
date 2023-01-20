1.在spring boot项目中引入spring security后，启动项目，spring security就会默认拦截所有请求，如果没有验证就会跳转
到一个默认的登录页面中，默认的用户名为user,密码会打印到控制台，

2.如果在配置文件中指定了用户名和密码，则就会使用配置的用户信息，例如：
```properties
spring.security.user.name=111
spring.security.user.password=111
```