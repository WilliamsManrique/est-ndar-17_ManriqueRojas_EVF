package pe.edu.vallegrande.controlador;

import pe.edu.vallegrande.model.Incidencia;
import pe.edu.vallegrande.model.IncidenciaDAO;
import pe.edu.vallegrande.vista.FormularioIncidencia;
import pe.edu.vallegrande.vista.VistaIncidencias;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class Controlador {
    private final IncidenciaDAO dao;
    private final VistaIncidencias vista;

    public Controlador(IncidenciaDAO dao, VistaIncidencias vista) {
        this.dao = dao;
        this.vista = vista;
        inicializar();
    }

    private void inicializar() {
        // Cargar todos al inicio
        refreshTabla();

        // eventos
        vista.getBtnNuevo().addActionListener(e -> mostrarFormularioNuevo());
        vista.getBtnRefrescar().addActionListener(e -> refreshTabla());
        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getBtnEliminar().addActionListener(e -> eliminarSeleccion());
        vista.getBtnEditar().addActionListener(e -> editarSeleccion());
        vista.getBtnReporte().addActionListener(e -> reportePendientes());
    }

    private void refreshTabla() {
        List<Incidencia> lista = dao.listarTodos();
        vista.llenarTabla(lista);
    }

    private void buscar() {
        String tipo = vista.getBuscarTipo();
        String aula = vista.getBuscarAula();
        String estado = vista.getBuscarEstado();
        List<Incidencia> lista = dao.buscar(tipo, aula, estado);
        vista.llenarTabla(lista);
    }

    private void mostrarFormularioNuevo() {
        FormularioIncidencia form = new FormularioIncidencia(vista);
        form.getBtnGuardar().addActionListener(ev -> {
            if (!form.validarCampos()) return;
            Incidencia inc = new Incidencia(
                    form.getTipo(),
                    form.getAula(),
                    form.getFecha(),
                    form.getEstado(),
                    form.getDescripcion()
            );
            if (dao.agregar(inc)) {
                JOptionPane.showMessageDialog(vista, "Incidencia registrada.");
                form.dispose();
                refreshTabla();
            } else JOptionPane.showMessageDialog(vista, "Error al registrar.", "Error", JOptionPane.ERROR_MESSAGE);
        });
        form.setVisible(true);
    }

    private void editarSeleccion() {
        int id = vista.getIdSeleccionado();
        if (id == -1) { JOptionPane.showMessageDialog(vista, "Selecciona una incidencia."); return; }
        // recuperar datos del row (más óptimo: crear método DAO.obtenerPorId)
        Object[] fila = vista.getFilaSeleccionada();
        if (fila == null) return;
        String tipo = (String)fila[1];
        String aula = (String)fila[2];
        String fecha = (String)fila[3];
        String estado = (String)fila[4];
        String descripcion = (String)fila[5];

        FormularioIncidencia form = new FormularioIncidencia(vista);
        form.setIncidenciaId(id);
        form.cargarDatos(tipo, aula, fecha, estado, descripcion);
        form.getBtnGuardar().addActionListener(ev -> {
            if (!form.validarCampos()) return;
            Incidencia inc = new Incidencia(
                    id,
                    form.getTipo(),
                    form.getAula(),
                    form.getFecha(),
                    form.getEstado(),
                    form.getDescripcion()
            );
            if (dao.actualizar(inc)) {
                JOptionPane.showMessageDialog(vista, "Incidencia actualizada.");
                form.dispose();
                refreshTabla();
            } else JOptionPane.showMessageDialog(vista, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
        });
        form.setVisible(true);
    }

    private void eliminarSeleccion() {
        int id = vista.getIdSeleccionado();
        if (id == -1) { JOptionPane.showMessageDialog(vista, "Selecciona una incidencia."); return; }
        int r = JOptionPane.showConfirmDialog(vista, "¿Eliminar incidencia seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            if (dao.eliminar(id)) {
                JOptionPane.showMessageDialog(vista, "Eliminado.");
                refreshTabla();
            } else JOptionPane.showMessageDialog(vista, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void reportePendientes() {
        List<Incidencia> pendientes = dao.buscar("","", "Pendiente");
        // Mostrar en una nueva ventana con JTable (reutilizamos VistaIncidencias en modo temporal)
        JDialog dlg = new JDialog(vista, "Reporte - Incidencias Pendientes", true);
        VistaIncidencias vistaReporte = new VistaIncidencias();
        vistaReporte.llenarTabla(pendientes);
        dlg.add(vistaReporte.getContentPane());
        dlg.setSize(800, 400);
        dlg.setLocationRelativeTo(vista);
        dlg.setVisible(true);
    }
}
