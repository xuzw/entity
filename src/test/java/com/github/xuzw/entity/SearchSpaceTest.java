package com.github.xuzw.entity;

import com.alibaba.fastjson.JSON;
import com.github.xuzw.entity.api.EntitySearchSpace;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月23日 下午4:31:59
 */
public class SearchSpaceTest {
    public static void main(String[] args) throws Exception {
        EntitySearchSpace searchSpace = new EntitySearchSpace("/Users/xuzewei/tmp/entity.repository.graphdb/");
        searchSpace.loadEntityRepository("/Users/xuzewei/tmp/entity.repository");
        System.out.println(searchSpace.has("徐泽威"));
        System.out.println(searchSpace.has("泽威"));
        System.out.println(JSON.toJSONString(searchSpace.search("徐泽威")));
        searchSpace.shutdown();
    }
}
