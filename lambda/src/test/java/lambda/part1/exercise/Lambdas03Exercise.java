package lambda.part1.exercise;

import lambda.part1.example.Lambdas03;
import org.junit.Test;

import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class Lambdas03Exercise {

    private interface GenericProduct<T> {
        T prod(T a, int i);

        default T twice(T t) {
            return prod(t, 2);
        }
    }

    @Test
    public void generic0() {
        final GenericProduct<Integer> prod = new GenericProduct<Integer>() {
            @Override
            public Integer prod(Integer a, int i) {
                return a*i;
            }
        }; // Use anonymous class

        assertEquals(prod.prod(3, 2), Integer.valueOf(6));
    }

    @Test
    public void generic1() {
        final GenericProduct<Integer> prod =
                (a, i) ->{
            System.out.println("before multiplication");
            return a*i; };  // Use statement lambda

        assertEquals(prod.prod(3, 2), Integer.valueOf(6));
    }

    @Test
    public void generic2() {
        final GenericProduct<Integer> prod = (a,i)->a*i; // Use expression lambda

        assertEquals(prod.prod(3, 2), Integer.valueOf(6));
    }

    private static String stringProd(String s, int i) {
        final StringBuilder sb = new StringBuilder();
        for (int j = 0; j < i; j++) {
            sb.append(s);
        }
        return sb.toString();
    }

    @Test
    public void strSum() {
        final GenericProduct<String> prod = Lambdas03Exercise::stringProd; // use stringProd;

        assertEquals(prod.prod("a", 2), "aa");
    }

    private final String delimeter = "-";

    private String stringSumWithDelimeter(String s, int i) {
      //  final StringJoiner sj = new StringJoiner(delimeter);
        Stream.Builder<String> stream = Stream.builder();
        for (int j = 0; j < i; j++) {
           stream.add(s);
           if(j!=i-1)
               stream.add(delimeter);
        }
        return stream.build().collect(Collectors.joining());
    }

    @Test
    public void strSum2() {
        final GenericProduct<String> prod = this::stringSumWithDelimeter; // use stringSumWithDelimeter;

        assertEquals(prod.prod("a", 3), "a-a-a");
    }


}
