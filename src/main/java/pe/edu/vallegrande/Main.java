package pe.edu.vallegrande;

import pe.edu.vallegrande.controlador.Controlador;
import pe.edu.vallegrande.model.IncidenciaDAO;
import pe.edu.vallegrande.vista.VistaIncidencias;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaIncidencias vista = new VistaIncidencias();
            IncidenciaDAO dao = new IncidenciaDAO();
            new Controlador(dao, vista);
            vista.setLocationRelativeTo(null);
            vista.setVisible(true);
        });
    }
}
