import algs.Bridges;
import algs.DFS;
import algs.TopologicalSearch; // ДОБАВЛЕНО
import graph.*;
import utils.Logger;
import utils.Reader;

public class Main {
    public static void main(String[] args) {

        Graph graph = Reader.readGraph("graph.txt");
        Logger.clearLog();
        Logger.info("Начало программы.");
        Graph G = GraphGenerator.graphGenerator(20, 19);

        System.out.println("Список смежности:");
        for (int i = 0; i < G.V; i++) {
            System.out.println(i + ": " + G.adj.get(i));
        }

        System.out.println("\nDFS обход начиная с вершины 0:");
        DFS dfs = new DFS(G);
        dfs.run(0);
        dfs.getResults();

        Digraph g1 = GraphGenerator.digraphGenerator(20, 19);

        // ДОБАВЛЕНО: Топологическая сортировка для графа g1
        System.out.println("\nТОПОЛОГИЧЕСКАЯ СОРТИРОВКА ДЛЯ ГРАФА g1");

        // Выводим информацию о графе g1
        for (int i = 0; i < g1.V; i++) {
            System.out.println("Вершина " + i + " -> " + g1.adj.get(i));
        }

        // Выполняем топологическую сортировку
        TopologicalSearch ts = new TopologicalSearch(g1);
        var result = ts.sort();

        // Выводим результат
        if (ts.hasCycle()) {
            System.out.println("Граф содержит цикл! Сортировка невозможна.");
            System.out.print("Обнаружен цикл: ");
            var cycle = ts.getCycle();
            if (!cycle.isEmpty()) {
                for (int i = 0; i < cycle.size(); i++) {
                    System.out.print(cycle.get(i));
                    if (i < cycle.size() - 1) {
                        System.out.print(" → ");
                    }
                }
                System.out.println();
            }
        } else {
            System.out.println("Топологический порядок вершин: " + result);

            // Дополнительная проверка
            System.out.println("Всего вершин в порядке: " + result.size());
        }

        // ДОБАВЛЕНО: Вывод подробных результатов
        System.out.println("\nДетали топологической сортировки:");
        ts.printResult();

        System.out.println("\nКОНЕЦ ТОПОЛОГИЧЕСКОЙ СОРТИРОВКИ");
    }
}