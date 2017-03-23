package com.github.xuzw.entity;

import com.alibaba.fastjson.JSONObject;
import com.github.xuzw.entity.api.RepositoryReader;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月23日 下午3:36:00
 */
public class ReaderTest {
    public static void main(String[] args) throws Exception {
        String path = "/Users/xuzewei/tmp/entity.repository";
        RepositoryReader repositoryReader = new RepositoryReader(path);
        System.out.println(JSONObject.toJSONString(repositoryReader.read()));
        System.out.println(JSONObject.toJSONString(repositoryReader.read()));
        System.out.println(JSONObject.toJSONString(repositoryReader.read()));
        System.out.println(JSONObject.toJSONString(repositoryReader.read()));
        repositoryReader.close();
    }
}
