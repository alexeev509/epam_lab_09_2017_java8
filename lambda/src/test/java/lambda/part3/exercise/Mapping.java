package lambda.part3.exercise;

import data.Employee;
import data.JobHistoryEntry;
import data.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"WeakerAccess"})
public class Mapping {

    private static class MapHelper<T> {

        private final List<T> list;

        public MapHelper(List<T> list) {
            this.list = list;
        }

        public List<T> getList() {
            return list;
        }

        // ([T], T -> R) -> [R]
        public <R> MapHelper<R> map(Function<T, R> f) {
            // TODO
           // throw new UnsupportedOperationException();
            List<R> newList=new ArrayList<>();
            for(int i=0; i<this.list.size();i++){
                newList.add(f.apply(list.get(i)));
            }
            return new MapHelper(newList);
        }

        // ([T], T -> [R]) -> [R]
        public <R> MapHelper<R> flatMap(Function<T, List<R>> f) {
            List<R> newList=new ArrayList<>();
            for(int i=0; i<this.list.size();i++)
                newList.addAll(f.apply(list.get(i)));
           return new MapHelper(newList);
            //throw new UnsupportedOperationException();
        }
    }

    @Test
    public void mapping() {
        List<Employee> employees = Arrays.asList(
            new Employee(new Person("a", "Galt", 30),
                Arrays.asList(
                        new JobHistoryEntry(2, "dev", "epam"),
                        new JobHistoryEntry(1, "dev", "google")
                )),
            new Employee(new Person("b", "Doe", 40),
                Arrays.asList(
                        new JobHistoryEntry(3, "qa", "yandex"),
                        new JobHistoryEntry(1, "qa", "epam"),
                        new JobHistoryEntry(1, "dev", "abc")
                )),
            new Employee(new Person("c", "White", 50),
                Collections.singletonList(
                        new JobHistoryEntry(5, "qa", "epam")
                ))
        );

        List<Employee> mappedEmployees = new MapHelper<>(employees)
                .map(e -> e.withPerson(e.getPerson().withFirstName("John")))
                .map(e -> e.withJobHistory(addOneYear(e.getJobHistory())))
                .map(e->e.withJobHistory(replaceQa(e.getJobHistory())))
                /*
                .map(TODO) // Изменить имя всех сотрудников на John .map(e -> e.withPerson(e.getPerson().withFirstName("John")))
                .map(TODO) // Добавить всем сотрудникам 1 год опыта .map(e -> e.withJobHistory(addOneYear(e.getJobHistory())))
                .map(TODO) // Заменить все qa на QA
                * */
                .getList();
                //My additional check
        for(int i=0; i<mappedEmployees.size();i++) {
            System.out.println(mappedEmployees.get(i).getPerson().getFirstName());
            for(int j=0; j<mappedEmployees.get(i).getJobHistory().size();j++){
                System.out.println(mappedEmployees.get(i).getJobHistory().get(j).getDuration());
            }
        }
              //
        List<Employee> expectedResult = Arrays.asList(
            new Employee(new Person("John", "Galt", 30),
                Arrays.asList(
                        new JobHistoryEntry(3, "dev", "epam"),
                        new JobHistoryEntry(2, "dev", "google")
                )),
            new Employee(new Person("John", "Doe", 40),
                Arrays.asList(
                        new JobHistoryEntry(4, "QA", "yandex"),
                        new JobHistoryEntry(2, "QA", "epam"),
                        new JobHistoryEntry(2, "dev", "abc")
                )),
            new Employee(new Person("John", "White", 50),
                Collections.singletonList(
                        new JobHistoryEntry(6, "QA", "epam")
                ))
        );

        assertEquals(mappedEmployees, expectedResult);
    }

    //I add this method
    private List<JobHistoryEntry> replaceQa(List<JobHistoryEntry> jobHistory) {
        for(int i=0; i<jobHistory.size();i++){
            if("qa".equals(jobHistory.get(i).getPosition()))
                jobHistory.set(i,jobHistory.get(i).withPosition("QA"));
        }
        return jobHistory;
    }
    //I add this method
    private List<JobHistoryEntry> addOneYear(List<JobHistoryEntry> jobHistory) {
        for(int i=0; i<jobHistory.size();i++)
            jobHistory.set(i,jobHistory.get(i).withDuration(jobHistory.get(i).getDuration() + 1));
        return jobHistory;
    }

