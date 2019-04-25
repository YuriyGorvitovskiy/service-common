package org.service.concept.two;

public interface Sort {
    enum Order {
        ASCENDING,
        DESCENDING
    };

    Order getOrder();

    int getPriority();
}
