package org.service.common.translator;

/**
 * Translator interface to translate JSON and Entity back and force.
 *
 * @param <E> Java Base Class, that is a normalized version of the entity used by the service.
 * @param <J> Java Base Class, with appropriate com.fasterxml.jackson annotations,
 * that describes JSON of all possible versions of the Entity.
 *
 * @author Yuriy Gorvitovskiy
 */
public interface JsonTranslator<E, J> {

    /**
     * Description function.
     *
     * @return Java Base Class, that is a normalized version of the entity used by the service.
     */
    public Class<E> getEntityClass();

    /**
     * Description function.
     *
     * @return Java Base Class, with appropriate com.fasterxml.jackson annotations,
     * that describes JSON of all possible versions of the Entity.
     */
    public Class<J> getJsonClass();

    /**
     * Translate java entity to the JSON instance.
     *
     * @param entiry java entity to be translated
     * @return new JSON describing instance
     * @throws Exception anything goes wrong
     */
    public J toJson(E entiry) throws Exception;

    /**
     * Translate JSON instance to the java entity.
     *
     * @param json describing instance to be translated
     * @return new JSON describing instance
     * @throws Exception anything goes wrong
     */
    public E fromJson(J json) throws Exception;
}
