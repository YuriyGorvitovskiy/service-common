package org.service.common.sql.query;

import java.util.Arrays;
import java.util.List;

import org.service.common.sql.DataType;

import com.google.common.collect.Iterables;

public interface ICondition extends IComparable {

    public static class Equal implements ICondition {

        public final IComparable left;
        public final IComparable right;

        public Equal(IComparable left, IComparable right) {
            this.left = left;
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

        public In(IComparable left, Literal... values) {
            this.left = left;
            this.values = Arrays.asList(values);
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

    @Override
    public default DataType getDataType() {
        return DataType.BOOLEAN;
    }

    public static ICondition equal(IComparable left, IComparable right) {
        return new Equal(left, right);
    }

    public static ICondition in(IComparable left, Literal... values) {
        return new In(left, values);
    }

}
