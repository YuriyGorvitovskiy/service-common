package org.service.command.dml.postgres;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.service.command.dml.postgres.DMLCommand.NEW_LINE;

import java.sql.Connection;

import org.junit.jupiter.api.Test;
import org.service.command.dml.DMLParams;
import org.service.command.dml.DMLResult;
import org.service.command.dml.postgres.DMLCommand;
import org.service.command.dml.predicate.And;
import org.service.command.dml.predicate.Equal;
import org.service.command.dml.predicate.In;
import org.service.command.dml.predicate.Less;
import org.service.command.dml.predicate.More;
import org.service.command.dml.predicate.Not;
import org.service.command.dml.predicate.Null;
import org.service.command.dml.predicate.Or;
import org.service.command.dml.predicate.Predicate;

import io.vavr.collection.List;

public class DMLCommand_UnitTest {
    static class DMLCommandMock implements DMLCommand<DMLParams, DMLResult> {
        @Override
        public DMLResult apply(DMLParams params, Connection db) {
            return null;
        }
    };

    @Test
    void buildConditionSql() {
        // Setup
        DMLCommandMock subject   = new DMLCommandMock();

        Predicate      condition = new Not(
                new And(List.of(
                        new Equal("col1", 22),
                        new Or(List.of(
                                new In("col2", List.of(1, 2, 3)),
                                new Not(new Null("col3")))),
                        new Less("col4", 22),
                        new Not(new More("col5", 12.4)))));

        StringBuilder  sb        = new StringBuilder();
        sb.append("SELECT *\n");
        sb.append("  FROM table\n");
        sb.append(" WHERE ");
        sb.append(subject.buildConditionSql(7, false, condition, false));

        assertEquals("SELECT *" + NEW_LINE +
                "  FROM table" + NEW_LINE +
                " WHERE " + NEW_LINE +
                "   NOT   ( col1 = ?" + NEW_LINE +
                "       AND   ( col2 IN (?, ?, ?)" + NEW_LINE +
                "            OR col3 IS NOT NULL)" + NEW_LINE +
                "       AND col4 < ?" + NEW_LINE +
                "       AND col5 <= ?)",
                sb.toString());

    }
}
