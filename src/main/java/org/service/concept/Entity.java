package org.service.concept;

import java.util.HashMap;
import java.util.Map;

public class Entity<ID> {

    public static abstract class Access<E> extends Extendable.Access<Attribute, E> {
        private static int counter = 0;

        public Access() {
            super(counter++);
        }
    }

    public final Type                type;

    public final ID                  id;

    public final Map<String, Object> attributes = new HashMap<>();

    public Entity(Type type, ID id) {
        this.type = type;
        this.id   = id;
    }

}
