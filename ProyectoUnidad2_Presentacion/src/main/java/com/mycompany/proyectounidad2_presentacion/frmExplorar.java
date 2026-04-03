/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.proyectounidad2_presentacion;

import com.mycompany.proyectounidad2_dominio.Estudiante;
import com.mycompany.proyectounidad2_dominio.Hobby;
import com.mycompany.proyectounidad2_dominio.Match;
import com.mycompany.proyectounidad2_dominio.TipoReaccion;
import com.mycompany.proyectounidad2_exceptions.NegocioException;
import com.mycompany.proyectounidad2_servicios.EstudianteService;
import com.mycompany.proyectounidad2_servicios.IEstudianteService;
import com.mycompany.proyectounidad2_servicios.IMatchService;
import com.mycompany.proyectounidad2_servicios.IReaccionService;
import com.mycompany.proyectounidad2_servicios.MatchService;
import com.mycompany.proyectounidad2_servicios.ReaccionService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * /**
 * Formulario encargado de mostrar perfiles disponibles para explorar.
 *
 * Permite al usuario: - Ver información de otros estudiantes - Dar LIKE o NO
 * INTERESA
 *
 * La lista se actualiza dinámicamente después de cada reacción.
 *
 * @author Afgord
 */
public class frmExplorar extends javax.swing.JFrame {

    private Estudiante usuarioActual;
    private List<Estudiante> perfiles;
    private int indiceActual = 0;
    private Long idObjetivo;

