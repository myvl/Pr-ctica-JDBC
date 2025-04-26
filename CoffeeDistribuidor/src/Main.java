import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.io.*;

// Clases de servicio y utilidades
class ConexionBD {
    private static Connection conexion;
    private static final String URL = "jdbc:postgresql://localhost:5432/Distribuidor_Cafe";
    private static final String USER = "developer";
    private static final String PASSWORD = "23100132";

    public static void conectar() {
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión establecida con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public static void desconectar() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada con éxito.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    public static Connection getConexion() {
        return conexion;
    }
}

// Clase principal de la aplicación
public class Main {
    private static JFrame frame;
    private static JPanel mainPanel;
    private static JPanel sidebarPanel;
    private static JPanel headerPanel;
    private static CardLayout cardLayout;
    private static JPanel contentPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> crearYMostrarGUI());


    }

    private static void crearYMostrarGUI() {
        frame = new JFrame("Sistema de Gestión de Café");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        mainPanel = new JPanel(new BorderLayout());

        // Barra lateral con nuevas opciones
        sidebarPanel = new JPanel(new GridLayout(0, 1));
        sidebarPanel.setPreferredSize(new Dimension(200, 720));
        sidebarPanel.setBackground(new Color(50, 50, 50));

        JButton btnInicio = crearBotonSidebar("Inicio");
        JButton btnCafes = crearBotonSidebar("Cafés");
        JButton btnProveedores = crearBotonSidebar("Proveedores");
        JButton btnCafeterias = crearBotonSidebar("Cafeterías");


        sidebarPanel.add(btnInicio);
        sidebarPanel.add(btnCafes);
        sidebarPanel.add(btnProveedores);
        sidebarPanel.add(btnCafeterias);


        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(1280, 50));
        headerPanel.setBackground(new Color(80, 80, 80));

        JLabel lblTitulo = new JLabel("Distribuidor Café", JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnCerrarSesion = new JButton("Salir de la Aplicación");
        btnCerrarSesion.setBackground(Color.RED);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "¿Seguro que quieres salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
            }
        });

        headerPanel.add(lblTitulo, BorderLayout.WEST);
        headerPanel.add(btnCerrarSesion, BorderLayout.EAST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);


        ConexionBD.conectar();
        Connection connection = ConexionBD.getConexion();
        // Agregar paneles
        contentPanel.add(new PanelInicio(), "Inicio");
        contentPanel.add(new PanelCafes(connection), "Cafés");
        contentPanel.add(new PanelProveedores(connection), "Proveedores");
        contentPanel.add(new PanelCafeterias(connection), "Cafeterías");  // <- CORREGIDO
        //contentPanel.add(new PanelReportes(), "Reportes");


        btnInicio.addActionListener(e -> cardLayout.show(contentPanel, "Inicio"));
        btnCafes.addActionListener(e -> cardLayout.show(contentPanel, "Cafés"));
        btnProveedores.addActionListener(e -> cardLayout.show(contentPanel, "Proveedores"));
        btnCafeterias.addActionListener(e -> cardLayout.show(contentPanel, "Cafeterías"));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
    // Método para crear un botón de la barra lateral con estilo personalizado
    private static JButton crearBotonSidebar(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(70, 70, 70)); // Gris intermedio
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.PLAIN, 16));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(100, 100, 100)); // Cambia color al pasar el ratón
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(70, 70, 70)); // Regresa al color original
            }
        });
        return boton;
    }
    // Panel de Inicio adaptado
    static class PanelInicio extends JPanel {
        public PanelInicio() {
            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(245, 245, 220)); // Color beige claro, similar al café con leche
            setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            // Panel superior con información
            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBackground(new Color(245, 245, 220));
            infoPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2),
                            "Bienvenido a Café Aroma Premium"),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            JTextArea infoArea = new JTextArea();
            infoArea.setText("Sistema Integral de Gestión para Distribuidores de Café\n\n"
                            + "Esta aplicación le permite administrar todo el ciclo del café:\n"
                            + "- Control de inventario de granos y productos terminados\n"
                            + "- Gestión de relaciones con proveedores locales e internacionales\n"
                            + "- Seguimiento de pedidos a cafeterías y clientes mayoristas\n"
                            + "- Generación de reportes de ventas y análisis de tendencias\n" +
                            "\n"
                    +"Nuestro compromiso: Distribuir el mejor café con trazabilidad desde el origen hasta la taza.");
            infoArea.setEditable(false);
            infoArea.setMargin(new Insets(10, 15, 10, 15));
            infoArea.setLineWrap(true);
            infoArea.setWrapStyleWord(true);
            infoArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
            infoArea.setBackground(new Color(253, 245, 230)); // Color papel antiguo
            infoArea.setForeground(new Color(101, 67, 33)); // Color marrón café

            infoPanel.add(infoArea, BorderLayout.CENTER);
            add(infoPanel, BorderLayout.NORTH);

            // Panel central con imágenes y botones
            JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 15));
            centerPanel.setBackground(new Color(245, 245, 220));

            // Panel izquierdo para imágenes de café
            JPanel imagePanel = new JPanel(new GridLayout(2, 1, 10, 10));
            imagePanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(139, 69, 19)),
                    "Nuestros Productos"));
            imagePanel.setBackground(new Color(245, 245, 220));

            // Imagen 1 - Café en grano
            JLabel imgCafe1;
            try {
                ImageIcon cafeIcon1 = new ImageIcon(getClass().getResource("/images/coffee1.jpg"));
                // Redimensionar la imagen para que se ajuste al espacio
                Image img = cafeIcon1.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                imgCafe1 = new JLabel(new ImageIcon(img));
                imgCafe1.setToolTipText("Café en grano de alta calidad");
            } catch (NullPointerException e) {
                imgCafe1 = new JLabel("Imagen no disponible", JLabel.CENTER);
                imgCafe1.setForeground(Color.RED);
            }
            imgCafe1.setHorizontalAlignment(JLabel.CENTER);
            imgCafe1.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(139, 69, 19), 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            imgCafe1.setBackground(Color.WHITE);
            imgCafe1.setOpaque(true);

            // Imagen 2 - Café molido
            JLabel imgCafe2;
            try {
                ImageIcon cafeIcon2 = new ImageIcon(getClass().getResource("/images/coffee2.jpg"));
                // Redimensionar la imagen
                Image img2 = cafeIcon2.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                imgCafe2 = new JLabel(new ImageIcon(img2));
                imgCafe2.setToolTipText("Café molido para todos los gustos");
            } catch (NullPointerException e) {
                imgCafe2 = new JLabel("Imagen no disponible", JLabel.CENTER);
                imgCafe2.setForeground(Color.RED);
            }
            imgCafe2.setHorizontalAlignment(JLabel.CENTER);
            imgCafe2.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(139, 69, 19), 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            imgCafe2.setBackground(Color.WHITE);
            imgCafe2.setOpaque(true);

            imagePanel.add(imgCafe1);
            imagePanel.add(imgCafe2);
            centerPanel.add(imagePanel);

            // Panel de botones con estilo café
            JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 15));
            buttonPanel.setBackground(new Color(245, 245, 220));
            buttonPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(139, 69, 19)),
                    "Acciones Rápidas"));

            // Botón para realizar copias de seguridad
            JButton backupButton = createCoffeeButton("☕ Realizar Respaldo",
                    new Color(139, 69, 19), // Marrón café
                    e -> realizarCopiasDeSeguridad());

            // Botón para restaurar la base de datos
            JButton restoreButton = createCoffeeButton("🔄 Restaurar Datos",
                    new Color(101, 67, 33), // Marrón más oscuro
                    e -> restaurarBaseDeDatos());

            // Botón para gestión de inventario
            JButton inventoryButton = createCoffeeButton("📦 Inventario de Café",
                    new Color(160, 82, 45), // Marrón sienna
                    e -> JOptionPane.showMessageDialog(this,
                            "Módulo de inventario de granos y productos terminados",
                            "Gestión de Inventario",
                            JOptionPane.INFORMATION_MESSAGE));

            buttonPanel.add(backupButton);
            buttonPanel.add(restoreButton);
            buttonPanel.add(inventoryButton);
            centerPanel.add(buttonPanel);

            add(centerPanel, BorderLayout.CENTER);

            // Panel inferior con estadísticas
            JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
            statsPanel.setBackground(new Color(245, 245, 220));
            statsPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(139, 69, 19)),
                    "Estadísticas del Café"));

            // Método para crear labels de estadísticas con estilo
            statsPanel.add(createStatLabel("Variedades de Café", "15"));
            statsPanel.add(createStatLabel("Proveedores Activos", "8"));
            statsPanel.add(createStatLabel("Cafeterías Asociadas", "23"));

            add(statsPanel, BorderLayout.SOUTH);
        }

        // Método auxiliar para crear botones con estilo de café
        private JButton createCoffeeButton(String text, Color bgColor, ActionListener action) {
            JButton button = new JButton(text);
            button.setBackground(bgColor);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("SansSerif", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(210, 180, 140)), // Color café claro
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)));
            button.addActionListener(action);
            return button;
        }

        // Método auxiliar para crear labels de estadísticas
        private JPanel createStatLabel(String title, String value) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(new Color(253, 245, 230));
            panel.setBorder(BorderFactory.createLineBorder(new Color(210, 180, 140)));

            JLabel titleLabel = new JLabel(title, JLabel.CENTER);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            titleLabel.setForeground(new Color(101, 67, 33));

            JLabel valueLabel = new JLabel(value, JLabel.CENTER);
            valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            valueLabel.setForeground(new Color(139, 69, 19));

            panel.add(titleLabel, BorderLayout.NORTH);
            panel.add(valueLabel, BorderLayout.CENTER);

            return panel;
        }

        // Método para realizar las copias de seguridad
        private void realizarCopiasDeSeguridad() {
            // Directorio donde se guardarán las copias de seguridad
            File backupDir = new File("backups/");
            if (!backupDir.exists()) {
                backupDir.mkdir(); // Crear el directorio si no existe
            }

            // Nombre del archivo de respaldo
            String timestamp = String.valueOf(System.currentTimeMillis());
            String backupFile = "backups/respaldodb_" + timestamp + ".sql";

            // Ruta completa al ejecutable pg_dump
            String pgDumpPath = "C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump.exe"; // Ajusta la versión y la ruta según tu instalación

            // Comando para crear la copia de seguridad de la base de datos en PostgreSQL
            List<String> command = Arrays.asList(
                    pgDumpPath,
                    "-U", "developer",
                    "-h", "localhost",
                    "-d", "Distribuidor_Cafe",
                    "-f", backupFile
            );

            try {
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.environment().put("PGPASSWORD", "23100132"); // Contraseña de la base de datos
                processBuilder.redirectErrorStream(true); // Redirigir el error al flujo de salida

                Process process = processBuilder.start();

                // Captura la salida del proceso
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // Leer la salida estándar
                String s;
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }

                int exitCode = process.waitFor();

                // Verificación de que la copia de seguridad se haya creado
                if (exitCode == 0 && new File(backupFile).exists()) {
                    JOptionPane.showMessageDialog(this, "Copia de seguridad creada exitosamente: " + backupFile, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear la copia de seguridad. Código de salida: " + exitCode, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException | InterruptedException e) {
                JOptionPane.showMessageDialog(this, "Error durante la creación de la copia de seguridad: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para restaurar la base de datos
        private void restaurarBaseDeDatos() {
            // Usar JFileChooser para seleccionar el archivo de respaldo
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar archivo de respaldo");

            // Abrir el cuadro de diálogo y esperar la selección
            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(this, "Proceso de restauración cancelado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return; // El usuario canceló la selección
            }

            File backupFile = fileChooser.getSelectedFile();

            // Ruta completa al ejecutable psql
            String psqlPath = "C:\\Program Files\\PostgreSQL\\16\\bin\\psql.exe"; // Ajusta la versión y la ruta según tu instalación

            // Comando para restaurar la base de datos usando psql
            List<String> command = Arrays.asList(
                    psqlPath,
                    "-U", "developer",
                    "-h", "localhost",
                    "-d", "Distribuidor_Cafe",
                    "-f", backupFile.getAbsolutePath() // Ruta al archivo de respaldo
            );

            try {
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.environment().put("PGPASSWORD", "23100132"); // Contraseña de la base de datos
                processBuilder.redirectErrorStream(true); // Redirigir el error al flujo de salida

                Process process = processBuilder.start();

                // Captura la salida del proceso
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // Leer la salida estándar
                String s;
                StringBuilder output = new StringBuilder();
                while ((s = stdInput.readLine()) != null) {
                    output.append(s).append("\n");
                    System.out.println(s);
                }

                int exitCode = process.waitFor();

                // Verificación de que la restauración se haya completado
                if (exitCode == 0) {
                    JOptionPane.showMessageDialog(this, "Base de datos restaurada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al restaurar la base de datos. Código de salida: " + exitCode + "\nSalida:\n" + output.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException | InterruptedException e) {
                JOptionPane.showMessageDialog(this, "Error durante la restauración de la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    static class PanelCafes extends JPanel {
        private JTable cafesTable;
        private DefaultTableModel tableModel;
        private Connection connection;

        public PanelCafes(Connection connection) {
            this.connection = connection;
            setLayout(new BorderLayout());

            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Cafés",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLACK));
            panelTabla.setBackground(new Color(230, 230, 250));

            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Precio");
            tableModel.addColumn("Descripción");
            tableModel.addColumn("Proveedor");

            cafesTable = new JTable(tableModel);
            cafesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            cafesTable.setFont(new Font("Arial", Font.PLAIN, 12));
            cafesTable.setRowHeight(25);
            cafesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            TableColumnModel columnModel = cafesTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(20);
            columnModel.getColumn(1).setPreferredWidth(150);
            columnModel.getColumn(2).setPreferredWidth(100);
            columnModel.getColumn(3).setPreferredWidth(200);
            columnModel.getColumn(4).setPreferredWidth(150);

            cafesTable.setBackground(Color.WHITE);
            cafesTable.setGridColor(new Color(200, 200, 200));
            cafesTable.setSelectionBackground(new Color(173, 216, 230));
            cafesTable.setSelectionForeground(Color.BLACK);

            cafesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                    return c;
                }
            });

            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
            cafesTable.setRowSorter(sorter);

            JScrollPane scrollPane = new JScrollPane(cafesTable);
            panelTabla.add(scrollPane, BorderLayout.CENTER);
            add(panelTabla, BorderLayout.CENTER);

            JPanel panelAgregar = new JPanel(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Café",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLACK));
            panelAgregar.setBackground(new Color(240, 240, 255));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField nombreField = new JTextField(15);
            JTextField precioField = new JTextField(15);
            JTextField descripcionField = new JTextField(15);
            JComboBox<String> proveedorCombo = new JComboBox<>();

            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT supplier_id, supplier_name FROM suppliers")) {
                while (rs.next()) {
                    proveedorCombo.addItem(rs.getInt("supplier_id") + " - " + rs.getString("supplier_name"));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            gbc.gridx = 0; gbc.gridy = 0;
            panelAgregar.add(new JLabel("Nombre*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(nombreField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            panelAgregar.add(new JLabel("Precio*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(precioField, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            panelAgregar.add(new JLabel("Descripción:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(descripcionField, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            panelAgregar.add(new JLabel("Proveedor*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(proveedorCombo, gbc);

            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            panelAgregar.add(new JLabel("* Campo obligatorio"), gbc);

            JPanel panelBotones = new JPanel(new FlowLayout());
            JButton agregarButton = new JButton("Agregar");
            JButton modificarButton = new JButton("Modificar");
            JButton eliminarButton = new JButton("Eliminar");

            agregarButton.setBackground(new Color(70, 130, 180));
            modificarButton.setBackground(new Color(34, 139, 34));
            eliminarButton.setBackground(new Color(255, 69, 0));
            agregarButton.setForeground(Color.WHITE);
            modificarButton.setForeground(Color.WHITE);
            eliminarButton.setForeground(Color.WHITE);

            modificarButton.setEnabled(false);
            eliminarButton.setEnabled(false);

            cafesTable.getSelectionModel().addListSelectionListener(e -> {
                boolean seleccion = cafesTable.getSelectedRow() != -1;
                modificarButton.setEnabled(seleccion);
                eliminarButton.setEnabled(seleccion);
                if (seleccion) {
                    int row = cafesTable.getSelectedRow();
                    nombreField.setText(tableModel.getValueAt(row, 1).toString());
                    precioField.setText(tableModel.getValueAt(row, 2).toString());
                    descripcionField.setText(tableModel.getValueAt(row, 3).toString());
                    // Establecer el proveedor seleccionado
                    String proveedor = tableModel.getValueAt(row, 4).toString();
                    for (int i = 0; i < proveedorCombo.getItemCount(); i++) {
                        if (proveedorCombo.getItemAt(i).endsWith(proveedor)) {
                            proveedorCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            });

            agregarButton.addActionListener(e -> {
                String nombre = nombreField.getText();
                String precioStr = precioField.getText();
                String descripcion = descripcionField.getText();
                String proveedorStr = (String) proveedorCombo.getSelectedItem();

                if (nombre.isEmpty() || precioStr.isEmpty() || proveedorStr == null || proveedorStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos obligatorios deben estar completos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double precio = Double.parseDouble(precioStr);
                    int supplierId = Integer.parseInt(proveedorStr.split(" - ")[0]);

                    if (existeCafe(nombre, supplierId)) {
                        int opcion = JOptionPane.showConfirmDialog(this,
                                "Ya existe un café con este nombre para este proveedor. ¿Desea actualizarlo?",
                                "Confirmación", JOptionPane.YES_NO_OPTION);
                        if (opcion == JOptionPane.YES_OPTION) {
                            int id = obtenerIdCafe(nombre, supplierId);
                            modificarCafe(id, nombre, precio, descripcion);
                        }
                    } else {
                        agregarCafe(nombre, precio, descripcion, supplierId);
                    }
                    limpiarCampos(nombreField, precioField, descripcionField);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Precio debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            modificarButton.addActionListener(e -> {
                int selectedRow = cafesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String nombre = nombreField.getText();
                    String precioStr = precioField.getText();
                    String descripcion = descripcionField.getText();

                    if (nombre.isEmpty() || precioStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nombre y Precio son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        double precio = Double.parseDouble(precioStr);
                        modificarCafe(id, nombre, precio, descripcion);
                        limpiarCampos(nombreField, precioField, descripcionField);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Precio debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            eliminarButton.addActionListener(e -> {
                int selectedRow = cafesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    eliminarCafe(id);
                    limpiarCampos(nombreField, precioField, descripcionField);
                }
            });

            panelBotones.add(agregarButton);
            panelBotones.add(modificarButton);
            panelBotones.add(eliminarButton);

            gbc.gridy = 5;
            panelAgregar.add(panelBotones, gbc);

            add(panelAgregar, BorderLayout.SOUTH);

            if (this.connection != null) {
                cargarCafes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean existeCafe(String nombre, int supplierId) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) FROM coffees WHERE coffee_name = ? AND supplier_id = ?")) {
                stmt.setString(1, nombre);
                stmt.setInt(2, supplierId);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al verificar café: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        private int obtenerIdCafe(String nombre, int supplierId) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT coffee_id FROM coffees WHERE coffee_name = ? AND supplier_id = ?")) {
                stmt.setString(1, nombre);
                stmt.setInt(2, supplierId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return rs.getInt(1);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al obtener ID del café: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return -1;
        }

        private void cargarCafes() {
            tableModel.setRowCount(0);
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT c.coffee_id, c.coffee_name, c.price, c.description, s.supplier_name FROM coffees c JOIN suppliers s ON c.supplier_id = s.supplier_id")) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("coffee_id"),
                            rs.getString("coffee_name"),
                            rs.getDouble("price"),
                            rs.getString("description"),
                            rs.getString("supplier_name")
                    });
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar cafés: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void agregarCafe(String nombre, double precio, String descripcion, int supplierId) {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO coffees (coffee_name, supplier_id, price, description) VALUES (?, ?, ?, ?);")) {
                ps.setString(1, nombre);
                ps.setInt(2, supplierId);
                ps.setDouble(3, precio);
                ps.setString(4, descripcion);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Café agregado exitosamente.");
                tableModel.setRowCount(0);
                cargarCafes();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al agregar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void modificarCafe(int id, String nombre, double precio, String descripcion) {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE coffees SET coffee_name = ?, price = ?, description = ? WHERE coffee_id = ?")) {
                ps.setString(1, nombre);
                ps.setDouble(2, precio);
                ps.setString(3, descripcion);
                ps.setInt(4, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Café modificado exitosamente.");
                tableModel.setRowCount(0);
                cargarCafes();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void eliminarCafe(int id) {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM coffees WHERE coffee_id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Café eliminado exitosamente.");
                tableModel.setRowCount(0);
                cargarCafes();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void limpiarCampos(JTextField nombre, JTextField precio, JTextField descripcion) {
            nombre.setText("");
            precio.setText("");
            descripcion.setText("");
        }
    }

    static class PanelProveedores extends JPanel {
        private JTable proveedoresTable;
        private DefaultTableModel tableModel;
        private Connection connection;

        public PanelProveedores(Connection connection) {
            this.connection = connection;
            setLayout(new BorderLayout());

            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Proveedores",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLACK));
            panelTabla.setBackground(new Color(230, 230, 250));

            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Contacto");
            tableModel.addColumn("Teléfono");
            tableModel.addColumn("Email");

            proveedoresTable = new JTable(tableModel);
            proveedoresTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            proveedoresTable.setFont(new Font("Arial", Font.PLAIN, 12));
            proveedoresTable.setRowHeight(25);
            proveedoresTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            TableColumnModel columnModel = proveedoresTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(20);
            columnModel.getColumn(1).setPreferredWidth(150);
            columnModel.getColumn(2).setPreferredWidth(150);
            columnModel.getColumn(3).setPreferredWidth(100);
            columnModel.getColumn(4).setPreferredWidth(250);

            proveedoresTable.setBackground(Color.WHITE);
            proveedoresTable.setGridColor(new Color(200, 200, 200));
            proveedoresTable.setSelectionBackground(new Color(173, 216, 230));
            proveedoresTable.setSelectionForeground(Color.BLACK);

            proveedoresTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                    return c;
                }
            });

            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
            proveedoresTable.setRowSorter(sorter);

            JScrollPane scrollPane = new JScrollPane(proveedoresTable);
            panelTabla.add(scrollPane, BorderLayout.CENTER);
            add(panelTabla, BorderLayout.CENTER);

            JPanel panelAgregar = new JPanel(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Proveedor",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLACK));
            panelAgregar.setBackground(new Color(240, 240, 255));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField nombreField = new JTextField(15);
            JTextField contactoField = new JTextField(15);
            JTextField telefonoField = new JTextField(15);
            JTextField emailField = new JTextField(15);

            gbc.gridx = 0; gbc.gridy = 0;
            panelAgregar.add(new JLabel("Nombre*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(nombreField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            panelAgregar.add(new JLabel("Contacto*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(contactoField, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            panelAgregar.add(new JLabel("Teléfono*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(telefonoField, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            panelAgregar.add(new JLabel("Email*:"), gbc);
            gbc.gridx = 1;
            panelAgregar.add(emailField, gbc);

            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            panelAgregar.add(new JLabel("* Campo obligatorio"), gbc);

            JPanel panelBotones = new JPanel(new FlowLayout());
            JButton agregarButton = new JButton("Agregar");
            JButton modificarButton = new JButton("Modificar");
            JButton eliminarButton = new JButton("Eliminar");
            JButton consultarCafesBtn = new JButton("Ver Cafés del Proveedor");

            agregarButton.setBackground(new Color(70, 130, 180));
            modificarButton.setBackground(new Color(34, 139, 34));
            eliminarButton.setBackground(new Color(255, 69, 0));
            consultarCafesBtn.setBackground(new Color(128, 0, 128)); // morado
            agregarButton.setForeground(Color.WHITE);
            modificarButton.setForeground(Color.WHITE);
            eliminarButton.setForeground(Color.WHITE);
            consultarCafesBtn.setForeground(Color.WHITE);

            modificarButton.setEnabled(false);
            eliminarButton.setEnabled(false);

            proveedoresTable.getSelectionModel().addListSelectionListener(e -> {
                boolean seleccion = proveedoresTable.getSelectedRow() != -1;
                modificarButton.setEnabled(seleccion);
                eliminarButton.setEnabled(seleccion);
                if (seleccion) {
                    int row = proveedoresTable.getSelectedRow();
                    nombreField.setText(tableModel.getValueAt(row, 1).toString());
                    contactoField.setText(tableModel.getValueAt(row, 2).toString());
                    telefonoField.setText(tableModel.getValueAt(row, 3).toString());
                    emailField.setText(tableModel.getValueAt(row, 4).toString());
                }
            });

            agregarButton.addActionListener(e -> {
                if (nombreField.getText().isEmpty() || contactoField.getText().isEmpty() ||
                        telefonoField.getText().isEmpty() || emailField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos marcados con * son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String nombre = nombreField.getText();
                String contacto = contactoField.getText();
                String telefono = telefonoField.getText();
                String email = emailField.getText();

                if (existeProveedor(nombre)) {
                    int opcion = JOptionPane.showConfirmDialog(this, "Ya existe un proveedor con este nombre. ¿Desea actualizarlo?", "Confirmación", JOptionPane.YES_NO_OPTION);
                    if (opcion == JOptionPane.YES_OPTION) {
                        int id = obtenerIdProveedor(nombre);
                        modificarProveedor(id, nombre, contacto, telefono, email);
                    }
                } else {
                    agregarProveedor(nombre, contacto, telefono, email);
                }
                limpiarCampos(nombreField, contactoField, telefonoField, emailField);
            });

            modificarButton.addActionListener(e -> {
                int row = proveedoresTable.getSelectedRow();
                if (row != -1) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    modificarProveedor(id, nombreField.getText(), contactoField.getText(), telefonoField.getText(), emailField.getText());
                    limpiarCampos(nombreField, contactoField, telefonoField, emailField);
                }
            });

            eliminarButton.addActionListener(e -> {
                int row = proveedoresTable.getSelectedRow();
                if (row != -1) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    eliminarProveedor(id);
                    limpiarCampos(nombreField, contactoField, telefonoField, emailField);
                }
            });

            consultarCafesBtn.addActionListener(e -> {
                int row = proveedoresTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(this, "Selecciona un proveedor primero.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                int proveedorId = (int) tableModel.getValueAt(row, 0);
                String proveedorNombre = tableModel.getValueAt(row, 1).toString();

                try (PreparedStatement stmt = connection.prepareStatement(
                        "SELECT coffee_name, price, description FROM coffees WHERE supplier_id = ?")) {
                    stmt.setInt(1, proveedorId);
                    ResultSet rs = stmt.executeQuery();

                    StringBuilder cafes = new StringBuilder();
                    while (rs.next()) {
                        cafes.append("☕ ").append(rs.getString("coffee_name"))
                                .append(" - $").append(rs.getDouble("price")).append("\n")
                                .append("   ").append(rs.getString("description")).append("\n\n");
                    }

                    if (cafes.length() == 0) {
                        cafes.append("Este proveedor aún no tiene cafés registrados.");
                    }

                    JTextArea textArea = new JTextArea(cafes.toString());
                    textArea.setEditable(false);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    JScrollPane scroll = new JScrollPane(textArea);
                    scroll.setPreferredSize(new Dimension(400, 250));

                    JOptionPane.showMessageDialog(this, scroll, "Cafés de " + proveedorNombre, JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al consultar cafés: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            panelBotones.add(agregarButton);
            panelBotones.add(modificarButton);
            panelBotones.add(eliminarButton);
            panelBotones.add(consultarCafesBtn);
            gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
            panelAgregar.add(panelBotones, gbc);

            add(panelAgregar, BorderLayout.SOUTH);

            if (this.connection != null) {
                cargarProveedores();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void cargarProveedores() {
            tableModel.setRowCount(0);
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM suppliers")) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("supplier_id"),
                            rs.getString("supplier_name"),
                            rs.getString("contact_name"),
                            rs.getString("phone"),
                            rs.getString("email")
                    });
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean existeProveedor(String nombre) {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM suppliers WHERE supplier_name = ?")) {
                stmt.setString(1, nombre);
                ResultSet rs = stmt.executeQuery();
                return rs.next() && rs.getInt(1) > 0;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al verificar proveedor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        private int obtenerIdProveedor(String nombre) {
            try (PreparedStatement stmt = connection.prepareStatement("SELECT supplier_id FROM suppliers WHERE supplier_name = ?")) {
                stmt.setString(1, nombre);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return rs.getInt(1);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al obtener ID del proveedor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return -1;
        }

        private void agregarProveedor(String nombre, String contacto, String telefono, String email) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO suppliers (supplier_name, contact_name, phone, email) VALUES (?, ?, ?, ?)")) {
                stmt.setString(1, nombre);
                stmt.setString(2, contacto);
                stmt.setString(3, telefono);
                stmt.setString(4, email);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Proveedor agregado exitosamente.");
                tableModel.setRowCount(0);
                cargarProveedores();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al agregar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void modificarProveedor(int id, String nombre, String contacto, String telefono, String email) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE suppliers SET supplier_name = ?, contact_name = ?, phone = ?, email = ? WHERE supplier_id = ?")) {
                stmt.setString(1, nombre);
                stmt.setString(2, contacto);
                stmt.setString(3, telefono);
                stmt.setString(4, email);
                stmt.setInt(5, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Proveedor modificado exitosamente.");
                tableModel.setRowCount(0);
                cargarProveedores();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void eliminarProveedor(int id) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM suppliers WHERE supplier_id = ?")) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Proveedor eliminado exitosamente.");
                tableModel.setRowCount(0);
                cargarProveedores();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void limpiarCampos(JTextField nombre, JTextField contacto, JTextField telefono, JTextField email) {
            nombre.setText("");
            contacto.setText("");
            telefono.setText("");
            email.setText("");
        }
    }

    static class PanelCafeterias extends JPanel {
        private JTable table;
        private DefaultTableModel model;
        private Connection conn;

        public PanelCafeterias(Connection connection) {
            this.conn = connection;
            setLayout(new BorderLayout());

            // Panel de tabla
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Cafeterías",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLACK));
            panelTabla.setBackground(new Color(230, 230, 250));

            model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Nombre");
            model.addColumn("Ubicación");
            model.addColumn("Horario");
            model.addColumn("Teléfono");

            table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setFont(new Font("Arial", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(20);
            columnModel.getColumn(1).setPreferredWidth(150);
            columnModel.getColumn(2).setPreferredWidth(200);
            columnModel.getColumn(3).setPreferredWidth(100);
            columnModel.getColumn(4).setPreferredWidth(100);

            table.setBackground(Color.WHITE);
            table.setGridColor(new Color(200, 200, 200));
            table.setSelectionBackground(new Color(173, 216, 230));
            table.setSelectionForeground(Color.BLACK);

            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                    return c;
                }
            });

            TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
            table.setRowSorter(sorter);

            JScrollPane scrollPane = new JScrollPane(table);
            panelTabla.add(scrollPane, BorderLayout.CENTER);
            add(panelTabla, BorderLayout.CENTER);

            // Panel de formulario
            JPanel panelFormulario = new JPanel(new GridBagLayout());
            panelFormulario.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Cafetería",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLACK));
            panelFormulario.setBackground(new Color(240, 240, 255));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField nombreField = new JTextField(15);
            JTextField ubicacionField = new JTextField(15);
            JTextField horarioField = new JTextField(15);
            JTextField telefonoField = new JTextField(15);

            gbc.gridx = 0; gbc.gridy = 0;
            panelFormulario.add(new JLabel("Nombre*:"), gbc);
            gbc.gridx = 1;
            panelFormulario.add(nombreField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            panelFormulario.add(new JLabel("Ubicación:"), gbc);
            gbc.gridx = 1;
            panelFormulario.add(ubicacionField, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            panelFormulario.add(new JLabel("Horario:"), gbc);
            gbc.gridx = 1;
            panelFormulario.add(horarioField, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            panelFormulario.add(new JLabel("Teléfono:"), gbc);
            gbc.gridx = 1;
            panelFormulario.add(telefonoField, gbc);

            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            panelFormulario.add(new JLabel("* Campo obligatorio"), gbc);

            // Botones
            JPanel panelBotones = new JPanel(new FlowLayout());
            JButton agregarBtn = new JButton("Agregar");
            JButton modificarBtn = new JButton("Modificar");
            JButton eliminarBtn = new JButton("Eliminar");

            agregarBtn.setBackground(new Color(70, 130, 180));
            modificarBtn.setBackground(new Color(34, 139, 34));
            eliminarBtn.setBackground(new Color(255, 69, 0));
            agregarBtn.setForeground(Color.WHITE);
            modificarBtn.setForeground(Color.WHITE);
            eliminarBtn.setForeground(Color.WHITE);

            modificarBtn.setEnabled(false);
            eliminarBtn.setEnabled(false);

            panelBotones.add(agregarBtn);
            panelBotones.add(modificarBtn);
            panelBotones.add(eliminarBtn);

            gbc.gridy = 5;
            panelFormulario.add(panelBotones, gbc);

            add(panelFormulario, BorderLayout.SOUTH);

            table.getSelectionModel().addListSelectionListener(e -> {
                boolean selected = table.getSelectedRow() != -1;
                modificarBtn.setEnabled(selected);
                eliminarBtn.setEnabled(selected);
                if (selected) {
                    int row = table.getSelectedRow();
                    nombreField.setText(model.getValueAt(row, 1).toString());
                    ubicacionField.setText(model.getValueAt(row, 2).toString());
                    horarioField.setText(model.getValueAt(row, 3).toString());
                    telefonoField.setText(model.getValueAt(row, 4).toString());
                }
            });

            // Funcionalidad botones
            agregarBtn.addActionListener(e -> {
                if (nombreField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El campo Nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO coffee_shops (shop_name, location, opening_hours, contact_phone) VALUES (?, ?, ?, ?)")) {
                    stmt.setString(1, nombreField.getText());
                    stmt.setString(2, ubicacionField.getText());
                    stmt.setString(3, horarioField.getText());
                    stmt.setString(4, telefonoField.getText());
                    stmt.executeUpdate();
                    model.setRowCount(0);
                    cargarDatos();
                    limpiarCampos(nombreField, ubicacionField, horarioField, telefonoField);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al agregar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            modificarBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row == -1) return;
                int id = (int) model.getValueAt(row, 0);
                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE coffee_shops SET shop_name=?, location=?, opening_hours=?, contact_phone=? WHERE shop_id=?")) {
                    stmt.setString(1, nombreField.getText());
                    stmt.setString(2, ubicacionField.getText());
                    stmt.setString(3, horarioField.getText());
                    stmt.setString(4, telefonoField.getText());
                    stmt.setInt(5, id);
                    stmt.executeUpdate();
                    model.setRowCount(0);
                    cargarDatos();
                    limpiarCampos(nombreField, ubicacionField, horarioField, telefonoField);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            eliminarBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row == -1) return;
                int id = (int) model.getValueAt(row, 0);
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM coffee_shops WHERE shop_id = ?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                    model.setRowCount(0);
                    cargarDatos();
                    limpiarCampos(nombreField, ubicacionField, horarioField, telefonoField);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cargarDatos();
        }

        private void cargarDatos() {
            model.setRowCount(0);
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM coffee_shops")) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("shop_id"),
                            rs.getString("shop_name"),
                            rs.getString("location"),
                            rs.getString("opening_hours"),
                            rs.getString("contact_phone")
                    });
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void limpiarCampos(JTextField nombre, JTextField ubicacion, JTextField horario, JTextField telefono) {
            nombre.setText("");
            ubicacion.setText("");
            horario.setText("");
            telefono.setText("");
        }
    }

}