import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Class<Test> testClass = Test.class;
        Method[] declaredMethods = testClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals("fun")) {
                Type genericParameterType = declaredMethod.getGenericParameterTypes()[0];
                GenericArrayType at = (GenericArrayType) genericParameterType;
                System.out.println(at.getGenericComponentType().getClass());
            }
        }
    }

    public <T extends CharSequence> void fun(T[] a) {}
}
