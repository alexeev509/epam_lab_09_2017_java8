package part1.exercise;

import part1.example.IntArraySpliterator;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.stream.StreamSupport;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;

    public RectangleSpliterator(int[][] array) {
      //  super(checkArrayAndCalcEstimatedSize(array), 0);       // TODO заменить
        //Size?? this is right??
       super(array.length*array[0].length, Spliterator.IMMUTABLE
                         | Spliterator.ORDERED
                          | Spliterator.SIZED
                         | Spliterator.SUBSIZED
                          | Spliterator.NONNULL);
        this.array = array;
    }

    private static long checkArrayAndCalcEstimatedSize(int[][] array) {
        // TODO

        return array.length * array[0].length;
    }

    @Override
    public OfInt trySplit() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        for(int i=0; i<array.length;i++)
            for (int j=0;j<array[0].length;j++)
                action.accept(array[i][j]);
        //throw new UnsupportedOperationException();
        return true;
    }

    public static void main(String[] args) {
        int[][]array=new int[][]{{1,2,3},{4,5,6}};
        long l= StreamSupport.intStream(new RectangleSpliterator(array), true)
                .asLongStream()
                .sum();
        System.out.println(l);
    }


}


class A {

    protected String val;

    A() {
        setVal();
    }

    public void setVal() {
        val = "A";
    }
}

class B extends A {

    @Override
    public void setVal() {
        val = "B";
    }

    public static void main(String[] args) {
        System.out.println(new B().val);

    }
}