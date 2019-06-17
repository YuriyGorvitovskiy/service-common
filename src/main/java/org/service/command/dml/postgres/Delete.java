package org.service.command.dml.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.service.command.dml.DeleteParams;
import org.service.command.dml.UpdateResult;

public class Delete implements DMLCommand<DeleteParams, UpdateResult> {
    static final String DELETE_FROM = "DELETE FROM ";
    static final String WHERE       = NEW_LINE +
            " WHERE ";

    @Override
    public UpdateResult apply(DeleteParams params, Connection db) {
        String sql = buildSql(params);
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            setValues(ps, params);
            return new UpdateResult(ps.executeUpdate());
        } catch (SQLException ex) {
            throw new RuntimeException(sql, ex);
        }
    }

    String buildSql(DeleteParams request) {
        StringBuilder sb = new StringBuilder();
        sb.append(DELETE_FROM);
        sb.append(request.table);
        sb.append(WHERE);

        sb.append(buildConditionSql(7, false, request.filter, false));
        return sb.toString();
    }

    void setValues(PreparedStatement ps, DeleteParams request) throws SQLException {
        setConditionValues(ps, 1, request.filter);
    }
}
