package co.medisoft.citas.model;

import java.time.LocalDateTime;

/** JavaBean que representa una cita médica y expone propiedades para JSP EL. */
public class Cita {
    private final long id;
    private final String documentoPaciente;
    private final String nombrePaciente;
    private final String especialidad;
    private final String medico;
    private final LocalDateTime fechaHora;
    private final String estado;

    public Cita(String documentoPaciente, String nombrePaciente, String especialidad,
                String medico, LocalDateTime fechaHora, String estado) {
        this(0, documentoPaciente, nombrePaciente, especialidad, medico, fechaHora, estado);
    }

    public Cita(long id, String documentoPaciente, String nombrePaciente, String especialidad,
                String medico, LocalDateTime fechaHora, String estado) {
        this.id = id;
        this.documentoPaciente = documentoPaciente;
        this.nombrePaciente = nombrePaciente;
        this.especialidad = especialidad;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    public long getId() { return id; }
    public String getDocumentoPaciente() { return documentoPaciente; }
    public String getNombrePaciente() { return nombrePaciente; }
    public String getEspecialidad() { return especialidad; }
    public String getMedico() { return medico; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getEstado() { return estado; }

    @Override
    public String toString() {
        return "ID " + id + " | " + nombrePaciente + " (" + documentoPaciente + ") | "
                + especialidad + " | " + medico + " | " + fechaHora + " | " + estado;
    }
}
