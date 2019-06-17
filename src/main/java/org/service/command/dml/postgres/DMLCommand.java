package org.service.command.dml.postgres;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.service.command.Command;
import org.service.command.dml.DMLParams;
import org.service.command.dml.DMLResult;
import org.service.command.dml.predicate.And;
import org.service.command.dml.predicate.Equal;
import org.service.command.dml.predicate.In;
import org.service.command.dml.predicate.Less;
import org.service.command.dml.predicate.Like;
import org.service.command.dml.predicate.More;
import org.service.command.dml.predicate.Not;
import org.service.command.dml.predicate.Null;
import org.service.command.dml.predicate.Or;
import org.service.command.dml.predicate.Predicate;

import io.vavr.collection.Seq;

public interface DMLCommand<P extends DMLParams, R extends DMLResult> extends Command<P, Connection, R> {

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

    public default String buildConditionSql(int indent,
                                            boolean invert,
                                            Predicate condition,
                                            boolean wrapWithParentheses) {
        return Match(condition).of(
                Case($(instanceOf(And.class)),
                        c -> buildConditionBool(indent, invert, AND, c.conditions, wrapWithParentheses)),
                Case($(instanceOf(Or.class)),
                        c -> buildConditionBool(indent, invert, OR, c.conditions, wrapWithParentheses)),
                Case($(instanceOf(Not.class)),
                        c -> buildConditionSql(indent, !invert, c.condition, wrapWithParentheses)),
                Case($(instanceOf(Equal.class)),
                        c -> buildConditionComp(c.column, invert ? NE : EQ)),
                Case($(instanceOf(Less.class)),
                        c -> buildConditionComp(c.column, invert ? GE : LT)),
                Case($(instanceOf(More.class)),
                        c -> buildConditionComp(c.column, invert ? LE : GT)),
                Case($(instanceOf(Like.class)),
                        c -> buildConditionComp(c.column, invert ? NOT_LIKE : LIKE)),
                Case($(instanceOf(Null.class)),
                        c -> buildConditionNull(c.column, invert ? IS_NOT_NULL : IS_NULL)),
                Case($(instanceOf(In.class)),
                        c -> buildConditionIn(c.column, invert ? NOT_IN : IN, c.values.size())));
    }

    public default String buildConditionBool(int indent,
                                             boolean invert,
                                             String sqlOp,
                                             Seq<Predicate> conditions,
                                             boolean wrapWithParentheses) {
        if (conditions.isEmpty()) {
            return "";
        }

        if (1 == conditions.size()) {
            return buildConditionSql(indent, invert, conditions.get(0), wrapWithParentheses);
        }

        StringBuilder sb = new StringBuilder();
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
        for (Predicate condition : conditions) {
            sb.append(sp);
            sb.append(buildConditionSql(indent, false, condition, true));
            sp = separator;
        }
        sb.append(wrapWithParentheses ? CLOSE : EMPTY);
        return sb.toString();
    }

    public default String newLineIndentWith(int indent, String sqlOp) {
        return NEW_LINE + StringUtils.repeat(SPACE, indent - sqlOp.length()) + sqlOp;
    }

    public default String indentWith(int indent, String sqlOp) {
        return StringUtils.repeat(SPACE, indent - sqlOp.length()) + sqlOp;
    }

    public default String buildConditionComp(String column, Object sqlOp) {
        return column + sqlOp + VAR;
    }

    public default String buildConditionNull(String column, Object sqlOp) {
        return column + sqlOp;
    }

    public default String buildConditionIn(String column, Object sqlOp, int valuesCount) {
        return column + sqlOp + OPEN + StringUtils.repeat(VAR, COMMA_SPACE, valuesCount) + CLOSE;
    }

    public default int setConditionValues(PreparedStatement ps, int index, Predicate condition) throws SQLException {
        if (condition instanceof And) {
            And and = (And) condition;
            return setConditionBoolValues(ps, index, and.conditions);
        } else if (condition instanceof Or) {
            Or and = (Or) condition;
            return setConditionBoolValues(ps, index, and.conditions);
        } else if (condition instanceof Not) {
            Not not = (Not) condition;
            return setConditionValues(ps, index, not.condition);
        } else if (condition instanceof Equal) {
            Equal eq = (Equal) condition;
            return setConditionCompValue(ps, index, eq.value);
        } else if (condition instanceof Less) {
            Less less = (Less) condition;
            return setConditionCompValue(ps, index, less.value);
        } else if (condition instanceof More) {
            More more = (More) condition;
            return setConditionCompValue(ps, index, more.value);
        } else if (condition instanceof Like) {
            Like like = (Like) condition;
            return setConditionCompValue(ps, index, like.value);
        } else if (condition instanceof Null) {
            return index;
        } else if (condition instanceof In) {
            In in = (In) condition;
            return setConditionInValues(ps, index, in.values);
        }
        return index;
    }

    public default int setConditionBoolValues(PreparedStatement ps, int index,
                                              Seq<Predicate> conditions) throws SQLException {
        for (Predicate condition : conditions) {
            index = setConditionValues(ps, index, condition);
        }
        return index;
    }

    public default int setConditionCompValue(PreparedStatement ps, int index, Object value) throws SQLException {
        ps.setObject(index, value);
        return index + 1;
    }

    public default int setConditionInValues(PreparedStatement ps, int index, Seq<?> values) throws SQLException {
        for (Object value : values) {
            ps.setObject(index++, value);
        }
        return index;
    }
}
