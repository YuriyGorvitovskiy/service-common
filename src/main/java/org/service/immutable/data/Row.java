package org.service.immutable.data;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

public class Row {
    public final String         schema;
    public final String         table;
    public final Map<String, ?> values;

    Row(String schema,
        String table,
        Map<String, ?> values) {

        this.schema = schema;
        this.table = table;
        this.values = values;
    }

    public boolean asBool(String column) {
        return ((Boolean) values.get(column).get()).booleanValue();
    }

    public long asLong(String column) {
        return ((Number) values.get(column).get()).longValue();
    }

    public int asInt(String column) {
        return ((Number) values.get(column).get()).intValue();
    }

    public double asDouble(String column) {
        return ((Number) values.get(column).get()).doubleValue();
    }

    public String asString(String column) {
        return Objects.toString(values.get(column).get());
    }

    public Instant asInstant(String column) {
        Object v = values.get(column).get();
        if (null == v) {
            return null;
        }
        if (v instanceof Instant) {
            return (Instant) v;
        }

        if (v instanceof Date) {
            return Instant.ofEpochMilli(((Date) v).getTime());
        }

        if (v instanceof Number) {
            return Instant.ofEpochMilli(((Number) v).longValue());
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T as(String column) {
        return (T) values.get(column).get();
    }

    @SafeVarargs
    public static Row of(String schema,
                         String table,
                         Tuple2<String, ?>... values) {
        return new Row(schema, table, HashMap.ofEntries(values));
    }

    public static Row of(String schema,
                         String table,
                         Map<String, ?> values) {
        return new Row(schema, table, values);
    }

}
