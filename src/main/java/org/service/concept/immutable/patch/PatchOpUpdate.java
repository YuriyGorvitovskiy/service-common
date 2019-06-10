package org.service.concept.immutable.patch;

import com.google.common.collect.ImmutableMap;

public class PatchOpUpdate implements PatchOp {

    @Override
    public Entity apply(Patch patch, Entity entity) {
        if (null == entity) {
            return null;
        }

        return new Entity(patch.id, ImmutableMap.<String, Object>builder()
            .putAll(entity.attributes)
            .putAll(patch.attributes)
            .build());
    }
}
