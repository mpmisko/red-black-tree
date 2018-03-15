package redblacktree;

import java.util.NoSuchElementException;

public class RedBlackTree<K extends Comparable<? super K>, V> {

  Node<K, V> root;

  public RedBlackTree() {
    this.root = null;
  }

  RedBlackTree(Node<K, V> root) {
    this.root = root;
  }

  public void put(K key, V value) {

    Tuple<Node<K, V>, Node<K, V>> pair = findNode(key);

    Node<K, V> parent = pair.getX();
    Node<K, V> current = pair.getY();

    if (current != null) { // found an existing key
      current.setValue(value);
      return;
    }

    if (parent == null) { // empty tree
      root = new Node<K, V>(key, value, Colour.BLACK);
      return;
    }
    /* create a new key */
    int comparison = key.compareTo(parent.getKey());
    Node<K, V> newNode = new Node<K, V>(key, value, Colour.RED);
    if (comparison < 0) {
      parent.setLeft(newNode);
    } else {
      assert comparison > 0;
      parent.setRight(newNode);
    }

    insertCaseOne(newNode);

    if(!root.isRootNode()) {
      root = findRoot();
    }
  }

  private void insertCaseOne(Node<K, V> current) {
    if(current.isRootNode()) {
      current.setBlack();
    } else {
      insertCaseTwo(current);
    }
  }

  private void insertCaseTwo(Node<K, V> current) {
    System.out.println("two");
    if(!current.getParent().isBlack()) {
      insertCaseThree(current);
    }
  }

  private void insertCaseThree(Node<K, V> current) {
    System.out.println("three");
    if(current.uncleExists() && current.getUncle().isRed()) {
      current.getUncle().setBlack();
      current.getParent().setBlack();
      current.getGrandparent().setRed();
      insertCaseOne(current.getGrandparent());
    } else {
      insertCaseFour(current);
    }
  }

  private void insertCaseFour(Node<K, V> current) {
    System.out.println("four");
    Node<K, V> parent = current.getParent();

    if(current.isRightChild() && parent.isLeftChild()) {
      parent.rotateLeft();
      insertCaseFive(parent);
    } else if (current.isLeftChild() && parent.isRightChild()) {
      parent.rotateRight();
      insertCaseFive(parent);
    } else {
      insertCaseFive(current);
    }
  }

  private void insertCaseFive(Node<K, V> current) {
    System.out.println("five");
    Node<K,V> parent = current.getParent();
    Node<K,V> grandParent = current.getGrandparent();

    parent.setBlack();
    current.getGrandparent().setRed();
    if(parent.isRightChild()) {
      grandParent.rotateLeft();
    } else {
      grandParent.rotateRight();
    }
  }

  private Tuple<Node<K, V>, Node<K, V>> findNode(K key) {
    Node<K, V> current = root;
    Node<K, V> parent = null;

    while (current != null) {
      parent = current;

      int comparison = key.compareTo(current.getKey());
      if (comparison < 0) {
        current = current.getLeft();
      } else if (comparison == 0) {
        break;
      } else {
        assert comparison > 0;
        current = current.getRight();
      }
    }
    return new Tuple<Node<K, V>, Node<K, V>>(parent, current);
  }

  public boolean contains(K key) {
    Tuple<Node<K, V>, Node<K, V>> pair = findNode(key);
    return pair.getY() != null;
  }

  public V get(K key) {
    Tuple<Node<K, V>, Node<K, V>> pair = findNode(key);
    Node<K, V> current = pair.getY();
    if (current == null) {
      throw new NoSuchElementException();
    }
    return current.getValue();
  }

  private Node<K,V> findRoot() {
    Node<K,V> res = root;
    while(res.getParent() != null) {
      res = res.getParent();
    }
    return res;
  }

  public void clear() {
    this.root = null;
  }

  public String toString() {
    return "RBT " + root + " ";
  }

}
