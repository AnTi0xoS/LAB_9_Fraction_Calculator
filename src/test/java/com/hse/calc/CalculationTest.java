package com.hse.calc;

import org.junit.*;

import static org.junit.Assert.*;

public class CalculationTest {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Before CalculatorTest.class");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("After CalculatorTest.class");
    }

    @Test
    public void testGetAnswer01OneOperationSummation() {
        System.out.println("1/2 + 1/3 = 5/6");
        assertEquals("5/6", Calculation.getAnswer("1/2+1/3"));
    }

    @Test
    public void testGetAnswer02OneOperationSubstraction() {
        System.out.println("1/2 - 1/3 = 1/6");
        assertEquals("1/6", Calculation.getAnswer("1/2-1/3"));
    }

    @Test
    public void testGetAnswer03OneOperationMultiplication() {
        System.out.println("5/3 * 4/7 = 20/21");
        assertEquals("20/21", Calculation.getAnswer("5/3*4/7"));
    }

    @Test
    public void testGetAnswer04OneOperationDivision() {
        System.out.println("7/2 : 7/2 = 1");
        assertEquals("1", Calculation.getAnswer("7/2:7/2"));
    }

    @Test
    public void testGetAnswer05AllOperationsWithEqualPriority() {
        System.out.println("5/2 - 3/9 + 1/4 - 13/15 + 6/7 = 337/140");
        assertEquals("337/140", Calculation.getAnswer("5/2-3/9+1/4-13/15+6/7"));
    }

    @Test
    public void testGetAnswer06AllOperationsWithNotEqualPriority() {
        System.out.println("5/2 - 3/9 : 1/4 * 13/15 + 6/7 = 1387/630");
        assertEquals("1387/630", Calculation.getAnswer("5/2-3/9:1/4*13/15+6/7"));
    }

    @Test
    public void testGetAnswer07Brackets() {
        System.out.println("( 3/4 + 5/8 ) * 1/2 = 11/16");
        assertEquals("11/16", Calculation.getAnswer("(3/4+5/8)*1/2"));
    }

    @Test
    public void testGetAnswer08InternalBrackets() {
        System.out.println("(6/32 - ((3/4 + 5/8) * 1/2)) = -1/2");
        assertEquals("-1/2", Calculation.getAnswer("(6/32-((3/4+5/8)*1/2))"));
    }

    @Test
    public void testGetAnswer09EqualPriorityOfBrackets() {
        System.out.println("(3/4 + 5/8) * (34/16 - 15/55) : (1/2 * 7/5) = 815/224");
        assertEquals("815/224", Calculation.getAnswer("(3/4+5/8)*(34/16-15/55):(1/2*7/5)"));
    }

    @Test
    public void testGetAnswer10NegativeFractions() {
        System.out.println("-1/3 + 1/4 = -1/12");
        assertEquals("-1/12", Calculation.getAnswer("-1/3+1/4"));
    }

    @Test
    public void testGetAnswer11NegativeFractionInDenominator() {
        System.out.println("-45/-234 + 1/2 = 9/13");
        assertEquals("9/13", Calculation.getAnswer("-45/-234+1/2"));
    }

    @Test
    public void testGetAnswer12NegativeFractionsWithBrackets1() {
        System.out.println("( -45/-234 ) + 1/2 * 7/23 = 9/13");
        assertEquals("9/13", Calculation.getAnswer("-45/-234+1/2"));
    }

    @Test
    public void testGetAnswer13NegativeFractionsWithBrackets2() {
        System.out.println("5/6 + (-3/15) = 19/30");
        assertEquals("19/30", Calculation.getAnswer("5/6+(-3/15)"));
    }

    @Test
    public void testGetAnswer14FinalCorrectTest() {
        System.out.println("((-1/3*3/4-1/2)*2/1):(-1/-5+(-6/7))=105/46");
        assertEquals("105/46",
                Calculation.getAnswer("((-1/3*3/4-1/2)*2/1):(-1/-5+(-6/7))"));
    }

    @Test
    public void testGetAnswer15ZeroInDenominator() {
        System.out.println("45/0 + 56/234");
        assertNull(Calculation.getAnswer("45/0+56/234"));
    }

    @Test
    public void testGetAnswer16ZeroAfterDivision() {
        System.out.println("3/4 : 0/6");
        assertNull(Calculation.getAnswer("3/4 : 0/6"));
    }

    @Test
    public void testCheckUserInput01WrongInput() {
        System.out.println("something");
        assertNull(Calculation.checkUserInput("something"));
    }

    @Test
    public void testCheckUserInput02OperationAfterBrackets() {
        System.out.println("1/2 + (* 1/4)");
        assertNull(Calculation.checkUserInput("1/2+(*1/4)"));
    }

    @Test
    public void testCheckUserInput03DoubleOperation() {
        System.out.println("1/2 + - 1/4");
        assertNull(Calculation.checkUserInput("1/2+-1/4"));
    }

    @Test
    public void testCheckUserInput04WrongFraction() {
        System.out.println("1/2/3 + 4/5");
        assertNull(Calculation.checkUserInput("1/2/3+4/5"));
    }

    @Test
    public void testCheckUserInput05DoubleFraction() {
        System.out.println("1/2 4/5 - 3/7");
        assertNull(Calculation.checkUserInput("1/2 4/5-3/7"));
    }
}