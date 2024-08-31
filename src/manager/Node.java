package manager;

class Node<T> {
    T data;
    Node<T> next;
    Node<T> prev;

    Node(Node<T> prev, T element, Node<T> next) {
        this.data = element;
        this.next = next;
        this.prev = prev;
    }
}