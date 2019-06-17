package org.service.command.dml;

import org.service.command.Request;

public class DMLRequest extends Request<DMLParams> {

    public DMLRequest(String request_id, DMLParams params) {
        super(request_id, params);
    }
}
