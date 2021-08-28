import java.lang.reflect.*;
import java.util.List;

public class Test {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        Class<Test> testClass = Test.class;
        Method fun = testClass.getDeclaredMethod("fun", List.class);
        System.out.println(fun.getParameterTypes()[0]);
        ParameterizedType t = (ParameterizedType) fun.getGenericParameterTypes()[0];
        WildcardType actualTypeArgument = (WildcardType) t.getActualTypeArguments()[0];
        System.out.println(actualTypeArgument.getUpperBounds()[0]);
    }

    public void fun(List<? extends CharSequence> l) {};
}
