import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MenuInterfaz extends JFrame {
    private final Scoreboard scoreboard;
    public static final Color NAVY = new Color(29, 64, 116);
    public static final Color DWhite = new Color(244, 244, 246);
    

    public MenuInterfaz(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;

        setTitle("Menú de Videojuego - MathOperations");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Carga la imagen de fondo
        ImageIcon imageIcon = new ImageIcon("math.jpg");
        Image image = imageIcon.getImage();

        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibuja la imagen de fondo
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        // Añade un espacio flexible antes del panel de botones
        panelPrincipal.add(Box.createVerticalGlue());
        // Añade un espacio vertical adicional
        panelPrincipal.add(Box.createVerticalStrut(100));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false); // Hace que el panel de botones sea transparente

        Dimension dimensionBoton = new Dimension(200, 50);

        JButton jugarButton = new JButton("Jugar");
        JButton scoreboardButton = new JButton("Scoreboard");
        JButton creditosButton = new JButton("Créditos");

        // Cambia el color de los botones a un tono más claro y el texto a un tono más oscuro
        jugarButton.setBackground(NAVY);
        jugarButton.setForeground(DWhite);
        scoreboardButton.setBackground(NAVY);
        scoreboardButton.setForeground(DWhite);
        creditosButton.setBackground(NAVY);
        creditosButton.setForeground(DWhite);

        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        jugarButton.setFont(buttonFont);
        scoreboardButton.setFont(buttonFont);
        creditosButton.setFont(buttonFont);

        JButton[] botones = {jugarButton, scoreboardButton, creditosButton};
        for (JButton boton : botones) {
            boton.setPreferredSize(dimensionBoton);
            boton.setMaximumSize(dimensionBoton);
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            // Centra el texto en los botones
            boton.setHorizontalAlignment(SwingConstants.CENTER);
            boton.setVerticalAlignment(SwingConstants.CENTER);
            panelBotones.add(boton);
            panelBotones.add(Box.createVerticalStrut(30));
        }

        panelPrincipal.add(panelBotones);

        // Añade un espacio flexible después del panel de botones
        panelPrincipal.add(Box.createVerticalGlue());

        add(panelPrincipal);

        jugarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreJugador = JOptionPane.showInputDialog(MenuInterfaz.this, "Ingresa tu nombre:");
                if (nombreJugador != null && !nombreJugador.trim().isEmpty()) {
                    new JuegoMatematico(scoreboard, nombreJugador).setVisible(true);
                }
            }
        });

        scoreboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarScoreboard();
            }
        });

        // Define los colores basados en la imagen


        creditosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane textPane = new JTextPane();
                textPane.setText("     Universidad Autónoma de Baja California\n Ingeniería en Software y Tecnologías Emergentes\n\n Creadores:\n Steven Nicolae De La Cruz Estrada\n Jose Miguel Velazquez Angulo\n Edgar Yahel Rico Guzman");
                textPane.setFont(new Font("Courier New", Font.BOLD, 14));
                textPane.setForeground(Color.BLACK); // Color del texto
        
                JOptionPane.showMessageDialog(MenuInterfaz.this, textPane, "Créditos", JOptionPane.PLAIN_MESSAGE);
            }
        });

        
    }

    private void mostrarScoreboard() {
        List<String> topScores = scoreboard.getTopScores();
        StringBuilder scoreboardText = new StringBuilder("Top 5 Scoreboard:\n\n");            for (String score : topScores) {
            scoreboardText.append(score).append("\n");
        }
        JOptionPane.showMessageDialog(this, scoreboardText.toString(), "Scoreboard", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        Scoreboard scoreboard = new Scoreboard("scoreboard.txt");
        SwingUtilities.invokeLater(() -> {
            MenuInterfaz menuInterfaz = new MenuInterfaz(scoreboard);
            menuInterfaz.setVisible(true);
        });
    }
}
