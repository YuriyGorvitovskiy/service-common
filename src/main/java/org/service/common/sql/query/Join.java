package org.service.common.sql.query;

public class Join implements IPseudoSql {
    public enum Kind {
        FROM,
        INNER,
        LEFT,
        RIGHT
    };

    final Kind       kind;
    final TableAlias table;
    final ICondition condition;

    public Join(Kind kind, TableAlias table, ICondition condition) {
        this.kind = kind;
        this.table = table;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return toPseudoSql("");
    }

    @Override
    public String toPseudoSql(String indent) {
        StringBuilder sb = new StringBuilder();
        switch (kind) {
            case FROM:
                sb.append("  FROM ");
                break;
            case INNER:
                sb.append(" INNER JOIN ");
                break;
            case LEFT:
                sb.append("  LEFT JOIN ");
                break;
            case RIGHT:
                sb.append(" RIGHT JOIN ");
                break;
        }
        sb.append(table.toPseudoSql(indent));
        if (null != condition) {
            sb.append(" ON ");
            sb.append(condition.toPseudoSql(indent));
        }
        return sb.toString();
    }
}
