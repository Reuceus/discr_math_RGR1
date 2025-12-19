package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import graph.*;

public class Reader {
    public static Graph readGraph(String filename) {
        Logger.info("Чтаю граф из файла.");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String firstLine = br.readLine();
            if (firstLine == null) {
                throw new RuntimeException("Файл пуст");
            }

            String[] parts = firstLine.split("\\s+");
            int V = Integer.parseInt(parts[0]);
            int E = Integer.parseInt(parts[1]);

            Graph G = new Graph(V);

            for (int i = 1; i <= E; i++) {
                String line = br.readLine();
                if (line == null) {
                    throw new RuntimeException("Недостаточно строк в файле. Ожидалось: " + E);
                }

                String[] edgeParts = line.split("\\s+");
                int u = Integer.parseInt(edgeParts[0]);
                int v = Integer.parseInt(edgeParts[1]);

                G.addEdge(u, v);
            }

            return G;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + e.getMessage());
        }
    }

    // ========== НОВЫЙ МЕТОД ДЛЯ ЧТЕНИЯ ОРИЕНТИРОВАННЫХ ГРАФОВ ==========

    /**
     * Читает ориентированный граф из файла
     * Формат файла:
     * Первая строка: V E (количество вершин и рёбер)
     * Следующие E строк: u v (ориентированное ребро из u в v)
     *
     * @param filename имя файла
     * @return объект Digraph
     */
    public static Digraph readDigraph(String filename) {
        Logger.info("Чтаю ориентированный граф из файла: " + filename);
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String firstLine = br.readLine();
            if (firstLine == null) {
                throw new RuntimeException("Файл пуст: " + filename);
            }

            String[] parts = firstLine.split("\\s+");
            int V = Integer.parseInt(parts[0]);
            int E = Integer.parseInt(parts[1]);

            Digraph G = new Digraph(V);
            Logger.info("Создан ориентированный граф с " + V + " вершинами и " + E + " рёбрами");

            int edgesRead = 0;
            String line;
            while ((line = br.readLine()) != null && edgesRead < E) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Пропускаем пустые строки и комментарии
                }

                String[] edgeParts = line.split("\\s+");
                if (edgeParts.length < 2) {
                    Logger.warning("Пропускаю некорректную строку: " + line);
                    continue;
                }

                int u = Integer.parseInt(edgeParts[0]);
                int v = Integer.parseInt(edgeParts[1]);

                // Проверка корректности индексов вершин
                if (u < 0 || u >= V || v < 0 || v >= V) {
                    Logger.warning("Пропускаю ребро с некорректными индексами: " + u + " -> " + v);
                    continue;
                }

                G.addEdge(u, v);
                edgesRead++;

                if (edgesRead % 1000 == 0) {
                    Logger.debug("Прочитано " + edgesRead + " рёбер из " + E);
                }
            }

            if (edgesRead < E) {
                Logger.warning("Прочитано только " + edgesRead + " рёбер из " + E + " ожидаемых");
            } else {
                Logger.info("Успешно прочитано " + edgesRead + " рёбер");
            }

            return G;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла " + filename + ": " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Некорректный формат числа в файле " + filename + ": " + e.getMessage());
        }
    }
}