package co.medisoft.citas.web;

import co.medisoft.citas.dao.CitaDao;
import co.medisoft.citas.model.Cita;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/** Controlador web MVC para el ciclo de vida de las citas de MediSoft. */
@WebServlet(name = "CitaServlet", urlPatterns = "/citas")
public class CitaServlet extends HttpServlet {
    private static final DateTimeFormatter FORMATO_HTML = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final CitaDao citaDao = new CitaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        try {
            if ("nuevo".equals(accion)) {
                mostrarFormulario(request, response, citaVacia(), "crear", 0);
            } else if ("editar".equals(accion)) {
                editar(request, response);
            } else if ("buscar".equals(accion)) {
                buscar(request, response);
            } else {
                listar(request, response);
            }
        } catch (SQLException exception) {
            getServletContext().log("Error de base de datos al procesar una solicitud GET de citas.", exception);
            mostrarError(request, response, "No se pudo consultar la información. Revisa la conexión con MySQL.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        try {
            if ("crear".equals(accion)) {
                Cita cita = leerCita(request);
                long id = citaDao.insertar(cita);
                redirigirConMensaje(request, response, "Cita registrada. ID: " + id);
            } else if ("actualizar".equals(accion)) {
                long id = leerId(request.getParameter("id"));
                Cita cita = leerCita(request);
                boolean actualizada = citaDao.actualizar(id, cita);
                redirigirConMensaje(request, response,
                        actualizada ? "Cita actualizada correctamente." : "No se encontró la cita indicada.");
            } else if ("eliminar".equals(accion)) {
                long id = leerId(request.getParameter("id"));
                boolean eliminada = citaDao.eliminar(id);
                redirigirConMensaje(request, response,
                        eliminada ? "Cita eliminada correctamente." : "No se encontró la cita indicada.");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
            }
        } catch (IllegalArgumentException | DateTimeParseException exception) {
            mostrarFormularioConError(request, response, exception.getMessage());
        } catch (SQLException exception) {
            getServletContext().log("Error de base de datos al procesar una solicitud POST de citas.", exception);
            mostrarFormularioConError(request, response,
                    "No se pudo guardar la información. Revisa los datos y la conexión con MySQL.");
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        request.setAttribute("citas", citaDao.listarTodas());
        request.getRequestDispatcher("/WEB-INF/views/lista-citas.jsp").forward(request, response);
    }

    private void buscar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            long id = leerId(request.getParameter("id"));
            Optional<Cita> resultado = citaDao.buscarPorId(id);
            request.setAttribute("citas", resultado.map(List::of).orElseGet(List::of));
            if (resultado.isEmpty()) {
                request.setAttribute("mensaje", "No se encontró una cita con ese ID.");
            }
        } catch (IllegalArgumentException exception) {
            request.setAttribute("citas", List.of());
            request.setAttribute("mensaje", exception.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/views/lista-citas.jsp").forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            long id = leerId(request.getParameter("id"));
            Optional<Cita> resultado = citaDao.buscarPorId(id);
            if (resultado.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/citas?mensaje=No%20se%20encontr%C3%B3%20la%20cita");
                return;
            }
            mostrarFormulario(request, response, resultado.get(), "actualizar", id);
        } catch (IllegalArgumentException exception) {
            response.sendRedirect(request.getContextPath() + "/citas?mensaje=ID%20de%20cita%20no%20v%C3%A1lido");
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response,
                                   Cita cita, String accion, long id)
            throws ServletException, IOException {
        request.setAttribute("cita", cita);
        request.setAttribute("accionFormulario", accion);
        request.setAttribute("idFormulario", id);
        request.setAttribute("fechaHoraFormulario", cita.getFechaHora().format(FORMATO_HTML));
        request.getRequestDispatcher("/WEB-INF/views/formulario-cita.jsp").forward(request, response);
    }

    private void mostrarFormularioConError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        Cita valorInicial = citaVacia();
        request.setAttribute("cita", valorInicial);
        request.setAttribute("accionFormulario", "actualizar".equals(accion) ? "actualizar" : "crear");
        request.setAttribute("idFormulario", request.getParameter("id"));
        request.setAttribute("fechaHoraFormulario", "");
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("/WEB-INF/views/formulario-cita.jsp").forward(request, response);
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.setAttribute("citas", List.of());
        request.getRequestDispatcher("/WEB-INF/views/lista-citas.jsp").forward(request, response);
    }

    private static Cita leerCita(HttpServletRequest request) {
        String documento = validarTexto(request.getParameter("documentoPaciente"), "Documento", 30);
        String nombre = validarTexto(request.getParameter("nombrePaciente"), "Nombre del paciente", 120);
        String especialidad = validarTexto(request.getParameter("especialidad"), "Especialidad", 100);
        String medico = validarTexto(request.getParameter("medico"), "Médico", 120);
        String fechaTexto = validarTexto(request.getParameter("fechaHora"), "Fecha y hora", 16);
        String estado = validarTexto(request.getParameter("estado"), "Estado", 20).toUpperCase();

        if (!List.of("PROGRAMADA", "ATENDIDA", "CANCELADA").contains(estado)) {
            throw new IllegalArgumentException("Selecciona un estado válido para la cita.");
        }
        try {
            LocalDateTime fechaHora = LocalDateTime.parse(fechaTexto);
            return new Cita(documento, nombre, especialidad, medico, fechaHora, estado);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Ingresa una fecha y hora válidas.", exception);
        }
    }

    private static String validarTexto(String valor, String etiqueta, int maximo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + etiqueta + " es obligatorio.");
        }
        String texto = valor.trim();
        if (texto.length() > maximo) {
            throw new IllegalArgumentException("El campo " + etiqueta + " supera el máximo de " + maximo + " caracteres.");
        }
        return texto;
    }

    private static long leerId(String valor) {
        try {
            long id = Long.parseLong(valor);
            if (id > 0) {
                return id;
            }
        } catch (NumberFormatException | NullPointerException ignored) {
            // Se convierte el error de parseo en un mensaje entendible para el usuario.
        }
        throw new IllegalArgumentException("El ID de la cita debe ser un número mayor que cero.");
    }

    private static Cita citaVacia() {
        return new Cita("", "", "", "", LocalDateTime.now().withSecond(0).withNano(0), "PROGRAMADA");
    }

    private void redirigirConMensaje(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws IOException {
        request.getSession().setAttribute("mensaje", mensaje);
        response.sendRedirect(request.getContextPath() + "/citas");
    }
}
