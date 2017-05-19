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
            AmbiguousImplementationException,
            ImplementationNotFoundException,
            InjectionCycleException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        List<Class> implClasses = new ArrayList<>();
        for (String name : implementationClassNames) {
            implClasses.add(Class.forName(name));
        }

        HashMap<Class, Object> dependency = new LinkedHashMap<Class, Object>();
        Class target = Class.forName(rootClassName);
        List<Class> params = new ArrayList<>();
        params.add(target);
        params.addAll(implClasses);
        List<Constructor> constructors = new ArrayList<>();

        int index = 0;
        while (index < params.size()) {
            Constructor currentConstructor = params.get(index).getConstructors()[0];
            Class[] current = currentConstructor.getParameterTypes();
            if (current.length != 0) {
                params.addAll(Arrays.asList(current));
            } else {
                dependency.put(params.get(index), params.get(index).newInstance());
            }
            constructors.add(currentConstructor);
            index++;
        }


        for (int i = params.size() - 1; i >= 0; i--) {
            if (dependency.get(params.get(i)) == null) {
                Class[] types = constructors.get(i).getParameterTypes();
                List<Object> args = new ArrayList<>();
                for (Class type : types) {
                    Object arg = null;
                    for (Class candidate : params) {
                        if (candidate.isAssignableFrom(type))
                            if (arg != null)
                                throw new AmbiguousImplementationException();
                            arg = dependency.get(candidate);
                    }
                    if (arg == null)
                        throw new ImplementationNotFoundException();
                    args.add(arg);
                }
                dependency.put(params.get(i), params.get(i).getConstructor(types).newInstance(args.toArray()));
            }
        }

        return dependency.get(target);
    }
}
