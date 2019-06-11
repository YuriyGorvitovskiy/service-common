package org.service.concept.db.postgres;

import static org.junit.Assert.assertEquals;
import static org.service.concept.db.postgres.Execute.NEW_LINE;

import org.junit.Test;
import org.service.concept.db.event.ConditionAnd;
import org.service.concept.db.event.ConditionEqual;
import org.service.concept.db.event.ConditionIn;
import org.service.concept.db.event.ConditionLess;
import org.service.concept.db.event.ConditionMore;
import org.service.concept.db.event.ConditionNot;
import org.service.concept.db.event.ConditionNull;
import org.service.concept.db.event.ConditionOr;
import org.service.concept.db.event.Page;
import org.service.concept.db.event.RequestSelect;
import org.service.concept.db.event.Sorting;

import com.google.common.collect.ImmutableList;

public class ExecuteSelect_UnitTest {

    @Test
    public void test_buildSql_simple() {
        // Setup
        ExecuteSelect subject = new ExecuteSelect();

        RequestSelect request = new RequestSelect(
                "table",
                ImmutableList.of("col1", "col2", "col3"),
                new ConditionEqual("col1", 22),
                ImmutableList.of(new Sorting("col2", true)),
                new Page(1, 10));

        // Execute
        String result = subject.buildSql(request);

        // Verify
        assertEquals("SELECT col1," + NEW_LINE +
                     "       col2," + NEW_LINE +
                     "       col3" + NEW_LINE +
                     "  FROM table" + NEW_LINE +
                     " WHERE col1 = ?" + NEW_LINE +
                     " ORDER BY col2 ASC" + NEW_LINE +
                     " LIMIT 10" + NEW_LINE +
                     "OFFSET 1",
                result);

    }

    @Test
    public void test_buildSql_complex() {
        // Setup
        ExecuteSelect subject = new ExecuteSelect();

        RequestSelect request = new RequestSelect(
                "table",
                ImmutableList.of("col1", "col2", "col3"),
                new ConditionNot(
                        new ConditionAnd(ImmutableList.of(
                                new ConditionEqual("col1", 22),
                                new ConditionOr(ImmutableList.of(
                                        new ConditionIn("col2", ImmutableList.of(1, 2, 3)),
                                        new ConditionNot(new ConditionNull("col3")))),
                                new ConditionLess("col4", 22),
                                new ConditionNot(new ConditionMore("col5", 12.4))))),
                ImmutableList.of(
                        new Sorting("col1", true),
                        new Sorting("col4", false),
                        new Sorting("col5", true)),
                new Page(10, 100));

        // Execute
        String result = subject.buildSql(request);

        // Verify
        assertEquals("SELECT col1," + NEW_LINE +
                     "       col2," + NEW_LINE +
                     "       col3" + NEW_LINE +
                     "  FROM table" + NEW_LINE +
                     " WHERE " + NEW_LINE +
                     "   NOT   ( col1 = ?" + NEW_LINE +
                     "       AND   ( col2 IN (?, ?, ?)" + NEW_LINE +
                     "            OR col3 IS NOT NULL)" + NEW_LINE +
                     "       AND col4 < ?" + NEW_LINE +
                     "       AND col5 <= ?)" + NEW_LINE +
                     " ORDER BY col1 ASC," + NEW_LINE +
                     "          col4 DESC," + NEW_LINE +
                     "          col5 ASC" + NEW_LINE +
                     " LIMIT 100" + NEW_LINE +
                     "OFFSET 10",
                result);

    }
}
