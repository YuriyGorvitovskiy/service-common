package org.service.concept.immutable.patch;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;

public class Processor {
    public final ImmutableMap<String, PatchOp> ops = ImmutableMap.<String, PatchOp>builder()
        .put(Patch.Op.INSERT, new PatchOpInsert())
        .put(Patch.Op.UPSERT, new PatchOpUpsert())
        .put(Patch.Op.UPDATE, new PatchOpUpdate())
        .put(Patch.Op.DELETE, new PatchOpDelete())
        .build();

    final State                                state;

    public Processor(State state) {
        this.state = state;
    }

    @Subscribe
    public void apply(Patch patch) {
        Entity  entity = state.get(patch.id);
        PatchOp op     = ops.get(patch.op);
        if (null != op) {
            state.put(patch.id, op.apply(patch, entity));
        }
    }
}
