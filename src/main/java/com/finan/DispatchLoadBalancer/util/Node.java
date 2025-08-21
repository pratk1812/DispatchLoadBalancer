package com.finan.DispatchLoadBalancer.util;

class Node<T extends Coordinated> {
  T payload;
  Node<T> left;
  Node<T> right;

  public Node(T payload) {
    this.payload = payload;
  }

  public Node(T payload, Node<T> left, Node<T> right) {
    this.payload = payload;
    this.left = left;
    this.right = right;
  }
}
