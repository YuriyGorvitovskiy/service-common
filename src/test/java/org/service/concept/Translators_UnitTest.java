package org.service.concept;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class Translators_UnitTest {

    @Test
    public void test() {
        assertSame(Translators.FORCE_BOOLEAN_TO_BOOLEAN, Translators.toJson(Primitive.BOOLEAN));
    }

}
