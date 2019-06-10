package org.service.concept.immutable.command;

import org.apache.commons.dbcp2.BasicDataSource;
import org.service.concept.immutable.Event;

public interface TableOp {

    public Event apply(TableCommand command, BasicDataSource datasource);

}
