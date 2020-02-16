package org.service.immutable.schema;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;

import org.apache.commons.lang3.StringUtils;
import org.service.immutable.data.Row;

public enum DataType {
    BOOLEAN {
        @Override
        public String postgres() {
            return "BOOLEAN";
        }

        @Override
        public void set(PreparedStatement ps, int i, Row row, String column) throws SQLException {
            ps.setBoolean(i, row.asBool(column));
        }

        @Override
        public void set(PreparedStatement ps, int i, Object value) throws SQLException {
            if (null == value) {
                ps.setNull(i, Types.BOOLEAN);
            } else {
                ps.setBoolean(i, ((Boolean) value).booleanValue());
            }
        }

    },
    INTEGER {
        @Override
        public String postgres() {
            return "BIGINT";
        }

        @Override
        public void set(PreparedStatement ps, int i, Row row, String column) throws SQLException {
            ps.setLong(i, row.asLong(column));
        }

        @Override
        public void set(PreparedStatement ps, int i, Object value) throws SQLException {
            if (null == value) {
                ps.setNull(i, Types.BIGINT);
            } else {
                ps.setLong(i, ((Number) value).longValue());
            }
        }
    },
    DOUBLE {
        @Override
        public String postgres() {
            return "DOUBLE PRECISION";
        }

        @Override
        public void set(PreparedStatement ps, int i, Row row, String column) throws SQLException {
            ps.setDouble(i, row.asDouble(column));
        }

        @Override
        public void set(PreparedStatement ps, int i, Object value) throws SQLException {
            if (null == value) {
                ps.setNull(i, Types.DOUBLE);
            } else {
                ps.setDouble(i, ((Number) value).doubleValue());
            }
        }
    },
    LABEL {
        @Override
        public String postgres() {
            return "VARCHAR(128)";
        }

        @Override
        public void set(PreparedStatement ps, int i, Row row, String column) throws SQLException {
            ps.setString(i, StringUtils.truncate(row.asString(column), 128));
        }

        @Override
        public void set(PreparedStatement ps, int i, Object value) throws SQLException {
            if (null == value) {
                ps.setNull(i, Types.VARCHAR);
            } else {
                ps.setString(i, (StringUtils.truncate(value.toString(), 128)));
            }
        }
    },
    SENTENCE {
        @Override
        public String postgres() {
            return "VARCHAR(4096)";
        }

        @Override
        public void set(PreparedStatement ps, int i, Row row, String column) throws SQLException {
            ps.setString(i, StringUtils.truncate(row.asString(column), 4096));
        }

        @Override
        public void set(PreparedStatement ps, int i, Object value) throws SQLException {
            if (null == value) {
                ps.setNull(i, Types.VARCHAR);
            } else {
                ps.setString(i, (StringUtils.truncate(value.toString(), 4096)));
            }
        }

    },
    TEXT {
        @Override
        public String postgres() {
            return "TEXT";
        }

        @Override
        public void set(PreparedStatement ps, int i, Row row, String column) throws SQLException {
            ps.setString(i, row.asString(column));
        }

        @Override
        public void set(PreparedStatement ps, int i, Object value) throws SQLException {
            if (null == value) {
                ps.setNull(i, Types.VARCHAR);
            } else {
                ps.setString(i, value.toString());
            }
        }
    },
    TIMESTAMP {
        @Override
        public String postgres() {
            return "TIMESTAMP WITH TIME ZONE";
        }

        @Override
        public void set(PreparedStatement ps, int i, Row row, String column) throws SQLException {
            ps.setTimestamp(i, new Timestamp(row.asInstant(column).toEpochMilli()));
        }

        @Override
        public void set(PreparedStatement ps, int i, Object value) throws SQLException {
            if (null == value) {
                ps.setNull(i, Types.TIMESTAMP_WITH_TIMEZONE);
            } else {
                ps.setTimestamp(i, new Timestamp(((Instant) value).toEpochMilli()));
            }

        }
    };

    public abstract String postgres();

    public abstract void set(PreparedStatement ps, int i, Row row, String column) throws SQLException;

    public abstract void set(PreparedStatement ps, int i, Object value) throws SQLException;
}
