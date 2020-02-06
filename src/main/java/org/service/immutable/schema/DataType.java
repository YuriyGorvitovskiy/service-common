package org.service.immutable.schema;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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
    };

    public abstract String postgres();

    public abstract void set(PreparedStatement ps, int i, Row row, String column) throws SQLException;
}
