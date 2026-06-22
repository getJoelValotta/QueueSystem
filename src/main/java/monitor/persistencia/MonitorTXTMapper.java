package monitor.persistencia;

import shared.persistencia.*;

public class MonitorTXTMapper extends AbstractFileMapper<MonitorConfig> {
    private static MonitorTXTMapper instance;
    String filePath = "monitorConfig.txt";

    private MonitorTXTMapper(String filePath) {
        super(filePath);
    }

    public static MonitorTXTMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new MonitorTXTMapper(filePath);
        }
        return instance;
    }

    @Override
    protected String serialize(MonitorConfig monitorConfig) {
        // Pasa los datos de monitorConfig a formato txt
        // (Formato: idMonitor,(estado,cantLlamados,idPuesto,dni))
        return monitorConfig.getId() + "," + monitorConfig.getSize() + "," + monitorConfig.getLlamados();
    }

    @Override
    protected MonitorConfig deserialize(String data) {
        // Pasa los datos de formato txt a un objeto MonitorConfig
        String[] parts = data.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Formato de datos inválido para MonitorConfig: " + data);
        }
        return new MonitorConfig(parts[0], Integer.parseInt(parts[1]), parts[2]);
    }

}
