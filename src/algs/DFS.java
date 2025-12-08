package algs;

import graph.Graph;
import utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DFS {

    public boolean[] visited;
    public Graph graph;
    private List<Integer> order;
    private int[] parent;

    public  DFS (Graph g) {
        this.graph = g;
        this.visited = new boolean[g.V];
        order = new ArrayList<>();
        this.parent = new int[g.V];
        Arrays.fill(parent, -1);
    }

    public void run(int start) {
        dfs(start);
        Logger.logDFSResults(visited, parent);
    }

    private void dfs(int u) {
        visited[u] = true;
        order.add(u);
        List<Integer> neighbours = graph.adj.get(u);

        System.out.println("Проходим " + u);
        Logger.info("Посещаю вершину " + u);

        if  (neighbours != null) {
            for (Integer v : neighbours) {
                if (v < 0 || v>= graph.V) {continue;}
                if (!visited[v]) {
                    parent[v] = u;
                    dfs(v);
                }
            }
        }
    }

    public void getResults() {
        System.out.println("Порядок обхода DFS:" + order);

        System.out.println("Родители");
        for (int i = 0; i < parent.length; i++) {
            System.out.println(i + ": " + parent[i]);
        }
    }
}
