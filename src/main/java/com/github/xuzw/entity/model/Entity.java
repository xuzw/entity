package com.github.xuzw.entity.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月23日 下午2:26:08
 */
public class Entity {
    private String name;
    private List<String> shortNames;
    private Map<String, String> properties;
    private long timestamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getShortNames() {
        return shortNames;
    }

    public void setShortNames(List<String> shortNames) {
        this.shortNames = shortNames;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public TreeMap<String, Object> toTreeMap() {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("name", name);
        treeMap.put("shortNames", shortNames);
        treeMap.put("properties", properties);
        treeMap.put("timestamp", timestamp);
        return treeMap;
    }

    public boolean hasName(String name) {
        if (this.name.equalsIgnoreCase(name)) {
            return true;
        }
        if (shortNames != null) {
            for (String shortName : shortNames) {
                if (shortName.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }
}
