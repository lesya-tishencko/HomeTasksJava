package ru.spbau.mit.testClasses;

/**
 * Created by lesya on 20.05.2017.
 */
public class ClassWithTwoDependency {
    public final InterfaceImpl dependency1;
    public final Interface dependency2;


    public ClassWithTwoDependency(Interface dependency2, InterfaceImpl dependency1) {
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
    }
}