    private IEstudianteService estudianteService = new EstudianteService();
    private IReaccionService reaccionService = new ReaccionService();
    private IMatchService matchService = new MatchService();

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmExplorar.class.getName());

    /**
     * Creates new form frmExplorar
     */
    public frmExplorar(Estudiante usuario) {
        initComponents();
        this.usuarioActual = usuario;

        txtHobbies.setLineWrap(true);
        txtHobbies.setWrapStyleWord(true);
        txtHobbies.setEditable(false);
        txtHobbies.setOpaque(false);

        btnAnterior.setIcon(cargarIcono("/imagenes/iconos/flecha_izquierda.png"));
        btnAnterior.setText("");
        btnAnterior.setBorderPainted(false);
        btnAnterior.setContentAreaFilled(false);

        btnSiguiente.setIcon(cargarIcono("/imagenes/iconos/flecha_derecha.png"));
        btnSiguiente.setText("");
        btnSiguiente.setBorderPainted(false);
        btnSiguiente.setContentAreaFilled(false);

        btnLike.setIcon(cargarIcono("/imagenes/iconos/like.png"));
        btnLike.setText("LIKE");
        btnLike.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLike.setIconTextGap(10);
        btnLike.setBackground(new java.awt.Color(76, 175, 80)); // verde
        btnLike.setForeground(java.awt.Color.WHITE);

        btnNoInteresa.setIcon(cargarIcono("/imagenes/iconos/dislike.png"));
        btnNoInteresa.setText("NO ME INTERESA");
        btnNoInteresa.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNoInteresa.setIconTextGap(10);
        btnNoInteresa.setBackground(new java.awt.Color(244, 67, 54)); // rojo
        btnNoInteresa.setForeground(java.awt.Color.WHITE);

        lblFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFoto.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        lblFoto.setPreferredSize(new java.awt.Dimension(120, 120));
        lblFoto.setMinimumSize(new java.awt.Dimension(120, 120));

        cargarPerfiles();
        mostrarPerfilActual();
        setLocationRelativeTo(null);
    }

    public frmExplorar(Estudiante usuario, Long idObjetivo) {
        initComponents();
        this.usuarioActual = usuario;
        this.idObjetivo = idObjetivo;

        txtHobbies.setLineWrap(true);
        txtHobbies.setWrapStyleWord(true);
        txtHobbies.setEditable(false);
        txtHobbies.setOpaque(false);

        btnAnterior.setIcon(cargarIcono("/imagenes/iconos/flecha_izquierda.png"));
        btnAnterior.setText("");
        btnAnterior.setBorderPainted(false);
        btnAnterior.setContentAreaFilled(false);

        btnSiguiente.setIcon(cargarIcono("/imagenes/iconos/flecha_derecha.png"));
        btnSiguiente.setText("");
        btnSiguiente.setBorderPainted(false);
        btnSiguiente.setContentAreaFilled(false);

        btnLike.setIcon(cargarIcono("/imagenes/iconos/like.png"));
        btnLike.setText("LIKE");
        btnLike.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLike.setIconTextGap(10);
        btnLike.setBackground(new java.awt.Color(76, 175, 80)); // verde
        btnLike.setForeground(java.awt.Color.WHITE);

        btnNoInteresa.setIcon(cargarIcono("/imagenes/iconos/dislike.png"));
        btnNoInteresa.setText("NO ME INTERESA");
        btnNoInteresa.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNoInteresa.setIconTextGap(10);
        btnNoInteresa.setBackground(new java.awt.Color(244, 67, 54)); // rojo
        btnNoInteresa.setForeground(java.awt.Color.WHITE);

        lblFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFoto.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        lblFoto.setPreferredSize(new java.awt.Dimension(120, 120));
        lblFoto.setMinimumSize(new java.awt.Dimension(120, 120));

        cargarPerfiles();
        posicionarEnObjetivo();
        mostrarPerfilActual();
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblNombre = new javax.swing.JLabel();
        lblCarrera = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        lblFoto = new javax.swing.JLabel();
        btnAnterior = new javax.swing.JButton();
        btnLike = new javax.swing.JButton();
        btnNoInteresa = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtHobbies = new javax.swing.JTextArea();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("¡¡EXPLORAR TUS POSIBLES MATCHES!!");

        lblNombre.setText("Nombre: ");

        lblCarrera.setText("Carrera: ");

        jLabel4.setText("Descripción:");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane1.setViewportView(txtDescripcion);

        lblFoto.setText("Foto:");

        btnAnterior.setText("Anterior");
        btnAnterior.addActionListener(this::btnAnteriorActionPerformed);

        btnLike.setText("LIKE");
        btnLike.addActionListener(this::btnLikeActionPerformed);

        btnNoInteresa.setText("NO ME INTERESA");
        btnNoInteresa.addActionListener(this::btnNoInteresaActionPerformed);

        btnSiguiente.setText("Siguiente");
        btnSiguiente.addActionListener(this::btnSiguienteActionPerformed);

        jLabel2.setText("Hobbies");

        txtHobbies.setColumns(20);
        txtHobbies.setRows(5);
        jScrollPane2.setViewportView(txtHobbies);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(lblCarrera))
                        .addContainerGap(365, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblFoto)
                        .addGap(126, 126, 126))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnAnterior)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLike)
                                .addGap(18, 18, 18)
                                .addComponent(btnNoInteresa)
                                .addGap(18, 18, 18)
                                .addComponent(btnSiguiente)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(lblFoto))
                .addGap(18, 18, 18)
                .addComponent(lblCarrera)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnterior)
                    .addComponent(btnLike)
                    .addComponent(btnNoInteresa)
                    .addComponent(btnSiguiente))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        btnSalir.setText("Regresar");
        btnSalir.addActionListener(this::btnSalirActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(btnSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnSalir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        if (perfiles == null || perfiles.isEmpty()) {
            return;
        }

        if (indiceActual > 0) {
            indiceActual--;
            mostrarPerfilActual();
        } else {
            JOptionPane.showMessageDialog(this, "Ya estás en el primer perfil.");
        }
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnNoInteresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoInteresaActionPerformed
        reaccionar(TipoReaccion.NO_INTERESA);
    }//GEN-LAST:event_btnNoInteresaActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        if (perfiles == null || perfiles.isEmpty()) {
            return;
        }

        if (indiceActual < perfiles.size() - 1) {
            indiceActual++;
            mostrarPerfilActual();
        } else {
            JOptionPane.showMessageDialog(this, "Ya no hay más perfiles.");
        }
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void btnLikeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLikeActionPerformed
        reaccionar(TipoReaccion.LIKE);
    }//GEN-LAST:event_btnLikeActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        frmPrincipal frm = new frmPrincipal(usuarioActual);
        frm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnLike;
    private javax.swing.JButton btnNoInteresa;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCarrera;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextArea txtHobbies;
    // End of variables declaration//GEN-END:variables

    private void cargarPerfiles() {
        try {
            List<Estudiante> compatibles = estudianteService.buscarConHobbiesEnComun(usuarioActual.getId());
            List<Estudiante> explorables = estudianteService.explorarPerfiles(usuarioActual.getId());
            List<Match> matches = matchService.obtenerMatchesDeEstudiante(usuarioActual.getId());

            Set<Long> idsConMatch = new HashSet<>();
            for (Match m : matches) {
                if (m.getEstudiante1().getId().equals(usuarioActual.getId())) {
                    idsConMatch.add(m.getEstudiante2().getId());
                } else {
                    idsConMatch.add(m.getEstudiante1().getId());
                }
            }

            Map<Long, Estudiante> mapa = new LinkedHashMap<>();

            // Primero compatibles
            for (Estudiante e : compatibles) {
                if (!idsConMatch.contains(e.getId())) {
                    mapa.put(e.getId(), e);
                }
            }

            // Luego el resto explorables
            for (Estudiante e : explorables) {
                if (!idsConMatch.contains(e.getId())) {
                    mapa.putIfAbsent(e.getId(), e);
                }
            }

            perfiles = new ArrayList<>(mapa.values());

            if (perfiles.isEmpty()) {
                btnLike.setEnabled(false);
                btnNoInteresa.setEnabled(false);
                btnSiguiente.setEnabled(false);
                btnAnterior.setEnabled(false);

                JOptionPane.showMessageDialog(this, "No hay perfiles disponibles.");
            } else {
                btnLike.setEnabled(true);
                btnNoInteresa.setEnabled(true);
                btnSiguiente.setEnabled(true);
                btnAnterior.setEnabled(true);
            }

        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al cargar perfiles.");
        }
    }

    private void mostrarPerfilActual() {
        if (perfiles == null || perfiles.isEmpty()) {
            lblNombre.setText("Sin perfiles");
            lblCarrera.setText("");
            txtDescripcion.setText("");
            txtHobbies.setText("");
            lblFoto.setIcon(null);
            lblFoto.setText("Sin foto");

            btnLike.setEnabled(false);
            btnNoInteresa.setEnabled(false);
            btnSiguiente.setEnabled(false);
            btnAnterior.setEnabled(false);
            return;
        }

        if (indiceActual < 0) {
            indiceActual = 0;
        }

        if (indiceActual >= perfiles.size()) {
            indiceActual = perfiles.size() - 1;
        }

        Estudiante e = perfiles.get(indiceActual);

        Estudiante detalle = estudianteService.buscarPorIdConHobbies(e.getId());

        lblNombre.setText(detalle.getNombre() + " " + detalle.getApPat());
        lblCarrera.setText(detalle.getCarrera());
        txtDescripcion.setText(detalle.getDescripcion() != null ? detalle.getDescripcion() : "");

        String hobbiesTexto = detalle.getHobbies().isEmpty()
                ? "Sin hobbies registrados"
                : detalle.getHobbies().stream()
                        .map(Hobby::getNombre)
                        .sorted()
                        .collect(Collectors.joining(", "));

        txtHobbies.setText(hobbiesTexto);

        cargarFotoPerfil(detalle.getFotoPerfil());

        btnAnterior.setEnabled(indiceActual > 0);
        btnSiguiente.setEnabled(indiceActual < perfiles.size() - 1);
        btnLike.setEnabled(true);
        btnNoInteresa.setEnabled(true);
    }

    private void reaccionar(TipoReaccion tipo) {
        if (perfiles == null || perfiles.isEmpty()) {
            return;
        }

        if (indiceActual < 0 || indiceActual >= perfiles.size()) {
            return;
        }

        Estudiante receptor = perfiles.get(indiceActual);

        try {
            if (tipo == TipoReaccion.LIKE) {
                int matchesAntes = matchService
                        .obtenerMatchesDeEstudiante(usuarioActual.getId())
                        .size();

                reaccionService.registrarReaccion(usuarioActual, receptor, tipo);

                int matchesDespues = matchService
                        .obtenerMatchesDeEstudiante(usuarioActual.getId())
                        .size();

                if (matchesDespues > matchesAntes) {
                    JOptionPane.showMessageDialog(this, "💥 ¡ES UN MATCH!");
                } else {
                    JOptionPane.showMessageDialog(this, "Te gusta este perfil");
                }

            } else {
                reaccionService.registrarReaccion(usuarioActual, receptor, tipo);
                JOptionPane.showMessageDialog(this, "Se registró tu reacción.");
            }

            cargarPerfiles();

            if (perfiles == null || perfiles.isEmpty()) {
                indiceActual = 0;
                mostrarPerfilActual();
                return;
            }

            if (indiceActual >= perfiles.size()) {
                indiceActual = perfiles.size() - 1;
            }

            mostrarPerfilActual();

        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al registrar la reacción.");
        }
    }

    private void cargarFotoPerfil(String nombreArchivo) {
        try {
            if (nombreArchivo == null || nombreArchivo.isBlank()) {
                nombreArchivo = "default.jpg";
            }

            java.net.URL url = getClass().getResource("/imagenes/perfiles/" + nombreArchivo);

            if (url == null) {
                url = getClass().getResource("/imagenes/perfiles/default.jpg");
            }

            ImageIcon iconoOriginal = new ImageIcon(url);
            java.awt.Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
                    100, 100, java.awt.Image.SCALE_SMOOTH
            );

            lblFoto.setIcon(new ImageIcon(imagenEscalada));
            lblFoto.setText("");

        } catch (Exception e) {
            lblFoto.setIcon(null);
            lblFoto.setText("Sin foto");
        }

        lblFoto.revalidate();
        lblFoto.repaint();
        pack();
        setLocationRelativeTo(null);
    }

    private ImageIcon cargarIcono(String ruta) {
        java.net.URL url = getClass().getResource(ruta);

        if (url == null) {
            System.out.println("No se encontró: " + ruta);
            return null;
        }

        ImageIcon icono = new ImageIcon(url);
        java.awt.Image img = icono.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void posicionarEnObjetivo() {
        if (idObjetivo == null || perfiles == null || perfiles.isEmpty()) {
            return;
        }

        for (int i = 0; i < perfiles.size(); i++) {
            if (perfiles.get(i).getId().equals(idObjetivo)) {
                indiceActual = i;
                return;
            }
        }
    }

}
