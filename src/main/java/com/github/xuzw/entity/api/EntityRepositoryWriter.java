package com.github.xuzw.entity.api;

import java.io.IOException;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;

import com.alibaba.fastjson.JSON;
import com.github.xuzw.entity.api.EntityRepositoryFileFormat.LineType;
import com.github.xuzw.entity.api.EntityRepositoryFileFormat.Metadata;
import com.github.xuzw.entity.model.Entity;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月23日 下午2:53:50
 */
public class EntityRepositoryWriter {
    private String path;
    private FileWriterWithEncoding writer;

    public EntityRepositoryWriter(String path) throws IOException {
        this.path = path;
        writer = new FileWriterWithEncoding(path, EntityRepositoryFileFormat.encoding, true);
    }

    public String getPath() {
        return path;
    }

    public void append(Entity entity) throws IOException {
        String entityLine = _toJsonLine(entity.toTreeMap(), LineType.entity);
        String metadataLine = _toJsonLine(_buildMetadata(entityLine).toTreeMap(), LineType.metadata_of_entity);
        StringBuffer sb = new StringBuffer();
        sb.append(metadataLine).append(EntityRepositoryFileFormat.line_separator);
        sb.append(entityLine).append(EntityRepositoryFileFormat.line_separator);
        writer.append(sb.toString());
    }

    public void close() {
        IOUtils.closeQuietly(writer);
    }

    private static String _toJsonLine(TreeMap<String, Object> treeMap, LineType lineType) {
        treeMap.put(LineType.property_key, lineType.getValue());
        return JSON.toJSONString(treeMap);
    }

    private static Metadata _buildMetadata(String line) {
        Metadata metadata = new Metadata();
        metadata.setSign(_sign(line));
        return metadata;
    }

    private static String _sign(String line) {
        return DigestUtils.md5Hex(line);
    }
}
