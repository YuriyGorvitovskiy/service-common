package org.service.common.treeql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.service.common.sql.DataType;
import org.service.common.sql.query.ICondition;
import org.service.common.sql.query.Literal;
import org.service.common.sql.query.Select;
import org.service.common.sql.query.Sorting.Order;
import org.service.common.sql.query.TableAlias;
import org.service.common.sql.query.TableAliasColumn;
import org.service.common.sql.schema.Schema;
import org.service.common.sql.schema.Table;

import com.google.common.collect.Lists;

public class TreeQLtoSQL {

    public static class AliasGenerator {
        Map<String, Integer> used = new HashMap<>();

        public TableAlias get(Table table) {
            String  letter  = ("" + table.getName().charAt(0)).toLowerCase();
            Integer counter = used.get(letter);

            if (null == counter) {
                used.put(letter, 1);
                return new TableAlias(letter, table);
            }
            String name = letter + counter;
            used.put(letter, counter + 1);
            return new TableAlias(name, table);
        }
    }

    public final Schema schema;

    public TreeQLtoSQL(Schema schema) {
        this.schema = schema;
    }

    public Select toSQL(QueryTree treeQL) {
        AliasGenerator aliasGenerator = new AliasGenerator();
        Select         select         = new Select();

        Table          table          = schema.getTable(treeQL.entityType);
        TableAlias     alias          = aliasGenerator.get(table);

        select.from(alias);

        appendToSelect(select, treeQL, alias);

        for (QueryLink link : treeQL.links) {
            appendToSelect(select, link, alias, aliasGenerator);
        }

        select.where(ICondition.and(Lists.transform(treeQL.conditions, (c) -> this.toSQL(alias, c))));

        return select;
    }

    public void appendToSelect(Select select, QueryLink treeQL, TableAlias parent, AliasGenerator aliasGenerator) {
        Table      table = schema.getTable(treeQL.entityType);
        TableAlias alias = aliasGenerator.get(table);

        appendToSelect(select, treeQL, alias);

        List<ICondition> conditions = new ArrayList<>();
        if (treeQL.linkField.startsWith("^")) {
            conditions.add(ICondition.equal(alias.getColumn(treeQL.linkField.substring(1)), parent.getColumn("id")));
        } else {
            conditions.add(ICondition.equal(alias.getColumn("id"), parent.getColumn(treeQL.linkField)));
        }
        conditions.addAll(Lists.transform(treeQL.conditions, (c) -> this.toSQL(alias, c)));
        ICondition condition = ICondition.and(conditions);

        if (treeQL.mustExists) {
            select.inner(alias, condition);
        } else {
            select.left(alias, condition);
        }

    }

    public void appendToSelect(Select select, QueryNode<?> treeQL, TableAlias alias) {
        for (String field : treeQL.fields) {
            select.column(alias.getColumn(field), alias.getName() + "_" + field);
        }

        for (Sorting sort : treeQL.sorting) {
            select.orderBy(alias.getColumn(sort.field), toSQL(sort.order));
        }
    }

    private ICondition toSQL(TableAlias table, Condition condition) {
        TableAliasColumn column   = table.getColumn(condition.field);
        DataType         dataType = column.getDataType();
        switch (condition.operation) {
            case IS_NULL:
                return ICondition.isNull(column);
            case EQUAL:
                return ICondition.equal(column, new Literal(dataType, condition.values.get(0)));
            case IN:
                return ICondition.in(column, Lists.transform(condition.values, (v) -> new Literal(dataType, v)));
        }
        return null;
    }

    private Order toSQL(Sorting.Order treeQL) {
        if (Sorting.Order.DESCENDING == treeQL) {
            return Order.DESCENDING;
        }
        return Order.ASCENDING;
    }
}
