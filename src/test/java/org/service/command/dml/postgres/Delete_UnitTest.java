package org.service.command.dml.postgres;

import static org.junit.Assert.assertEquals;
import static org.service.command.dml.postgres.DMLCommand.NEW_LINE;

import org.junit.Test;
import org.service.command.dml.DeleteParams;
import org.service.command.dml.postgres.Delete;
import org.service.command.dml.predicate.And;
import org.service.command.dml.predicate.Equal;
import org.service.command.dml.predicate.In;
import org.service.command.dml.predicate.Less;
import org.service.command.dml.predicate.More;
import org.service.command.dml.predicate.Not;
import org.service.command.dml.predicate.Null;
import org.service.command.dml.predicate.Or;

import io.vavr.collection.List;

public class Delete_UnitTest {

    @Test
    public void test_buildSql_simple() {
        // Setup
        Delete subject = new Delete();

        DeleteParams request = new DeleteParams("table", new Equal("col1", 22));

        // Execute
        String result = subject.buildSql(request);

        // Verify
        assertEquals("DELETE FROM table" + NEW_LINE +
                " WHERE col1 = ?",
                result);

    }

    @Test
    public void test_buildSql_complex() {
        // Setup
        Delete subject = new Delete();

        DeleteParams request = new DeleteParams(
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
        assertEquals("DELETE FROM table" + NEW_LINE +
                " WHERE " + NEW_LINE +
                "   NOT   ( col1 = ?" + NEW_LINE +
                "       AND   ( col2 IN (?, ?, ?)" + NEW_LINE +
                "            OR col3 IS NOT NULL)" + NEW_LINE +
                "       AND col4 < ?" + NEW_LINE +
                "       AND col5 <= ?)",
                result);

    }
}
