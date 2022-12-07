#VERSION 1.1.0
#基础镜像为java8

FROM williamyeh/java8:latest

#作者签名
MAINTAINER tarzan "1334512682@qq.com"

# 删除之前的镜像文件
RUN rm -rf /opt/running/tarzan-cms*

#拷贝jar包，到容器内的指定位置
ADD ./target/tarzan-cms.jar  /opt/running/tarzan-cms.jar

#容器对外映射端口
EXPOSE 80

# 切换到jar包文件夹下
WORKDIR /opt/running/

#运行启动命令
CMD ["java", "-jar","-Dfile.encoding=UTF-8","tarzan-cms.jar","--spring.profiles.active=dev"]

