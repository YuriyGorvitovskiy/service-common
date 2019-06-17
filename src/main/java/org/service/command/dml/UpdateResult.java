package org.service.command.dml;

public class UpdateResult implements DMLResult {

    public final long updates;

    public UpdateResult(long updates) {
        this.updates = updates;
    }
}
