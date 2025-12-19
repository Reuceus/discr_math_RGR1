package algs;

import graph.Digraph;
import utils.Logger;
import java.util.*;

public class TopologicalSearch extends DFS {

    private boolean[] inStack;
    private final List<Integer> sorted;
    private boolean hasCycle;
    private final List<Integer> cyclePath;

    public TopologicalSearch(Digraph digraph) {
        super(digraph);
        this.inStack = new boolean[digraph.V];
        this.sorted = new ArrayList<>();
        this.hasCycle = false;
        this.cyclePath = new ArrayList<>();

        Logger.info("Инициализирован TopologicalSearch для ориентированного графа с " + digraph.V + " вершинами");
    }

    public List<Integer> sort() {
        Logger.info("=== НАЧАЛО ТОПОЛОГИЧЕСКОЙ СОРТИРОВКИ ===");

        int vertices = graph.V; // Получаем количество вершин из графа

        Logger.info("Количество вершин: " + vertices);

        // Сбрасываем состояние для повторных вызовов
        resetState();

        for (int u = 0; u < vertices; u++) {
            if (!visited[u]) {
                Logger.debug("Начинаю обработку непосещённой вершины " + u);
                if (!dfsSort(u)) {
                    hasCycle = true;
                    Logger.error("Обнаружен цикл! Сортировка прервана");
                    logCycle();
                    return Collections.emptyList();
                }
            }
        }

        Collections.reverse(sorted);
        Logger.info("Топологическая сортировка завершена успешно");
        Logger.info("Результат: " + sorted);
        logDependencies();
        return sorted;
    }

    private void resetState() {
        int vertices = graph.V;
        Arrays.fill(visited, false);
        Arrays.fill(inStack, false);
        Arrays.fill(parent, -1);
        sorted.clear();
        hasCycle = false;
        cyclePath.clear();

        // Инициализируем массивы если размер изменился
        if (visited.length != vertices) {
            visited = new boolean[vertices];
            inStack = new boolean[vertices];
            parent = new int[vertices];
            Arrays.fill(parent, -1);
        }
    }

    private boolean dfsSort(int u) {
        visited[u] = true;
        inStack[u] = true;

        Logger.debug("Вход в вершину " + u + " (добавляю в стек)");
        // Получаем соседей через метод графа
        Iterable<Integer> neighbours = graph.adj.get(u);

        if (neighbours != null) {
            List<Integer> neighbourList = new ArrayList<>();
            for (int v : neighbours) {
                neighbourList.add(v);
            }
            Logger.debug("Вершина " + u + " имеет соседей: " + neighbourList);

            for (int v : neighbours) {
                if (v < 0 || v >= graph.V) {
                    Logger.warning("Пропускаю некорректную вершину: " + v);
                    continue;
                }

                Logger.debug("Проверяю ребро " + u + " → " + v);

                // Обнаружение цикла
                if (inStack[v]) {
                    Logger.error("ЦИКЛ ОБНАРУЖЕН: " + u + " → " + v);
                    recordCycle(u, v);
                    return false;
                }

                // Рекурсивный вызов для непосещённых вершин
                if (!visited[v]) {
                    parent[v] = u;
                    Logger.debug("Рекурсивный переход: " + u + " → " + v);
                    if (!dfsSort(v)) {
                        return false;
                    }
                } else {
                    Logger.debug("Вершина " + v + " уже посещена, пропускаю");
                }
            }
        } else {
            Logger.debug("Вершина " + u + " не имеет исходящих рёбер (сток)");
        }

        inStack[u] = false;
        sorted.add(u);

        Logger.debug("Выход из вершины " + u + " (убираю из стека, добавляю в результат)");
        Logger.debug("Текущий результат (в обратном порядке): " + sorted);
        return true;
    }

    private void recordCycle(int u, int v) {
        cyclePath.clear();

        // Начинаем с вершины, которая создала цикл
        cyclePath.add(v);

        // Идём назад по родительским ссылкам
        int current = u;
        while (current != v && current != -1) {
            cyclePath.add(current);
            current = parent[current];
        }

        // Замыкаем цикл
        if (current == v) {
            cyclePath.add(v);
            Collections.reverse(cyclePath);
        }
    }

    private void logCycle() {
        if (!cyclePath.isEmpty()) {
            StringBuilder cycleStr = new StringBuilder("Цикл: ");
            for (int i = 0; i < cyclePath.size(); i++) {
                cycleStr.append(cyclePath.get(i));
                if (i < cyclePath.size() - 1) {
                    cycleStr.append(" → ");
                }
            }
            Logger.error(cycleStr.toString());
        }
    }

    private void logDependencies() {
        Logger.info("=== ПРОВЕРКА ЗАВИСИМОСТЕЙ ===");

        // Создаем карту позиций для быстрой проверки
        Map<Integer, Integer> positions = new HashMap<>();
        for (int i = 0; i < sorted.size(); i++) {
            positions.put(sorted.get(i), i);
        }

        boolean allCorrect = true;
        int violations = 0;

        for (int u = 0; u < graph.V; u++) {
            Iterable<Integer> neighbours = graph.adj.get(u);
            if (neighbours != null) {
                for (int v : neighbours) {
                    if (v < 0 || v >= graph.V) continue;

                    Integer posU = positions.get(u);
                    Integer posV = positions.get(v);

                    if (posU != null && posV != null) {
                        if (posU > posV) {
                            Logger.warning("Нарушение зависимости: " + u + "(" + posU +
                                    ") → " + v + "(" + posV + ")");
                            allCorrect = false;
                            violations++;
                        } else {
                            Logger.debug("OK: " + u + "(" + posU +
                                    ") → " + v + "(" + posV + ")");
                        }
                    }
                }
            }
        }

        if (allCorrect) {
            Logger.info("Все зависимости соблюдены ✓");
        } else {
            Logger.warning("Найдено нарушений: " + violations);
        }
    }

    public void printResult() {
        List<Integer> result = sort();

        System.out.println("\n=== РЕЗУЛЬТАТ ТОПОЛОГИЧЕСКОЙ СОРТИРОВКИ ===");

        if (result.isEmpty()) {
            System.out.println("Граф содержит цикл!");
            if (!cyclePath.isEmpty()) {
                System.out.print("Обнаруженный цикл: ");
                System.out.println();
            }
        } else {
            System.out.println("Топологический порядок: " + result);

            // Дополнительная информация
            System.out.println("\nДополнительная информация:");
            System.out.println("Родительские связи:");
            boolean hasParents = false;
            for (int i = 0; i < parent.length; i++) {
                if (parent[i] != -1) {
                    System.out.println("  " + i + " ← " + parent[i]);
                    hasParents = true;
                }
            }
            if (!hasParents) {
                System.out.println("  (нет родительских связей)");
            }
        }
    }

    public boolean hasCycle() {
        return hasCycle;
    }

    public List<Integer> getCycle() {
        return new ArrayList<>(cyclePath);
    }
}