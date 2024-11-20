
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class SocialNetwork {
	class Edge {

		private int wait;
		private int to;
		private int from;

		public Edge(int from, int to, int wait) {
			this.wait = wait;
			this.to = to;
			this.from = from;
		}

		public int getFrom() {
			return this.from;
		}

		public int getTo() {
			return this.to;
		}

		public int getWait() {
			return this.wait;
		}

		public void setWait(int wait) {
			this.wait = wait;
		}
	}

	class Vertex {

		private String firstName;
		private String lastName;
		private int enrollmentNum;

		public Vertex(int enrollmentNum, String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.enrollmentNum = enrollmentNum;
		}

		public String getFirstName() {
			return this.firstName;
		}

		public String getLastName() {
			return this.lastName;
		}

		public int getEnrollmentNum() {
			return this.enrollmentNum;
		}

	}

	public HashMap<Integer, LinkedList<Edge>> adjList;

	SocialNetwork() {
		adjList = new HashMap<>();
	}

	public void addVertex(int enrollmentNum) {
		adjList.putIfAbsent(enrollmentNum, new LinkedList<>());
	}

	public void addEdge(Edge e) {
		addVertex(e.getFrom());
		addVertex(e.getTo());
		adjList.get(e.getFrom()).add(new Edge(e.getFrom(), e.getTo(), e.getWait()));
	}

	public void print() {
		for (int vertex : adjList.keySet()) {
			System.out.print("Vertex " + vertex + ": ");
			for (Edge edge : adjList.get(vertex)) {
				System.out.print(" -> " + edge.getTo() + " (Wait: " + edge.getWait() + ")");
			}
			System.out.println();
		}
	}

	public void dijkstra(int start, int end) {
	    // Get the list of all vertex numbers and create a mapping to indices
	    List<Integer> vertices = new ArrayList<>(adjList.keySet());
	    int numVertices = vertices.size();

	    // Arrays for distances, previous nodes, and visited status
	    int[] distances = new int[numVertices];
	    int[] previous = new int[numVertices];
	    boolean[] visited = new boolean[numVertices];

	    // Create a direct mapping from vertex number to array index
	    HashMap<Integer, Integer> vertexToIndex = new HashMap<>();
	    for (int i = 0; i < numVertices; i++) {
	        vertexToIndex.put(vertices.get(i), i);
	    }

	    // Check if start and end vertices exist
	    if (!vertexToIndex.containsKey(start) || !vertexToIndex.containsKey(end)) {
	        System.out.println("Invalid start or end vertex.");
	        return;
	    }

	    // Initialize arrays
	    Arrays.fill(distances, Integer.MAX_VALUE);
	    Arrays.fill(previous, -1);
	    Arrays.fill(visited, false);

	    // Get the indices for start and end vertices
	    int startIndex = vertexToIndex.get(start);
	    int endIndex = vertexToIndex.get(end);

	    distances[startIndex] = 0;
	    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
	    pq.add(new int[]{startIndex, 0});

	    while (!pq.isEmpty()) {
	        int[] current = pq.poll();
	        int currentIndex = current[0];
	        int currentDistance = current[1];

	        if (visited[currentIndex]) continue;
	        visited[currentIndex] = true;

	        // Stop if we reach the end vertex
	        if (currentIndex == endIndex) break;

	        // Get the actual vertex number for the current index
	        int currentVertex = vertices.get(currentIndex);

	        // Update distances for neighbors
	        for (Edge edge : adjList.get(currentVertex)) {
	            int neighbor = edge.getTo();
	            if (!vertexToIndex.containsKey(neighbor)) continue;

	            int neighborIndex = vertexToIndex.get(neighbor);
	            int newDist = currentDistance + edge.getWait();

	            if (!visited[neighborIndex] && newDist < distances[neighborIndex]) {
	                distances[neighborIndex] = newDist;
	                previous[neighborIndex] = currentIndex;
	                pq.add(new int[]{neighborIndex, newDist});
	            }
	        }
	    }

	    // Print shortest distance to the end vertex
	    if (distances[endIndex] == Integer.MAX_VALUE) {
	        System.out.println("No path exists from vertex " + start + " to vertex " + end + ".");
	        return;
	    }
	    System.out.println("Shortest distance from vertex " + start + " to vertex " + end + ": " + distances[endIndex]);

	    // Reconstruct and print the path
	    LinkedList<Integer> path = new LinkedList<>();
	    for (int at = endIndex; at != -1; at = previous[at]) {
	        path.addFirst(vertices.get(at));
	    }
	    System.out.println("Path: " + path);
	}



}