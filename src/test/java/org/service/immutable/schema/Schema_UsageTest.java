package org.service.immutable.schema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.vavr.Tuple2;
import io.vavr.Tuple3;
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
                                Column.of("id").type(DataType.INTEGER),
                                Column.of("name").type(DataType.LABEL),
                                Column.of("code").type(DataType.LABEL),
                                Column.of("modified_at").type(DataType.TIMESTAMP)),
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
                        Column.of("id").type(DataType.INTEGER),
                        Column.of("name").type(DataType.LABEL),
                        Column.of("code").type(DataType.LABEL),
                        Column.of("modified_at").type(DataType.TIMESTAMP))
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
        Object[][]                                schemaInputs         = {
                { 1, "main" },
        };

        Object[][]                                tableInputs          = {
                { 1, 1, "t1" },
                { 2, 1, "t2" },
                { 3, 1, "t3" },
        };

        Object[][]                                columnInputs         = {
                { 1, 1, "id", DataType.INTEGER },
                { 2, 1, "name", DataType.INTEGER },
                { 3, 1, "code", DataType.INTEGER },
                { 4, 1, "modified_at", DataType.INTEGER },
                { 5, 2, "id", DataType.INTEGER },
                { 6, 2, "name", DataType.INTEGER },
                { 7, 2, "code", DataType.INTEGER },
                { 8, 2, "modified_at", DataType.INTEGER },
                { 9, 3, "id", DataType.INTEGER },
                { 10, 3, "name", DataType.INTEGER },
                { 11, 3, "code", DataType.INTEGER },
                { 12, 3, "modified_at", DataType.INTEGER },
        };

        Object[][]                                indexInputs          = {
                { 1, 1, "pk_t1", true },
                { 2, 1, "ix_t1_name_code", false },
                { 3, 2, "pk_t2", true },
                { 4, 2, "ix_t2_name_code", false },
                { 5, 3, "pk_t3", true },
                { 6, 3, "ix_t3_name_code", false },
        };

        Object[][]                                index2columnInputs   = {
                { 1, "id" },
                { 2, "name" },
                { 2, "code" },
                { 3, "id" },
                { 4, "name" },
                { 4, "code" },
                { 5, "id" },
                { 6, "name" },
                { 6, "code" },
        };

        Map<Integer, Stream<Object[]>>            indexId2Columns      = Stream.of(index2columnInputs)
            .groupBy(i -> (Integer) i[0]);

        Map<Integer, Map<Boolean, Stream<Index>>> table2Primary2Idexes = Stream.of(indexInputs)
            .map(i -> new Tuple3<Integer, Boolean, Index>(
                    (Integer) i[1],
                    (Boolean) i[3],
                    Index.of((String) i[2],
                            indexId2Columns.get((Integer) i[0]).get().map(c -> (String) c[1]))))
            .groupBy(t -> t._1)
            .mapValues(s -> s.groupBy(g -> g._2).mapValues(v -> v.map(z -> z._3)));

        Map<Integer, Stream<Column>>              table2Columns        = Stream.of(columnInputs)
            .map(i -> new Tuple2<Integer, Column>(
                    (Integer) i[1],
                    Column.of((String) i[2], (DataType) i[3])))
            .groupBy(t -> t._1)
            .mapValues(s -> s.map(z -> z._2));

        Map<Integer, Stream<Table>>               schemaId2tables      = Stream.of(tableInputs)
            .map(i -> new Tuple2<Integer, Table>(
                    (Integer) i[1],
                    Table.of((String) i[2],
                            table2Columns.get((Integer) i[0]).get(),
                            table2Primary2Idexes.get((Integer) i[0]).get().get(true).get().get(0),
                            table2Primary2Idexes.get((Integer) i[0]).get().get(false).get())))
            .groupBy(t -> t._1)
            .mapValues(s -> s.map(z -> z._2));

        Map<String, Schema>                       name2Schema          = Stream.of(schemaInputs)
            .map(i -> Schema.of((String) i[1], schemaId2tables.get((Integer) i[0]).get()))
            .collect(HashMap.collector(s -> s.name, s -> s));

        // Verify
        Schema schema = name2Schema.get("main").get();

        assertNotNull(schema);
        assertEquals(DataType.INTEGER, schema.tables.get("t1").get().columnMap.get("id").get().type);
        assertEquals("code", schema.tables.get("t2").get().indexMap.get("ix_t2_name_code").get().columns.get(1).name);
        assertEquals(schema, schema.tables.get("t3").get().primary.columns.get(0).table.schema);
    }
}
