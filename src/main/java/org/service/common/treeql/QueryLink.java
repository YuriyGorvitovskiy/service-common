package org.service.common.treeql;

public class QueryLink extends QueryNode<QueryLink> {

    /// Will be set by parent
    QueryNode<?>  parentNode;

    final String  linkField;

    final boolean mustExists;

    public QueryLink(String linkField, String entityType, boolean mustExists) {
        super(entityType);
        this.linkField  = linkField;
        this.mustExists = mustExists;
    }

    @Override
    public String toTreeQL(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent);
        sb.append(linkField);
        sb.append(":\n");

        toTreeQL(sb, indent + "    ");
        return sb.toString();
    }

}
