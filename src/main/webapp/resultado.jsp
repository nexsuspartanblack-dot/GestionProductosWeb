<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Resultados | Rolo's Motos Cars</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/estilos.css">
</head>
<body>

    <header class="navbar">
        <div class="brand">
            <div class="brand-icon">R</div>
            <span>Rolo's Motos Cars</span>
        </div>

        <span class="nav-label">Gestión de repuestos</span>
    </header>

    <main class="container">

        <section class="card result-card">

            <div class="result-header">

                <div class="success-icon ${encontrado ? '' : 'error-icon'}">
                    ${encontrado ? '✓' : '!'}
                </div>

                <span class="eyebrow">Resultado de la operación</span>

                <h1 style="font-size:30px">${titulo}</h1>

                <p class="subtitle" style="margin:0 auto">
                    Rolo's Motos Cars · Sistema de gestión de repuestos.
                </p>

            </div>

            <div class="result-data">

                <div class="data-row">
                    <span class="data-label">Método HTTP</span>

                    <span class="method ${metodo == 'GET' ? 'get' : ''}">
                        ${metodo}
                    </span>
                </div>

                <div class="data-row">
                    <span class="data-label">Nombre del repuesto</span>

                    <span class="data-value">${nombre}</span>
                </div>

                <div class="data-row">
                    <span class="data-label">Precio (COP) / Información</span>

                    <span class="data-value">${precio}</span>
                </div>

                <% if (request.getAttribute("cantidad") != null) { %>
                <div class="data-row">
                    <span class="data-label">Cantidad disponible</span>

                    <span class="data-value">${cantidad}</span>
                </div>
                <% } %>

            </div>

            <a class="button"
               href="${pageContext.request.contextPath}/index.html">
                ← Volver al panel
            </a>

        </section>

    </main>

    <footer class="footer">
        Rolo's Motos Cars · ADSO · Análisis y Desarrollo de Software
    </footer>

</body>
</html>
