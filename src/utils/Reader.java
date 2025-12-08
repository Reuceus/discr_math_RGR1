package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import graph.*;

public class Reader {
    public static Graph readGraph (String filename) {
        Logger.info("Читаю граф из файла.");
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
                int u =  Integer.parseInt(edgeParts[0]);
                int v =  Integer.parseInt(edgeParts[1]);

                G.addEdge(u,v);
            }

            return G;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + e.getMessage());
        }
    }
}
