package org.service.common.treeql;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.service.common.sql.DataType;
import org.service.common.sql.query.Select;
import org.service.common.sql.schema.Column;
import org.service.common.sql.schema.Schema;
import org.service.common.sql.schema.Table;
import org.service.common.treeql.Condition.Operation;
import org.service.common.treeql.Sorting.Order;
import org.service.common.util.ResourceString;

public class TreeQLtoSQL_UnitTest {

    static Schema schema;

    @BeforeClass
    public static void createSchema() {
        schema = new Schema("test",
                            new Table("story",
                                      new Column("id", DataType.INTEGER),
                                      new Column("name", DataType.STRING),
                                      new Column("managed_by", DataType.REFERENCE_EXTERNAL),
                                      new Column("description", DataType.TEXT)),

                            new Table("task",
                                      new Column("id", DataType.INTEGER),
                                      new Column("name", DataType.STRING),
                                      new Column("story_id", DataType.INTEGER),
                                      new Column("assigned_to", DataType.REFERENCE_EXTERNAL),
                                      new Column("description", DataType.TEXT)));

    }

    @Test
    public void testSingleTable() {
        // Setup
        QueryTree   treeQL  = new QueryTree("task")
            .field("id")
            .field("name")
            .field("assigned_to")
            .condition("id", Operation.EQUAL, 12L);

        TreeQLtoSQL subject = new TreeQLtoSQL(schema);

        // Execute
        Select select = subject.toSQL(treeQL);

        // Validate
        assertEquals(new ResourceString(getClass(), "TreeQLtoSQL_UnitTest.testSingleTable.sql").toString(),
                     select.toPseudoSql(""));
    }

    @Test
    public void testMultiTable() {
        // Execute
        QueryTree   treeQL  = new QueryTree("story")
            .field("id")
            .field("name")
            .field("managed_by")
            .link(new QueryLink("^story_id", "task", true)
                .field("id")
                .field("name")
                .field("assigned_to")
                .sort("assigned_to", Order.DESCENDING))
            .condition("id", Operation.IN, 13L, 14L, 15L)
            .sort("managed_by", Order.ASCENDING);

        TreeQLtoSQL subject = new TreeQLtoSQL(schema);

        // Execute
        Select select = subject.toSQL(treeQL);

        // Validate
        assertEquals(new ResourceString(getClass(), "TreeQLtoSQL_UnitTest.testMultiTable.sql").toString(),
                     select.toPseudoSql(""));
    }

}
