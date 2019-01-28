package org.service.common.treeql;

import java.util.ArrayList;
import java.util.List;

public abstract class QueryNode<N extends QueryNode<N>> {

    final String          entityType;

    final List<String>    fields     = new ArrayList<>();

    final List<QueryLink> links      = new ArrayList<>();

    final List<Condition> conditions = new ArrayList<>();

    final List<Sorting>   sorting    = new ArrayList<>();

    protected QueryNode(String entityType) {
        this.entityType = entityType;
    }

    @SuppressWarnings("unchecked")
    public N field(String field) {
        fields.add(field);
        return (N) this;
    }

    @SuppressWarnings("unchecked")
    public N link(QueryLink link) {
        link.parentNode = this;
        links.add(link);
        return (N) this;
    }

    @SuppressWarnings("unchecked")
    public N condition(String field, Condition.Operation operation, Object... values) {
        conditions.add(new Condition(field, operation, values));
        return (N) this;
    }

    @SuppressWarnings("unchecked")
    public N sort(String field, Sorting.Order order) {
        sorting.add(new Sorting(field, order));
        return (N) this;
    }

    public String toTreeQL(String indent) {
        StringBuilder sb = new StringBuilder();
        toTreeQL(sb, indent);
        return sb.toString();
    }

    protected void toTreeQL(StringBuilder sb, String indent) {
        final String NEXT_INDENT      = indent + "    ";
        final String NEXT_LINE        = "\n" + indent;
        final String NEXT_LINE_INDENT = "\n" + NEXT_INDENT;

        sb.append(indent);
        sb.append("type: ");
        sb.append(entityType);

        if (!fields.isEmpty()) {
            sb.append(NEXT_LINE);
            sb.append("fields: [");
            sb.append(String.join(", ", fields));
            sb.append("]");
        }

        if (!links.isEmpty()) {
            sb.append(NEXT_LINE);
            sb.append("links:");
            for (QueryLink link : links) {
                sb.append("\n");
                sb.append(link.toTreeQL(NEXT_INDENT));
            }
        }

        if (!conditions.isEmpty()) {
            sb.append(NEXT_LINE);
            sb.append("conditions:");
            for (Condition condition : conditions) {
                sb.append(NEXT_LINE_INDENT);
                sb.append(condition.toTreeQL(NEXT_INDENT));
            }
        }

        if (!sorting.isEmpty()) {
            sb.append(NEXT_LINE);
            sb.append("sorting:");
            for (Sorting sort : sorting) {
                sb.append(NEXT_LINE_INDENT);
                sb.append(sort.toTreeQL(NEXT_INDENT));
            }
        }
    }

}
