# 连接Pulsar

### 安装库pulsar-client

增加下载速度 

```
repositories {
    maven { url 'https://maven.aliyun.com/repository/public/' }
    maven { url 'https://maven.aliyun.com/repository/google/'}
    maven { url 'https://maven.aliyun.com/repository/jcenter/'}
    mavenCentral()
}
```



```
def pulsarVersion = '2.9.1'

dependencies {
    compile group: 'org.apache.pulsar', name: 'pulsar-client', version: pulsarVersion
}
```

