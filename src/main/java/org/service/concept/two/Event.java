package org.service.concept.two;

import java.util.Date;

public interface Event extends Action {

    Id getUser();

    Date getTime();
}
