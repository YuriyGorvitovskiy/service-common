package org.service.command.dml.postgres;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

import java.sql.Connection;

import org.apache.commons.dbcp2.BasicDataSource;
import org.service.command.Processor;
import org.service.command.dml.CountParams;
import org.service.command.dml.DMLRequest;
import org.service.command.dml.DMLResult;
import org.service.command.dml.DeleteParams;
import org.service.command.dml.InsertParams;
import org.service.command.dml.SelectParams;
import org.service.command.dml.UpdateParams;
import org.service.command.dml.UpsertParams;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class DMLProcessor extends Processor {

    final BasicDataSource dataSource;
    final Select          select = new Select();
    final Count           count  = new Count();
    final Insert          insert = new Insert();
    final Update          update = new Update();
    final Upsert          upsert = new Upsert();
    final Delete          delete = new Delete();

    public DMLProcessor(BasicDataSource dataSource, EventBus requestBus, EventBus responseBus, EventBus payloadBus) {
        super(requestBus, responseBus, payloadBus);
        this.dataSource = dataSource;
    }

    @Subscribe
    public void consume(DMLRequest request) {
        try (Connection db = dataSource.getConnection()) {
            reply(request,
                    Match(request.params).<DMLResult>of(
                            Case($(instanceOf(SelectParams.class)), (p) -> select.apply(p, db)),
                            Case($(instanceOf(CountParams.class)), (p) -> count.apply(p, db)),
                            Case($(instanceOf(InsertParams.class)), (p) -> insert.apply(p, db)),
                            Case($(instanceOf(UpdateParams.class)), (p) -> update.apply(p, db)),
                            Case($(instanceOf(UpsertParams.class)), (p) -> upsert.apply(p, db)),
                            Case($(instanceOf(DeleteParams.class)), (p) -> delete.apply(p, db))));
        } catch (Throwable ex) {
            failure(request, ex);
        }
    }
}
