package monitor.persistencia;

import shared.persistencia.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;;

public class MonitorXMLMapper extends AbstractFileMapper<MonitorConfig> {
    private static MonitorXMLMapper instance;
    String filePath = "monitorConfig.xml";

    private MonitorXMLMapper(String filePath) {
        super(filePath);
    }

    public static MonitorXMLMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new MonitorXMLMapper(filePath);
        }
        return instance;
    }

    @Override
    protected String serialize(MonitorConfig monitorConfig) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.writeValueAsString(monitorConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected MonitorConfig deserialize(String data) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.readValue(data, new TypeReference<MonitorConfig>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
