package org.service.concept.immutable.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.service.concept.immutable.Event;

public class TableOpInsert implements TableOp {

    @Override
    public Event apply(TableCommand command, BasicDataSource datasource) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(command.table);

        List<Object> params = new ArrayList<>();
        SqlHelper.appendValues(sql, params, command.values);

        try (Connection connection = datasource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            SqlHelper.setParams(ps, params);
            ps.executeUpdate();
        } catch (SQLException ex) {

        }

        return null;
    }
}
