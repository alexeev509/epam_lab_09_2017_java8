package part1.exercise;


import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.stream.StreamSupport;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;
    private final int endExclusive;
    private int startInclusive;

    public RectangleSpliterator(int[][] array, int startInclusive, int endExclusive) {
          // TODO
       super(endExclusive-startInclusive, Spliterator.IMMUTABLE
                         | Spliterator.ORDERED
                          | Spliterator.SIZED
                         | Spliterator.SUBSIZED
                          | Spliterator.NONNULL);
        this.array = array;
        this.startInclusive=startInclusive;
        this.endExclusive=endExclusive;
    }
    public RectangleSpliterator(int[][] array) {
        this(array,0, (int) checkArrayAndCalcEstimatedSize(array));
    }

    private static long checkArrayAndCalcEstimatedSize(int[][] array) {
        if(Objects.isNull(array))
            throw new RuntimeException("mass equals null");

        return array.length * array[0].length;
    }

    @Override
    public OfInt trySplit() {
        int length = endExclusive - startInclusive;
        if (length < 2) {
            return null;
        }

        int mid = startInclusive + length / 2;
        RectangleSpliterator result = new RectangleSpliterator(array, startInclusive, mid);
        startInclusive = mid;
        return result;
    }

    @Override
    public long estimateSize() {
        return endExclusive-startInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInclusive < endExclusive) {
            int value = array[(startInclusive / array[0].length)][(startInclusive % array[0].length)];
            ++startInclusive;
            action.accept(value);
            return true;
        }
        return false;
    }

    public static void main(String[] args)  {
        int[][]array=new int[][]{{1,2,3},{4,5,6},{7,8,9}};
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