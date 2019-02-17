package org.service.concept;

import java.util.HashMap;
import java.util.Map;

public class Primitive extends Extendable<Primitive, String> {

    public static class Access<E> extends Extendable.Access<Primitive, E> {
        private static int counter = 0;

        public Access() {
            super(counter++);
        }

        @Override
        protected E create(Primitive instance) {
            throw new UnsupportedOperationException(instance.id);
        }
    }

    private Primitive(String id) {
        super(id);
        ALL.put(id, this);
    }

    public static Map<String, Primitive> ALL       = new HashMap<>();

    public static final Primitive        BOOLEAN   = new Primitive("boolean");
    public static final Primitive        INTEGER   = new Primitive("integer");
    public static final Primitive        DOUBLE    = new Primitive("double");
    public static final Primitive        UUID      = new Primitive("uuid");
    public static final Primitive        STRING    = new Primitive("string");
    public static final Primitive        TEXT      = new Primitive("text");
    public static final Primitive        MOMENT    = new Primitive("moment");
    public static final Primitive        PRIMITIVE = new Primitive("primitive");

    public static Primitive valueOf(String str) {
        Primitive pr = ALL.get(str);
        if (pr != null) {
            return pr;
        }
        throw new IllegalArgumentException(str);
    }

    @Override
    public String toString() {
        return id;
    }
}
