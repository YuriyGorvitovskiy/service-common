package org.service.concept;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Comparators {
    public static final IComparator EQUALS           = (f) -> {
                                                         Attribute attribute   = f.attribute;
                                                         Object    filterValue = f.values.get(0);
                                                         return (e) -> Objects.equals(e.attributes.get(attribute), filterValue);
                                                     };
    public static final IComparator NOT_EQUALS       = not(EQUALS);

    public static final IComparator IN               = (f) -> (e) -> {
                                                         Object entityValue = e.attributes.get(f.attribute);
                                                         for (Object filterValue : f.values) {
                                                             if (Objects.equals(entityValue, filterValue)) {
                                                                 return true;
                                                             }
                                                         }
                                                         return false;
                                                     };
    public static final IComparator NOT_IN           = not(IN);

    public static final IComparator CONTAINS         = (f) -> {
                                                         Attribute attribute    = f.attribute;
                                                         String    escapedValue = Pattern
                                                             .quote(Objects.toString(f.values.get(0)));
                                                         Pattern   regEx        = Pattern.compile(".*" + escapedValue + ".*");
                                                         return (e) -> regEx
                                                             .matcher(Objects.toString(e.attributes.get(attribute)))
                                                             .find();
                                                     };
    public static final IComparator NOT_CONTAINS     = not(CONTAINS);

    public static final IComparator INTEGER_LESS     = (f) -> {
                                                         Attribute attribute   = f.attribute;
                                                         Number    filterValue = (Number) f.values.get(0);
                                                         return                                                                 //
                                                         (e) -> {
                                                             Number entityValue = (Number) e.attributes.get(attribute);
                                                             return entityValue.longValue() < filterValue.longValue();
                                                         };
                                                     };
    public static final IComparator NOT_INTEGER_LESS = not(INTEGER_LESS);

    public static final IComparator DOUBLE_LESS      = (f) -> {
                                                         Attribute attribute   = f.attribute;
                                                         Number    filterValue = (Number) f.values.get(0);
                                                         return                                                                 //
                                                         (e) -> {
                                                             Number entityValue = (Number) e.attributes.get(attribute);
                                                             return entityValue.longValue() < filterValue.longValue();
                                                         };
                                                     };
    public static final IComparator NOT_DOUBLE_LESS  = not(DOUBLE_LESS);

    //    LESS_THEN,NOT_LESS,MORE_THEN,NOT_MORE,BETWEEN,OUTSIDE,EMPTY,NOT_EMPTY,

    public static IComparator not(IComparator comp) {
        return (f) -> {
            Predicate<Entity> straight = comp.getPredicate(f);
            return (e) -> !straight.test(e);
        };
    }

}
