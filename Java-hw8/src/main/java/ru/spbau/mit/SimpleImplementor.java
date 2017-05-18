package ru.spbau.mit;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lesya on 18.05.2017.
 */
public class SimpleImplementor implements Implementor {
    public SimpleImplementor(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className) throws ImplementorException {
        URL classUrl;
        try {
            classUrl = new File(directoryPath).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new ImplementorException(e.getMessage(), e.getCause());
        }
        ClassLoader loader = new URLClassLoader(new URL[]{classUrl});
        Class<?> clazz;
        try {
            clazz = loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new ImplementorException(e.getMessage(), e.getCause());
        }
        String path = outputDirectory;
        String[] parts = clazz.getCanonicalName().split("\\.");
        for (String part : parts) {
            path += "\\" + part;
        }
        path += "Impl.java";
        generateJava(clazz, path);
        return clazz.getCanonicalName() + "Impl";
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        Class<?> clazz;
        try {
            clazz = ClassLoader.getSystemClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new ImplementorException(e.getMessage(), e.getCause());
        }
        String path = outputDirectory + "\\" + clazz.getSimpleName() + "Impl.java";
        generateJava(clazz, path);
        return clazz.getSimpleName() + "Impl";
    }

    private void generateJava(Class<?> clazz, String path) throws ImplementorException {
        try(PrintWriter writer = new PrintWriter(path)){
            String pack = clazz.getPackage().getName();
            if (!pack.contains("java.lang")) {
                writer.println("package " + pack + ";");
                writer.println();
            }
            writer.print("public class " + clazz.getSimpleName() + "Impl ");
            String impl = clazz.isInterface() ? "implements " : "extends ";
            writer.println(impl + clazz.getCanonicalName() + " {");

            List<Class<?>> classes = new ArrayList<>();
            classes.add(clazz);
            int index = 0;
            while (index < classes.size()) {
                Class<?>[] currentInterf = classes.get(index).getInterfaces();
                if (currentInterf.length != 0)
                    classes.addAll(Arrays.asList(currentInterf));
                Class<?> parent = classes.get(index).getSuperclass();
                if (parent != null)
                    classes.add(parent);
                index++;
            }

            List<Method> methods = new ArrayList<>();
            for (Class<?> current : classes) {
                methods.addAll(Arrays.asList(current.getDeclaredMethods()));
            }

            for (Method method : methods) {
                writer.println();
                generateMethod(method, writer);
            }

            writer.println("}");
        } catch (IOException e) {
            throw new ImplementorException(e.getMessage(), e.getCause());
        }
    }

    private void generateMethod(Method method, PrintWriter writer) throws IOException {
        int mod = method.getModifiers();
        String mods = "\t";
        mods += Modifier.isPublic(mod) ? "public " : (Modifier.isPrivate(mod) ? "private " : "protected ");
        mods += Modifier.isFinal(mod) ? "final " : "";
        mods += Modifier.isStatic(mod) ? "static " : "";
        writer.print(mod + method.getReturnType().getCanonicalName() + " " + method.getName() + "(");

        Parameter[] params = method.getParameters();
        for (int i = 0; i < params.length; i++) {
            int modParam = params[i].getModifiers();
            String paramStr = Modifier.isFinal(modParam) ? "final " : "";
            paramStr += params[i].getType().getCanonicalName() + " " + params[i].getName();
            paramStr += i == params.length - 1 ? ") " : ", ";
            writer.print(paramStr);
        }

        AnnotatedType[] exceptions = method.getAnnotatedExceptionTypes();
        if (exceptions.length != 0) {
            writer.print("throws ");
            for (int i = 0; i < exceptions.length; i++) {
                String except = exceptions[i].getType().getTypeName();
                except += i == exceptions.length - 1 ? " " : ", ";
                writer.append(except);

            }
        }

        writer.println("{");
        writer.println("\tthrow new UnsupportedOperationException();");
        writer.println("}");
    }

    private String outputDirectory;
}
