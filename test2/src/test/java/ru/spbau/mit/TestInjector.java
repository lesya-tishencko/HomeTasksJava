package ru.spbau.mit;

/**
 * Created by lesya on 19.05.2017.
 */
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

import ru.spbau.mit.testClasses.*;


public class TestInjector {

    @Test
    public void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initialize(
                "ru.spbau.mit.testClasses.ClassWithoutDependencies",
                Collections.<String>emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initialize(
                "ru.spbau.mit.testClasses.ClassWithOneClassDependency",
                Collections.singletonList("ru.spbau.mit.testClasses.ClassWithoutDependencies"));
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    public void injectorShouldBeAmbigous() throws Exception {
        List<String> collect = new ArrayList<>();
        collect.add("ru.spbau.mit.testClasses.InterfaceImpl1");
        collect.add("ru.spbau.mit.testClasses.InterfaceImpl2");
        try {
            Object object = Injector.initialize("ru.spbau.mit.testClasses.ClassWithTwoDependency", collect);
        }
        catch (AmbiguousImplementationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initialize(
                "ru.spbau.mit.testClasses.ClassWithOneInterfaceDependency",
                Collections.singletonList("ru.spbau.mit.testClasses.InterfaceImpl"));
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }
}
