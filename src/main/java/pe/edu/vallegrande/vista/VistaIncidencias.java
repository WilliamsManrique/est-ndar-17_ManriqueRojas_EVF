package pe.edu.vallegrande.vista;

import pe.edu.vallegrande.model.Incidencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaIncidencias extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtBuscarTipo, txtBuscarAula;
    private JComboBox<String> cbBuscarEstado;
    private JButton btnNuevo, btnEditar, btnEliminar, btnBuscar, btnRefrescar, btnReporte;

    public VistaIncidencias() {
        super("Registro de Incidencias Técnicas");
        initComponents();
    }

    private void initComponents() {
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel: acciones y filtros
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnNuevo = new JButton("Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnRefrescar = new JButton("Refrescar");
        btnReporte = new JButton("Reporte Pendientes");
        top.add(btnNuevo);
        top.add(btnEditar);
        top.add(btnEliminar);
        top.add(btnRefrescar);
        top.add(btnReporte);

        top.add(new JLabel("Tipo:"));
        txtBuscarTipo = new JTextField(10); top.add(txtBuscarTipo);
        top.add(new JLabel("Aula:"));
        txtBuscarAula = new JTextField(8); top.add(txtBuscarAula);
        top.add(new JLabel("Estado:"));
        cbBuscarEstado = new JComboBox<>(new String[]{"", "Pendiente","Procesando","Resuelto"}); top.add(cbBuscarEstado);
        btnBuscar = new JButton("Buscar"); top.add(btnBuscar);

        add(top, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID","Tipo","Aula","Fecha","Estado","Descripción"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // Métodos para controlar la tabla:
    public void llenarTabla(List<Incidencia> lista) {
        tableModel.setRowCount(0);
        for (Incidencia i : lista) {
            tableModel.addRow(new Object[]{
                    i.getId(),
                    i.getTipo(),
                    i.getAula(),
                    i.getFecha().toString(),
                    i.getEstado(),
                    i.getDescripcion()
            });
        }
    }

    public int getIdSeleccionado() {
        int fila = table.getSelectedRow();
        if (fila == -1) return -1;
        return (int) tableModel.getValueAt(fila, 0);
    }

    public String getBuscarTipo() { return txtBuscarTipo.getText().trim(); }
    public String getBuscarAula() { return txtBuscarAula.getText().trim(); }
    public String getBuscarEstado() {
        String s = (String) cbBuscarEstado.getSelectedItem();
        return (s == null) ? "" : s;
    }

    // getters botones
    public JButton getBtnNuevo() { return btnNuevo; }
    public JButton getBtnEditar() { return btnEditar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnRefrescar() { return btnRefrescar; }
    public JButton getBtnReporte() { return btnReporte; }

    // Obtener fila completa (opcional)
    public Object[] getFilaSeleccionada() {
        int r = table.getSelectedRow();
        if (r == -1) return null;
        Object[] row = new Object[tableModel.getColumnCount()];
        for (int i=0;i<row.length;i++) row[i] = tableModel.getValueAt(r,i);
        return row;
    }
}
