import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Arrays;

public class SILab2Test {

    private Item createItem(String name, int quantity, int price, double discount) {
        return new Item(name, quantity, price, discount);
    }

    @Test
    public void testEveryStatement() {
        // Тест со null листа
        Exception ex1 = assertThrows(RuntimeException.class, () -> SILab2.checkCart(null, "1234567890123456"));
        assertEquals("allItems list can't be null!", ex1.getMessage());

        // Тест со артикл со null име
        Exception ex2 = assertThrows(RuntimeException.class, () -> SILab2.checkCart(
                Arrays.asList(createItem(null, 1, 100, 0)), "1234567890123456"));
        assertEquals("Invalid item!", ex2.getMessage());

        // Тест со артикл со празно име
        Exception ex3 = assertThrows(RuntimeException.class, () -> SILab2.checkCart(
                Arrays.asList(createItem("", 1, 100, 0)), "1234567890123456"));
        assertEquals("Invalid item!", ex3.getMessage());

        // Тест со валиден артикл без попуст и без тригер за -30
        double r1 = SILab2.checkCart(Arrays.asList(createItem("Item1", 5, 100, 0)), "1234567890123456");
        assertEquals(500, r1, 0.001);

        // Тест со валиден артикл со попуст и цена > 300 (тригер за -30)
        double r2 = SILab2.checkCart(Arrays.asList(createItem("Item2", 5, 350, 0.1)), "1234567890123456");
        assertEquals(1545, r2, 0.001);

        // Тест со валиден артикл со quantity > 10 (тригер за -30)
        double r3 = SILab2.checkCart(Arrays.asList(createItem("Item3", 15, 100, 0)), "1234567890123456");
        assertEquals(1470, r3, 0.001);

        // Тест со празна листа
        double r4 = SILab2.checkCart(Arrays.asList(), "1234567890123456");
        assertEquals(0, r4, 0.001);

        // Тест со null card number
        Exception ex4 = assertThrows(RuntimeException.class, () -> SILab2.checkCart(
                Arrays.asList(createItem("Apple", 1, 100, 0)), null));
        assertEquals("Invalid card number!", ex4.getMessage());

        // Тест со краток card number
        Exception ex5 = assertThrows(RuntimeException.class, () -> SILab2.checkCart(
                Arrays.asList(createItem("Apple", 1, 100, 0)), "123"));
        assertEquals("Invalid card number!", ex5.getMessage());

        // Тест со карактер кој не е број во card number
        Exception ex6 = assertThrows(RuntimeException.class, () -> SILab2.checkCart(
                Arrays.asList(createItem("Apple", 1, 100, 0)), "12345678901234A6"));
        assertEquals("Invalid character in card number!", ex6.getMessage());
    }

    @Test
    public void testMultipleCondition() {
        // 8 комбинации за price > 300 || discount > 0 || quantity > 10
        // (A=price>300, B=discount>0, C=quantity>10)

        // 000: price<=300, discount=0, quantity<=10 -> false, нема -30
        double sum0 = SILab2.checkCart(Arrays.asList(createItem("I0", 5, 100, 0)), "1234567890123456");
        assertEquals(500, sum0, 0.001);

        // 001: price<=300, discount=0, quantity>10 -> true, има -30
        double sum1 = SILab2.checkCart(Arrays.asList(createItem("I1", 15, 100, 0)), "1234567890123456");
        assertEquals(1470, sum1, 0.001);

        // 010: price<=300, discount>0, quantity<=10 -> true, има -30
        double sum2 = SILab2.checkCart(Arrays.asList(createItem("I2", 5, 100, 0.2)), "1234567890123456");
        assertEquals(370, sum2, 0.001);

        // 011: price<=300, discount>0, quantity>10 -> true, има -30
        double sum3 = SILab2.checkCart(Arrays.asList(createItem("I3", 20, 100, 0.1)), "1234567890123456");
        assertEquals(1770, sum3, 0.001);

        // 100: price>300, discount=0, quantity<=10 -> true, има -30
        double sum4 = SILab2.checkCart(Arrays.asList(createItem("I4", 5, 400, 0)), "1234567890123456");
        assertEquals(1970, sum4, 0.001);

        // 101: price>300, discount=0, quantity>10 -> true, има -30
        double sum5 = SILab2.checkCart(Arrays.asList(createItem("I5", 15, 400, 0)), "1234567890123456");
        assertEquals(5970, sum5, 0.001);

        // 110: price>300, discount>0, quantity<=10 -> true, има -30
        double sum6 = SILab2.checkCart(Arrays.asList(createItem("I6", 5, 400, 0.1)), "1234567890123456");
        assertEquals(1770, sum6, 0.001);

        // 111: price>300, discount>0, quantity>10 -> true, има -30
        double sum7 = SILab2.checkCart(Arrays.asList(createItem("I7", 15, 400, 0.1)), "1234567890123456");
        assertEquals(5370, sum7, 0.001);
    }
}
