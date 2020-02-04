package org.service.concept2;

import java.util.List;

public interface Reporter {
    List<ReportEntity> query(RequestEntity request);
}
