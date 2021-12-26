package com.project.azul;

import com.project.azul.models.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({GameTest.class,
        PickTest.class,
        ScoreTest.class,
        StateTests.class,
        PickExceptionTest.class,
        RegisterTest.class})

public class UnitTests {
}
