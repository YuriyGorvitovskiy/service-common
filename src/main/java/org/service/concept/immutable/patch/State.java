package org.service.concept.immutable.patch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class State {

    final Map<String, Entity> entities = new ConcurrentHashMap<>();

    public State() {
    }

    public Entity get(String id) {
        return null == id ? null : entities.get(id);
    }

    public void put(String id, Entity entity) {
        if (null != id) {
            entities.put(id, entity);
        }
    }
}
