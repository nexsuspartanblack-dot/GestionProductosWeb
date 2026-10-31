
package com.albertosena.gestionproductos;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/registrar")
public class RegistroServlet extends HttpServlet {

    private static final int LONGITUD_MAXIMA_NOMBRE = 100;

    // Registra un repuesto en MySQL mediante POST.
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String precioTexto = request.getParameter("precio");
        String cantidadTexto = request.getParameter("cantidad");

        if (nombre == null || nombre.isBlank()
                || precioTexto == null || precioTexto.isBlank()
                || cantidadTexto == null || cantidadTexto.isBlank()) {

            mostrarResultado(request, response,
                    "Datos incompletos",
                    "Debes ingresar el nombre, el precio y la cantidad.",
                    false, "POST");
            return;
        }

        nombre = nombre.trim();

        if (nombre.length() > LONGITUD_MAXIMA_NOMBRE) {
            mostrarResultado(request, response,
                    "Nombre demasiado largo",
                    "El nombre del repuesto no puede superar los 100 caracteres.",
                    false, "POST");
            return;
        }

        try {
            BigDecimal precio = new BigDecimal(precioTexto);
            int cantidad = Integer.parseInt(cantidadTexto);

            if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarResultado(request, response,
                        "Precio inválido",
                        "El precio debe ser mayor que cero.",
                        false, "POST");
                return;
            }

            if (precio.scale() > 2) {
                mostrarResultado(request, response,
                        "Formato de precio inválido",
                        "El precio no puede tener más de dos decimales.",
                        false, "POST");
                return;
            }

            if (cantidad < 0) {
                mostrarResultado(request, response,
                        "Cantidad inválida",
                        "La cantidad disponible no puede ser negativa.",
                        false, "POST");
                return;
            }

            String sql = "INSERT INTO productos (nombre, precio, cantidad) "
                    + "VALUES (?, ?, ?)";

            try (Connection conexion = Conexion.conectar();
                 PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setString(1, nombre);
                consulta.setBigDecimal(2, precio);
                consulta.setInt(3, cantidad);

                consulta.executeUpdate();
            }

            request.setAttribute("titulo", "Repuesto registrado");
            request.setAttribute("nombre", nombre);
            request.setAttribute("precio", precio.toPlainString());
            request.setAttribute("cantidad", cantidad);
            request.setAttribute("encontrado", true);

        } catch (NumberFormatException e) {

            mostrarResultado(request, response,
                    "Formato de datos inválido",
                    "Verifica que el precio sea numérico y la cantidad sea un número entero.",
                    false, "POST");
            return;

        } catch (SQLException e) {

            getServletContext().log("Error al registrar el repuesto", e);

            mostrarResultado(request, response,
                    "Error al registrar",
                    "No fue posible guardar el repuesto en la base de datos.",
                    false, "POST");
            return;
        }

        request.setAttribute("metodo", "POST");

        request.getRequestDispatcher("/resultado.jsp")
                .forward(request, response);
    }

    // Consulta un repuesto por su nombre mediante GET.
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");

        if (nombre == null || nombre.isBlank()) {
            mostrarResultado(request, response,
                    "Nombre requerido",
                    "Escribe el nombre del repuesto que deseas consultar.",
                    false, "GET");
            return;
        }

        nombre = nombre.trim();

        if (nombre.length() > LONGITUD_MAXIMA_NOMBRE) {
            mostrarResultado(request, response,
                    "Nombre demasiado largo",
                    "El nombre de búsqueda no puede superar los 100 caracteres.",
                    false, "GET");
            return;
        }

        String sql = "SELECT nombre, precio, cantidad FROM productos "
                + "WHERE LOWER(nombre) = LOWER(?) LIMIT 1";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, nombre);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    request.setAttribute("titulo", "Repuesto encontrado");
                    request.setAttribute("nombre",
                            resultado.getString("nombre"));
                    request.setAttribute("precio",
                            resultado.getBigDecimal("precio").toPlainString());
                    request.setAttribute("cantidad",
                            resultado.getInt("cantidad"));
                    request.setAttribute("encontrado", true);

                } else {
                    request.setAttribute("titulo", "Repuesto no encontrado");
                    request.setAttribute("nombre", nombre);
                    request.setAttribute("precio",
                            "Este repuesto no se encuentra registrado en la base de datos.");
                    request.setAttribute("encontrado", false);
                }
            }

        } catch (SQLException e) {

            getServletContext().log("Error al consultar el repuesto", e);

            mostrarResultado(request, response,
                    "Error en la consulta",
                    "No fue posible consultar la base de datos.",
                    false, "GET");
            return;
        }

        request.setAttribute("metodo", "GET");

        request.getRequestDispatcher("/resultado.jsp")
                .forward(request, response);
    }

    // Centraliza la presentación de mensajes.
    private void mostrarResultado(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String titulo,
                                  String mensaje,
                                  boolean encontrado,
                                  String metodo)
            throws ServletException, IOException {

        request.setAttribute("titulo", titulo);
        request.setAttribute("nombre", "");
        request.setAttribute("precio", mensaje);
        request.setAttribute("encontrado", encontrado);
        request.setAttribute("metodo", metodo);

        request.getRequestDispatcher("/resultado.jsp")
                .forward(request, response);
    }
}
