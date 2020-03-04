package org.service.immutable.data;

public class Patch {

    public enum Operation {
        INSERT,
        UPDATE,
        DELETE
    }

    public final Operation op;

    public final Row row;

    public Patch(Operation op, Row row) {
        this.op = op;
        this.row = row;
    }
}
