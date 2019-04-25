package org.service.concept;

import java.util.HashMap;
import java.util.Map;

public class Entity {

    public static abstract class Access<E> extends Extendable.Access<Attribute, E> {
        private static int counter = 0;

        public Access() {
            super(counter++);
        }
    }

    public final ID                     id;

    public final Map<Attribute, Object> attributes = new HashMap<>();

    public Entity(ID id) {
        this.id = id;
    }

}
