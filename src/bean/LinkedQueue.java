package bean;

//import java.util.LinkedList;//˫��������

public class LinkedQueue<E> {
	private Node front;//ͷ���
	private Node rear;//β���
	private int size = 0;

	//����һ���ڲ��������еĽ��
	private class Node {
		private E data;
		private Node nextNode;//��һ����������
		
		public Node() {
			// TODO Auto-generated constructor stub
		}
		public Node(E data, Node nextNode) {
			this();
			this.data = data;
			this.nextNode = nextNode;
		}
	}
	//��ʼ��һ���ն���
	public LinkedQueue() {
		front = null;
		rear = null;
	}
	//��ʼ��һ������,ֻ��һ�����
	public LinkedQueue(E e) {
		front = new Node(e, null);
		rear = front;
		size++;
	}
	//��ȡ���н����
	public int size() {
		return size;
	}
	//�ж϶����Ƿ�Ϊ��
	public boolean isEmpty() {
		return size == 0;
	}
	
	//���
	public void enqueue(E e) {
		if (this.isEmpty()) {//�����ǰ����Ϊ��
			front = new Node(e, null);
			rear = front;
		}
		else {
			Node node = new Node(e, null);//�����½��
			rear.nextNode = node;//ԭ��β������һ�����Ϊ�ý��
			rear = node;//��ǰβ������Ϊ�ý��
		}
		size++;
	}
	//����
	public E dequeue() {
		Node oldNode = front;//ԭ��ͷ������
//		System.out.println("ԭ��ͷ���:"+oldNode.data.toString());
		front = front.nextNode;//��ͷ���Ϊԭ����һ��
		if (oldNode.nextNode != null) {
			oldNode.nextNode = null;
		}
		size--;
		return oldNode.data;
	}
	//��ȡͷ��������
	public E getFront() {
		return front.data;
	}
	//��ն���
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
//			System.out.println("temp����"+length);
			return tempBuilder.delete(length - 2, length).append("]").toString();
		}
	}
}
