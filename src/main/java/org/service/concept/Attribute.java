package org.service.concept;

public class Attribute extends Extendable<Attribute, String> {

    public static abstract class Access<E> extends Extendable.Access<Attribute, E> {
        private static int counter = 0;

        public Access() {
            super(counter++);
        }
    }

    public final Type      owner;

    public final Primitive primitive;

    public final Type      target;

    public Attribute(Type owner, String id, Primitive primitive, Type target) {
        super(id);
        this.owner     = owner;
        this.primitive = primitive;
        this.target    = target;
    }
}
