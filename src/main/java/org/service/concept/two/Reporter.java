package org.service.concept.two;

import java.util.List;

public interface Reporter {
    List<ReportEntity> query(RequestEntity request);
}
