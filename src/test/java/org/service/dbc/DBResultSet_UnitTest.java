package org.service.dbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import io.vavr.collection.List;

public class DBResultSet_UnitTest {

    @Test
    public void stream() throws SQLException {
        // Setup
        ResultSet rs = mock(ResultSet.class);
        doReturn(true, true, false).when(rs).next();
        doReturn(1, 2).when(rs).getInt(1);

        // Execute
        List<Integer> list = DBResultSet.stream(rs)
            .map(r -> {
                try {
                    return r.getInt(1);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }).toList();

        // Verify
        assertEquals(List.of(1, 2), list);
    }
}
