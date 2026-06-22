package totem;

import totem.persistencia.*;

public class LlamaMappersTotem {
    public static void persistir(Totem totem) {
        TotemConfigTXTMapper.getInstance("totemConfig.txt").save(totem.getId());
    }

    public static Totem recuperar() throws RuntimeException {
        String id = TotemConfigTXTMapper.getInstance("totemConfig.txt").load();
        return new Totem(id, null);
    }

}
