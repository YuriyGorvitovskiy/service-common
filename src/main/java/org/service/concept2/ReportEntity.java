package org.service.concept2;

import java.util.List;
import java.util.Map;

public interface ReportEntity {

    Map<RelationInfo, List<ReportEntity>> getRelations();

    Map<ScalarInfo, Object> getProps();
}
