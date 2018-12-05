package com.mmall.demo.elasticJob;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobRootConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class JobDemo {
	public static void main(String[] args) throws UnknownHostException {
		 System.out.println("Start...");
	        System.out.println(InetAddress.getLocalHost());
	        new JobScheduler(createRegistryCenter(), createSimpleJobConfiguration()).init();
//	        new JobScheduler(createRegistryCenter(), createDataflowJobConfiguration()).init();
	}
	
	private static CoordinatorRegistryCenter createRegistryCenter() {
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(
                new ZookeeperConfiguration("192.168.17.15:2181", "dd-demo3"));
        regCenter.init();
        return regCenter;
    }

    private static LiteJobConfiguration createSimpleJobConfiguration() {
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder("SimpleJobDemo", "0/15 * * * * ?", 4).build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, MyElasticJob.class.getCanonicalName());
        // 定义Lite作业根配置
        JobRootConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();

        return (LiteJobConfiguration) simpleJobRootConfig;

    }

    private static LiteJobConfiguration createDataflowJobConfiguration() {
        // 定义作业核心配置
        JobCoreConfiguration dataflowCoreConfig = JobCoreConfiguration.newBuilder("DataflowJob", "0/30 * * * * ?", 4).build();
        // 定义DATAFLOW类型配置
        DataflowJobConfiguration dataflowJobConfig = new DataflowJobConfiguration(dataflowCoreConfig, MyElasticDataflowJob.class.getCanonicalName(), false);
        // 定义Lite作业根配置
        JobRootConfiguration dataflowJobRootConfig = LiteJobConfiguration.newBuilder(dataflowJobConfig).build();

        return (LiteJobConfiguration) dataflowJobRootConfig;
    }
}