    private static class LazyMapHelper<T, R> {

        private final List<T> list;
        private final Function<T, R> function;

        public LazyMapHelper(List<T> list, Function<T, R> function) {
            this.list=list;
            this.function=function;
        }

        public static <T> LazyMapHelper<T, T> from(List<T> list) {
            return new LazyMapHelper<>(list, Function.identity());
        }

        public List<R> force() {
            // TODO
            List<R> result = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                result.add(function.apply(list.get(i)));
            }
            return result;
            //throw new UnsupportedOperationException();
        }

        public <R2> LazyMapHelper<T, R2> map(Function<R, R2> f) {
            // TODO
            return new LazyMapHelper(list,function.andThen(f));
            //throw new UnsupportedOperationException();
        }
    }



    @Test
    public void lazyMapping() {
        List<Employee> employees = Arrays.asList(
            new Employee(
                new Person("a", "Galt", 30),
                Arrays.asList(
                        new JobHistoryEntry(2, "dev", "epam"),
                        new JobHistoryEntry(1, "dev", "google")
                )),
            new Employee(
                new Person("b", "Doe", 40),
                Arrays.asList(
                        new JobHistoryEntry(3, "qa", "yandex"),
                        new JobHistoryEntry(1, "qa", "epam"),
                        new JobHistoryEntry(1, "dev", "abc")
                )),
            new Employee(
                new Person("c", "White", 50),
                Collections.singletonList(
                        new JobHistoryEntry(5, "qa", "epam")
                ))
        );

        List<Employee> mappedEmployees = LazyMapHelper.from(employees)
                .map(e -> e.withPerson(e.getPerson().withFirstName("John")))
                .map(e -> e.withJobHistory(addOneYear(e.getJobHistory())))
                .map(e->e.withJobHistory(replaceQa(e.getJobHistory())))
                /*
                .map(TODO) // Изменить имя всех сотрудников на John .map(e -> e.withPerson(e.getPerson().withFirstName("John")))
                .map(TODO) // Добавить всем сотрудникам 1 год опыта .map(e -> e.withJobHistory(addOneYear(e.getJobHistory())))
                .map(TODO) // Заменить все qu на QA
                */
                .force();

        for(int i=0; i<mappedEmployees.size();i++) {
            System.out.println(mappedEmployees.get(i).getPerson().getFirstName());
            for(int j=0; j<mappedEmployees.get(i).getJobHistory().size();j++){
                System.out.println(mappedEmployees.get(i).getJobHistory().get(j).getDuration());
            }
        }

        List<Employee> expectedResult = Arrays.asList(
            new Employee(new Person("John", "Galt", 30),
                Arrays.asList(
                        new JobHistoryEntry(3, "dev", "epam"),
                        new JobHistoryEntry(2, "dev", "google")
                )),
            new Employee(new Person("John", "Doe", 40),
                Arrays.asList(
                        new JobHistoryEntry(4, "QA", "yandex"),
                        new JobHistoryEntry(2, "QA", "epam"),
                        new JobHistoryEntry(2, "dev", "abc")
                )),
            new Employee(new Person("John", "White", 50),
                Collections.singletonList(
                        new JobHistoryEntry(6, "QA", "epam")
                ))
        );

        assertEquals(mappedEmployees, expectedResult);
    }

    // TODO * LazyFlatMapHelper
    private static class LazyFlatMapHelper <T,R>{
        private final List<T> list;
        private final Function<T, R> function;

        private LazyFlatMapHelper(List<T> list, Function<T, R> function) {
            this.list = list;
            this.function = function;
        }

        public List<R> force() {
            //will realize
            return null;
        }

        public <R2> LazyMapHelper<T, R2> flatMap(Function<R, List<R2>> f) {
            return null;
        }

    }
}
