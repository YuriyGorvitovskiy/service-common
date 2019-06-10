package org.service.common.sql.dml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.service.common.sql.DataType;
import org.service.common.sql.query.Literal;
import org.service.common.sql.schema.Column;
import org.service.common.sql.schema.Table;

public class Insert_UnitTest {

    @Test
    public void basicInsert() {
        Table  table  = new Table("task",
                                  new Column("id", DataType.INTEGER),
                                  new Column("name", DataType.STRING),
                                  new Column("assigned_to", DataType.REFERENCE_EXTERNAL),
                                  new Column("description", DataType.TEXT));

        Insert result = new Insert(table)
            .value(table.getColumn("id"), new Literal(DataType.INTEGER, 1234L))
            .value(table.getColumn("name"), new Literal(DataType.STRING, "Test INSERT"))
            .value(table.getColumn("assigned_to"), new Literal(DataType.REFERENCE_EXTERNAL, "user:2345"))
            .value(table.getColumn("description"), new Literal(DataType.TEXT, "Hurry up"));

        assertEquals("INSERT INTO task (id, name, assigned_to, description)\n" +
                     "     VALUES (1234, 'Test INSERT', 'user:2345', 'Hurry up')",
                     result.toPseudoSql(""));
    }

    @Test
    public void simpleInsert() {
        Table  table  = new Table("task",
                                  new Column("id", DataType.INTEGER),
                                  new Column("name", DataType.STRING),
                                  new Column("assigned_to", DataType.REFERENCE_EXTERNAL),
                                  new Column("description", DataType.TEXT));

        Insert result = new Insert(table)
            .value("id", 1234L)
            .value("name", "Test INSERT")
            .value("assigned_to", "user:2345")
            .value("description", "Hurry up");

        assertEquals("INSERT INTO task (id, name, assigned_to, description)\n" +
                     "     VALUES (1234, 'Test INSERT', 'user:2345', 'Hurry up')",
                     result.toPseudoSql(""));
    }
}
