package graph;

public class Digraph extends Graph{
    public Digraph(int V) {
        super(V);
    }

    @Override
    public void addEdge(int v, int w) {
        adj.get(v).add(w);
    }
}
