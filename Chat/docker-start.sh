# 干掉运行的容器
docker rm -f ikun-chat
docker rmi registry.cn-hangzhou.aliyuncs.com/thread-docker-hub/ikun-chat:latest

# 拉取最新镜像
docker pull registry.cn-hangzhou.aliyuncs.com/thread-docker-hub/ikun-chat:latest

# sqllite
dbfile=$1

# 设置变量默认值
# /Users/thread/workspaces/GitHubRepository/IkunChat/Chat/src/main/resources/db
# /root/ikun-chat/db
if [ -z $1 ]; then
  dbfile=/root/ikun-chat/db
fi

echo sqllite：$dbfile

# 运行容器
docker run --name ikun-chat \
           -d \
           -v $dbfile:/ikun-chat/db \
           -p 777:777 -p 8888:8888 \
registry.cn-hangzhou.aliyuncs.com/thread-docker-hub/ikun-chat
