package per.wilson.chat.websocket.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author Wilson
 */
public class ReflectUtils {
    /**
     * 反射获取类方法，若类未加载，则根据 {@code initialize} 判断是否加载类
     *
     * @param canonicalClassName 类全名(包路径+类名)
     * @param initialize         使用类加载器是否初始化该类
     * @param methodName         方法名
     * @param filter             方法过滤条件
     * @return {@code null} if can't find method
     * @throws ClassNotFoundException while can't find class
     * @throws NoSuchMethodException  while filter result is empty
     */
    public static Method method(String canonicalClassName, boolean initialize, String methodName, Predicate<Method> filter) throws ClassNotFoundException, NoSuchMethodException {
        return Arrays.stream(Class.forName(canonicalClassName, initialize, ReflectUtils.class.getClassLoader())
                .getMethods())
                .filter(method -> Objects.equals(method.getName(), methodName))
                .filter(filter)
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Can't find method " + methodName + " of class " + canonicalClassName));
    }

    /**
     * 反射获取类方法，若类未加载，则根据 {@code initialize} 判断是否加载类
     *
     * @param clazz      类
     * @param initialize 使用类加载器是否初始化该类
     * @param methodName 方法名
     * @param filter     方法过滤条件
     * @return {@code null} if can't find method
     * @throws ClassNotFoundException while can't find class
     * @throws NoSuchMethodException  while filter result is empty
     */
    public static Method method(Class<?> clazz, boolean initialize, String methodName, Predicate<Method> filter) throws ClassNotFoundException, NoSuchMethodException {
        return Arrays.stream(Class.forName(clazz.getCanonicalName(), initialize, ReflectUtils.class.getClassLoader())
                .getMethods())
                .filter(method -> Objects.equals(method.getName(), methodName))
                .filter(filter)
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Can't find method " + methodName + " of class " + clazz.getCanonicalName()));
    }

    /**
     * 反射获取类方法，若类未加载，则根据 {@code initialize} 判断是否加载类
     *
     * @param object     实例
     * @param initialize 使用类加载器是否初始化该类
     * @param methodName 方法名
     * @param filter     方法过滤条件
     * @return {@code null} if can't find method
     * @throws ClassNotFoundException while can't find class
     * @throws NoSuchMethodException  while filter result is empty
     */
    public static <T> Method method(T object, boolean initialize, String methodName, Predicate<Method> filter) throws ClassNotFoundException, NoSuchMethodException {
        return Arrays.stream(Class.forName(object.getClass().getCanonicalName(), initialize, ReflectUtils.class.getClassLoader())
                .getMethods())
                .filter(method -> Objects.equals(method.getName(), methodName))
                .filter(filter)
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Can't find method " + methodName + " of class " + object.getClass().getCanonicalName()));
    }

    /**
     * 反射获取类方法
     *
     * @param canonicalClassName 类全名(包路径+类名)
     * @param methodName         方法名
     * @param filter             方法过滤条件
     * @return {@code null} if can't find method
     * @throws ClassNotFoundException while can't find class
     * @throws NoSuchMethodException  while filter result is empty
     */
    public static Method method(String canonicalClassName, String methodName, Predicate<Method> filter) throws ClassNotFoundException, NoSuchMethodException {
        return Arrays.stream(Class.forName(canonicalClassName)
                .getMethods())
                .filter(method -> Objects.equals(method.getName(), methodName))
                .filter(filter)
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Can't find method " + methodName + " of class " + canonicalClassName));
    }

    /**
     * 反射获取类方法
     *
     * @param clazz      类
     * @param methodName 方法名
     * @param filter     方法过滤条件
     * @return {@code null} if can't find method
     * @throws NoSuchMethodException while filter result is empty
     */
    public static Method method(Class<?> clazz, String methodName, Predicate<Method> filter) throws NoSuchMethodException {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> Objects.equals(method.getName(), methodName))
                .filter(filter)
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Can't find method " + methodName + " of class " + clazz.getCanonicalName()));
    }

    /**
     * 反射获取类方法
     *
     * @param object     实例
     * @param methodName 方法名
     * @param filter     方法过滤条件
     * @return {@code null} if can't find method
     * @throws NoSuchMethodException while filter result is empty
     */
    public static <T> Method method(T object, String methodName, Predicate<Method> filter) throws NoSuchMethodException {
        return Arrays.stream(object.getClass().getMethods())
                .filter(method -> Objects.equals(method.getName(), methodName))
                .filter(filter)
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Can't find method " + methodName + " of class " + object.getClass().getCanonicalName()));
    }
}
