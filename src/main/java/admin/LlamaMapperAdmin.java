package admin;

import admin.persistencia.*;

public class LlamaMapperAdmin {
    public static void persistir(Admin admin) {
        AdminConfigTXTMapper.getInstance("adminConfig.txt").save(admin);
    }

    public static Admin recupera() throws RuntimeException {
        return AdminConfigTXTMapper.getInstance("adminConfig.txt").load();
    }
}
