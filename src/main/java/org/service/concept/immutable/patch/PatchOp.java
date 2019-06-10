package org.service.concept.immutable.patch;

public interface PatchOp {

    public Entity apply(Patch patch, Entity entity);
}
