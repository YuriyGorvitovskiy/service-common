package org.service.concept;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Extendable<E extends Extendable<E, I>, I> {

    public static abstract class Access<E extends Extendable<E, ?>, X> {

        private int index;

        public Access(int index) {
            this.index = index;
        }

        @SuppressWarnings("unchecked")
        public X get(E extendable) {
            if (extendable.associations.size() > index) {
                return (X) extendable.associations.get(index);
            }
            return null;
        }

        public X getOrCreate(E concept) {
            X extension = get(concept);
            if (null == extension) {
                extension = create(concept);
                associate(concept, extension);
            }
            return extension;
        }

        public Access<E, X> associate(E extendable, X extension) {
            if (extendable.associations.size() <= index) {
                extendable.associations.add(index, extension);
            } else {
                extendable.associations.set(index, extension);
            }
            return this;
        }

        protected abstract X create(E instance);
    }

    final List<Object> associations = new ArrayList<>();

    public final I     id;

    protected Extendable(I id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (null == o || !(this.getClass() == o.getClass())) {
            return false;
        }
        return Objects.equals(id, ((Extendable<?, ?>) o).id);
    }
}
