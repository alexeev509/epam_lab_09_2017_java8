package part2.exercise;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class ZipWithIndexDoubleSpliterator extends Spliterators.AbstractSpliterator<IndexedDoublePair> {


    private final OfDouble inner;
    private int currentIndex;

    public ZipWithIndexDoubleSpliterator(OfDouble inner) {
        this(0, inner);
    }

    private ZipWithIndexDoubleSpliterator(int firstIndex, OfDouble inner) {
        super(inner.estimateSize(), inner.characteristics());
        currentIndex = firstIndex;
        this.inner = inner;
    }

    @Override
    public int characteristics() {
        // TODO
       return inner.characteristics();
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        // TODO
        return inner.tryAdvance((double p)->action.accept(new IndexedDoublePair(currentIndex++, p)));
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        // TODO
      inner.forEachRemaining((double p)->action.accept(new IndexedDoublePair(currentIndex++,p)));
    }

    //Dont ready
    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        // TODO
         if (inner.hasCharacteristics(Spliterator.SIZED| Spliterator.SUBSIZED)) {
         //  use inner.trySplit
             inner.trySplit();
             return null;
         } else {

             return super.trySplit();
         }
    }

    @Override
    public long estimateSize() {
       return inner.estimateSize();
    }

    public static void main(String[] args) {

       // StreamSupport.doubleStream(new ZipWithIndexDoubleSpliterator(0, Arrays.spliterator(new double[]{4.0,5.0})));
    }
}
