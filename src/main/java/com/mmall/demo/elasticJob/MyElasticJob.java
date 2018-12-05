package com.mmall.demo.elasticJob;


import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class MyElasticJob implements SimpleJob {

	public void execute(ShardingContext shardingContext) {
		int shardingItem = shardingContext.getShardingItem();
		switch (shardingItem) {
		case 0:
			System.out.println("MyElasticJob - 0");
			break;
		case 1:
			System.out.println("MyElasticJob - 1");
			break;
		case 2:
			System.out.println("MyElasticJob - 2");
			break;
		default:
			System.out.println("MyElasticJob - default");
		}
	}

}
