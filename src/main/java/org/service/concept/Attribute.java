package org.service.concept;

public class Attribute extends Extendable<Attribute, String> {

    public interface Name {
        public static final String ID = "id";
    }

    public static abstract class Access<X> extends Extendable.Access<Attribute, X> {
        private static int counter = 0;

        public Access() {
            super(counter++);
        }
    }

    public final Type      owner;

    public final Primitive primitive;

    public final Type      target;

    public final String    reverse;

    public Attribute(Type owner, String id, Primitive primitive, Type target, String reverse) {
        super(id);
        this.owner     = owner;
        this.primitive = primitive;
        this.target    = target;
        this.reverse   = reverse;
    }
}
