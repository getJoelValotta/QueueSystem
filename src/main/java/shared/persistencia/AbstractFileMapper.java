package shared.persistencia;

import java.io.*;

public abstract class AbstractFileMapper<T> {
    protected final String filePath;

    public AbstractFileMapper(String filePath) {
        this.filePath = filePath;
    }

    public final void save(T object) {
        System.out.println("Guardando objeto en " + filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(serialize(object));
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo archivo", e);
        }

    }

    public final T load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            if (content.length() > 0 && content.charAt(content.length() - 1) == '\n') {
                content.setLength(content.length() - 1);
            }
            return deserialize(content.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo", e);
        }
    }

    protected abstract String serialize(T object);

    protected abstract T deserialize(String data);

}