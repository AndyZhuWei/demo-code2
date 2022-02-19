# Centos7环境安装Docker
1.安装必要的一些系统工具
yum install -y yum-utils device-mapper-persistent-data lvm2
* yum-utils yum工具集
* device-mapper-persistent-data 数据存储的驱动包（docker内部数据存储时需要）
* lvm2  数据存储的驱动包（docker内部数据存储时需要）
2.添加软件源信息
修改我们的yum源
yum-config-manager --add-repo  https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo   # yum-config-manager 这个工具就是前边安装yum-utils中的工具，简化我们切换yum源的工具
3.创建缓存并让yum自己决定哪个快用哪个
yum makecache fast
4.安装docker
yum -y install docker-ce
5.开启Docker服务
service docker start
6. 验证
输入docker version
通过输出我们可以看到客户端和服务器的版本信息
在安装docker的过程中也会把docker的客户端安装了，docker是一个cs架构

7.拉取镜像进行测试
docker pull hello-world   #远程仓库拉取镜像，如果不能下载可能是国内的网络和docker境外仓库网络问题，出现这种情况后我们用阿里云的加速代理就可以了

docker run hello-world    #通过镜像启动一个容器


# 阿里云Docker镜像加速
在阿里云中搜索“镜像服务“，在左侧列表中找到镜像加速服务，执行其中的命令即可
类似如下：
```shell
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://xxxx.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```





