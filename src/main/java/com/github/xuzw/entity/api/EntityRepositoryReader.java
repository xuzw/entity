package com.github.xuzw.entity.api;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.xuzw.entity.api.EntityRepositoryFileFormat.LineType;
import com.github.xuzw.entity.api.EntityRepositoryFileFormat.Metadata;
import com.github.xuzw.entity.model.Entity;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年3月23日 下午3:07:49
 */
public class EntityRepositoryReader {
    private String path;
    private FileReader reader;
    private BufferedReader bReader;

    public EntityRepositoryReader(String path) throws FileNotFoundException {
        this.path = path;
        reader = new FileReader(path);
        bReader = new BufferedReader(reader);
    }

    public String getPath() {
        return path;
    }

    public Entity read() throws IOException, EntityRepositoryFileFormatException {
        String line = bReader.readLine();
        if (line == null) {
            return null;
        }
        JSONObject json = JSONObject.parseObject(line);
        LineType lineType = LineType.parse(json.getString(LineType.property_key));
        if (lineType == LineType.metadata_of_entity) {
            Metadata metadata = JSON.toJavaObject(json, Metadata.class);
            String nextLine = bReader.readLine();
            if (!_isValidSign(metadata.getSign(), nextLine)) {
                throw new EntityRepositoryFileFormatException("invalid sign");
            }
            return JSONObject.parseObject(nextLine, Entity.class);
        } else if (lineType == LineType.entity) {
            return JSON.toJavaObject(json, Entity.class);
        } else {
            return read();
        }
    }

    public void close() {
        IOUtils.closeQuietly(bReader);
        IOUtils.closeQuietly(reader);
    }

    private static boolean _isValidSign(String sign, String line) {
        return sign.equals(DigestUtils.md5Hex(line));
    }
}
