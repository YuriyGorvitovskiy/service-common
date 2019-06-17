package org.service.command.dml;

public class CountResult implements DMLResult {

    public final long count;

    public CountResult(long count) {
        this.count = count;
    }

}