# Docker的基本概念
Docker是容器化的平台(也有一些虚拟化的技术，在容器内部也会有一些轻量级的操作系统是一个mini的。）

## docker体系结构
由内向外
server docker daemon 服务端docker服务守护进程<------>rest api（通信层）<------>client Docker CLI(docker客户端)

##容器与镜像
* 镜像:镜像是文件，是只读的，提供了运行程序完整的软硬件资源，是应用程序的“集装箱”
* 容器:是镜像的实例，由Docker负责创建，容器之间彼此隔离

##Docker的执行流程
![Docker的执行流程](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\Docker执行流程.png)

最小化安装时缺少ifconfig 和route命令，此时执行如下命令安装
yum install net-tools

# Docker快速部署Tomcat
## Docker常用命令
* docker pull 镜像名<:tags> -从远程仓库抽取镜像
* docker images - 查看本地镜像
* docker run 镜像名<:tags> -创建容器，启动应用
* docker ps -查看正在运行中的镜像
* docker rm <-f> 容器id -删除容器
* docker rmi <-f> 镜像名<:tags> -删除镜像

## Docker官方提供的远程仓库
https://hub.docker.com/
根据需要进行搜索，一般官方提供的镜像都有logo并且镜像名字一般就是组件名，如果有其他的诸如斜杠之类的都是个人或者是第三方提供的，我们需要小心。

# Docker快速部署Tomcat运行
1.拉取tomcat镜像
docker pull tomcat   #不指定版本拉取的就是latest(也不是最新，是下载使用最多的版本)
2.查看本地镜像有哪些
docker images
3.安装指定版本的镜像
docker pull tomcat:8.5.46-jdk8-openjdk  #8.5.46-jdk8-openjdk就是具体的版本号，版本号可以登录到docker镜像仓库中查找
4.创建容器
docker run tomcat
此时就在当前控制台打印了启动日志，但是此时有一个问题。
Docker宿主机与容器怎么通信？解决方案就是端口映射。写法如下：
docker run -p 8000:8080 tomcat  #8000是宿主机开放的端口 8080是容器中tomcat启动时的端口
此时通过浏览器访问ip:8000即可访问tomcat
通过以下命令查看端口号
netstat -tulpn
上述启动的容器是前端运行的，我们需要后台运行
docker run -p 8000:8080 -d tomcat #-d表示后台运行
运行后会返回一个长串

5.停止容器
docker ps   #列出当前运行的镜像容器
docker stop 容器编号
docker rm 容器编号
或者
docker rm -f 容器编号

6.移除镜像
docker rmi 镜像名<:tags>
或者
docker rmi -f 镜像名<:tags>


# 容器内部结构
tomcat容器内部结构
![tomcat容器内部结构](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\tomcat容器内部结构.png)

## 在容器内部执行命令
* 格式 docker exec [-it] 容器id 命令
  exec:在对应容器在中执行命令
  -it:采用交互方式执行命令
  
* 实例 docker exec -it 3213123123 /bin/bash

```shell
docker exec -it 8b708e01d8b6 /bin/bash
root@8b708e01d8b6:/usr/local/tomcat# pwd
/usr/local/tomcat
root@8b708e01d8b6:/usr/local/tomcat#
```
exit就退出了当前容器

所有的镜像和容器都存储在/var/lib/docker中



# 容器生命周期
![容器生命周期](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\容器生命周期.png)

## 创建容器
```shell
docker create tomcat
ff4854fc170d247dabdf15126d068bd5d9cdacca240f432f70acc58fae635948
[root@localhost ~]# docker ps  #默认只会列出正在运行的容器，如果要看所有则要加上参数-a
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE                        COMMAND             CREATED              STATUS                       PORTS                                       NAMES
ff4854fc170d   tomcat                       "catalina.sh run"   About a minute ago   Created                                                                  busy_bartik
```
此处的Created就是stopped的一个分支状态，stopped是一个统称的状态
## 启动容器
```shell
[root@localhost ~]# docker start ff4854fc170d
ff4854fc170d
[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE                        COMMAND             CREATED         STATUS                       PORTS                                       NAMES
ff4854fc170d   tomcat                       "catalina.sh run"   5 minutes ago   Up 40 seconds                8080/tcp                                    busy_bartik
```
Up就是图中的running状态
##暂停容器
```shell
[root@localhost ~]# docker pause ff4854fc170d
ff4854fc170d
[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE                        COMMAND             CREATED         STATUS                       PORTS                                       NAMES
ff4854fc170d   tomcat                       "catalina.sh run"   6 minutes ago   Up 2 minutes (Paused)        8080/tcp                                    busy_bartik
```
## 恢复容器
```shell
[root@localhost ~]# docker unpause ff4854fc170d
ff4854fc170d
[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE                        COMMAND             CREATED         STATUS                       PORTS                                       NAMES
ff4854fc170d   tomcat                       "catalina.sh run"   7 minutes ago   Up 3 minutes                 8080/tcp                                    busy_bartik
```
## 停止容器
```shell
[root@localhost ~]# docker stop ff4854fc170d
ff4854fc170d
[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE                        COMMAND             CREATED         STATUS                       PORTS                                       NAMES
ff4854fc170d   tomcat                       "catalina.sh run"   9 minutes ago   Exited (143) 2 seconds ago                                               busy_bartik
[root@localhost ~]# docker start ff4854fc170d
ff4854fc170d
[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE                        COMMAND             CREATED          STATUS                        PORTS                                       NAMES
ff4854fc170d   tomcat                       "catalina.sh run"   11 minutes ago   Up 2 seconds                  8080/tcp                                    busy_bartik
```
Exited表示的就是stopped的另一个分支
## 删除容器
```shell
docker rm -f ff4854fc170d
ff4854fc170d
[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE                        COMMAND             CREATED        STATUS                        PORTS                                       NAMES
```
删除后容器列表中就不存在了

#Dockerfile构建镜像
![Dockerfile构建镜像](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\dockerfile构建镜像.jpg)

## Dockerfile自动部署tomcat应用
![Dockerfile自动部署tomcat应用](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\Dockerfile自动部署tomcat应用.jpg)

##演示
* 创建目录first-dockerfile/docker-web
* 在之前的目录下创建index.html文件，在文件中书写一些信息
```html
<h1>我是Docker应用首页</h1>
```
* 在first-dockerfile下面创建Dockerfile文件（注意没有扩展名）
内如如下：
```dockerfile
FROM tomcat:latest
MAINTAINER xxxx
WORKDIR /usr/local/tomcat/webapps
ADD docker-web ./docker-web
```
解释：
第一行FROM表示基准镜像，表明我们当前的自定义的镜像以那个镜像为基准镜像来进行构建
第二行 MAINTAINER表示我们当前的自定义镜像的拥有者是谁
第三行WORKDIR就是tomcat基准镜像中的工作目录，也就是切换工作目录。如果不存在会自动创建
第四行ADD表示添加第一个目录到工作目录 也就是把Dockerfile当前所在目录中的docker-web目录下的所有文件拷贝到工作目录
usr/local/tomcat/webapps下的docker-web目录中，如果不存在则自动创建目录
* 将first-dockerfile上传到虚拟机中，比如/usr/images下
* 使用docker build -t 机构/镜像名<:tags> Dockerfile进行构建
docker build -t xxx.com/mywebapp:1.0 dockerfilePath
  注意：此处需要指定Dockerfile所在的目录即可
* 此时使用docker images 就可以看到我们的镜像了
* 然后创建容器
docker run -d -p 8001:8080 xxx.com/mywebapp:1.0
* 测试
http://xxx:8001/docker-web/index.html
  
# 镜像分层（layer）概念
![镜像分层（layer）概念](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\镜像分层(layer)概念.jpg)

## mywebapp执行过程
![mywebapp执行过程](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\mywebapp执行过程.jpg)
分成四步执行，
第一步是拉取tomcat的镜像并且用这个镜像创建一个临时容器，这个容器只能用于构建镜像，
   那串数字就是临时容器的id
第二步就是又有一个容器id，这个容器id就是当前docker对我们的容器进行的一个快照，这个快照就是以临时容器来体现的，
每执行一步都会对当前系统的环境做一个快照

这个构建过程就体现了docker的两个特点
1.按层进行堆叠，第一个机制采用分曾
2.系统快照，以临时容器体现，这么做的好处就是在构建过程中临时容器是可以复用的。加快构建速度节省资源。

演示
在/usr/images下创建docker_layer目录，然后创建Dockerfile文件，内容如下
```dockerfile
FROM centos
RUN ["echo","aaa"]
RUN ["echo","bbb"]
RUN ["echo","ccc"]
RUN ["echo","ddd"]
```
对这个文件进行构建
docker build -t xxx.com/docker_layer:1.0 .
观察输出的信息
然后我们再次修改Dockerfile内容，把最后两行做修改
```dockerfile
FROM centos
RUN ["echo","aaa"]
RUN ["echo","bbb"]
RUN ["echo","eee"]
RUN ["echo","fff"]
```
重新构建
docker build -t xxx.com/docker_layer:1.1 .
观察输出，我们可以看到如果命令没有发生变化时是采用之前的已有的临时容器

# Dockerfile基础命令
## FROM-基于基准镜像
* FROM centos #制作基准镜像(基于centos:lastest)
* FROM scratch #不依赖任何基准镜像base image
* FROM tomcat:9.0.22-jdk8-openjdk
* 尽量使用官方提供的Base Image
## LABEL & MAINTAINER -说明信息
* MAINTAINER xxx
* LABEL version="1.0"
* LABEL description="xxx"
## WORKDIR - 设置工作目录
* WORKDIR /usr/local
* WORKDIR /usr/local/newdir #自动创建
* 尽量使用绝对路径
## ADD & COPY -复制文件
* ADD hello / #复制到根路径
* ADD test.tar.gz / #添加根目录并解压
* ADD 除了复制，还具备添加远程文件功能
## ENV- 设置环境常量
* ENV JAVA_HOME /usr/local/openjdk8
* RUN ${JAVA_HOME}/bin/java -jar test.jar
* 尽量使用环境常量，可提高程序维护性

#Dockerfile执行指令
## RUN & CMD & ENTRYPOINT
* RUN: 在Build构建时执行命令
* ENTRYPOINT: 在容器启动时执行命令
* CMD:容器启动后执行默认的命令或参数
本质区别是执行时机不同
![执行时机不同](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\执行时机不同.jpg)  

RUN-构建运行
* RUN yum install -y vim #Shell命令格式
* RUN ["yum","install","-y","vim"] #Exec命令

Shell运行方式
![Shell运行方式](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\Shell运行方式.jpg)

Exec运行方式
![Exec运行方式](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\Exec运行方式.jpg)

ENTRYPOINT启动命令
* ENTRYPOINT(入口点)用于在容器启动时执行命令
* Dockerfile中只有最后一个ENTRYPOINTY会被执行
* ENTRYPOINT ["ps"] #推荐使用Exec格式
一定会被执行

CMD默认命令
* CMD用于设置默认执行的命令
* 如Dockerfile中出现多个CMD,则只有最后一个被执行
* 如容器启动时附加指令,则CMD被忽略
* CMD ["ps","-ef"] #推荐使用Exec格式
不一定会被执行
  
## 演示
1.进入/usr/image,创建目录docker_run,并在其中创建Dockerfile
2.文件内容如下
```dockerfile
FROM centos
RUN ["echo","image building!!!"]
CMD ["echo","container starting..."]
```
保存
3.构建镜像
docker build -t xxx.com/docker_run .
4.观察输出
发现输出了image building!!!
5.运行容器
docker run xxx.com/docker_run
发现输出了container starting...
6.CMD会被忽略是什么意思，见如下命令
docker run xxx.com/docker_run ls
此时发现会列出ls命令的结果，但是没有了这个container starting...输出，忽略就是如果额外加入了命令则CMD就会被忽略
7.重新编辑Dockerfile
```dockerfile
FROM centos
RUN ["echo","image building!!!"]
ENTRYPOINT ["echo","container starting..."]
```
8.重新构建，创建容器
构建时输出了image building!!!
容器运行时输出了container starting...
这么看单独使用ENTRYPOINT时和CMD是没有什么区别
9.重新编辑Dockerfile
```dockerfile
FROM centos
RUN ["echo","image building!!!"]
ENTRYPOINT ["ps"]
CMD ["-ef"]
```
如果在Dockerfile中上边写ENTRYPOINT,下边写CMD的时候这两部分会联合起来
10.重新构建运行
发现会输出ps -ef的命令
这么做的好处是CMD可以接外部命令，比如以下命令
docker run xxx.com/docker_run -aux
执行后就会忽略Dockerfile中CMD的-ef，从而执行
ps -aux命令了

总结
CMD代表的是一个默认指令，这里的默认指令不一定是一个完整的指令，也可以是一个参数或者命令的一部分

# 实战
## 构建Redis镜像

Redis简介

![Redis简介](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\Redis简介.jpg)

1.在images中创建docker-redis目录，将redis-4.0.14.tar.gz和配置文件redis-7000.conf上传进去

2.在此目录下创建Dockerfile文件
```dockerfile
FROM centos:7
RUN ["yum","install","-y","gcc","gcc-c++","net-tools","make"]
WORKDIR /usr/local
ADD redis-4.0.14.tar.gz .
WORKDIR /usr/local/redis-4.0.14/src
RUN make && make install
WORKDIR /usr/local/redis-4.0.14
ADD redis-7000.conf .
EXPOSE 7000
CMD ["redis-server","redis-7000.conf"]
```
3.镜像构建
docker build -t zhuwei.com/docker-redis .

4.观察本地镜像仓库
docker images

5.创建容器
docker run -p 7000:7000 zhuwei.com/docker-redis
观察输出
6.重新开一个窗口观察7000端口是否已经被侦听
netstat -tunlp
7.进入容器观察
docker ps
docker exec -it xxx /bin/bash
观察输出



# 容器间Link单向通信
![容器间Link单向通信](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\容器间Link单向通信.jpg)
创建容器后内部会有一个虚拟ip，外界无法访问，仅仅是容器内部沟通的标识
但通过虚拟ip通信局限性很大，因为随便创建一个容器ip就会改变
![容器ip改变后](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\容器ip改变后.jpg)
这样就导致原本写好的虚拟ip地址需要随时改变，那有什么好的办法嘛？
其实我们可以给每个容器起一个名称，用名称连接即可。

## 演示

docker run -d --name web tomcat:latest #--name就是命名容器的参数
docker run -d --name database -it centos /bin/bash #为什么要加-it和/bin/bash呢，因为centos容器创建完后默认会自动退出
      如果想要保持运行状态就要加-it进入交互模式并且进入/bin/bash并在后台运行

怎么看容器的虚拟ip？
docker inspect 容器id
会显示容器的原始数据信息
其中IPAddress就是虚拟IP
```shell
[root@localhost ~]# docker ps 
CONTAINER ID   IMAGE           COMMAND             CREATED              STATUS              PORTS      NAMES
a15a8f779d9b   centos          "/bin/bash"         About a minute ago   Up About a minute              database
7cbf658cb215   tomcat:latest   "catalina.sh run"   About a minute ago   Up About a minute   8080/tcp   web
[root@localhost ~]# docker exec -it a15a8f779d9b /bin/bash
[root@a15a8f779d9b /]# ip addr
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
48: eth0@if49: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:11:00:03 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.17.0.3/16 brd 172.17.255.255 scope global eth0
       valid_lft forever preferred_lft forever
[root@a15a8f779d9b /]# ping 172.17.0.2
PING 172.17.0.2 (172.17.0.2) 56(84) bytes of data.
64 bytes from 172.17.0.2: icmp_seq=1 ttl=64 time=0.365 ms
64 bytes from 172.17.0.2: icmp_seq=2 ttl=64 time=0.271 ms
64 bytes from 172.17.0.2: icmp_seq=3 ttl=64 time=0.255 ms
```
进入容器内部可以通过虚拟ip 和对方可以ping通。但是通过名称就不可以。

退出容器删除后我们先删除web的容器
docker rm -f 7cbf658cb215
（删除所有容器
docker rm $(docker ps -aq)
）
此时我们通过如下命令来创建web容器
docker run -d --name web --link database tomcat:8.5.46-jdk8-openjdk
其中--link database 表示的就是指向database名称的容器
```shell
[root@localhost ~]# docker run -d --name web --link database tomcat:8.5.46-jdk8-openjdk
ee7f985fd0b5d3e709f2e7d1a7dbc0778ec8138c7cbf4416aa175e600ecc1d33
[root@localhost ~]# docker ps
CONTAINER ID   IMAGE                        COMMAND             CREATED          STATUS          PORTS      NAMES
ee7f985fd0b5   tomcat:8.5.46-jdk8-openjdk   "catalina.sh run"   5 seconds ago    Up 3 seconds    8080/tcp   web
a15a8f779d9b   centos                       "/bin/bash"         16 minutes ago   Up 16 minutes              database
[root@localhost ~]# docker exec -it ee7f985fd0b5 /bin/bash
root@ee7f985fd0b5:/usr/local/tomcat# ping database
PING database (172.17.0.3) 56(84) bytes of data.
64 bytes from database (172.17.0.3): icmp_seq=1 ttl=64 time=0.162 ms
64 bytes from database (172.17.0.3): icmp_seq=2 ttl=64 time=0.273 ms
^C
--- database ping statistics ---
2 packets transmitted, 2 received, 0% packet loss, time 1000ms
rtt min/avg/max/mdev = 0.162/0.217/0.273/0.057 ms
```
这样web向databse的单向通信就成功了

# Bridge网桥双向通信
基于上述我们创建双向的link可以解决双向通信，但是比较麻烦，所以Docker给我们提供了Bridge网桥的方式
![Bridge网桥双向通信](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\Bridge网桥双向通信.jpg)
此处的网桥充当了Docker环境与外边宿主机的通讯源
网桥还有一个用户就是在网络层面对容器进行分组

bridge是虚拟出来的一个组件

## 演示
```shell
[root@localhost images]# docker run -d --name web tomcat:8.5.46-jdk8-openjdk
520048d7abc40b78f4841dc73277bb40e66d1fa825fed1bd449d96f2299bd92b
[root@localhost images]# docker run -d -it --name database centos /bin/bash 
a5e9b2f3b99f3833c9cec222948efe3000b0ddcc0a5ccf4e162bf8ad9bbbc082
[root@localhost images]# docker ps
CONTAINER ID   IMAGE                        COMMAND             CREATED              STATUS              PORTS      NAMES
a5e9b2f3b99f   centos                       "/bin/bash"         8 seconds ago        Up 7 seconds                   database
520048d7abc4   tomcat:8.5.46-jdk8-openjdk   "catalina.sh run"   About a minute ago   Up About a minute   8080/tcp   web
```
此时创建好的两个容器彼此不是互联互通的
```shell
[root@localhost images]# docker network ls
NETWORK ID     NAME      DRIVER    SCOPE
e64f6ec9fc93   bridge    bridge    local
641a13ded44a   host      host      local
45ad6a9cfd21   none      null      local
```
network是docker网络环境的命令，此处的命令是列出当前网络环境的明细。
默认每一个docker都会提供一个bridge的网桥，这个网桥承担起容器与外界通信的桥梁
此处如果我们要实现某些容器之间互联互通就要创建一个新的网桥
```shell
[root@localhost images]# docker network create -d bridge my-bridge
c9e324a5aa51aea450ab106401a2f3ad8660edaaaf8b32b7f6efa43df388565f
[root@localhost images]# docker network ls
NETWORK ID     NAME        DRIVER    SCOPE
e64f6ec9fc93   bridge      bridge    local
641a13ded44a   host        host      local
c9e324a5aa51   my-bridge   bridge    local
45ad6a9cfd21   none        null      local
```
容器和网桥进行关联，天然的容器就可以通过关联的网桥进行通信了
```shell
[root@localhost images]# docker network connect my-bridge web
[root@localhost images]# docker network connect my-bridge database
```
进行测试
进入database测试是否可以ping通web
```shell
[root@localhost images]# docker ps 
CONTAINER ID   IMAGE                        COMMAND             CREATED          STATUS          PORTS      NAMES
a5e9b2f3b99f   centos                       "/bin/bash"         12 minutes ago   Up 12 minutes              database
520048d7abc4   tomcat:8.5.46-jdk8-openjdk   "catalina.sh run"   13 minutes ago   Up 13 minutes   8080/tcp   web
[root@localhost images]# docker exec -it a5e9b2f3b99f /bin/bash
[root@a5e9b2f3b99f /]# ping web
PING web (172.18.0.2) 56(84) bytes of data.
64 bytes from web.my-bridge (172.18.0.2): icmp_seq=1 ttl=64 time=0.136 ms
64 bytes from web.my-bridge (172.18.0.2): icmp_seq=2 ttl=64 time=0.279 ms
64 bytes from web.my-bridge (172.18.0.2): icmp_seq=3 ttl=64 time=0.133 ms
^C
--- web ping statistics ---
3 packets transmitted, 3 received, 0% packet loss, time 2006ms
rtt min/avg/max/mdev = 0.133/0.182/0.279/0.069 ms

```
进入web测试是否可以ping通database
```shell
[root@localhost images]# docker ps
CONTAINER ID   IMAGE                        COMMAND             CREATED          STATUS          PORTS      NAMES
a5e9b2f3b99f   centos                       "/bin/bash"         14 minutes ago   Up 14 minutes              database
520048d7abc4   tomcat:8.5.46-jdk8-openjdk   "catalina.sh run"   15 minutes ago   Up 15 minutes   8080/tcp   web
[root@localhost images]# docker exec -it 520048d7abc4 /bin/bash
root@520048d7abc4:/usr/local/tomcat# ping database
PING database (172.18.0.3) 56(84) bytes of data.
64 bytes from database.my-bridge (172.18.0.3): icmp_seq=1 ttl=64 time=0.059 ms
64 bytes from database.my-bridge (172.18.0.3): icmp_seq=2 ttl=64 time=0.219 ms
64 bytes from database.my-bridge (172.18.0.3): icmp_seq=3 ttl=64 time=0.221 ms
64 bytes from database.my-bridge (172.18.0.3): icmp_seq=4 ttl=64 time=0.293 ms
^C
--- database ping statistics ---
4 packets transmitted, 4 received, 0% packet loss, time 3007ms
rtt min/avg/max/mdev = 0.059/0.198/0.293/0.085 ms

```
经测试双向都可以ping通

总结：
作为双向互连其中最核心的点就是通过创建一个新的网桥，将已有的容器与网桥进行绑定，那么默认所有与网桥绑定的容器都是互联互通的

## 网桥实现原理
![网桥实现原理](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\网桥实现原理.jpg)
在创建网桥后，都会在宿主机上按照一个虚拟网卡，这个虚拟网卡也承担了一个网关的作用。
虚拟网卡ip都是虚拟的，如果和外界通信需要和物理网卡进行地址转换

# Volume容器间共享数据
为什么需要数据共享？
![为什么需要数据共享](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\数据共享.jpg)
如果所示，两个tomcat容器中是有相同的web页面，如果要更新某个web页面时，需要更新这两个容器，如果容器比较多，那么更新就比较麻烦，为此提出了数据共享的方案
![数据共享方案](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\数据共享2.pg.jpg)

## 通过设置—v挂载宿主机目录
* 格式：
* docker run --name 容器名 -v 宿主机路径:容器内挂载路径 镜像名
* 实例：
* docker run --name t1 -v /usr/webapps:/usr/local/tomcat/webapps tomcat

## 通过--volumes-from 共享容器内挂载点
* 创建共享容器
* docker create --name webpage
     -v /webapps:/tomcat/webapps tomcat /bin/true
* 共享容器挂载点
* docker run --volumes-from webpage --name t1 -d tomcat

## 演示-v挂载的方式
* 1.在虚拟机/usr目录下创建webapps/volume-test目录，
在volume-test中创建index.html文件，文件内容如下：
```html
<h1>I'm volume test page!!!</h1>
```
* 2.在webapps目录下执行以下命令
```shell
docker run --name t1 -p 8000:8080 -d -v /usr/webapps:/usr/local/tomcat/webapps tomcat:
```
在浏览中进行测试
进入容器内部进行观察
```shell
docker exec it xx /bin/bash
cd webapps
ls
cd volume-test
```

在创建一个新的容器
```shell
docker run --name t2 -p 8001:8080 -d -v /usr/webapps:/usr/local/tomcat/webapps tomcat:
```
在浏览中进行测试
进入容器内部进行观察
```shell
docker exec it xx /bin/bash
cd webapps
ls
cd volume-test
```
3.在宿主机上进入/usr/webapps/volume-test,修改index.html文件
```shell
<h1>I'm volume test page!!!!!!</h1>
```
保存退出
4.直接在浏览器中测试前边的两个容器的地址，发现已经变更了

## 演示共享容器的挂载的方式
进入宿主机/usr/webapps
```shell
docker create --name webpage -v /usr/webapps:/usr/local/tomcat/webapps tomcat:  /bin/true
```
其中/bin/true没有什么特别含义 相当于一个占位符
创建实际运行容器
```shell
docke run -p 8002:8080 --volumes-from webpage --name t3 -d tomcat:
```
在创建新的
```shell
docke run -p 8003:8080 --volumes-from webpage --name t4 -d tomcat:
```
测试

总结：
在实际使用过程中根据容器的数量来决定使用-v还是共享容器的方式，如果就两台容器就使用-v参数即可

# Docker Compose
容器编排工具
![容器编排工具](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\容器编排工具.jpg)
哪么什么是容器编排工具呢?我们看一个场景
![多容器部署的麻烦事](D:\IT\demo-code2\src\main\java\cn\andy\demo\democode2\docker\多容器部署的麻烦事.jpg)
图中的方框中是一台宿主机，如果按照原始容器来做，我们需要部署三个容器，分别是nginx、tomcat以及mysql。
nginx对tomcat提供负载均衡以及反向代理服务，tomcat里面按照java web服务，而服务里面需要访问底层的mysql数据，这些都是需要互联互通.
同时每个容器都有属于自己独立的配置文件，如果现在有一个应用要上线，难道要给运维提供一推命令和文件嘛，显然这些不现实的
此时Docker Compose就给我们解决了这个问题。
Docker Compse通过解析一个的脚本自动化的帮我们先安排mysql容器，然后后tomcat容器，最后按照nginx容器，同时彼此形成一个依赖关系，
而过程中每个容器所需要的配置文件都可以相应的绑定，而这一切都只需要一个脚本即可。
先部署哪个在部署哪个，这样整体的过程我们就称为容器编排。 

* Docker Compose单机多容器部署工具
  如果要使用集群环境对很多台宿主机进行部署就需要使用Docker xx 或者 Kbxx来解决集群部署问题
* 通过yml文件定义多容器如何部署
* WIN/MAC默认提供Docker Compose,Linux需安装

## 安装
```shell
sudo curl -L "https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s )-$(uname -m)" -0 /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose -version
```

## 演示安装WordPress
1.创建脚本
```shell
cd /usr/
mkdir wordpress
cd wordpress
vim docker-compose.yml
```
docker-compose.yml的内容如下：
```yml
version: "3.9"
    
services:
  db:
    image: mysql:5.7
    volumes:
      - db_data:/var/lib/mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: somewordpress
      MYSQL_DATABASE: wordpress
      MYSQL_USER: wordpress
      MYSQL_PASSWORD: wordpress
    
  wordpress:
    depends_on:
      - db
    image: wordpress:latest
    volumes:
      - wordpress_data:/var/www/html
    ports:
      - "8000:80"
    restart: always
    environment:
      WORDPRESS_DB_HOST: db
      WORDPRESS_DB_USER: wordpress
      WORDPRESS_DB_PASSWORD: wordpress
      WORDPRESS_DB_NAME: wordpress
volumes:
  db_data: {}
  wordpress_data: {}
```
2.对前边的脚本进行解析执行
```shell
docker-compose up -d
```
up标识解析执行
3.验证
```shell
docker ps
```
浏览器访问验证


#Docker-compose应用实战
1.准备操作素材
创建目录
bsbdj/bsbdj-app
bsbdj/bsbdj-db
在bsbdj-db中有一个文件init-db.sql,初始化数据的脚本
在bsbdj-app内容如下

bsbdj.jar采用springboot开发
2.准备对我们的app和db做镜像
将前边的bsbdj目录上传到宿主机/usr/image下
```shell
cd /usr/image/bsbdj-app
vim Dockerfile
```
文件内容如下
```Dockerfile
FROM openjdk:8u222-jre
WORKDIR /usr/local/bsbdj
ADD babdj.jar .
ADD application.yml .
ADD application-dev.yml .
EXPOSE 80
CMD ["java","-jar","bsbdj.jar"]
```
3.创建镜像
```shell
docker build -t mashibing.com/bsbdj-app .
```
docker images
4.运行容器
```shell
docker run mashibing.com/bsbdj-app
```
此时启动正常，但是不能访问，因为数据库容器还没有创建
5.创建基于msql5.7的数据库容器
```shell
cd /usr/image/bsbdj-db
vim Dockerfile
```
文件内容如下：
```Dockerfile
FROM mysql:5.7
WORKDIR /docker-entrypoint-initd.d
ADD init-db.sql . 
```
docker-entrypoint-initd.d这个目录是mysql5.7提供的初始化数据的目录
6.创建数据库镜像
```shell
docker build -t masshibing.com/bsbdj-db .
docker images
```
7.创建容器
```shell
docker run -d -e MYSQL_ROOT_PASSWORD=root mashibing.com/bsbdj-db
```
MYSQL_ROOT_PASSWORD为初始化环境变量，设置root的密码
8.验证
```shell
docker ps
docker exec -it xxx /bin/bash
```
在容器内部执行
```shell
mysql -uroot -proot
```
就可以进入到容器里mysql的内部，可以查看数据
9.怎么通过docker-compose把上边的app和db联系在一起呢
需求app底层要和db通信
我们怎么通过docker-compose一键发布呢
在宿主机/usr/images/bsbdj下创建docker-compose.yml文件
```yml
version: '3.3'
services: 
  db: 
    build: ./bsbdj-db/
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
  app:
    build: ./bsbdj-app
    depends_on:
      - db
    ports:
      - "80:80"
    restart: always
      

```
version表示告诉docker-compose解析时使用3.3解析规则，不同版本方法有稍微不同
services用于描述一个一个容器及相关信息
db表示服务名，指定即将创建容器的作用的服务名，用于docker-compose创建容器以及网络主机名
build构建镜像
restart重启的意思，只要发现容器宕机了就会重启一个
MYSQL_ROOT_PASSWORD 初始化mysql的root密码
至此db的配置就完了
总结：docker-compose在进行容器编排时，首先会对db这个目录进行镜像创建，在创建容器，如果容器出现了宕机会自动重启
开始app配置
depends_on表示依赖，其中db就表示上边的db,只要有这个依赖关系，那么docker就会为这两个容器设置互联互通，它们在网络层面通过服务名就可以互相通信
这个前边用网桥创建的情形类似，只不过现在变成了depen_on
ports之前app内部暴漏的是80，在宿主机上要有一个与之对应那么就是在这个设置的。前边80是宿主机的端口，后边的是容器内部暴漏的端口
单引号和双引号对yml来说是兼容的，没有问题
要根据依赖的关系在yml来顺序进行配置
10.对配置文件进行编译执行
```shell
docker-compose up
```
前台运行
我们需要后台运行
```shell
docker-compsoe up -d
```
如果想要看输出，则执行如下命令
```shell
docker-compse logs
```
此时的输出的是所有服务打印的日志信息，如果想要看指定服务的，则执行如下命令
```shell
docker-compse logs app
```
如果想要关闭应用，则执行如下命令
```shell
docker-compose down
```
如果app和db连接不通，需要检查app应用的jdbc连接配置
```shell
cd bsbdj-app
vim application-dev.yml
```
将其中的spring.datasource.url中的域名改为db即可
11.验证
```shell
docker ps
```
浏览器进行验证

实际场景中直接使用docker-compose还是比较少的，原因就在于docker-compose只支持单击容器编排部署
如果在大集群就需要docker xx 或者k8s

  
























































