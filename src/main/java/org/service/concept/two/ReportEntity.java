package org.service.concept.two;

import java.util.List;
import java.util.Map;

public interface ReportEntity {

    Map<RelationInfo, List<ReportEntity>> getRelations();

    Map<ScalarInfo, Object> getProps();
}
