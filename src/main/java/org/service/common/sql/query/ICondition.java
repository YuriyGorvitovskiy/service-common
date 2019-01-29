package org.service.common.sql.query;

import java.util.Arrays;
import java.util.List;

import org.service.common.sql.DataType;

import com.google.common.collect.Iterables;

public interface ICondition extends IComparable {

    public static class IsNull implements ICondition {

        public final IComparable left;

        public IsNull(IComparable left) {
            this.left = left;
        }

        @Override
        public String toString() {
            return toPseudoSql("");
        }

        @Override
        public String toPseudoSql(String indent) {
            return left.toPseudoSql(indent) + " IS NULL";
        }
    }

    public static class Equal implements ICondition {

        public final IComparable left;
        public final IComparable right;

        public Equal(IComparable left, IComparable right) {
            this.left  = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return toPseudoSql("");
        }

        @Override
        public String toPseudoSql(String indent) {
            return left.toPseudoSql(indent) + " = " + right.toPseudoSql(indent);
        }
    }

    public static class In implements ICondition {

        public final IComparable   left;
        public final List<Literal> values;

        public In(IComparable left, List<Literal> values) {
            this.left   = left;
            this.values = values;
        }

        @Override
        public String toString() {
            return toPseudoSql("");
        }

        @Override
        public String toPseudoSql(String indent) {
            return left.toPseudoSql(indent) + " IN (" +
                   String.join(", ", Iterables.transform(values, (v) -> v.toPseudoSql(indent)))
                   + ")";
        }

    }

    public static class And implements ICondition {

        public final List<ICondition> conditions;

        public And(List<ICondition> conditions) {
            this.conditions = conditions;
        }

        @Override
        public String toString() {
            return toPseudoSql("");
        }

        @Override
        public String toPseudoSql(String indent) {
            return String.join(" AND ", Iterables.transform(conditions, (v) -> "(" + v.toPseudoSql(indent) + ")"));
        }

    }

    @Override
    public default DataType getDataType() {
        return DataType.BOOLEAN;
    }

    public static ICondition isNull(IComparable left) {
        return new IsNull(left);
    }

    public static ICondition equal(IComparable left, IComparable right) {
        return new Equal(left, right);
    }

    public static ICondition in(IComparable left, List<Literal> values) {
        return new In(left, values);
    }

    public static ICondition in(IComparable left, Literal... values) {
        return in(left, Arrays.asList(values));
    }

    public static ICondition and(List<ICondition> conditions) {
        if (conditions.isEmpty()) {
            return null;
        }
        if (1 == conditions.size()) {
            return conditions.get(0);
        }

        return new And(conditions);
    }

    public static ICondition in(ICondition... conditions) {
        return and(Arrays.asList(conditions));
    }

}
