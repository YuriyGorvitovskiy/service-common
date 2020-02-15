package org.service.dbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import io.vavr.collection.Stream;

public class DBResultSet implements Iterable<ResultSet> {
    class Iter implements Iterator<ResultSet> {

        @Override
        public boolean hasNext() {
            try {
                return rs.next();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public ResultSet next() {
            return rs;
        }

    }

    final ResultSet rs;

    public DBResultSet(ResultSet rs) {
        this.rs = rs;
    }

    @Override
    public Iterator<ResultSet> iterator() {
        return new Iter();
    }

    public static Stream<ResultSet> stream(ResultSet rs) {
        return Stream.ofAll(new DBResultSet(rs));
    }
}
