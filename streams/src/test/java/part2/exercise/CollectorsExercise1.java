package part2.exercise;

import data.Employee;
import data.JobHistoryEntry;
import data.Person;
import javafx.util.Pair;
import jdk.nashorn.internal.objects.annotations.Function;
import org.junit.Test;
import part1.example.StreamsExample;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

public class CollectorsExercise1 {

    private static class PersonPositionDuration {
        private final Person person;
        private final String position;
        private final int duration;

        public PersonPositionDuration(Person person, String position, int duration) {
            this.person = person;
            this.position = position;
            this.duration = duration;
        }

        public Person getPerson() {
            return person;
        }

        public String getPosition() {
            return position;
        }

        public int getDuration() {
            return duration;
        }
    }

    private static class PersonEmploer{
        private final String person;
        private final String employer;

        public PersonEmploer(Person person, String employer) {
            this.person = person.getFirstName()+" "+person.getLastName()+" "+person.getAge();
            this.employer = employer;
        }

        public String getPerson() {
            return person;
        }

        public String getEmployer() {
            return employer;
        }
    }


    // "epam" -> "Alex Ivanov 23, Semen Popugaev 25, Ivan Ivanov 33"
    @Test
    public void getEmployeesByEmployer() {
        Stream<PersonEmploer> personEmploerStream=getEmployees().stream()
                .flatMap(e->e.getJobHistory().stream()
                .map(j->new PersonEmploer(e.getPerson(),j.getEmployer())));

       // personEmploerStream.forEach(e-> System.out.println(e.getEmployer()+" "+e.getPerson()));

        Map<String, String> result =personEmploerStream.collect(groupingBy(PersonEmploer::getEmployer,
                mapping(PersonEmploer::getPerson,joining(","))));
        for (Map.Entry entry: result.entrySet()) {
            System.out.println(entry.getKey()+"->"+entry.getValue());
        }

    }

    @Test
    public void getTheCoolestOne() {
        Map<String, Person> coolestByPosition = getCoolestByPosition(getEmployees());

        coolestByPosition.forEach((position, person) -> System.out.println(position + " -> " + person));
    }

    private Map<String, Person> getCoolestByPosition(List<Employee> employees) {

        // First option
        // Collectors.maxBy
        // Collectors.collectingAndThen
        // Collectors.groupingBy
        Map<String,Person> coolestByPosition=employees.stream()
                .flatMap(e->e.getJobHistory().stream()
                .map(p->new PersonPositionDuration(e.getPerson(),p.getPosition(),p.getDuration())))
                .collect(groupingBy(PersonPositionDuration::getPosition,
                        collectingAndThen(maxBy(comparing(PersonPositionDuration::getDuration)),p -> p.isPresent() ? p.get().getPerson() : null)));



        Map<Person,JobHistoryEntry> map=new TreeMap<>();
           //  employees.stream()
                   //  .flatMap()
                    // .collect(toMap(Employee::getPerson,Employee::getJobHistory)).entrySet().stream()
                    /* .forEach(e->{
                         e.getValue().forEach(n->map.put(e.getKey(),n));
                         System.out.println(e.getValue().size());
                     });*/
        Comparator<JobHistoryEntry> comparator = (e1,e2) -> Integer.compare(e1.getDuration(),e2.getDuration());
        Map<String,Person> result = employees.stream()
                .flatMap(e -> e.getJobHistory().stream().map(h -> new Pair<>(e,h)))
                .collect(Collectors.groupingBy(p -> p.getValue().getPosition(),Collectors.collectingAndThen(
                        Collectors.maxBy((p1,p2) -> comparator.compare(p1.getValue(),p2.getValue())),p -> p.isPresent()? p.get().getKey().getPerson() : null)));

        System.out.println("result:");
        System.out.println(result);
        for (Map.Entry entry:map.entrySet()) {
            System.out.println(entry.getKey().toString()+" "+entry.getValue().toString());
        }
        System.out.println();

              //        .
                        //.max()

        System.out.println(coolestByPosition);

        // Second option
        // Collectors.toMap
        // iterate twice: stream...collect(...).stream()...
        // TODO
        return coolestByPosition;
    }

    private List<Employee> getEmployees() {
        return Arrays.asList(
                new Employee(
                        new Person("John", "Galt", 20),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(2, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 21),
                        Arrays.asList(
                                new JobHistoryEntry(4, "BA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 22),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 23),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(2, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 24),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "BA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 25),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 26),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("Bob", "Doe", 27),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(2, "dev", "abc")
                        )),
                new Employee(
                        new Person("John", "White", 28),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "BA", "epam")
                        )),
                new Employee(
                        new Person("John", "Galt", 29),
                        Arrays.asList(
                                new JobHistoryEntry(3, "dev", "epam"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("John", "Doe", 30),
                        Arrays.asList(
                                new JobHistoryEntry(4, "QA", "yandex"),
                                new JobHistoryEntry(2, "QA", "epam"),
                                new JobHistoryEntry(5, "dev", "abc")
                        )),
                new Employee(
                        new Person("Bob", "White", 31),
                        Collections.singletonList(
                                new JobHistoryEntry(6, "QA", "epam")
                        ))
        );
    }

}
