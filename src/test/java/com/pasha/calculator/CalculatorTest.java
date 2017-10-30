package com.pasha.calculator;

import com.sun.corba.se.spi.orb.StringPair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    @Test
    public void testCalculate() {

        List<StringPair> expressionAndResult = new ArrayList<>();

        expressionAndResult.add(new StringPair("(2+2*(3-1+5))", "16"));

        expressionAndResult.add(new StringPair(".2+2.", "2.2"));
        expressionAndResult.add(new StringPair("2-2", "0"));
        expressionAndResult.add(new StringPair("2*2", "4"));
        expressionAndResult.add(new StringPair("2/2.2", "0.9091"));
        expressionAndResult.add(new StringPair("-2/2", "-1.0000"));
        expressionAndResult.add(new StringPair("-2/-2", "1.0000"));
        expressionAndResult.add(new StringPair("  2   /    2  ", "1.0000"));
        expressionAndResult.add(new StringPair("1.5+1.4", "2.9"));
        expressionAndResult.add(new StringPair("2.2*3.3", "7.26"));
        expressionAndResult.add(new StringPair("2.285566655 * 3.42432423", "7.82652127599655065"));
        expressionAndResult.add(new StringPair("1111111111111111111111111111111111111111+222222222222222", "1111111111111111111111111333333333333333"));
        expressionAndResult.add(new StringPair("(2+2)", "4"));


        for (StringPair pair: expressionAndResult) {
            String expected = pair.getSecond();
            String actual = String.valueOf(Calculator.calculate(pair.getFirst()));
            assertEquals(expected, actual);
        }

        // Check division by zero
        expressionAndResult.add(new StringPair("1  /  0", null));
        StringPair pair = expressionAndResult.get(expressionAndResult.size()-1);
        Calculator.calculate(pair.getFirst());

        // Check unknown format
        expressionAndResult.add(new StringPair("(1-2))-1", null));
        pair = expressionAndResult.get(expressionAndResult.size()-1);
        Calculator.calculate(pair.getFirst());

        expressionAndResult.add(new StringPair("(1-2)-1..", null));
        pair = expressionAndResult.get(expressionAndResult.size()-1);
        Calculator.calculate(pair.getFirst());
    }
}