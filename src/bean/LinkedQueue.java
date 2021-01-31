package bean;

//import java.util.LinkedList;//双向链队列

public class LinkedQueue<E> {
	private Node front;//头结点
	private Node rear;//尾结点
	private int size = 0;

	//定义一个内部类代表队列的结点
	private class Node {
		private E data;
		private Node nextNode;//下一个结点的引用
		
		public Node() {
			// TODO Auto-generated constructor stub
		}
		public Node(E data, Node nextNode) {
			this();
			this.data = data;
			this.nextNode = nextNode;
		}
	}
	//初始化一个空队列
	public LinkedQueue() {
		front = null;
		rear = null;
	}
	//初始化一个队列,只有一个结点
	public LinkedQueue(E e) {
		front = new Node(e, null);
		rear = front;
		size++;
	}
	//获取队列结点数
	public int size() {
		return size;
	}
	//判断队列是否为空
	public boolean isEmpty() {
		return size == 0;
	}
	
	//入队
	public void enqueue(E e) {
		if (this.isEmpty()) {//如果当前队列为空
			front = new Node(e, null);
			rear = front;
		}
		else {
			Node node = new Node(e, null);//创建新结点
			rear.nextNode = node;//原有尾结点的下一个结点为该结点
			rear = node;//当前尾结点更换为该结点
		}
		size++;
	}
	//出队
	public E dequeue() {
		Node oldNode = front;//原有头结点出队
//		System.out.println("原有头结点:"+oldNode.data.toString());
		front = front.nextNode;//新头结点为原有下一个
		if (oldNode.nextNode != null) {
			oldNode.nextNode = null;
		}
		size--;
		return oldNode.data;
	}
	//获取头结点的数据
	public E getFront() {
		return front.data;
	}
	//清空队列
	public void clear() {
		front = null;
		rear = null;
		size = 0;
	}
	
	public String toString() {
		if (this.isEmpty()) {
			return "[]";
		}
		else {
			StringBuilder tempBuilder = new StringBuilder("[");
			for(Node currentNode = front; currentNode != null; currentNode = currentNode.nextNode) {
//				System.out.println("-------------"+currentNode.data.toString()+"--------------");
				tempBuilder.append(currentNode.data.toString() + ", ");
			}
			int length = tempBuilder.length();
//			System.out.println("temp长度"+length);
			return tempBuilder.delete(length - 2, length).append("]").toString();
		}
	}
}
