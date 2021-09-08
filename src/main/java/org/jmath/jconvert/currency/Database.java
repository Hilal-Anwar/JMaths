package org.jmath.jconvert.currency;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public  class Database {
    private final Properties properties;
    private final String filename;
    public Database(String name) {
        properties = new Properties();
        filename = System.getProperty("user.home") + "//" + name + ".dat";
        try (var fileInputStream = new FileInputStream(filename)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.err.println("No such file is present");
        }

    }

    public void setData(Object name, Object value) throws IOException {
        properties.put(name, value);
        try (FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
            properties.store(fileOutputStream, "Currencies Rate");
        }
    }

    String getData(Object key) {
        return properties.getProperty(key.toString());
    }

}
