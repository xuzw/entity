package com.github.xuzw.entity;

import com.github.xuzw.entity.api.EntityRepositoryWriter;
import com.github.xuzw.entity.model.EntityBuilder;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月23日 下午3:19:09
 */
public class WriterTest {
    public static void main(String[] args) throws Exception {
        String path = "/Users/xuzewei/tmp/entity.repository";
        EntityRepositoryWriter repositoryWriter = new EntityRepositoryWriter(path);
        repositoryWriter.append(new EntityBuilder().name("徐泽威").shortName("我").property("性别", "男").property("出生日期", "1991-02-17").property("手机号", "13426290598").property("身份证号", "410221199102179874").property("居住地", "b102-168家园-来广营东路-北京市").timestamp(System.currentTimeMillis()).build());
        repositoryWriter.append(new EntityBuilder().name("徐泽威").shortName("泽威").build());
        repositoryWriter.append(new EntityBuilder().name("电子版圣经").property("书签", "徒26章").build());
        repositoryWriter.close();
    }
}
