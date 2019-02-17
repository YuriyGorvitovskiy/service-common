package org.service.concept;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Translators {

    public static final DateTimeFormatter                      INSTANT_FORMATTER         =                          //
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);

    public static final ITranslator<Object, Object>            TRIVIAL                   =                          //
            (s) -> s;

    public static final ITranslator<Object, String>            ANY_TO_STRING             =                          //
            (s) -> null == s ? null : String.valueOf(s);

    public static final ITranslator<Boolean, Boolean>          BOOLEAN_TO_BOOLEAN        =                          //
            (s) -> s;

    public static final ITranslator<Boolean, String>           BOOLEAN_TO_STRING         =                          //
            (s) -> null == s ? null : s.toString();

    public static final ITranslator<String, Boolean>           STRING_TO_BOOLEAN         =                          //
            (s) -> null == s ? null : Boolean.valueOf(s);

    public static final ITranslator<Long, Number>              LONG_TO_NUMBER            =                          //
            (s) -> s;

    public static final ITranslator<Number, Long>              NUMBER_TO_LONG            =                          //
            (s) -> null == s ? null : s.longValue();

    public static final ITranslator<Long, String>              LONG_TO_STRING            =                          //
            (s) -> null == s ? null : s.toString();

    public static final ITranslator<String, Long>              STRING_TO_LONG            =                          //
            (s) -> null == s ? null : Long.valueOf(s);

    public static final ITranslator<Double, Number>            DOUBLE_TO_NUMBER          =                          //
            (s) -> s;

    public static final ITranslator<Number, Double>            NUMBER_TO_DOUBLE          =                          //
            (s) -> null == s ? null : s.doubleValue();

    public static final ITranslator<Double, String>            DOUBLE_TO_STRING          =                          //
            (s) -> null == s ? null : s.toString();

    public static final ITranslator<String, Double>            STRING_TO_DOUBLE          =                          //
            (s) -> null == s ? null : Double.valueOf(s);

    public static final ITranslator<String, String>            STRING_TO_STRING          =                          //
            (s) -> s;

    public static final ITranslator<Instant, String>           INSTANT_TO_STRING         =                          //
            (s) -> null == s ? null : INSTANT_FORMATTER.format(s);

    public static final ITranslator<String, Instant>           STRING_TO_INSTANT         =                          //
            (s) -> null == s ? null : OffsetDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME).toInstant();

    public static final ITranslator<Primitive, String>         PRIMITIVE_TO_STRING       =                          //
            (s) -> null == s ? null : s.toString();

    public static final ITranslator<String, Primitive>         STRING_TO_PRIMITIVE       =                          //
            (s) -> null == s ? null : Primitive.valueOf(s);

    public static final ITranslator<Object, Boolean>           FORCE_BOOLEAN_TO_BOOLEAN  =                          //
            (s) -> (Boolean) s;

    public static final ITranslator<Object, String>            FORCE_BOOLEAN_TO_STRING   =                          //
            (s) -> null == s ? null : ((Boolean) s).toString();

    public static final ITranslator<Object, Boolean>           FORCE_STRING_TO_BOOLEAN   =                          //
            (s) -> null == s ? null : Boolean.valueOf((String) s);

    public static final ITranslator<Object, Number>            FORCE_LONG_TO_NUMBER      =                          //
            (s) -> (Long) s;

    public static final ITranslator<Object, Long>              FORCE_NUMBER_TO_LONG      =                          //
            (s) -> null == s ? null : ((Number) s).longValue();

    public static final ITranslator<Object, String>            FORCE_LONG_TO_STRING      =                          //
            (s) -> null == s ? null : ((Long) s).toString();

    public static final ITranslator<Object, Long>              FORCE_STRING_TO_LONG      =                          //
            (s) -> null == s ? null : Long.valueOf((String) s);

    public static final ITranslator<Object, Number>            FORCE_DOUBLE_TO_NUMBER    =                          //
            (s) -> (Double) s;

    public static final ITranslator<Object, Double>            FORCE_NUMBER_TO_DOUBLE    =                          //
            (s) -> null == s ? null : ((Number) s).doubleValue();

    public static final ITranslator<Object, String>            FORCE_DOUBLE_TO_STRING    =                          //
            (s) -> null == s ? null : ((Double) s).toString();

    public static final ITranslator<Object, Double>            FORCE_STRING_TO_DOUBLE    =                          //
            (s) -> null == s ? null : Double.valueOf((String) s);

    public static final ITranslator<Object, String>            FORCE_INSTANT_TO_STRING   =                          //
            (s) -> null == s ? null : INSTANT_FORMATTER.format((Instant) s);

    public static final ITranslator<Object, Instant>           FORCE_STRING_TO_INSTANT   =                          //
            (s) -> null == s ? null : OffsetDateTime.parse((String) s, DateTimeFormatter.ISO_DATE_TIME).toInstant();

    public static final ITranslator<Object, String>            FORCE_PRIMITIVE_TO_STRING =                          //
            (s) -> null == s ? null : ((Primitive) s).toString();

    public static final ITranslator<Object, Primitive>         FORCE_STRING_TO_PRIMITIVE =                          //
            (s) -> null == s ? null : Primitive.valueOf((String) s);

    public static final ITranslator<Object, String>            FORCE_STRING_TO_STRING    =                          //
            (s) -> (String) s;

    static final Primitive.Access<ITranslator<Object, ?>>      TO_JSON                   =                          //
            new Primitive.Access<ITranslator<Object, ?>>() {
                {
                    this.associate(Primitive.BOOLEAN, Translators.FORCE_BOOLEAN_TO_BOOLEAN)
                        .associate(Primitive.INTEGER, Translators.FORCE_LONG_TO_NUMBER)
                        .associate(Primitive.DOUBLE, Translators.FORCE_DOUBLE_TO_NUMBER)
                        .associate(Primitive.UUID, Translators.FORCE_STRING_TO_STRING)
                        .associate(Primitive.STRING, Translators.FORCE_STRING_TO_STRING)
                        .associate(Primitive.TEXT, Translators.FORCE_STRING_TO_STRING)
                        .associate(Primitive.MOMENT, Translators.FORCE_INSTANT_TO_STRING)
                        .associate(Primitive.PRIMITIVE, Translators.FORCE_PRIMITIVE_TO_STRING);
                }
            };

    static final Primitive.Access<ITranslator<Object, ?>>      FROM_JSON                 =                          //
            new Primitive.Access<ITranslator<Object, ?>>() {
                {
                    this.associate(Primitive.BOOLEAN, Translators.FORCE_BOOLEAN_TO_BOOLEAN)
                        .associate(Primitive.INTEGER, Translators.FORCE_NUMBER_TO_LONG)
                        .associate(Primitive.DOUBLE, Translators.FORCE_NUMBER_TO_DOUBLE)
                        .associate(Primitive.UUID, Translators.FORCE_STRING_TO_STRING)
                        .associate(Primitive.STRING, Translators.FORCE_STRING_TO_STRING)
                        .associate(Primitive.TEXT, Translators.FORCE_STRING_TO_STRING)
                        .associate(Primitive.MOMENT, Translators.FORCE_STRING_TO_INSTANT)
                        .associate(Primitive.PRIMITIVE, Translators.FORCE_STRING_TO_PRIMITIVE);
                }
            };

    static final Primitive.Access<ITranslator<Object, String>> TO_XML                    =                          //
            new Primitive.Access<ITranslator<Object, String>>() {
                {
                    this.associate(Primitive.BOOLEAN, Translators.FORCE_BOOLEAN_TO_STRING)
                        .associate(Primitive.INTEGER, Translators.FORCE_LONG_TO_STRING)
                        .associate(Primitive.DOUBLE, Translators.FORCE_DOUBLE_TO_STRING)
                        .associate(Primitive.UUID, Translators.FORCE_STRING_TO_STRING)
                        .associate(Primitive.STRING, Translators.FORCE_STRING_TO_STRING)
                        .associate(Primitive.TEXT, Translators.FORCE_STRING_TO_STRING)
                        .associate(Primitive.MOMENT, Translators.FORCE_INSTANT_TO_STRING)
                        .associate(Primitive.PRIMITIVE, Translators.FORCE_PRIMITIVE_TO_STRING);
                }
            };

    static final Primitive.Access<ITranslator<String, ?>>      FROM_XML                  =                          //
            new Primitive.Access<ITranslator<String, ?>>() {
                {
                    this.associate(Primitive.BOOLEAN, Translators.STRING_TO_BOOLEAN)
                        .associate(Primitive.INTEGER, Translators.STRING_TO_LONG)
                        .associate(Primitive.DOUBLE, Translators.STRING_TO_DOUBLE)
                        .associate(Primitive.UUID, Translators.STRING_TO_STRING)
                        .associate(Primitive.STRING, Translators.STRING_TO_STRING)
                        .associate(Primitive.TEXT, Translators.STRING_TO_STRING)
                        .associate(Primitive.MOMENT, Translators.STRING_TO_INSTANT)
                        .associate(Primitive.PRIMITIVE, Translators.STRING_TO_PRIMITIVE);
                }
            };

    protected static ITranslator<Object, ?> toJson(Primitive primitive) {
        return TO_JSON.get(primitive);
    }

    protected static ITranslator<Object, ?> fromJson(Primitive primitive) {
        return FROM_JSON.get(primitive);
    }

    protected static ITranslator<Object, String> toXml(Primitive primitive) {
        return TO_XML.get(primitive);
    }

    protected static ITranslator<String, ?> fromXml(Primitive primitive) {
        return FROM_XML.get(primitive);
    }

    @SuppressWarnings("unchecked")
    public static <T> ITranslator<T, T> trivial(T s) {
        return (ITranslator<T, T>) TRIVIAL;
    }
}
