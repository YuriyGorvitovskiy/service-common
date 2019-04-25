package org.service.concept;

import java.util.Arrays;
import java.util.List;

public class Batch {

    public final Type         type;

    public final List<String> ids;

    public Batch(Type type, List<String> ids) {
        this.type = type;
        this.ids  = ids;
    }

    public Batch(Type type, String... ids) {
        this.type = type;
        this.ids  = Arrays.asList(ids);
    }
}
