import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scoreboard {
    private final List<Score> scores;
    private final String fileName;

    public Scoreboard(String fileName) {
        this.fileName = fileName;
        this.scores = loadScores();
    }

    private List<Score> loadScores() {
        List<Score> loadedScores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    loadedScores.add(new Score(name, score));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return loadedScores;
    }

    private void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Score score : scores) {
                writer.write(score.getName() + ":" + score.getScore());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addScore(String playerName, int score) {
        scores.add(new Score(playerName, score));
        Collections.sort(scores, Collections.reverseOrder());
        if (scores.size() > 5) {
            scores.remove(scores.size() - 1); // Solo mantener el top 5
        }
        saveScores(); // Guardar los cambios en el archivo
    }

    public List<String> getTopScores() {
        List<String> topScores = new ArrayList<>();
        for (int i = 0; i < Math.min(5, scores.size()); i++) {
            Score score = scores.get(i);
            topScores.add((i + 1) + ". " + score.getName() + ": " + score.getScore());
        }
        return topScores;
    }
    

    public static class Score implements Comparable<Score> {
        private final String name;
        private final int score;

        public Score(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        @Override
        public int compareTo(Score other) {
            return Integer.compare(score, other.score);
        }
    }
}

