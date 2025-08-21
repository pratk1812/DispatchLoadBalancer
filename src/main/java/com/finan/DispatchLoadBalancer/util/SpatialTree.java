package com.finan.DispatchLoadBalancer.util;

import static com.finan.DispatchLoadBalancer.util.DispatcherUtil.buildBalanced;
import static com.finan.DispatchLoadBalancer.util.DispatcherUtil.findNearestRecursive;
import static com.finan.DispatchLoadBalancer.util.DispatcherUtil.removeRecursive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A spatial index that organizes {@link Coordinated} objects in a k-dimensional tree structure for
 * efficient nearest-neighbor searches and removals.
 *
 * @param <T> the type of objects stored in the tree; must implement {@link Coordinated}
 */
public class SpatialTree<T extends Coordinated> {
  Node<T> root;

  private SpatialTree() {
    // Private constructor to enforce use of factory methods.
  }

  /**
   * Builds a balanced spatial tree (k-d tree) from the provided payloads.
   *
   * <p>This method partitions the input list on alternating dimensions to produce a balanced tree
   * that supports fast nearest-neighbor queries.
   *
   * @param <T> the element type; must implement {@link Coordinated}
   * @param payloads the collection of items to index by their coordinates
   * @return a new {@link SpatialTree} containing all supplied payloads
   * @throws NullPointerException if {@code payloads} is null
   */
  public static <T extends Coordinated> SpatialTree<T> createSimpleSTree(List<T> payloads) {
    SpatialTree<T> spatialTree = new SpatialTree<>();
    spatialTree.root = buildBalanced(new ArrayList<>(payloads), 0);
    return spatialTree;
  }

  /**
   * Finds all indexed payloads sorted by their distance to the given target.
   *
   * <p>Executes a nearest-neighbor traversal of the tree, setting each node’s {@link
   * Coordinated#setSearchDistance(double)} to the distance from {@code target}. Results are
   * returned in ascending order of that distance.
   *
   * @param target the query point whose coordinates are obtained via {@link Coordinated#getX()} and
   *     {@link Coordinated#getY()}
   * @return a list of stored {@code T} instances ordered from closest to farthest; returns an empty
   *     list if the tree has no nodes
   */
  public List<T> findNearest(Coordinated target) {
    if (root == null) return Collections.emptyList();

    PriorityQueue<Node<T>> heap =
        new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.payload.getSearchDistance()));
    findNearestRecursive(root, target, 0, heap);
    return heap.stream().map(node -> node.payload).toList();
  }

  /**
   * Removes the node matching the specified identifier and location from the tree.
   *
   * <p>Walks the k-d tree according to the target coordinate, removes the matching node if found,
   * and reattaches or rebuilds subtrees to preserve the tree’s structure.
   *
   * @param nodeId the unique identifier of the node to remove, as returned by {@link
   *     Coordinated#getId()}
   * @param location the coordinate object used for traversal (its x/y from {@link
   *     Coordinated#getX()}/{@link Coordinated#getY()})
   * @return {@code true} if a node was found and removed; {@code false} otherwise
   */
  public boolean remove(String nodeId, Coordinated location) {
    AtomicBoolean removed = new AtomicBoolean(false);
    root = removeRecursive(root, nodeId, location, 0, removed);
    return removed.get();
  }
}
