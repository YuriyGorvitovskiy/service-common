package org.service.common.sql;

public enum DataType {
    /**
     * <ul>
     *  <li>Postgres: BOOLEAN</li>
     *  </ul>
     */
    BOOLEAN,

    /**
     * <ul>
     *  <li>Postgres: BIGINT</li>
     *  </ul>
     */
    INTEGER,

    /**
     * <ul>
     *  <li>Postgres: DOUBLE PRECISION</li>
     *  </ul>
     */
    DOUBLE,

    /**
     * <ul>
     *  <li>Postgres: CHARACTER VARYING(256)</li>
     *  </ul>
     */
    STRING,

    /**
     * <ul>
     *  <li>Postgres: TEXT</li>
     *  </ul>
     */
    TEXT,

    /**
     * <ul>
     *  <li>Postgres: TIMESTAMP WITH TIME ZONE</li>
     *  </ul>
     */
    TIMESTAMP,

    /**
     * <ul>
     *  <li>Postgres: CHARACTER VARYING(256)</li>
     *  </ul>
     */
    REFERENCE_EXTERNAL
}
