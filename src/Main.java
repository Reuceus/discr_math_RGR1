import algs.Bridges;
import graph.Graph;
import algs.DFS;
import graph.GraphGenerator;

public class Main {
    public static void main(String[] args) {
        Graph G = GraphGenerator.graphGenerator(20,25); //Альтернатива: Graph G = new Graph(6); G.addEdge(0, 1); и т.д.
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