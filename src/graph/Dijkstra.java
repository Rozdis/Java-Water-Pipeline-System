package graph;

import java.util.*;

public class Dijkstra {
    public static Node getLowestDistanceNode(Set<Node> unsettledNodes){
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for(Node node : unsettledNodes){
            if(node.getDistance() < lowestDistance){
                lowestDistance = node.getDistance();
                lowestDistanceNode = node;
            }
        }

        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode){
        Integer sourceDistance = sourceNode.getDistance();
        if(sourceDistance + edgeWeigh < evaluationNode.getDistance()){
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            List<Node> shortestPath = new LinkedList<Node>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    public static Graph calculateShortestPathFromSource(Graph graph, Node source){
        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<Node>();
        Set<Node> unsettledNodes = new HashSet<Node>();
        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0){
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);

            for (Map.Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()){
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeigh = adjacencyPair.getValue();
                if(!settledNodes.contains(adjacentNode)){
                    calculateMinimumDistance(adjacentNode, edgeWeigh, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }
}
