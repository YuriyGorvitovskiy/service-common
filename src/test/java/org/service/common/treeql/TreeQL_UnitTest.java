package org.service.common.treeql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.service.common.treeql.Condition.Operation;
import org.service.common.treeql.Sorting.Order;
import org.service.common.util.ResourceString;

public class TreeQL_UnitTest {

    @Test
    public void testSingleTable() {
        // Execute
        QueryTree subject = new QueryTree("task")
            .field("id")
            .field("name")
            .field("assigned_to")
            .condition("id", Operation.EQUAL, 12L);

        // Validate
        assertEquals(new ResourceString(getClass(), "TreeQL_UnitTest.testSingleTable.tql").toString(),
                     subject.toTreeQL(""));
    }

    @Test
    public void testMultiTable() {
        // Execute
        QueryTree subject = new QueryTree("story")
            .field("id")
            .field("name")
            .field("managed_by")
            .link(new QueryLink("^story_id", "task", true)
                .field("id")
                .field("name")
                .field("assigned_to")
                .sort("assigned_to", Order.ASCENDING))
            .condition("id", Operation.IN, 12L, 13L, 14L)
            .sort("managed_by", Order.ASCENDING);

        // Validate
        assertEquals(new ResourceString(getClass(), "TreeQL_UnitTest.testMultiTable.tql").toString(),
                     subject.toTreeQL(""));
    }

}
