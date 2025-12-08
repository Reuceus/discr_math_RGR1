package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class Logger {

    private static final String LOG_FILE = "log.txt";

    private static void write (String level, String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(LocalDateTime.now() + "[" + level + "]\n" + message + "\n\n");
        } catch (IOException e) {
            System.out.println("Ошибка записи в отчет: " + e.getMessage());
        }
    }

    public static void logDFSResults(boolean[] visited, int[] parent) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Итоги DFS ===\n");
        sb.append("Visited: ");
        for (int i = 0; i < visited.length; i++) {
            sb.append(i).append("=").append(visited[i]).append(" ");
        }
        sb.append("\nParent: ");
        for (int i = 0; i < parent.length; i++) {
            sb.append(i).append("->").append(parent[i]).append(" ");
        }
        sb.append("\n=================\n");

        info(sb.toString());
    }

    public static void logBridges(List<String> bridges) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Найденные мосты ===\n");

        if (bridges.isEmpty()) {
            sb.append("Мостов нет.\n");
        } else {
            for (String b : bridges) {
                sb.append(b).append("\n");
            }
        }

        sb.append("========================\n");
        info(sb.toString());
    }


    public static void info(String msg) {
        write("INFO", msg);
    }

    public static void warning(String msg) {
        write("WARNING", msg);
    }

    public static void error(String msg) {
        write("ERROR", msg);
    }

    public static void clearLog() {
        try (FileWriter fw = new FileWriter(LOG_FILE, false)) {
            // false = перезаписать файл (очистить)
        } catch (IOException e) {
            System.out.println("Ошибка очистки лог-файла: " + e.getMessage());
        }
    }
}
