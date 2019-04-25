package org.service.common.persistence;

import java.util.Collection;
import java.util.Map;

public interface ISaverImplementation {
    public void upsert(String type,
                       Collection<String> ids,
                       Map<String, Object> attributes);

    public void update(String type,
                       Collection<String> ids,
                       Map<String, Object> attributes);

    public void delete(String type,
                       Collection<String> ids);
}
