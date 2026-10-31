package com.albertosena.gestionproductos;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {

    private static final Path ARCHIVO_CONFIGURACION =
            Paths.get(System.getProperty("user.home"), "db.properties");

    public static Connection conectar() throws SQLException {

        Properties propiedades = new Properties();

        // Carga la configuración externa para proteger las credenciales.
        try (InputStream entrada =
                     Files.newInputStream(ARCHIVO_CONFIGURACION)) {

            propiedades.load(entrada);

        } catch (IOException e) {
            throw new SQLException(
                    "No se pudo leer el archivo db.properties.", e);
        }

        String url = propiedades.getProperty("db.url");
        String usuario = propiedades.getProperty("db.user");
        String contrasena = propiedades.getProperty("db.password");

        if (url == null || usuario == null || contrasena == null
                || url.isBlank() || usuario.isBlank()
                || contrasena.isBlank()) {
            throw new SQLException(
                    "Faltan propiedades en el archivo db.properties.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "No se encontró el controlador JDBC de MySQL.", e);
        }

        return DriverManager.getConnection(url, usuario, contrasena);
    }
}