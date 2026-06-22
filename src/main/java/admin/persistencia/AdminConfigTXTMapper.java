package admin.persistencia;

import shared.persistencia.*;
import admin.Admin;

public class AdminConfigTXTMapper extends AbstractFileMapper<Admin> {
    private static AdminConfigTXTMapper instance;
    String filePath = "adminConfig.txt";

    private AdminConfigTXTMapper(String filePath) {
        super(filePath);
    }

    public static AdminConfigTXTMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new AdminConfigTXTMapper(filePath);
        }
        return instance;
    }

    @Override
    protected String serialize(Admin admin) {
        return admin.getMetodoPersistencia() + "," + admin.getMetodoEncriptacion();
    }

    @Override
    protected Admin deserialize(String data) {
        String[] parts = data.split(",");
        return new Admin(parts[0], parts[1]);
    }

}