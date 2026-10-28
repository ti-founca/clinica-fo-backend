package py.edu.unca.clinicas.pacientes.domain;

/** Paciente de la clínica. {@code id} es nulo mientras no haya sido persistido. */
public record Paciente(
        Long id,
        String nombres,
        String apellidos,
        String numeroDocumento,
        String direccion,
        String barrio,
        String ciudad) {

    public Paciente conId(Long nuevoId) {
        return new Paciente(nuevoId, nombres, apellidos, numeroDocumento, direccion, barrio, ciudad);
    }
}
