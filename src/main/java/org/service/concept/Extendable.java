package org.service.concept;

import java.util.ArrayList;
import java.util.List;

public abstract class Extendable<C extends Extendable<C, ID>, ID> {

    public static abstract class Access<C extends Extendable<C, ?>, E> {

        private int index;

        public Access(int index) {
            this.index = index;
        }

        @SuppressWarnings("unchecked")
        public E get(C instance) {
            if (instance.associations.size() > index) {
                return (E) instance.associations.get(index);
            }
            return null;
        }

        public E getOrCreate(C concept) {
            E extension = get(concept);
            if (null == extension) {
                extension = create(concept);
                associate(concept, extension);
            }
            return extension;
        }

        public Access<C, E> associate(C concept, E extension) {
            if (concept.associations.size() <= index) {
                concept.associations.add(index, extension);
            } else {
                concept.associations.set(index, extension);
            }
            return this;
        }

        protected abstract E create(C instance);
    }

    final List<Object> associations = new ArrayList<>();

    public final ID    id;

    protected Extendable(ID id) {
        this.id = id;
    }
}
