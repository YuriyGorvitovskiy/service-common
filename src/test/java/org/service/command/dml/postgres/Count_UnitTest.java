package org.service.command.dml.postgres;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.service.command.dml.postgres.DMLCommand.NEW_LINE;

import org.junit.jupiter.api.Test;
import org.service.command.dml.CountParams;
import org.service.command.dml.predicate.And;
import org.service.command.dml.predicate.Equal;
import org.service.command.dml.predicate.In;
import org.service.command.dml.predicate.Less;
import org.service.command.dml.predicate.More;
import org.service.command.dml.predicate.Not;
import org.service.command.dml.predicate.Null;
import org.service.command.dml.predicate.Or;

import io.vavr.collection.List;

public class Count_UnitTest {

    @Test
    void buildSql_simple() {
        // Setup
        Count       subject = new Count();

        CountParams request = new CountParams("table", new Equal("col1", 22));

        // Execute
        String result = subject.buildSql(request);

        // Verify
        assertEquals("SELECT COUNT(*)" + NEW_LINE +
                "  FROM table" + NEW_LINE +
                " WHERE col1 = ?",
                result);

    }

    @Test
    void buildSql_complex() {
        // Setup
        Count       subject = new Count();

        CountParams request = new CountParams(
                "table",
                new Not(
                        new And(List.of(
                                new Equal("col1", 22),
                                new Or(List.of(
                                        new In("col2", List.of(1, 2, 3)),
                                        new Not(new Null("col3")))),
                                new Less("col4", 22),
                                new Not(new More("col5", 12.4))))));

        // Execute
        String result = subject.buildSql(request);

        // Verify
        assertEquals("SELECT COUNT(*)" + NEW_LINE +
                "  FROM table" + NEW_LINE +
                " WHERE " + NEW_LINE +
                "   NOT   ( col1 = ?" + NEW_LINE +
                "       AND   ( col2 IN (?, ?, ?)" + NEW_LINE +
                "            OR col3 IS NOT NULL)" + NEW_LINE +
                "       AND col4 < ?" + NEW_LINE +
                "       AND col5 <= ?)",
                result);

    }
}
