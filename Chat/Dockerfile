# 引用jdk8
FROM openjdk:8

# 增加jar包
ADD target/Chat-1.1.0-SNAPSHOT.jar /Chat-1.1.0-SNAPSHOT.jar

# 新建文件夹 并 移动jar包
RUN mkdir /ikun-chat && mkdir /ikun-chat/db && mv /Chat-1.1.0-SNAPSHOT.jar /ikun-chat

# 暴漏端口
EXPOSE 777 8888

# 运行服务
CMD cd /ikun-chat && java -jar Chat-1.1.0-SNAPSHOT.jar --spring.datasource.url=jdbc:sqlite:./db/test.db
