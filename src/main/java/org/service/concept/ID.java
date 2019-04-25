package org.service.concept;

import java.util.Objects;

public class ID {

    public final Type   type;

    public final String id;

    public ID(Type type, String id) {
        this.type = type;
        this.id   = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || !(obj instanceof ID)) {
            return false;
        }
        ID o = (ID) obj;
        return Objects.equals(type, o.type) &&
               Objects.equals(id, o.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
