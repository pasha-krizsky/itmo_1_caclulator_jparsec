package com.pasha.calculator;

import org.jparsec.OperatorTable;
import org.jparsec.Parser;
import org.jparsec.Scanners;
import org.jparsec.error.ParserException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.jparsec.Scanners.isChar;

public class Calculator {

    private static final int precision = 4;

    public static BigDecimal calculate(String expr) {

        BigDecimal result = null;

        try {
            result = parser().parse(expr);
        } catch (ParserException e) {

            if (e.getCause() instanceof IllegalArgumentException) {
                System.out.println(e.getCause().getMessage() + e.getLocation());
            } else {
                System.out.println("Unknown format: " + e.getLocation());
            }
        }

        return result;
    }

    static final Parser<BigDecimal> NUMBER = Scanners.DECIMAL.map(BigDecimal::new);

    static Parser<BigDecimal> parser() {

        Parser.Reference<BigDecimal> ref = Parser.newReference();
        Parser<BigDecimal> term = ref.lazy().between(isChar('('), isChar(')')).or(NUMBER);

        Parser<BigDecimal> parser = new OperatorTable<BigDecimal>()
                .prefix(isChar(' ').retn(a -> a), 40)
                .postfix(isChar(' ').retn(a -> a), 40)
                .prefix(isChar('-').retn(a -> a.negate()), 30)
                .infixl(isChar('+').retn((a, b) -> a.add(b)), 10)
                .infixl(isChar('-').retn((a, b) -> a.subtract(b)), 10)
                .infixl(isChar('*').retn((a, b) -> a.multiply(b)), 20)
                .infixl(isChar('/').retn( (a, b) -> {
                    if (b.equals(BigDecimal.ZERO)) {
                        throw new IllegalArgumentException("Cannot divide by zero");
                    }
                    return a.divide(b, precision, RoundingMode.HALF_UP);
                }), 20)
                .build(term);

        ref.set(parser);
        return parser;
    }
}
