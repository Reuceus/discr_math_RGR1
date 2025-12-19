package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class Logger {

    private static final String LOG_FILE = "log.txt";
    private static boolean debugEnabled = false; // Новое поле для управления отладочным выводом

    private static void write (String level, String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(LocalDateTime.now() + "[" + level + "]\n" + message + "\n\n");
        } catch (IOException e) {
            System.out.println("Ошибка записи в отчет: " + e.getMessage());
        }
    }

    public static void logDFSResults(boolean[] visited, int[] parent) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Итоги DFS ===\n");
        sb.append("Visited: ");
        for (int i = 0; i < visited.length; i++) {
            sb.append(i).append("=").append(visited[i]).append(" ");
        }
        sb.append("\nParent: ");
        for (int i = 0; i < parent.length; i++) {
            sb.append(i).append("->").append(parent[i]).append(" ");
        }
        sb.append("\n=================\n");

        info(sb.toString());
    }

    public static void logBridges(List<String> bridges) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Найденные мосты ===\n");

        if (bridges.isEmpty()) {
            sb.append("Мостов нет.\n");
        } else {
            for (String b : bridges) {
                sb.append(b).append("\n");
            }
        }

        sb.append("========================\n");
        info(sb.toString());
    }


    public static void info(String msg) {
        write("INFO", msg);
    }

    public static void warning(String msg) {
        write("WARNING", msg);
    }

    public static void error(String msg) {
        write("ERROR", msg);
    }

    public static void clearLog() {
        try (FileWriter fw = new FileWriter(LOG_FILE, false)) {
            // false = перезаписать файл (очистить)
        } catch (IOException e) {
            System.out.println("Ошибка очистки лог-файла: " + e.getMessage());
        }
    }

    // ========== НОВЫЕ МЕТОДЫ ДЛЯ ТОПОЛОГИЧЕСКОЙ СОРТИРОВКИ ==========

    /**
     * Отладочное сообщение (пишется только если debug включен)
     */
    public static void debug(String msg) {
        if (debugEnabled) {
            write("DEBUG", msg);
        }
    }

    /**
     * Включает или выключает отладочный вывод
     */
    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
    }

    /**
     * Проверяет, включен ли отладочный вывод
     */
    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    /**
     * Логирование начала топологической сортировки
     */
    public static void logTopologicalSortStart(int vertexCount, int edgeCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== НАЧАЛО ТОПОЛОГИЧЕСКОЙ СОРТИРОВКИ ===\n");
        sb.append("Вершин: ").append(vertexCount).append("\n");
        sb.append("Рёбер: ").append(edgeCount);
        info(sb.toString());
    }

    /**
     * Логирование результата топологической сортировки
     */
    public static void logTopologicalSortResult(List<Integer> order, boolean hasCycle) {
        StringBuilder sb = new StringBuilder();

        if (hasCycle) {
            sb.append("=== ТОПОЛОГИЧЕСКАЯ СОРТИРОВКА: ОШИБКА ===\n");
            sb.append("Обнаружен цикл в графе\n");
            sb.append("Топологическая сортировка невозможна");
        } else {
            sb.append("=== ТОПОЛОГИЧЕСКАЯ СОРТИРОВКА: УСПЕХ ===\n");
            sb.append("Порядок вершин: ");
            if (order.isEmpty()) {
                sb.append("(пусто)");
            } else {
                for (int i = 0; i < order.size(); i++) {
                    sb.append(order.get(i));
                    if (i < order.size() - 1) {
                        sb.append(" → ");
                    }
                }
            }
            sb.append("\nВсего вершин в порядке: ").append(order.size());
        }

        info(sb.toString());
    }

    /**
     * Логирование обнаруженного цикла
     */
    public static void logCycleDetection(List<Integer> cycle) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ОБНАРУЖЕНИЕ ЦИКЛА ===\n");

        if (cycle == null || cycle.isEmpty()) {
            sb.append("Цикл обнаружен, но путь не восстановлен");
        } else {
            sb.append("Цикл найден: ");
            for (int i = 0; i < cycle.size(); i++) {
                sb.append(cycle.get(i));
                if (i < cycle.size() - 1) {
                    sb.append(" → ");
                }
            }
            sb.append("\nДлина цикла: ").append(cycle.size()).append(" вершин");
        }

        error(sb.toString());
    }

    /**
     * Логирование входа/выхода из вершины при DFS обходе
     */
    public static void logVertexVisit(String action, int vertex, List<Integer> stackState) {
        StringBuilder sb = new StringBuilder();
        sb.append(action).append(" вершину: ").append(vertex).append("\n");
        sb.append("Текущий стек: ");

        if (stackState == null || stackState.isEmpty()) {
            sb.append("пуст");
        } else {
            sb.append("[");
            for (int i = 0; i < stackState.size(); i++) {
                sb.append(stackState.get(i));
                if (i < stackState.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
        }

        debug(sb.toString());
    }

    /**
     * Логирование проверки рёбер на корректность порядка
     */
    public static void logEdgeValidation(int u, int v, int posU, int posV, boolean isValid) {
        StringBuilder sb = new StringBuilder();
        sb.append("Проверка ребра ").append(u).append(" → ").append(v).append(": ");

        if (isValid) {
            sb.append("OK");
        } else {
            sb.append("НАРУШЕНИЕ");
        }

        sb.append(" (u[").append(posU).append("], v[").append(posV).append("])");

        if (isValid) {
            debug(sb.toString());
        } else {
            warning(sb.toString());
        }
    }

    /**
     * Логирование списка источников (вершин с нулевой входящей степенью)
     */
    public static void logSources(List<Integer> sources) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ИСТОЧНИКИ ГРАФА ===\n");

        if (sources == null || sources.isEmpty()) {
            sb.append("Источников не найдено");
        } else {
            sb.append("Найдено источников: ").append(sources.size()).append("\n");
            sb.append("Список: ");
            for (int i = 0; i < sources.size(); i++) {
                sb.append(sources.get(i));
                if (i < sources.size() - 1) {
                    sb.append(", ");
                }
            }
        }

        info(sb.toString());
    }

    /**
     * Логирование проверки DAG (Directed Acyclic Graph)
     */
    public static void logDAGCheck(boolean isDAG, String details) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ПРОВЕРКА DAG ===\n");

        if (isDAG) {
            sb.append("Граф является ациклическим (DAG) ✓\n");
        } else {
            sb.append("Граф содержит циклы (не DAG) ✗\n");
        }

        if (details != null && !details.isEmpty()) {
            sb.append("Детали: ").append(details);
        }

        info(sb.toString());
    }

    /**
     * Логирование родителей в топологической сортировке
     */
    public static void logTopologicalParents(int[] parent) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== РОДИТЕЛЬСКИЕ СВЯЗИ ===\n");

        boolean hasParents = false;
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] != -1) {
                sb.append(i).append(" ← ").append(parent[i]).append("\n");
                hasParents = true;
            }
        }

        if (!hasParents) {
            sb.append("Родительские связи отсутствуют");
        }

        debug(sb.toString());
    }

    /**
     * Логирование времени выполнения операции
     */
    public static void logExecutionTime(String operation, long startTime, long endTime) {
        long duration = endTime - startTime;
        StringBuilder sb = new StringBuilder();
        sb.append("=== ВРЕМЯ ВЫПОЛНЕНИЯ ===\n");
        sb.append("Операция: ").append(operation).append("\n");
        sb.append("Время: ").append(duration).append(" мс");

        info(sb.toString());
    }
}