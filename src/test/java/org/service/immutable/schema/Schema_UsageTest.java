package org.service.immutable.schema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.service.immutable.data.Row;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

public class Schema_UsageTest {

    @Test
    public void codeSchema() {
        // Execute
        Schema schema = Schema.of(
                "main",
                Table.of("t1",
                        Stream.of(
                                Column.of().name("id").type(DataType.INTEGER),
                                Column.of().name("name").type(DataType.LABEL),
                                Column.of().name("code").type(DataType.LABEL),
                                Column.of().name("modified_at").type(DataType.TIMESTAMP)),
                        Index.of("pk_t1").column("id"),
                        Stream.of(Index.of("ix_t1_name_code").columns("name", "code"))),
                Table.of()
                    .name("t2")
                    .column("id", DataType.INTEGER)
                    .column("name", DataType.LABEL)
                    .column("code", DataType.LABEL)
                    .column("modified_at", DataType.TIMESTAMP)
                    .primary("pk_t2", "id")
                    .index("ix_t2_name_code", "name", "code"))
            .table(Table.of()
                .name("t3")
                .columns(
                        Column.of("id", DataType.INTEGER),
                        Column.of("name", DataType.LABEL),
                        Column.of("code", DataType.LABEL),
                        Column.of("modified_at", DataType.TIMESTAMP))
                .primary("pk_t3", "id")
                .indexes(Index.of("ix_t3_name_code").columns("name", "code")));

        // Verify
        assertNotNull(schema);
        assertEquals(DataType.INTEGER, schema.tables.get("t1").get().columnMap.get("id").get().type);
        assertEquals("code", schema.tables.get("t2").get().indexMap.get("ix_t2_name_code").get().columns.get(1).name);
        assertEquals(schema, schema.tables.get("t3").get().primary.columns.get(0).table.schema);

    }

