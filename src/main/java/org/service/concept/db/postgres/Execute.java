package org.service.concept.db.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.service.concept.db.event.Condition;
import org.service.concept.db.event.ConditionAnd;
import org.service.concept.db.event.ConditionEqual;
import org.service.concept.db.event.ConditionIn;
import org.service.concept.db.event.ConditionLess;
import org.service.concept.db.event.ConditionLike;
import org.service.concept.db.event.ConditionMore;
import org.service.concept.db.event.ConditionNot;
import org.service.concept.db.event.ConditionNull;
import org.service.concept.db.event.ConditionOr;

import com.google.common.collect.ImmutableList;

public interface Execute<Q, A> {

    public static final String EMPTY       = "";
    public static final String SPACE       = " ";
    public static final String NEW_LINE    = "\n";
    public static final String OPEN        = "(";
    public static final String CLOSE       = ")";
    public static final String COMMA_SPACE = ", ";
    public static final String COMMA_LINE  = "," + NEW_LINE;
    public static final String VAR         = "?";

    public static final String NOT         = "NOT ";
    public static final String AND         = "AND ";
    public static final String OR          = "OR ";
    public static final String EQ          = " = ";
    public static final String NE          = " = ";
    public static final String LT          = " < ";
    public static final String LE          = " <= ";
    public static final String GT          = " > ";
    public static final String GE          = " >= ";
    public static final String IN          = " IN ";
    public static final String NOT_IN      = " NOT IN ";
    public static final String LIKE        = " LIKE ";
    public static final String NOT_LIKE    = " NOT LIKE ";
    public static final String IS_NULL     = " IS NULL";
    public static final String IS_NOT_NULL = " IS NOT NULL";

    public A execute(Connection db, Q request) throws SQLException;

    public default void buildConditionSql(StringBuilder sb,
                                          int indent,
                                          boolean invert,
                                          Condition condition,
                                          boolean wrapWithParentheses) {
        if (condition instanceof ConditionAnd) {
            ConditionAnd and = (ConditionAnd) condition;
            buildConditionBool(sb, indent, invert, AND, and.conditions, wrapWithParentheses);
        } else if (condition instanceof ConditionOr) {
            ConditionOr and = (ConditionOr) condition;
            buildConditionBool(sb, indent, invert, OR, and.conditions, wrapWithParentheses);
        } else if (condition instanceof ConditionNot) {
            ConditionNot not = (ConditionNot) condition;
            buildConditionSql(sb, indent, !invert, not.condition, wrapWithParentheses);
        } else if (condition instanceof ConditionEqual) {
            ConditionEqual eq = (ConditionEqual) condition;
            buildConditionComp(sb, eq.column, invert ? NE : EQ);
        } else if (condition instanceof ConditionLess) {
            ConditionLess less = (ConditionLess) condition;
            buildConditionComp(sb, less.column, invert ? GE : LT);
        } else if (condition instanceof ConditionMore) {
            ConditionMore more = (ConditionMore) condition;
            buildConditionComp(sb, more.column, invert ? LE : GT);
        } else if (condition instanceof ConditionLike) {
            ConditionLike like = (ConditionLike) condition;
            buildConditionComp(sb, like.column, invert ? NOT_LIKE : LIKE);
        } else if (condition instanceof ConditionNull) {
            ConditionNull nl = (ConditionNull) condition;
            buildConditionNull(sb, nl.column, invert ? IS_NOT_NULL : IS_NULL);
        } else if (condition instanceof ConditionIn) {
            ConditionIn in = (ConditionIn) condition;
            buildConditionIn(sb, in.column, invert ? NOT_IN : IN, in.values.size());
        }
    }

