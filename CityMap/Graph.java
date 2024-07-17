import java.util.*;

public class Graph {
    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();

    public void addEdge(String source, String destination, int weight) {
        this.adjacencyList.putIfAbsent(source, new ArrayList<>());
        this.adjacencyList.putIfAbsent(destination, new ArrayList<>());
        this.adjacencyList.get(source).add(new Edge(destination, weight));
    }

    public Map<String, Integer> dijkstra(String start) {
        Map<String, Integer> distances = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));
        priorityQueue.add(new Node(start, 0));
        distances.put(start, 0);

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            for (Edge edge : adjacencyList.getOrDefault(currentNode.name, Collections.emptyList())) {
                int newDist = distances.get(currentNode.name) + edge.weight;
                if (newDist < distances.getOrDefault(edge.destination, Integer.MAX_VALUE)) {
                    distances.put(edge.destination, newDist);
                    priorityQueue.add(new Node(edge.destination, newDist));
                }
            }
        }
        return distances;
    }

    private static class Edge {
        String destination;
        int weight;

        Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    private static class Node {
        String name;
        int distance;

        Node(String name, int distance) {
            this.name = name;
            this.distance = distance;
        }
    }

    public List<String> shortestPath(String start, String end) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shortestPath'");
    }
}
