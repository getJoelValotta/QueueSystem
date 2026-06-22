package server.persistencia;

import java.awt.List;
import java.util.LinkedList;

import shared.persistencia.AbstractFileMapper;

import server.ServerConfig;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerConfigJSONMapper extends AbstractFileMapper<ServerConfig> {
    private static ServerConfigJSONMapper instance;
    String filePath = "serverConfig.json";

    private ServerConfigJSONMapper(String filePath) {
        super(filePath);
    }

    public static ServerConfigJSONMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new ServerConfigJSONMapper(filePath);
        }
        return instance;
    }

    @Override
    protected String serialize(ServerConfig config) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(config);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected ServerConfig deserialize(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, ServerConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
