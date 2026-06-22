package monitor.persistencia;

import shared.persistencia.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MonitorJSONMapper extends AbstractFileMapper<MonitorConfig> {
    private static MonitorJSONMapper instance;
    String filePath = "monitorConfig.json";

    private MonitorJSONMapper(String filePath) {
        super(filePath);
    }

    public static MonitorJSONMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new MonitorJSONMapper(filePath);
        }
        return instance;
    }

    @Override
    protected String serialize(MonitorConfig monitorConfig) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(monitorConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected MonitorConfig deserialize(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, new TypeReference<MonitorConfig>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}