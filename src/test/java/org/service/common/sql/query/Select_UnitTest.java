package org.service.common.sql.query;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.service.common.sql.DataType;
import org.service.common.sql.schema.Column;
import org.service.common.sql.schema.Table;
import org.service.common.util.ResourceString;

public class Select_UnitTest {

    @Test
    public void simpleSelect() {
        // Setup
        Table task_table = new Table("task",
                                     new Column("id", DataType.INTEGER),
                                     new Column("name", DataType.STRING),
                                     new Column("assigned_to", DataType.REFERENCE_EXTERNAL),
                                     new Column("description", DataType.TEXT));

        TableAlias a = new TableAlias("a", task_table);

        // Execute
        Select subject = new Select()
            .column(a.getColumn("id"))
            .column(a.getColumn("name"))
            .column(a.getColumn("assigned_to"))
            .from(a)
            .where(ICondition.equal(a.getColumn("id"),
                                    new Literal(DataType.INTEGER, 12L)));

        // Validate
        assertEquals(new ResourceString(getClass(), "Query_UnitTest.simpleSelect.sql").toString(),
                     subject.toPseudoSql(""));
    }

    @Test
    public void simpleJoin() {
        // Setup
        Table story_table = new Table("story",
                                      new Column("id", DataType.INTEGER),
                                      new Column("name", DataType.STRING),
                                      new Column("managed_by", DataType.REFERENCE_EXTERNAL),
                                      new Column("description", DataType.TEXT));

        Table task_table = new Table("task",
                                     new Column("id", DataType.INTEGER),
                                     new Column("name", DataType.STRING),
                                     new Column("story_id", DataType.INTEGER),
                                     new Column("assigned_to", DataType.REFERENCE_EXTERNAL),
                                     new Column("description", DataType.TEXT));

        TableAlias s = new TableAlias("s", story_table);
        TableAlias t = new TableAlias("t", task_table);

        // Execute
        Select subject = new Select()
            .column(s.getColumn("id"), "story_id")
            .column(s.getColumn("name"), "story_name")
            .column(s.getColumn("managed_by"))
            .column(t.getColumn("id"), "task_id")
            .column(t.getColumn("name"), "task_name")
            .column(t.getColumn("assigned_to"))
            .from(s)
            .inner(t, ICondition.equal(s.getColumn("id"),
                                       t.getColumn("story_id")))
            .where(ICondition.in(s.getColumn("id"),
                                 new Literal(DataType.INTEGER, 13L),
                                 new Literal(DataType.INTEGER, 14L),
                                 new Literal(DataType.INTEGER, 15L)))
            .orderBy(s.getColumn("managed_by"), Sorting.Order.ASCENDING)
            .orderBy(t.getColumn("assigned_to"), Sorting.Order.DESCENDING);

        // Validate
        assertEquals(new ResourceString(getClass(), "Query_UnitTest.simpleJoin.sql").toString(),
                     subject.toPseudoSql(""));
    }
}
