package algs;

import  java.util.*;
import graph.*;
import utils.Logger;

public class Bridges {

    private int time;
    private List<String> bridges;

    public Bridges () {
        this.time = 0;
        this.bridges = new ArrayList<>();
    }

    public void findBridges(Graph g) {
        int V = g.V;
        boolean[] visited = new boolean[V];
        int[] disc = new int[V];
        int[] low =  new int[V];
        int[] parent = new int[V];
        Arrays.fill(parent, -1);

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                dfs(i, visited, disc, low, parent, g);
            }
        }
        if(bridges.isEmpty()){
            System.out.println("Мосты не найдены.");
        } else {
            System.out.println("Найденные мосты:");
            for (String edge : bridges) {
                System.out.println(edge);
            }
        }

        Logger.logBridges(bridges);
    }

    private void dfs(int u, boolean[] visited, int[] disc, int[] low, int[] parent, Graph g) {
        visited[u] = true;
        disc[u] = low[u] = ++time;

        for (int v : g.adj.get(u)) {
            if (!visited[v]) {
                parent[v] = u;
                dfs(v, visited, disc, low, parent, g);

                low[u] = Math.min(low[u], low[v]);

                if (low[v] > disc[u]) {
                    bridges.add(u + " - " + v);
                }
            } else if (v != parent[u]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }
}
