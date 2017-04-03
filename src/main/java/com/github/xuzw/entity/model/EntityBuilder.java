package com.github.xuzw.entity.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月23日 下午3:11:36
 */
public class EntityBuilder {
    private List<String> shortNames = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();
    private Entity entity = new Entity();

    public EntityBuilder() {
        entity.setProperties(properties);
        entity.setShortNames(shortNames);
    }

    public EntityBuilder name(String name) {
        entity.setName(name);
        return this;
    }

    public EntityBuilder shortName(String shortName) {
        shortNames.add(shortName);
        return this;
    }

    public EntityBuilder shortNames(List<String> shortNames) {
        this.shortNames.addAll(shortNames);
        return this;
    }

    public EntityBuilder property(String key, String value) {
        properties.put(key, value);
        return this;
    }

    public EntityBuilder timestamp(long timestamp) {
        entity.setTimestamp(timestamp);
        return this;
    }

    public Entity build() {
        return entity;
    }
}
