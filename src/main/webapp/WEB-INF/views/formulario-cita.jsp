<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:choose><c:when test="${accionFormulario eq 'actualizar'}">Editar cita</c:when><c:otherwise>Registrar cita</c:otherwise></c:choose> | MediSoft</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
</head>
<body>
<main class="container narrow">
    <a class="back-link" href="${pageContext.request.contextPath}/citas">← Volver a las citas</a>
    <header class="page-header compact">
        <div>
            <p class="eyebrow">MEDISOFT · GESTIÓN CLÍNICA</p>
            <h1><c:choose><c:when test="${accionFormulario eq 'actualizar'}">Editar cita</c:when><c:otherwise>Registrar cita</c:otherwise></c:choose></h1>
            <p class="muted">Completa los datos del paciente y de la cita.</p>
        </div>
    </header>
    <section class="panel">
        <c:if test="${not empty error}">
            <div class="notice error"><c:out value="${error}"/></div>
        </c:if>
        <form action="${pageContext.request.contextPath}/citas" method="post" class="appointment-form">
            <input type="hidden" name="accion" value="<c:out value='${accionFormulario}'/>">
            <c:if test="${accionFormulario eq 'actualizar'}">
                <input type="hidden" name="id" value="<c:out value='${idFormulario}'/>">
            </c:if>
            <label for="documentoPaciente">Documento del paciente</label>
            <input id="documentoPaciente" name="documentoPaciente" maxlength="30" required
                   value="<c:out value='${empty param.documentoPaciente ? cita.documentoPaciente : param.documentoPaciente}'/>">

            <label for="nombrePaciente">Nombre del paciente</label>
            <input id="nombrePaciente" name="nombrePaciente" maxlength="120" required
                   value="<c:out value='${empty param.nombrePaciente ? cita.nombrePaciente : param.nombrePaciente}'/>">

            <label for="especialidad">Especialidad</label>
            <input id="especialidad" name="especialidad" maxlength="100" required
                   value="<c:out value='${empty param.especialidad ? cita.especialidad : param.especialidad}'/>">

            <label for="medico">Médico</label>
            <input id="medico" name="medico" maxlength="120" required
                   value="<c:out value='${empty param.medico ? cita.medico : param.medico}'/>">

            <label for="fechaHora">Fecha y hora</label>
            <input id="fechaHora" name="fechaHora" type="datetime-local" required
                   value="<c:out value='${empty param.fechaHora ? fechaHoraFormulario : param.fechaHora}'/>">

            <label for="estado">Estado</label>
            <select id="estado" name="estado" required>
                <c:set var="estadoElegido" value="${empty param.estado ? cita.estado : param.estado}"/>
                <option value="PROGRAMADA" ${estadoElegido eq 'PROGRAMADA' ? 'selected' : ''}>Programada</option>
                <option value="ATENDIDA" ${estadoElegido eq 'ATENDIDA' ? 'selected' : ''}>Atendida</option>
                <option value="CANCELADA" ${estadoElegido eq 'CANCELADA' ? 'selected' : ''}>Cancelada</option>
            </select>

            <div class="form-actions">
                <a class="button quiet" href="${pageContext.request.contextPath}/citas">Cancelar</a>
                <button class="button primary" type="submit">
                    <c:choose><c:when test="${accionFormulario eq 'actualizar'}">Guardar cambios</c:when><c:otherwise>Registrar cita</c:otherwise></c:choose>
                </button>
            </div>
        </form>
    </section>
    <footer>MediSoft · Atención organizada, cuidado cercano.</footer>
</main>
</body>
</html>
