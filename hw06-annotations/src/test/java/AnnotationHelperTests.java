import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.dgp.framework.AnnotationHelper;

class AnnotationHelperTests {

    @Test
    void shouldReturnMethodsWithNeededAnnotation() {
        List<Method> methods = AnnotationHelper.getAnnotatedMethods(A.class, Annotation1.class);

        Assertions.assertEquals(2, methods.size());

        List<String> names = methods.stream().map(Method::getName).collect(Collectors.toList());

        Assertions.assertEquals(
                1, names.stream().filter(n -> n == "doSomething1").count());
        Assertions.assertEquals(
                1, names.stream().filter(n -> n == "doSomething3").count());
        Assertions.assertEquals(
                0, names.stream().filter(n -> n == "doSomething2").count());
    }

    @Test
    void shouldReturnEmptyArrayOfMethodsIfNoAnnotaedMethods() {
        List<Method> methods = AnnotationHelper.getAnnotatedMethods(A.class, Annotation3.class);

        Assertions.assertEquals(0, methods.size());
    }

    class A {

        @Annotation1
        public void doSomething1() {}

        @Annotation2
        public void doSomething2() {}

        @Annotation1
        @Annotation2
        public void doSomething3() {}
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Annotation1 {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Annotation2 {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Annotation3 {}
}
