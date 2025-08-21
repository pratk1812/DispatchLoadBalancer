package com.finan.DispatchLoadBalancer.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

class DispatcherUtil {
  static final int DIMENSION = 2;

  public static double haversine(double lat1, double lon1, double lat2, double lon2) {
    final int R = 6371; // Earth radius in km
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
  }

  static double haversine(Coordinated c1, Coordinated c2) {
    return haversine(c1.getX(), c1.getY(), c2.getX(), c2.getY());
  }

  static <T extends Coordinated> Node<T> buildBalanced(List<T> payloads, int depth) {
    if (payloads.isEmpty()) return null;
    if (payloads.size() == 1) return new Node<>(payloads.get(0));

    int axis = depth % DIMENSION;
    int medianIndex = payloads.size() / 2;
    T median = quickSelect(payloads, medianIndex, axis);

    List<T> leftList = new ArrayList<>();
    List<T> rightList = new ArrayList<>();

    for (T p : payloads) {
      if (median.isSameCoord(p) && !median.getId().equals(p.getId())) {
        median.addSameCoordPayload(p);
        continue;
      }
      double val = extract(p, axis);
      double mVal = extract(median, axis);
      if (val < mVal) leftList.add(p);
      else if (val >= mVal) rightList.add(p);
    }
    Node<T> node = new Node<>(median);
    node.left = buildBalanced(leftList, depth + 1);
    node.right = buildBalanced(rightList, depth + 1);
    return node;
  }

  static <T extends Coordinated> T quickSelect(List<T> list, int k, int axis) {
    return quickSelectHelper(list, 0, list.size() - 1, k, axis);
  }

  static <T extends Coordinated> T quickSelectHelper(
      List<T> list, int left, int right, int k, int axis) {
    if (left == right) return list.get(left);

    int pivotIndex = partition(list, left, right, axis);
    if (k == pivotIndex) {
      return list.get(k);
    } else if (k < pivotIndex) {
      return quickSelectHelper(list, left, pivotIndex - 1, k, axis);
    } else {
      return quickSelectHelper(list, pivotIndex + 1, right, k, axis);
    }
  }

  static <T extends Coordinated> int partition(List<T> list, int left, int right, int axis) {
    double pivotValue = extract(list.get(right), axis);
    int storeIndex = left;

    for (int i = left; i < right; i++) {
      if (extract(list.get(i), axis) < pivotValue) {
        Collections.swap(list, storeIndex, i);
        storeIndex++;
      }
    }
    Collections.swap(list, storeIndex, right);
    return storeIndex;
  }

  static <T extends Coordinated> double extract(T payload, int axis) {
    return axis == 0 ? payload.getX() : payload.getY();
  }

  static <T extends Coordinated> void findNearestRecursive(
      Node<T> node, Coordinated target, int depth, PriorityQueue<Node<T>> heap) {
    if (node == null) return;

    double dist = DispatcherUtil.haversine(node.payload, target);
    node.payload.setSearchDistance(dist);
    heap.offer(new Node<>(node.payload));

    int axis = depth % DIMENSION;
    double targetCoord = extract(target, axis);
    double nodeCoord = extract(node.payload, axis);

    Node<T> nearBranch = targetCoord < nodeCoord ? node.left : node.right;
    Node<T> farBranch = targetCoord < nodeCoord ? node.right : node.left;

    findNearestRecursive(nearBranch, target, depth + 1, heap);

    if (heap.size() < 2
        || Math.pow(targetCoord - nodeCoord, 2) < heap.peek().payload.getSearchDistance()) {
      findNearestRecursive(farBranch, target, depth + 1, heap);
    }
  }

  static double distanceSquared(Coordinated a, Coordinated b) {
    double dx = a.getX() - b.getX();
    double dy = a.getY() - b.getY();
    return dx * dx + dy * dy;
  }

  static <T extends Coordinated> Node<T> removeRecursive(
      Node<T> node, String payloadId, Coordinated location, int depth, AtomicBoolean removed) {
    if (node == null) return null;

    int axis = depth % DIMENSION;

    if (node.payload.isSameCoord(location)) {
      if (node.payload.getId().equals(payloadId)) {
        removed.set(true);

        List<T> dupes = node.payload.getSameCoordPayload();
        if (!dupes.isEmpty()) {
          T promoted = dupes.remove(0);
          for (T d : dupes) {
            promoted.addSameCoordPayload(d);
          }
          Node<T> replacement = new Node<>(promoted);
          replacement.left = node.left;
          replacement.right = node.right;
          return replacement;
        }

        if (node.right != null) {
          Node<T> min = findMin(node.right, axis, depth + 1);
          node.payload = min.payload;
          node.right =
              removeRecursive(node.right, node.payload.getId(), node.payload, depth + 1, removed);
        } else if (node.left != null) {
          Node<T> min = findMin(node.left, axis, depth + 1);
          node.payload = min.payload;
          node.right =
              removeRecursive(node.left, node.payload.getId(), node.payload, depth + 1, removed);
          node.left = null;
        } else {
          return null;
        }
        return node;
      }

      Predicate<T> removePredicate = t -> t.getId().equals(payloadId);
      boolean dupRemoved = node.payload.removeSameCoordPayload(payloadId);
      if (dupRemoved) {
        removed.set(true);
      }
      return node;
    }

    double locVal = extract(location, axis);
    double nodeVal = extract(node.payload, axis);
    if (locVal < nodeVal) {
      node.left = removeRecursive(node.left, payloadId, location, depth + 1, removed);
    } else {
      node.right = removeRecursive(node.right, payloadId, location, depth + 1, removed);
    }
    return node;
  }

  static <T extends Coordinated> Node<T> findMin(Node<T> node, int axisWanted, int depth) {
    if (node == null) return null;
    int cd = depth % DIMENSION;

    if (cd == axisWanted) {
      return (node.left == null) ? node : findMin(node.left, axisWanted, depth + 1);
    }

    Node<T> leftMin = findMin(node.left, axisWanted, depth + 1);
    Node<T> rightMin = findMin(node.right, axisWanted, depth + 1);

    Node<T> min = node;
    if (leftMin != null
        && extract(leftMin.payload, axisWanted) < extract(min.payload, axisWanted)) {
      min = leftMin;
    }
    if (rightMin != null
        && extract(rightMin.payload, axisWanted) < extract(min.payload, axisWanted)) {
      min = rightMin;
    }
    return min;
  }
}
