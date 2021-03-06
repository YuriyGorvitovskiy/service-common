package org.service.common.config;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class Configuration_UnitTest {

    public class BaseConfig {
        public String  stringvalue = "default";
        public Boolean boolvalue;
    }

    public class TopConfig extends BaseConfig {
        public Byte    bytevalue   = 1;
        public Short   shortvalue  = 2;
        public Integer intvalue    = 3;
        public Long    longvalue   = 4L;
        public Float   floatvalue  = 5.0F;
        public Double  doublevalue = 6.0;

        public final Map<String, String> map = new HashMap<String, String>();

        public final BottomConfig bottom = new BottomConfig();

        public transient String transientfield = "transient";
        protected String        protectedfield = "protected";
        String                  packagefield   = "package";
    }

    public static class BottomConfig {
        public boolean boolvalue;
        public byte    bytevalue;
        public short   shortvalue;
        public int     intvalue;
        public long    longvalue;
        public float   floatvalue;
        public double  doublevalue;

        public static String staticfield = "static";
    }

    @Test
    void extract_string() {
        // Setup
        Properties props = new Properties();
        props.setProperty("org.config.test.stringvalue", "string-value");
        props.setProperty("org.config.test.boolvalue", "true");
        props.setProperty("org.config.test.bytevalue", "123");
        props.setProperty("org.config.test.shortvalue", "1234");
        props.setProperty("org.config.test.intvalue", "12345");
        props.setProperty("org.config.test.longvalue", "1234567890");
        props.setProperty("org.config.test.floatvalue", "12.34");
        props.setProperty("org.config.test.doublevalue", "1234.567");
        props.setProperty("org.config.test.map.simple-string", "simple-string");
        props.setProperty("org.config.test.map.complex.long", "complex-string");
        props.setProperty("org.config.test.bottom.boolvalue", "true");
        props.setProperty("org.config.test.bottom.bytevalue", "123");
        props.setProperty("org.config.test.bottom.shortvalue", "1234");
        props.setProperty("org.config.test.bottom.intvalue", "12345");
        props.setProperty("org.config.test.bottom.longvalue", "1234567890");
        props.setProperty("org.config.test.bottom.floatvalue", "12.34");
        props.setProperty("org.config.test.bottom.doublevalue", "1234.567");

        props.setProperty("org.config.test.transientfield", "something else");
        props.setProperty("org.config.test.protectedfield", "something else");
        props.setProperty("org.config.test.packagefield", "something else");
        props.setProperty("org.config.test.bottom.staticfield", "something else");

        // Execute
        TopConfig config = Configurator.extract(props, "org.config.test", new TopConfig());

        // Validate
        assertEquals("string-value", config.stringvalue);
        assertEquals(Boolean.TRUE, config.boolvalue);
        assertEquals(Byte.valueOf((byte) 123), config.bytevalue);
        assertEquals(Short.valueOf((short) 1234), config.shortvalue);
        assertEquals(Integer.valueOf(12345), config.intvalue);
        assertEquals(Long.valueOf(1234567890L), config.longvalue);
        assertEquals(Float.valueOf(12.34F), config.floatvalue);
        assertEquals(Double.valueOf(1234.567), config.doublevalue);

        Map<String, String> expectMap = new HashMap<>();
        expectMap.put("simple-string", "simple-string");
        expectMap.put("complex.long", "complex-string");
        assertEquals(expectMap, config.map);

        assertEquals(true, config.bottom.boolvalue);
        assertEquals((byte) 123, config.bottom.bytevalue);
        assertEquals((short) 1234, config.bottom.shortvalue);
        assertEquals(12345, config.bottom.intvalue);
        assertEquals(1234567890L, config.bottom.longvalue);
        assertEquals(12.34F, config.bottom.floatvalue, 0.00001);
        assertEquals(1234.567, config.bottom.doublevalue, 0.00001);

        assertEquals("transient", config.transientfield);
        assertEquals("protected", config.protectedfield);
        assertEquals("package", config.packagefield);
        assertEquals("static", BottomConfig.staticfield);
    }

    @Test
    void extract_objects() {
        // Setup
        Properties props = new Properties();
        props.put("org.config.test.stringvalue", "string-value");
        props.put("org.config.test.boolvalue", true);
        props.put("org.config.test.bytevalue", 123);
        props.put("org.config.test.shortvalue", 1234);
        props.put("org.config.test.intvalue", 12345);
        props.put("org.config.test.longvalue", 1234567890L);
        props.put("org.config.test.floatvalue", 12.34);
        props.put("org.config.test.doublevalue", 1234.567);
        props.put("org.config.test.map.simple-string", "simple-string");
        props.put("org.config.test.map.complex.long", "complex-string");
        props.put("org.config.test.bottom.boolvalue", true);
        props.put("org.config.test.bottom.bytevalue", 123);
        props.put("org.config.test.bottom.shortvalue", 1234);
        props.put("org.config.test.bottom.intvalue", 12345);
        props.put("org.config.test.bottom.longvalue", 1234567890);
        props.put("org.config.test.bottom.floatvalue", 12.34);
        props.put("org.config.test.bottom.doublevalue", 1234.567);

        // Execute
        TopConfig config = Configurator.extract(props, "org.config.test", new TopConfig());

        // Validate
        assertEquals("string-value", config.stringvalue);
        assertEquals(Boolean.TRUE, config.boolvalue);
        assertEquals(Byte.valueOf((byte) 123), config.bytevalue);
        assertEquals(Short.valueOf((short) 1234), config.shortvalue);
        assertEquals(Integer.valueOf(12345), config.intvalue);
        assertEquals(Long.valueOf(1234567890L), config.longvalue);
        assertEquals(Float.valueOf(12.34F), config.floatvalue);
        assertEquals(Double.valueOf(1234.567), config.doublevalue);

        Map<String, String> expectMap = new HashMap<>();
        expectMap.put("simple-string", "simple-string");
        expectMap.put("complex.long", "complex-string");
        assertEquals(expectMap, config.map);

        assertEquals(true, config.bottom.boolvalue);
        assertEquals((byte) 123, config.bottom.bytevalue);
        assertEquals((short) 1234, config.bottom.shortvalue);
        assertEquals(12345, config.bottom.intvalue);
        assertEquals(1234567890L, config.bottom.longvalue);
        assertEquals(12.34F, config.bottom.floatvalue, 0.00001);
        assertEquals(1234.567, config.bottom.doublevalue, 0.00001);
    }

    @Test
    void extract_default() {
        // Execute
        Properties props = new Properties();
        props.setProperty("org.config.test.intvalue", "wrong value");

        TopConfig config = Configurator.extract(props, "org.config.test", new TopConfig());

        // Validate
        assertEquals("default", config.stringvalue);
        assertEquals(null, config.boolvalue);
        assertEquals(Byte.valueOf((byte) 1), config.bytevalue);
        assertEquals(Short.valueOf((short) 2), config.shortvalue);
        assertEquals(Integer.valueOf(3), config.intvalue);
        assertEquals(Long.valueOf(4L), config.longvalue);
        assertEquals(Float.valueOf(5F), config.floatvalue);
        assertEquals(Double.valueOf(6.0), config.doublevalue);

        assertEquals(Collections.EMPTY_MAP, config.map);

        assertEquals(false, config.bottom.boolvalue);
        assertEquals((byte) 0, config.bottom.bytevalue);
        assertEquals((short) 0, config.bottom.shortvalue);
        assertEquals(0, config.bottom.intvalue);
        assertEquals(0L, config.bottom.longvalue);
        assertEquals(0F, config.bottom.floatvalue, 0.00001);
        assertEquals(0.0, config.bottom.doublevalue, 0.00001);
    }
}
