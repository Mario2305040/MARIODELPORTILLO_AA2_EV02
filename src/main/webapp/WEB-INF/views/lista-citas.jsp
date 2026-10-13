<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Citas | MediSoft</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
</head>
<body>
<main class="container">
    <header class="page-header">
        <div>
            <p class="eyebrow">MEDISOFT · GESTIÓN CLÍNICA</p>
            <h1>Citas médicas</h1>
            <p class="muted">Administra las citas programadas de tus pacientes.</p>
        </div>
        <a class="button primary" href="${pageContext.request.contextPath}/citas?accion=nuevo">Registrar cita</a>
    </header>

    <c:if test="${not empty sessionScope.mensaje}">
        <div class="notice success"><c:out value="${sessionScope.mensaje}"/></div>
        <c:remove var="mensaje" scope="session"/>
    </c:if>
    <c:if test="${not empty mensaje}">
        <div class="notice"><c:out value="${mensaje}"/></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="notice error"><c:out value="${error}"/></div>
    </c:if>

    <section class="panel search-panel" aria-labelledby="search-title">
        <div>
            <h2 id="search-title">Consultar por ID</h2>
            <p class="muted">La búsqueda se envía mediante GET.</p>
        </div>
        <form action="${pageContext.request.contextPath}/citas" method="get" class="search-form">
            <input type="hidden" name="accion" value="buscar">
            <label for="idBusqueda">ID de la cita</label>
            <input id="idBusqueda" name="id" type="number" min="1" required>
            <button class="button secondary" type="submit">Buscar</button>
            <a class="button quiet" href="${pageContext.request.contextPath}/citas">Ver todas</a>
        </form>
    </section>

    <section class="panel" aria-labelledby="list-title">
        <div class="section-heading">
            <div>
                <h2 id="list-title">Listado de citas</h2>
                <p class="muted">Consulta, edita o elimina un registro.</p>
            </div>
            <span class="count"><c:out value="${citas.size()}"/> registros</span>
        </div>
        <c:choose>
            <c:when test="${empty citas}">
                <div class="empty-state">No hay citas para mostrar. Registra una cita para comenzar.</div>
            </c:when>
            <c:otherwise>
                <div class="table-wrap">
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th><th>Paciente</th><th>Especialidad</th><th>Médico</th>
                            <th>Fecha y hora</th><th>Estado</th><th>Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="cita" items="${citas}">
                            <tr>
                                <td><strong><c:out value="${cita.id}"/></strong></td>
                                <td><c:out value="${cita.nombrePaciente}"/><small><c:out value="${cita.documentoPaciente}"/></small></td>
                                <td><c:out value="${cita.especialidad}"/></td>
                                <td><c:out value="${cita.medico}"/></td>
                                <td><c:out value="${cita.fechaHora}"/></td>
                                <td><span class="status"><c:out value="${cita.estado}"/></span></td>
                                <td class="actions">
                                    <a class="button small secondary" href="${pageContext.request.contextPath}/citas?accion=editar&amp;id=${cita.id}">Editar</a>
                                    <form action="${pageContext.request.contextPath}/citas" method="post" onsubmit="return confirm('¿Eliminar esta cita?');">
                                        <input type="hidden" name="accion" value="eliminar">
                                        <input type="hidden" name="id" value="${cita.id}">
                                        <button class="button small danger" type="submit">Eliminar</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
    <footer>MediSoft · Atención organizada, cuidado cercano.</footer>
</main>
</body>
</html>
