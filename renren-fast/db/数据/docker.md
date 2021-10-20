# 1. docker

​	1.安装实用工并设置**稳定的**存储库。

```
 sudo yum install -y yum-utils

$ sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
```

​	2.安装*最新版本*的 Docker 发动机和容器

```
 sudo yum install docker-ce docker-ce-cli containerd.io
```

​	3.启动docker

```
sudo systemctl start docker
```

​	4.验证docker启动

```
sudo docket images
sudo docker -v
```

​	5.开启docker服务

```
sudo systemctl enable docker
```

​	6.镜像加速

```
	sudo mkdir -p /etc/docker 
	sudo tee /etc/docker/daemon.json <<-'EOF'
    { 
    "registry-mirrors": ["https://23391u9p.mirror.aliyuncs.com"] 
    } EOF 
    sudo systemctl daemon-reload sudo systemctl restart docker
```

# 2. docker 服务的安装



## 1.安装 My S Q L 5.7

```
docker pull mysql:5.7
```

2.配置 My S Q L 5.7

```
 docker run -p 3306:3306 --name mysql \
> -v /mydata/mysql/log:/var/log/mysql \
> -v /mydata/mysql/data:/var/lib/mysql \
> -v /mydata/mysql/conf:/etc/mysql \
> -e MYSQL_ROOT_PASSWORD=root \
> -d mysql:5.7
```

3.查看正在运行的服务

```
docker ps
```

4.配置My S Q L 5.7

```
vim /mydata/mysql/conf/my.cnf
```

```
client]
default-character-set=utf8
[mysql]
default-character-set=utf8
[mysqld]
init_connect='SET collation_connection=utf8_unicode_ci'
init_connect='SET NAMES utf8'
character-set-server=utf8
collation-server=utf8_unicode_ci
skip-character-set-client-handshake
skip-name-resolve
```

5.重启My S Q L服务

```
docker restart mysql
```

6跟随docker自启动

```
docker update mysql  --restart=always
```

1.报错:

```
IPv4 forwarding is disabled. Networking will not work
```

2.解决办法:

```
1.echo "net.ipv4.ip_forward=1" >>/etc/sysctl.conf
2.systemctl restart network && systemctl restart docker
3.sysctl net.ipv4.ip_forward
输出>>net.ipv4.ip_forward = 1
```



## 2.redis安装

```latex
1.
mkdir -p /mydata/redis/conf
2.
touch /mydata/redis/conf/redis.conf
3.
docker run -p 6379:6379 --name redis \
-v /mydata/redis/data:/data \
-v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf \
-d redis redis-server /etc/redis/redis.conf
4.
docker exec -it redis redis-cli
```

5跟随docker自启动

```
docker update redis  --restart=always
```

1.报错:

```
IPv4 forwarding is disabled. Networking will not work
```

2.解决办法:

```
1.echo "net.ipv4.ip_forward=1" >>/etc/sysctl.conf
2.systemctl restart network && systemctl restart docker
3.sysctl net.ipv4.ip_forward
输出>>net.ipv4.ip_forward = 1

```

