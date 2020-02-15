package org.service.dbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.vavr.collection.List;
import io.vavr.collection.Stream;

public class DBConnection implements AutoCloseable {

    final Connection           jdbc;
    final DBConnectionConsumer onClose;

    public DBConnection(Connection jdbc) {
        this(jdbc, c -> c.close());
    }

    public DBConnection(Connection jdbc, DBConnectionConsumer onClose) {
        this.jdbc = jdbc;
        this.onClose = onClose;
    }

    @Override
    public void close() {
        try {
            onClose.accept(jdbc);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean execute(String sql) {
        return this.execute(sql, DBStatementBinder.EMPTY);
    }

    public boolean execute(String sql, DBStatementBinder prepare) {
        try (PreparedStatement ps = jdbc.prepareStatement(sql)) {
            prepare.prepare(ps);
            return ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int executeUpdate(String sql) {
        return this.executeUpdate(sql, DBStatementBinder.EMPTY);
    }

    public int executeUpdate(String sql, DBStatementBinder binder) {
        try (PreparedStatement ps = jdbc.prepareStatement(sql)) {
            binder.prepare(ps);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public long executeLargeUpdate(String sql) {
        return this.executeLargeUpdate(sql, DBStatementBinder.EMPTY);
    }

    public long executeLargeUpdate(String sql, DBStatementBinder binder) {
        try (PreparedStatement ps = jdbc.prepareStatement(sql)) {
            binder.prepare(ps);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> int[] executeBatch(String sql, DBStatementBatchBinder<T> binder, Stream<T> data) {
        try (PreparedStatement ps = jdbc.prepareStatement(sql)) {
            for (T d : data) {
                binder.prepare(ps, d);
            }
            return ps.executeBatch();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> long[] executeLargeBatch(String sql, DBStatementBatchBinder<T> binder, Stream<T> data) {
        try (PreparedStatement ps = jdbc.prepareStatement(sql)) {
            for (T d : data) {
                binder.prepare(ps, d);
            }
            return ps.executeLargeBatch();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> List<T> executeQuery(String sql, DBResultConsumer<T> consumer) {
        return executeQuery(sql, DBStatementBinder.EMPTY, consumer);
    }

    public <T> List<T> executeQuery(String sql, DBStatementBinder binder, DBResultConsumer<T> consumer) {
        try (PreparedStatement ps = jdbc.prepareStatement(sql)) {
            binder.prepare(ps);
            try (ResultSet rs = ps.executeQuery()) {
                return Stream.ofAll(new DBResultSet(rs)).map(r -> {
                    try {
                        return consumer.accept(r);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }).toList();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> T executeSingle(String sql, DBResultConsumer<T> consumer) {
        return executeSingle(sql, DBStatementBinder.EMPTY, consumer);
    }

    public <T> T executeSingle(String sql, DBStatementBinder binder, DBResultConsumer<T> consumer) {
        try (PreparedStatement ps = jdbc.prepareStatement(sql)) {
            binder.prepare(ps);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? consumer.accept(rs) : null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Long executeCount(String sql) {
        return executeCount(sql, DBStatementBinder.EMPTY);
    }

    public Long executeCount(String sql, DBStatementBinder binder) {
        return executeSingle(sql, binder, rs -> rs.getLong(1));
    }
}
