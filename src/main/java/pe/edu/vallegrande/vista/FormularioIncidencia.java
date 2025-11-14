package pe.edu.vallegrande.vista;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FormularioIncidencia extends JDialog {
    private JTextField txtTipo, txtAula, txtFecha; // fecha en formato yyyy-MM-dd
    private JComboBox<String> cbEstado;
    private JTextArea txtDescripcion;
    private JButton btnGuardar, btnCancelar;
    private int incidenciaId = 0; // 0 significa nuevo

    public FormularioIncidencia(Window parent) {
        super(parent, "Formulario Incidencia", ModalityType.APPLICATION_MODAL);
        initComponents();
    }

    private void initComponents() {
        setSize(450, 400);
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Tipo (PC/proyector/...):"), c);
        c.gridx = 1; txtTipo = new JTextField(); panel.add(txtTipo, c);

        c.gridx = 0; c.gridy = 1; panel.add(new JLabel("Aula/ambiente:"), c);
        c.gridx = 1; txtAula = new JTextField(); panel.add(txtAula, c);

        c.gridx = 0; c.gridy = 2; panel.add(new JLabel("Fecha (yyyy-MM-dd):"), c);
        c.gridx = 1; txtFecha = new JTextField(LocalDate.now().toString()); panel.add(txtFecha, c);

        c.gridx = 0; c.gridy = 3; panel.add(new JLabel("Estado:"), c);
        c.gridx = 1; cbEstado = new JComboBox<>(new String[]{"Pendiente","Procesando","Resuelto"}); panel.add(cbEstado, c);

        c.gridx = 0; c.gridy = 4; panel.add(new JLabel("Descripción:"), c);
        c.gridx = 1; txtDescripcion = new JTextArea(6, 20); panel.add(new JScrollPane(txtDescripcion), c);

        add(panel, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        botones.add(btnGuardar);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dispose());
        setLocationRelativeTo(getParent());
    }

    // Validación simple y lectura
    public boolean validarCampos() {
        if (txtTipo.getText().isBlank() || txtAula.getText().isBlank() || txtFecha.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Tipo, Aula y Fecha son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            LocalDate.parse(txtFecha.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Usa formato yyyy-MM-dd", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public String getTipo() { return txtTipo.getText().trim(); }
    public String getAula() { return txtAula.getText().trim(); }
    public LocalDate getFecha() { return LocalDate.parse(txtFecha.getText().trim()); }
    public String getEstado() { return (String) cbEstado.getSelectedItem(); }
    public String getDescripcion() { return txtDescripcion.getText().trim(); }
    public JButton getBtnGuardar() { return btnGuardar; }

    public void setIncidenciaId(int id) { this.incidenciaId = id; }
    public int getIncidenciaId() { return incidenciaId; }

    public void cargarDatos(String tipo, String aula, String fecha, String estado, String descripcion) {
        txtTipo.setText(tipo);
        txtAula.setText(aula);
        txtFecha.setText(fecha);
        cbEstado.setSelectedItem(estado);
        txtDescripcion.setText(descripcion);
    }
}
