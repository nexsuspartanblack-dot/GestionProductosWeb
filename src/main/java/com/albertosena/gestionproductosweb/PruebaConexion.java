package com.albertosena.gestionproductos;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {

        try (Connection conexion = Conexion.conectar()) {

            if (conexion != null && !conexion.isClosed()) {
                System.out.println("================================");
                System.out.println("CONEXION EXITOSA CON MYSQL");
                System.out.println("Base de datos: ejercicio_productos");
                System.out.println("================================");
            }

        } catch (SQLException e) {

            System.out.println("ERROR AL CONECTAR CON MYSQL");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
}