    public default void buildConditionBool(StringBuilder sb,
                                           int indent,
                                           boolean invert,
                                           String sqlOp,
                                           ImmutableList<Condition> conditions,
                                           boolean wrapWithParentheses) {
        if (CollectionUtils.isEmpty(conditions)) {
            return;
        }

        if (1 == conditions.size()) {
            buildConditionSql(sb, indent, invert, conditions.get(0), wrapWithParentheses);
        }

        if (invert) {
            sb.append(NEW_LINE);
            sb.append(indentWith(indent, NOT));
            indent += 4;
            wrapWithParentheses = true;
        }

        sb.append(wrapWithParentheses ? indentWith(4, OPEN + SPACE) : EMPTY);
        String separator = NEW_LINE + indentWith(indent, sqlOp);
        String sp        = EMPTY;
        indent += 4;
        for (Condition condition : conditions) {
            sb.append(sp);
            buildConditionSql(sb, indent, false, condition, true);
            sp = separator;
        }
        sb.append(wrapWithParentheses ? CLOSE : EMPTY);
    }

    public default String newLineIndentWith(int indent, String sqlOp) {
        return NEW_LINE + StringUtils.repeat(SPACE, indent - sqlOp.length()) + sqlOp;
    }

    public default String indentWith(int indent, String sqlOp) {
        return StringUtils.repeat(SPACE, indent - sqlOp.length()) + sqlOp;
    }

    public default void buildConditionComp(StringBuilder sb, String column, Object sqlOp) {
        sb.append(column);
        sb.append(sqlOp);
        sb.append(VAR);
    }

    public default void buildConditionNull(StringBuilder sb, String column, Object sqlOp) {
        sb.append(column);
        sb.append(sqlOp);
    }

    public default void buildConditionIn(StringBuilder sb, String column, Object sqlOp, int valuesCount) {
        sb.append(column);
        sb.append(sqlOp);
        sb.append(OPEN);
        sb.append(StringUtils.repeat(VAR, COMMA_SPACE, valuesCount));
        sb.append(CLOSE);
    }

    public default int setConditionValues(PreparedStatement ps, int index, Condition condition) throws SQLException {
        if (condition instanceof ConditionAnd) {
            ConditionAnd and = (ConditionAnd) condition;
            return setConditionBoolValues(ps, index, and.conditions);
        } else if (condition instanceof ConditionOr) {
            ConditionOr and = (ConditionOr) condition;
            return setConditionBoolValues(ps, index, and.conditions);
        } else if (condition instanceof ConditionNot) {
            ConditionNot not = (ConditionNot) condition;
            return setConditionValues(ps, index, not.condition);
        } else if (condition instanceof ConditionEqual) {
            ConditionEqual eq = (ConditionEqual) condition;
            return setConditionCompValue(ps, index, eq.value);
        } else if (condition instanceof ConditionLess) {
            ConditionLess less = (ConditionLess) condition;
            return setConditionCompValue(ps, index, less.value);
        } else if (condition instanceof ConditionMore) {
            ConditionMore more = (ConditionMore) condition;
            return setConditionCompValue(ps, index, more.value);
        } else if (condition instanceof ConditionLike) {
            ConditionLike like = (ConditionLike) condition;
            return setConditionCompValue(ps, index, like.value);
        } else if (condition instanceof ConditionNull) {
            return index;
        } else if (condition instanceof ConditionIn) {
            ConditionIn in = (ConditionIn) condition;
            return setConditionInValues(ps, index, in.values);
        }
        return index;
    }

    public default int setConditionBoolValues(PreparedStatement ps, int index,
                                              ImmutableList<Condition> conditions) throws SQLException {
        for (Condition condition : conditions) {
            index = setConditionValues(ps, index, condition);
        }
        return index;
    }

    public default int setConditionCompValue(PreparedStatement ps, int index, Object value) throws SQLException {
        ps.setObject(index, value);
        return index + 1;
    }

    public default int setConditionInValues(PreparedStatement ps, int index, ImmutableList<Object> values) throws SQLException {
        for (Object value : values) {
            ps.setObject(index++, value);
        }
        return index;
    }
}
