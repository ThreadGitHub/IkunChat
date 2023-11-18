mvn clean & mvn package -Dmaven.test.skip=true

# 处理参数
tag=$1
if [ -z $1 ]; then
  tag=latest
fi

# 构建镜像 --platform linux/amd64 设为x86架构
docker build --platform linux/amd64 -t registry.cn-hangzhou.aliyuncs.com/thread-docker-hub/ikun-chat:$tag .

# 推送镜像
docker push registry.cn-hangzhou.aliyuncs.com/thread-docker-hub/ikun-chat:$tag
