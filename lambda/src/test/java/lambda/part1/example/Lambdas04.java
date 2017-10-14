package lambda.part1.example;

import data.Person;
import org.junit.Test;

@SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
public class Lambdas04 {

    private void runFromCurrentThread(Runnable runnable) {
        runnable.run();
    }

    @Test
    public void closure() {
        Person person = new Person("John", "Galt", 33);

        runFromCurrentThread(new Runnable() {

            @Override
            public void run() {
                person.print();
            }
        });
    }


    @Test
    public void closure_lambda() {
        Person person = new Person("John", "Galt", 33);

        // statement lambda
        runFromCurrentThread(() -> {
            System.out.println("Before print");
            person.print();
        });
        // expression lambda
        runFromCurrentThread(() -> person.print());
        // method reference
        runFromCurrentThread(person::print);
    }

    private Person _person;

    public Person getPerson() {
        return _person;
    }

    static void staticMethod() {
        Runnable run = () -> {
            System.out.println();
        };
    }

    void nonStaticMethod() {
        Runnable run = () -> {
            System.out.println(this);
        };
    }

    @Test
    public void closure_this_lambda() {
        _person = new Person("John", "Galt", 33);

        runFromCurrentThread(new Runnable() {

            private final Lambdas04 nestedReference = Lambdas04.this;

            @Override
            public void run() {
                nestedReference._person.print();
            }
        });
        runFromCurrentThread(() -> this._person.print()); // GC Problems



        runFromCurrentThread(new Runnable() {

            private final Person nestedReference = Lambdas04.this._person;

            @Override
            public void run() {
                nestedReference.print();
            }
        });
        runFromCurrentThread(this._person::print);

        _person = new Person("a", "a", 1);

        runFromCurrentThread(() -> /*this.*/_person.print()); // GC Problems
        runFromCurrentThread(/*this.*/_person::print);
    }


    private Runnable runLaterFromCurrentThread(Runnable runnable) {
        return () -> {
            System.out.println("before run");
            runnable.run();
        };
    }

    @Test
    public void closure_this_lambda2() {
        _person = new Person("John", "Galt", 33);

        Runnable r1 = runLaterFromCurrentThread(() -> this._person.print());
        Runnable r2 = runLaterFromCurrentThread(this._person::print);
        Runnable r3 = runLaterFromCurrentThread(this.getPerson()::print);


        _person = new Person("a", "a", 1);

        r1.run();
        r2.run();
        r3.run();
    }
}
