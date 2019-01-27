package org.service.common.sql.query;

public class Sorting implements IPseudoSql {
    public enum Order {
        ASCENDING,
        DESCENDING,
    }

    final IComparable column;
    final Order       order;

    public Sorting(IComparable column, Order order) {
        this.column = column;
        this.order = order;
    }

    @Override
    public String toPseudoSql(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(column);
        switch (order) {
            case ASCENDING:
                sb.append(" ASC");
                break;
            case DESCENDING:
                sb.append(" DESC");
                break;
        }
        return sb.toString();
    }
}
