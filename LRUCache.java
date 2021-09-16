import java.util.*;


public class LRUCache {
    HashMap<Integer,Node> data;
    Node head;
    Node tail;
    Integer capacity;
	
	static class Node {
		public Object value;
		public Object key;
		public Node head;
		public Node tail;
		public Node(Object key, Object value) {
			this.key = key;
			this.value = value;
		} 
		
		public Node() {
			
		}
		
		@Override
		public String toString() {
			return ((key == null) ? "null" : String.valueOf(key)) +
					":" +
					((value == null) ? "null" : String.valueOf(value));
		}
	}

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.data = new HashMap<>(this.capacity);
        this.head = new Node();
        this.tail = new Node();
		this.head.tail = this.tail;
		this.tail.head = this.head;
    }
	
	private void moveToHead(Node node) {
		node.tail = this.head.tail;  
		node.head = this.head;
		
		node.tail.head = node;
		this.head.tail = node;
	}
	
	private void unlink(Node node) {
		node.head.tail = node.tail;
		node.tail.head = node.head;	
	}
	
	private void dropTail() {
		this.data.remove(tail.head.key);
		this.unlink(tail.head);	
	}
    
    public int get(int key) {
        if (data.containsKey(key)) {
            Node node = data.get(key);
			unlink(node);
            moveToHead(node);
            return (Integer) node.value;
        } else {
            return -1; //cache miss
        }
    }
	
	public void put(int key, int val) {
		if (this.data.containsKey(key)) {	
			Node node = this.data.get(key);
			moveToHead(node);
			return;
		} 
		
		if ( this.data.size() >= this.capacity) {
			this.dropTail();
		} 
		Node node = new Node(key,val);
		this.data.put(key, node);	
		moveToHead(node);
	}
	
	public void printCache(){

		List<String> l = new ArrayList<>();
		l.add("{");
		Node t = this.head.tail;
		while( t != null && t.key != null && t.value != null) {
			StringBuilder sb = new StringBuilder();
			//System.out.println("\tcur ->" + t);
			sb.append(t.key).append(":").append(t.value);
			if (t.tail.key != null) {
				sb.append(",");
			}
			t = t.tail;
			l.add(sb.toString());
		}
		l.add("}");
		StringBuilder sb = new StringBuilder();
		for (String s : l) {
			sb.append(s);	
		}
		System.out.println(" >>" + sb.toString());
		
	}
	
	public static void main(String[] args) {
		LRUCache lRUCache = new LRUCache(2);

		lRUCache.put(1, 1); // cache is {1=1}
		lRUCache.printCache();
		
		lRUCache.put(2, 2); // cache is {1=1, 2=2}
		lRUCache.printCache();
		System.out.println("key: 1 ->Expecting 1 and got " + lRUCache.get(1));    // return 1
		lRUCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
		lRUCache.printCache();
		System.out.println("key: 2 ->Expecting -1 and got " + lRUCache.get(2));    // returns -1 (not found)
		
		lRUCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
		lRUCache.printCache();
		System.out.println("key: 1 ->Expecting -1 and got " + lRUCache.get(1));    // return -1 (not found)
		System.out.println("key: 3 ->Expecting 3 and got " + lRUCache.get(3));    // return 3
		System.out.println("key: 4 ->Expecting 4 and got " + lRUCache.get(4));    // return 4
		
	}
}
