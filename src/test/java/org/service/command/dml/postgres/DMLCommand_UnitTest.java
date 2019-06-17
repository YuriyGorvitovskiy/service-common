package org.service.concept.db.postgres;

import static org.junit.Assert.assertEquals;
import static org.service.concept.db.postgres.Execute.NEW_LINE;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.service.concept.db.event.Condition;
import org.service.concept.db.event.ConditionAnd;
import org.service.concept.db.event.ConditionEqual;
import org.service.concept.db.event.ConditionIn;
import org.service.concept.db.event.ConditionLess;
import org.service.concept.db.event.ConditionMore;
import org.service.concept.db.event.ConditionNot;
import org.service.concept.db.event.ConditionNull;
import org.service.concept.db.event.ConditionOr;

import com.google.common.collect.ImmutableList;

public class Execute_UnitTest {

    @Test
    public void test_buildConditionSql() {
        // Setup
        Execute<String, String> subject   = new Execute<String, String>() {

                                              @Override
                                              public String execute(Connection db, String request) throws SQLException {
                                                  return null;
                                              }
                                          };

        Condition               condition = new ConditionNot(
                new ConditionAnd(ImmutableList.of(
                        new ConditionEqual("col1", 22),
                        new ConditionOr(ImmutableList.of(
                                new ConditionIn("col2", ImmutableList.of(1, 2, 3)),
                                new ConditionNot(new ConditionNull("col3")))),
                        new ConditionLess("col4", 22),
                        new ConditionNot(new ConditionMore("col5", 12.4)))));

        StringBuilder           sb        = new StringBuilder();
        sb.append("SELECT *\n");
        sb.append("  FROM table\n");
        sb.append(" WHERE ");
        subject.buildConditionSql(sb, 7, false, condition, false);

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
