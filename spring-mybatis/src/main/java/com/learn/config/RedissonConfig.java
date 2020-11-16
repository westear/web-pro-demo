package com.learn.config;

import com.learn.redisson.RedissonUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
@PropertySource(value = {"classpath:redis.properties"})
@Import(value = {RedissonUtil.class})
public class RedissonConfig {

    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Bean
    public RedissonClient redisson() {
        Config config = new Config();

        //指定使用单节点部署方式
//        config.useSentinelServers().addSentinelAddress("redis://47.115.46.150:6379");

        //指定使用哨兵部署方式
//        config.useSentinelServers()
//                //设置sentinel.conf配置里的sentinel别名
//                //比如sentinel.conf里配置为sentinel monitor my-sentinel-name 127.0.0.1 6379 2,那么这里就配置my-sentinel-name
//                .setMasterName("my-sentinel-name")
//                //这里设置sentinel节点的服务IP和端口，sentinel是采用Paxos拜占庭协议，一般sentinel至少3个节点
//                //记住这里不是配置redis节点的服务端口和IP，sentinel会自己把请求转发给后面monitor的redis节点
//                .addSentinelAddress("redis://127.0.0.1:26379")
//                .addSentinelAddress("redis://127.0.0.1:26389")
//                .addSentinelAddress("redis://127.0.0.1:26399");

        //使用集群部署方式
        String clusterHost = env.getProperty("cluster.host");
        String[] clusterPort = Objects.requireNonNull(env.getProperty("cluster.ports")).split(",");

        ClusterServersConfig clusterServersConfig = config.useClusterServers();

        // 集群状态扫描间隔时间，单位是毫秒
        clusterServersConfig.setScanInterval(2000);
        //等待节点回复命令的时间。该时间从命令发送成功时开始计时。默认值：3000
        clusterServersConfig.setTimeout(5000);
        //命令重试发送时间间隔，单位：毫秒。默认值：1500
        clusterServersConfig.setRetryInterval(2000);
        //如果尝试达到 retryAttempts（命令失败重试次数） 仍然不能将命令发送至某个指定的节点时，将抛出错误。默认值：3次
        clusterServersConfig.setRetryAttempts(3);

        for (int i = 0; i < clusterPort.length; i++) {
            clusterPort[i] = clusterHost + ":" + clusterPort[i];
//            clusterServersConfig = clusterServersConfig.addNodeAddress(clusterHost + ":" + clusterPort[i]);
        }

        clusterServersConfig.addNodeAddress(clusterPort);

        //创建客户端(发现这一非常耗时，基本在2秒-4秒左右)
        return Redisson.create(config);
    }
}
