package org.service.concept;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class Translators_UnitTest {

    @Test
    public void test() {
        assertSame(Translators.FORCE_BOOLEAN_TO_BOOLEAN, Translators.toJson(Primitive.BOOLEAN));
    }

}