    @Test
    public void restoreSchema() {
        // Setup
        Row[]                                  schemaRows           = new Row[] {
                Row.of("main", "schemas", HashMap.of("id", 1L, "name", "main"))
        };

        Row[]                                  tableRows            = new Row[] {
                Row.of("main", "tables", HashMap.of("id", 1L, "schema", 1L, "name", "t1")),
                Row.of("main", "tables", HashMap.of("id", 2L, "schema", 1L, "name", "t2")),
                Row.of("main", "tables", HashMap.of("id", 3L, "schema", 1L, "name", "t3")),
        };

        Row[]                                  columnRows           = new Row[] {
                Row.of("main", "columns", HashMap.of("id", 1L, "table", 1L, "name", "id", "type", DataType.INTEGER)),
                Row.of("main", "columns", HashMap.of("id", 2L, "table", 1L, "name", "name", "type", DataType.LABEL)),
                Row.of("main", "columns", HashMap.of("id", 3L, "table", 1L, "name", "code", "type", DataType.LABEL)),
                Row.of("main", "columns", HashMap.of("id", 4L, "table", 1L, "name", "modified_at", "type", DataType.TIMESTAMP)),

                Row.of("main", "columns", HashMap.of("id", 5L, "table", 2L, "name", "id", "type", DataType.INTEGER)),
                Row.of("main", "columns", HashMap.of("id", 6L, "table", 2L, "name", "name", "type", DataType.LABEL)),
                Row.of("main", "columns", HashMap.of("id", 7L, "table", 2L, "name", "code", "type", DataType.LABEL)),
                Row.of("main", "columns", HashMap.of("id", 8L, "table", 2L, "name", "modified_at", "type", DataType.TIMESTAMP)),

                Row.of("main", "columns", HashMap.of("id", 9L, "table", 3L, "name", "id", "type", DataType.INTEGER)),
                Row.of("main", "columns", HashMap.of("id", 10L, "table", 3L, "name", "name", "type", DataType.LABEL)),
                Row.of("main", "columns", HashMap.of("id", 11L, "table", 3L, "name", "code", "type", DataType.LABEL)),
                Row.of("main",
                        "columns",
                        HashMap.of("id", 12L, "table", 3L, "name", "modified_at", "type", DataType.TIMESTAMP)),
        };

        Row[]                                  indexRows            = new Row[] {
                Row.of("main", "indexes", HashMap.of("id", 1L, "table", 1L, "primary", true, "name", "pk_t1")),
                Row.of("main", "indexes", HashMap.of("id", 2L, "table", 1L, "primary", false, "name", "ix_t1_name_code")),
                Row.of("main", "indexes", HashMap.of("id", 3L, "table", 2L, "primary", true, "name", "pk_t2")),
                Row.of("main", "indexes", HashMap.of("id", 4L, "table", 2L, "primary", false, "name", "ix_t2_name_code")),
                Row.of("main", "indexes", HashMap.of("id", 5L, "table", 3L, "primary", true, "name", "pk_t3")),
                Row.of("main", "indexes", HashMap.of("id", 6L, "table", 3L, "primary", false, "name", "ix_t3_name_code")),
        };

        Row[]                                  index2ColumnRows     = new Row[] {
                Row.of("main", "index_column", HashMap.of("index", 1L, "column", 1L, "order", 0)),
                Row.of("main", "index_column", HashMap.of("index", 2L, "column", 2L, "order", 0)),
                Row.of("main", "index_column", HashMap.of("index", 2L, "column", 3L, "order", 1)),

                Row.of("main", "index_column", HashMap.of("index", 3L, "column", 5L, "order", 0)),
                Row.of("main", "index_column", HashMap.of("index", 4L, "column", 6L, "order", 0)),
                Row.of("main", "index_column", HashMap.of("index", 4L, "column", 7L, "order", 1)),

                Row.of("main", "index_column", HashMap.of("index", 5L, "column", 9L, "order", 0)),
                Row.of("main", "index_column", HashMap.of("index", 6L, "column", 10L, "order", 0)),
                Row.of("main", "index_column", HashMap.of("index", 6L, "column", 11L, "order", 1)),
        };

        Map<Long, String>                      id2ColumnName        = Stream.of(columnRows)
            .collect(HashMap.collector(
                    r -> r.asLong("id"),
                    r -> r.asString("name")));

        Map<Long, Stream<Column>>              table2Columns        = Stream.of(columnRows)
            .groupBy(r -> r.asLong("table"))
            .mapValues(s -> s
                .map(r -> Column.of(
                        r.asLong("id"),
                        r.asString("name"),
                        r.as("type"))));

        Map<Long, Stream<String>>              indexId2ColumnNames  = Stream.of(index2ColumnRows)
            .sortBy(r -> r.asInt("order"))
            .groupBy(r -> r.asLong("index"))
            .mapValues(s -> s.map(
                    r -> id2ColumnName.get(r.asLong("column")).get()));

        Map<Long, Map<Boolean, Stream<Index>>> table2Primary2Idexes = Stream.of(indexRows)
            .groupBy(r -> r.asLong("table"))
            .mapValues(s -> s
                .groupBy(r -> r.asBool("primary"))
                .mapValues(p -> p
                    .map(r -> Index.of(
                            r.asLong("id"),
                            r.asString("name"),
                            indexId2ColumnNames.get(r.asLong("id")).get()))));

        Map<Long, Stream<Table>>               schemaId2tables      = Stream.of(tableRows)
            .groupBy(r -> r.asLong("schema"))
            .mapValues(s -> s
                .map(r -> Table.of(
                        r.asLong("id"),
                        r.asString("name"),
                        table2Columns.get(r.asLong("id")).getOrElse(Stream.empty()),
                        table2Primary2Idexes.get(r.asLong("id")).get().get(true).fold(() -> null, i -> i.get(0)),
                        table2Primary2Idexes.get(r.asLong("id")).get().get(false).getOrElse(Stream.empty()))));

        Map<String, Schema>                    name2Schema          = Stream.of(schemaRows)
            .map(r -> Schema.of(
                    r.asLong("id"),
                    r.asString("name"),
                    schemaId2tables.get(r.asLong("id")).getOrElse(Stream.empty())))
            .collect(HashMap.collector(s -> s.name, s -> s));

        // Verify
        Schema schema = name2Schema.get("main").get();

        assertNotNull(schema);
        assertEquals(DataType.INTEGER, schema.tables.get("t1").get().columnMap.get("id").get().type);
        assertEquals("code", schema.tables.get("t2").get().indexMap.get("ix_t2_name_code").get().columns.get(1).name);
        assertEquals(schema, schema.tables.get("t3").get().primary.columns.get(0).table.schema);
    }
}
