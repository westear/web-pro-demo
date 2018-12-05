package com.mmall.demo.elasticJob;

import java.util.ArrayList;
import java.util.List;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

public class MyElasticDataflowJob implements DataflowJob<String>{

	public List<String> fetchData(ShardingContext shardingContext) {
		switch (shardingContext.getShardingItem()) {
        case 0: 
            // get data from database by sharding item 0
            List<String> data1 = new ArrayList<String>();
            data1.add("get data from database by sharding item 0");
            return data1;
        case 1: 
            // get data from database by sharding item 1
            List<String> data2 = new ArrayList<String>();
            data2.add("get data from database by sharding item 1");
            return data2;
        case 2: 
            // get data from database by sharding item 2
            List<String> data3 = new ArrayList<String>();
            data3.add("get data from database by sharding item 2");
            return data3;
        // case n: ...
    }
    return null;
	}

	public void processData(ShardingContext shardingContext, List<String> data) {
		int count=0;
        // process data
        // ...
        for (String string : data) {
            count++;
            System.out.println(string);
            if (count>10) {
                return;
            }
        }
	}

}
