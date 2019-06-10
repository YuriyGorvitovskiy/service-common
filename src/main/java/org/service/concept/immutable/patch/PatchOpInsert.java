package org.service.concept.immutable.patch;

import com.google.common.collect.ImmutableMap;

public class PatchOpInsert implements PatchOp {

    @Override
    public Entity apply(Patch patch, Entity entity) {
        if (null != entity) {
            return entity;
        }
        return new Entity(patch.id, ImmutableMap.<String, Object>builder()
            .putAll(patch.attributes)
            .build());
    }
}
