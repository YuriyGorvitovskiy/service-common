package org.service.common.treeql;

public class Sorting {

    enum Order {
        ASCENDING,
        DESCENDING,
    };

    final String field;

    final Order  order;

    public Sorting(String field, Order order) {
        this.field = field;
        this.order = order;
    }

    public String toTreeQL(String indent) {
        switch (order) {
            case ASCENDING:
                return field + ": ASC";
            case DESCENDING:
                return field + ": DESC";
        }
        return null;
    }

}
