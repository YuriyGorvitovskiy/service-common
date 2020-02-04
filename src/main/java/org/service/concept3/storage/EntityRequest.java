package org.service.concept3.storage;

public abstract class EntityRequest {

    public final String type;

    protected EntityRequest(String type) {
        this.type = type;
    }
}
