package com.finan.DispatchLoadBalancer.util;

import java.util.List;

public interface Coordinated {
  double getX();

  double getY();

  String getId();

  <T> List<T> getSameCoordPayload();

  double getSearchDistance();

  void setSearchDistance(double distance);

  default boolean isSameCoord(Coordinated o) {
    return getX() == o.getX() && getY() == o.getY();
  }

  default void addSameCoordPayload(Coordinated o) {
    getSameCoordPayload().add(o);
  }

  default boolean removeSameCoordPayload(String payloadId) {
    return getSameCoordPayload()
        .removeIf(
            object -> {
              if (object instanceof Coordinated coordinated) {
                return coordinated.getId().equals(payloadId);
              }
              return false;
            });
  }
}
