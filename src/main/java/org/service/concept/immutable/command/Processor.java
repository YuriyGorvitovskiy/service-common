package org.service.concept.immutable.command;

import org.service.concept.immutable.Event;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class Processor {
    public final ImmutableMap<String, TableOp> ops = ImmutableMap.<String, TableOp>builder()
        .put(TableCommand.Op.INSERT, new TableOpInsert())
        .put(TableCommand.Op.UPDATE, new TableOpUpdate())
        .put(TableCommand.Op.DELETE, new TableOpDelete())
        .build();

    final EventBus                             topic;

    public Processor(EventBus topic) {
        this.topic = topic;
    }

    @Subscribe
    public void apply(TableCommand command) {
        TableOp op = ops.get(command.op);
        if (null != op) {
            Event event = op.apply(command, null);
            if (null != event) {
                topic.post(event);
            }
        }
    }
}
