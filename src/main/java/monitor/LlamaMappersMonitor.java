package monitor;

import monitor.persistencia.*;

public class LlamaMappersMonitor {

    public static void persist(String modo, Monitor monitor) {
        MonitorConfig monitorConfig = new MonitorConfig(monitor.getId(), monitor.getSize(), monitor.llamadosAString());
        if (modo.equals("txt")) {
            MonitorTXTMapper.getInstance("monitorConfig.txt").save(monitorConfig);
        } else if (modo.equals("json")) {
            MonitorJSONMapper.getInstance("monitorConfig.json").save(monitorConfig);
        } else if (modo.equals("xml")) {
            MonitorXMLMapper.getInstance("monitorConfig.xml").save(monitorConfig);
        } else {
            throw new IllegalArgumentException("Modo de persistencia no soportado: " + modo);
        }
    }

    public static Monitor load(String modo) throws RuntimeException {
        MonitorConfig monitorConfig;
        if (modo.equals("txt")) {
            monitorConfig = MonitorTXTMapper.getInstance("monitorConfig.txt").load();
        } else if (modo.equals("json")) {
            monitorConfig = MonitorJSONMapper.getInstance("monitorConfig.json").load();
        } else if (modo.equals("xml")) {
            monitorConfig = MonitorXMLMapper.getInstance("monitorConfig.xml").load();
        } else {
            throw new IllegalArgumentException("Modo de persistencia no soportado: " + modo);
        }
        return new Monitor(monitorConfig.getId(), monitorConfig.getSize(), monitorConfig.getLlamados());
    }
}
