import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.dgp.framework.AnnotationHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AnnotationHelperTests {

    @Test
    public void shouldReturnMethodsWithNeededAnnotation(){
        Method[] methods = AnnotationHelper.getAnnotatedMethods(A.class, Annotation1.class);

        Assertions.assertEquals(2, methods.length);

        List<String> names = Arrays.stream(methods).map(Method::getName).collect(Collectors.toList());

        Assertions.assertTrue(names.stream().filter(n-> n == "doSomething1").count() == 1);
        Assertions.assertTrue(names.stream().filter(n-> n == "doSomething3").count() == 1);
        Assertions.assertTrue(names.stream().filter(n-> n == "doSomething2").count() == 0);
    }

    public class A{

        @Annotation1
        public void doSomething1(){}

        @Annotation2
        public void doSomething2(){}

        @Annotation1
        @Annotation2
        public void doSomething3(){}

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Annotation1 {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Annotation2 {

    }
}
