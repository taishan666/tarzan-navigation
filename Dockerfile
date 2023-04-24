#VERSION 1.1.0
#基础镜像为java11

FROM williamyeh/java8:latest

#作者签名
MAINTAINER tarzan "1334512682@qq.com"

# 删除之前的镜像文件
RUN rm -rf /opt/running/tarzan-cms*

#拷贝jar包，到容器内的指定位置
ADD ./target/tarzan-nav.jar  /opt/running/tarzan-nav.jar

#容器对外映射端口
EXPOSE 8088

# 切换到jar包文件夹下
WORKDIR /opt/running/

#运行启动命令
CMD ["java", "-jar","-Dfile.encoding=UTF-8","tarzan-nav.jar","--spring.profiles.active=dev"]

