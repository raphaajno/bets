package com.betfair.aping;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class UTF8PropertiesLoader {

    public static ResourceBundle loadBundle(String baseName, Locale locale) {
        try {
            // Usamos a instância da classe para obter o getResourceAsStream
            String fileName ="messages_pt_BR.properties";
            InputStream inputStream = UTF8PropertiesLoader.class.getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new FileNotFoundException("Arquivo de propriedades não encontrado: " + fileName);
            }

            // Criar um Reader com a codificação UTF-8
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            Properties properties = new Properties();
            properties.load(reader);

            return new PropertyResourceBundle(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
