package ru.spbau.mit.testClasses;

/**
 * Created by lesya on 19.05.2017.
 */
public class ClassWithOneClassDependency {
    public final ClassWithoutDependencies dependency;

    public ClassWithOneClassDependency(ClassWithoutDependencies dependency) {
        this.dependency = dependency;
    }
}