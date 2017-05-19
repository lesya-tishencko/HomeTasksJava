package ru.spbau.mit.testClasses;

/**
 * Created by lesya on 19.05.2017.
 */
public class ClassWithOneInterfaceDependency {
    public final Interface dependency;

    public ClassWithOneInterfaceDependency(Interface dependency) {
        this.dependency = dependency;
    }
}