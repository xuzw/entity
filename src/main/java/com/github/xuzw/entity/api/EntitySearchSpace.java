package com.github.xuzw.entity.api;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.github.xuzw.entity.model.Entity;
import com.github.xuzw.entity.model.EntityBuilder;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月23日 下午3:37:57
 */
public class EntitySearchSpace {
    public static final Label label_entity = Label.label("entity");
    public static final String node_prop_key_name = "name";
    public static final String node_prop_key_shortNames = "shortNames";
    public static final String node_prop_key_timestamp = "timestamp";
    private String workPath;
    private GraphDatabaseService graphDb;

    public EntitySearchSpace(String workPath) {
        this.workPath = workPath;
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(workPath));
    }

    public String getWorkPath() {
        return workPath;
    }

    public GraphDatabaseService getGraphDb() {
        return graphDb;
    }

    private Node _findNode(String name) {
        return graphDb.findNode(label_entity, node_prop_key_name, name);
    }

    public boolean has(String name) {
        boolean has = false;
        try (Transaction tx = graphDb.beginTx()) {
            if (_findNode(name) != null) {
                has = true;
            }
            tx.success();
        }
        return has;
    }

    public Node findNode(String name) {
        Node node = null;
        try (Transaction tx = graphDb.beginTx()) {
            node = _findNode(name);
            tx.success();
        }
        return node;
    }

    public Entity search(String string) {
        EntityBuilder builder = null;
        String keyword = string.trim();
        try (Transaction tx = graphDb.beginTx()) {
            Node node = _findNode(keyword);
            if (node != null) {
                builder = new EntityBuilder();
                builder.name(keyword).timestamp((long) node.getProperty(node_prop_key_timestamp));
                if (node.hasProperty(node_prop_key_shortNames)) {
                    String[] oldShortNames = (String[]) node.getProperty(node_prop_key_shortNames);
                    for (String oldShortName : oldShortNames) {
                        builder.shortName(oldShortName);
                    }
                }
                Map<String, Object> properties = node.getAllProperties();
                for (String property : properties.keySet()) {
                    if (property.equals(node_prop_key_name) || property.equals(node_prop_key_shortNames) || property.equals(node_prop_key_timestamp)) {
                        continue;
                    }
                    builder.property(property, (String) properties.get(property));
                }
            }
            tx.success();
        }
        return builder == null ? null : builder.build();
    }

    private static boolean _isBlank(List<String> strings) {
        return strings == null || strings.isEmpty();
    }

    private static boolean _isBlank(Map<String, String> map) {
        return map == null || map.isEmpty();
    }

    public void load(Entity entity) {
        String name = entity.getName();
        List<String> shortNames = entity.getShortNames();
        Map<String, String> properties = entity.getProperties();
        try (Transaction tx = graphDb.beginTx()) {
            Node node = _findNode(name);
            if (node == null) {
                node = graphDb.createNode(label_entity);
                node.setProperty(node_prop_key_name, name);
            }
            node.setProperty(node_prop_key_timestamp, entity.getTimestamp());
            if (!_isBlank(shortNames)) {
                Set<String> mergeShortNames = new HashSet<>();
                if (node.hasProperty(node_prop_key_shortNames)) {
                    String[] oldShortNames = (String[]) node.getProperty(node_prop_key_shortNames);
                    for (String oldShortName : oldShortNames) {
                        mergeShortNames.add(oldShortName);
                    }
                }
                for (String shortName : shortNames) {
                    mergeShortNames.add(shortName);
                }
                node.setProperty(node_prop_key_shortNames, mergeShortNames.toArray(new String[0]));
            }
            if (!_isBlank(properties)) {
                for (String property : properties.keySet()) {
                    if (StringUtils.isNoneBlank(properties.get(property))) {
                        node.setProperty(property, properties.get(property));
                    }
                }
            }
            tx.success();
        }
    }

    public void unload(Entity entity) {
        try (Transaction tx = graphDb.beginTx()) {
            Node node = _findNode(entity.getName());
            node.delete();
            tx.success();
        }
    }

    public void loadEntityRepository(String repositoryPath) throws IOException, EntityRepositoryFileFormatException {
        EntityRepositoryReader repositoryReader = new EntityRepositoryReader(repositoryPath);
        Entity entity = null;
        while ((entity = repositoryReader.read()) != null) {
            load(entity);
        }
        repositoryReader.close();
    }

    public void unloadEntityRepository(String repositoryPath) throws IOException, EntityRepositoryFileFormatException {
        EntityRepositoryReader repositoryReader = new EntityRepositoryReader(repositoryPath);
        Entity entity = null;
        while ((entity = repositoryReader.read()) != null) {
            unload(entity);
        }
        repositoryReader.close();
    }

    public void shutdown() {
        graphDb.shutdown();
    }
}
