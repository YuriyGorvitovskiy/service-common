package org.service.concept;

import java.util.HashMap;
import java.util.Map;

public class Type extends Extendable<Type, String> {

    public static interface Name {
        public static final String TYPE      = "type";
        public static final String ATTRIBUTE = "attribute";
        public static final String PRIMITIVE = "primitive";
    }

    public static abstract class Access<E> extends Extendable.Access<Attribute, E> {
        private static int counter = 0;

        public Access() {
            super(counter++);
        }
    }

    public final Primitive              idPrimitive;

    public final Map<String, Attribute> attributes = new HashMap<>();

    public Type(String id, Primitive idPrimitive) {
        super(id);
        this.idPrimitive = idPrimitive;
    }
}
