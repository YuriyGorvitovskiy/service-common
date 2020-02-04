package org.service.concept2;

import java.util.Date;

public interface Event extends Action {

    Id getUser();

    Date getTime();
}
