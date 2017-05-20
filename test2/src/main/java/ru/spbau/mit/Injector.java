package ru.spbau.mit;

/**
 * Created by lesya on 19.05.2017.
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


public final class Injector {
    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws ClassNotFoundException,
            AmbiguousImplementationException, ImplementationNotFoundException,
            InjectionCycleException, IllegalAccessException,
            InstantiationException, NoSuchMethodException,
            InvocationTargetException {

        List<Class> implClasses = new ArrayList<>();
        for (String name : implementationClassNames) {
            implClasses.add(Class.forName(name));
        }

        Class target = Class.forName(rootClassName);
        return init(target, implClasses, new LinkedHashSet<>());
    }

    private static Object init(Class clazz, List<Class> implClasses, HashSet<Class> neededClasses) throws AmbiguousImplementationException,
            ImplementationNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException, InjectionCycleException {

        if (params.indexOf(clazz.getCanonicalName()) < 0) {
            params.add(clazz);

            if (neededClasses.contains(clazz)) {
                throw new InjectionCycleException();
            }
            Constructor constructor = clazz.getConstructors()[0];
            Class[] types = constructor.getParameterTypes();
            List<Object> args = new ArrayList<>();
            neededClasses.addAll(Arrays.asList(types));
            for (Class type : types) {
                Object arg = null;
                for (Class candidate : implClasses) {
                    if (candidate.isAssignableFrom(type))
                        if (arg != null)
                            throw new AmbiguousImplementationException();
                    neededClasses.remove(candidate);
                    arg = init(candidate, implClasses, neededClasses);
                }
                if (arg == null)
                    throw new ImplementationNotFoundException();
                args.add(arg);
            }
            dependency.put(clazz, clazz.getConstructor(types).newInstance(args.toArray()));
        }
        return dependency.get(clazz);
    }

    private static HashMap<Class, Object> dependency = new LinkedHashMap<>();
    private static List<Class> params = new ArrayList<>();
}
