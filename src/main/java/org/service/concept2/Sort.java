package org.service.concept2;

public interface Sort {
    enum Order {
        ASCENDING,
        DESCENDING
    };

    Order getOrder();

    int getPriority();
}
