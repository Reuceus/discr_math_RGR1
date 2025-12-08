package graph;
import utils.Logger;

import java.util.*;

public class GraphGenerator {

    public static Graph graphGenerator(int V, int E) {
        Graph G = new Graph(V);
        Random rand = new Random();
        Set<String> edges = new  HashSet<>();

        while (edges.size() < E){
            int u  = rand.nextInt(V);
            int v = rand.nextInt(V);

            if (u != v) {
                String edge = Math.min(u, v) + "-"  + Math.max(u,v);
                if (!edges.contains(edge)) {
                    edges.add(edge);
                    G.addEdge(u, v);
                }
            }
        }

        Logger.info("Граф успешно сгенерирован.");
        return G;
    }
}
