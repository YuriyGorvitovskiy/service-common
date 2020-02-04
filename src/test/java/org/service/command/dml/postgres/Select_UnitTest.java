package org.service.command.dml.postgres;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.service.command.dml.postgres.DMLCommand.NEW_LINE;

import org.junit.jupiter.api.Test;
import org.service.command.dml.SelectParams;
import org.service.command.dml.postgres.Select;
import org.service.command.dml.predicate.And;
import org.service.command.dml.predicate.Equal;
import org.service.command.dml.predicate.In;
import org.service.command.dml.predicate.Less;
import org.service.command.dml.predicate.More;
import org.service.command.dml.predicate.Not;
import org.service.command.dml.predicate.Null;
import org.service.command.dml.predicate.Or;
import org.service.command.dml.predicate.OrderBy;
import org.service.command.dml.predicate.Page;

import io.vavr.collection.List;

public class Select_UnitTest {

    @Test
    void buildSql_simple() {
        // Setup
        Select subject = new Select();

        SelectParams request = new SelectParams(
                "table",
                List.of("col1", "col2", "col3"),
                new Equal("col1", 22),
                List.of(new OrderBy("col2", true)),
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
    void buildSql_complex() {
        // Setup
        Select subject = new Select();

        SelectParams request = new SelectParams(
                "table",
                List.of("col1", "col2", "col3"),
                new Not(
                        new And(List.of(
                                new Equal("col1", 22),
                                new Or(List.of(
                                        new In("col2", List.of(1, 2, 3)),
                                        new Not(new Null("col3")))),
                                new Less("col4", 22),
                                new Not(new More("col5", 12.4))))),
                List.of(
                        new OrderBy("col1", true),
                        new OrderBy("col4", false),
                        new OrderBy("col5", true)),
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
