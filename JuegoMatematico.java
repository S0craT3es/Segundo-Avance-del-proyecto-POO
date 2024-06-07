import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class JuegoMatematico extends JFrame {
    private JLabel preguntaLabel;
    private JButton[] opcionesBotones;
    private int respuestaCorrecta;
    private int puntuacion;
    private int tiempoRestante;
    private Timer temporizador;
    private JLabel puntuacionLabel;
    private Clip clipCorrecto;
    private Clip clipIncorrecto;
    private final Scoreboard scoreboard;
    private final String nombreJugador;

    public JuegoMatematico(Scoreboard scoreboard, String nombreJugador) {
        this.scoreboard = scoreboard;
        this.nombreJugador = nombreJugador;

        setTitle("Juego Matemático");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        preguntaLabel = new JLabel("", SwingConstants.CENTER);
        panel.add(preguntaLabel, BorderLayout.NORTH);

        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new GridLayout(2, 2));

        opcionesBotones = new JButton[4];
        for (int i = 0; i < 4; i++) {
            opcionesBotones[i] = new JButton();
            opcionesBotones[i].addActionListener(this::verificarRespuesta);
            panelOpciones.add(opcionesBotones[i]);
        }

        panel.add(panelOpciones, BorderLayout.CENTER);

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BorderLayout());
        puntuacionLabel = new JLabel("Puntuacion: " + puntuacion);
        JLabel tiempoLabel = new JLabel("Tiempo: " + tiempoRestante);

        panelInfo.add(puntuacionLabel, BorderLayout.WEST);
        panelInfo.add(tiempoLabel, BorderLayout.EAST);

        panel.add(panelInfo, BorderLayout.SOUTH);

        add(panel);

        tiempoRestante = 10;
        temporizador = new Timer();
        temporizador.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tiempoRestante--;
                tiempoLabel.setText("Tiempo: " + tiempoRestante);

                if (tiempoRestante <= 0) {
                    temporizador.cancel();
                    JOptionPane.showMessageDialog(JuegoMatematico.this, "Tiempo agotado. Puntuacion final: " + puntuacion);
                    guardarPuntuacion();
                    dispose();
                }
            }
        }, 1000, 1000);

        generarPregunta();

        try {
            clipCorrecto = cargarSonido("Correct-Answer-Sound-Effect.wav");
            clipIncorrecto = cargarSonido("error.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private Clip cargarSonido(String ruta) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File soundFile = new File(ruta);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        return clip;
    }

    private void generarPregunta() {
        Random random = new Random();
        int a = random.nextInt(10);
        int b = random.nextInt(10);
        int operacion = random.nextInt(3);
        int resultadoCorrecto = 0;

        if (operacion == 0) {
            resultadoCorrecto = a + b;
            preguntaLabel.setText("¿Cuanto es " + a + " + " + b + "?");
        } else if (operacion == 1) {
            resultadoCorrecto = a * b;
            preguntaLabel.setText("¿Cuanto es " + a + " x " + b + "?");
        } else if (operacion == 2) {
            resultadoCorrecto = a - b;
            preguntaLabel.setText("¿Cuanto es " + a + " - " + b + "?");
        }

        respuestaCorrecta = random.nextInt(4);
        for (int i = 0; i < 4; i++) {
            if (i == respuestaCorrecta) {
                opcionesBotones[i].setText(String.valueOf(resultadoCorrecto));
            } else {
                int incorrecto;
                do {
                    incorrecto = random.nextInt(20);
                } while (incorrecto == resultadoCorrecto);
                opcionesBotones[i].setText(String.valueOf(incorrecto));
            }
        }
    }

    private void verificarRespuesta(ActionEvent e) {
        JButton botonPulsado = (JButton) e.getSource();
        int indiceRespuestaElegida = -1;
        for (int i = 0; i < opcionesBotones.length; i++) {
            if (opcionesBotones[i] == botonPulsado) {
                indiceRespuestaElegida = i;
                break;
            }
        }

        if (indiceRespuestaElegida == respuestaCorrecta) {
            puntuacion++;
            puntuacionLabel.setText("Puntuacion: " + puntuacion);
            if (clipCorrecto != null) {
                clipCorrecto.setFramePosition(0);
                clipCorrecto.start();
            }
            JOptionPane.showMessageDialog(this, "¡Correcto!");
        } else {
            if (clipIncorrecto != null) {
                clipIncorrecto.setFramePosition(0);
                clipIncorrecto.start();
            }
            JOptionPane.showMessageDialog(this, "¡Incorrecto!");
        }

        generarPregunta();
    }

    private void guardarPuntuacion() {
        scoreboard.addScore(nombreJugador, puntuacion);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Scoreboard scoreboard = new Scoreboard("scoreboard.txt");
            MenuInterfaz menuInterfaz = new MenuInterfaz(scoreboard);
            menuInterfaz.setVisible(true);
        });
    }
}
