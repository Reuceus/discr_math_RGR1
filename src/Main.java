import algs.Bridges;
import graph.Graph;
import algs.DFS;
import graph.GraphGenerator;
import utils.Logger;
import utils.Reader;

public class Main {
    public static void main(String[] args) {

        Logger.clearLog();
        Logger.info("Начало программы.");
        Graph G = Reader.readGraph("graph.txt");
        Bridges b = new Bridges();

        System.out.println("Список смежности:");
        for (int i = 0; i < G.V; i++) {
            System.out.println(i + ": " + G.adj.get(i));
        }

        System.out.println("\nDFS обход начиная с вершины 0:");
        DFS dfs = new DFS(G);
        dfs.run(0);
        dfs.getResults();
        b.findBridges(G);
    }
}