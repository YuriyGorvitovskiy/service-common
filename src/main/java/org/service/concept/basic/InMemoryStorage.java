package org.service.concept.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.service.concept.Attribute;
import org.service.concept.Batch;
import org.service.concept.Entity;
import org.service.concept.ID;
import org.service.concept.IFetcher;
import org.service.concept.IMutator;
import org.service.concept.Patch;
import org.service.concept.Type;

public class InMemoryStorage implements IMutator, IFetcher {

    public static class TypeStorage {
        final Type                type;
        final Map<String, Entity> entities = new HashMap<>();

        public TypeStorage(Type type) {
            this.type = type;
        }

        public void apply(Patch patch) {
            BiConsumer<String, Map<Attribute, Object>> operation = getOperation(patch.operation);
            for (String id : patch.batch.ids) {
                operation.accept(id, patch.attributes);
            }
        }

        public List<Entity> get(Batch batch) {
            List<Entity> result = new ArrayList<>(batch.ids.size());
            for (String id : batch.ids) {
                Entity entity = entities.get(id);
                if (null != entity) {
                    result.add(entity);
                }
            }
            return result;
        }

        protected BiConsumer<String, Map<Attribute, Object>> getOperation(Patch.Operation operation) {
            switch (operation) {
                case DELETE:
                    return (id, attributes) -> this.delete(id);
                case UPDATE:
                    return (id, attributes) -> this.update(id, attributes);
                case UPSERT:
                    return (id, attributes) -> this.upsert(id, attributes);
            }
            return null;
        }

        protected void upsert(String id, Map<Attribute, Object> attributes) {
            Entity entity = entities.computeIfAbsent(id, (key) -> new Entity(new ID(type, key)));
            entity.attributes.putAll(attributes);
        }

        protected void update(String id, Map<Attribute, Object> attributes) {
            Entity entity = entities.get(id);
            if (null == entity) {
                return;
            }
            entity.attributes.putAll(attributes);
        }

        protected void delete(String id) {
            entities.remove(id);
        }

    }

    final Map<Type, TypeStorage> storages = new HashMap<>();

    @Override
    public void apply(Patch patch) {
        storages.computeIfAbsent(patch.batch.type, (key) -> new TypeStorage(key)).apply(patch);
    }

    @Override
    public List<Entity> get(Batch batch) {
        return storages.computeIfAbsent(batch.type, (key) -> new TypeStorage(key)).get(batch);
    }

}
