package com.rs.core.utils.file;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author FuzzyAvacado
 */
public class ClassFileLoader {

    @SuppressWarnings("rawtypes")
    public static Class[] getClasses(final String packageName)
            throws ClassNotFoundException, IOException {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        final String path = packageName.replace('.', '/');
        final Enumeration<URL> resources = classLoader.getResources(path);
        final List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            final URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile().replaceAll("%20", " ")));
        }
        final ArrayList<Class> classes = new ArrayList<>();
        for (final File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    @SuppressWarnings("rawtypes")
    private static List<Class> findClasses(final File directory,
                                           final String packageName) {
        final List<Class> classes = new ArrayList<>();
        if (!directory.exists())
            return classes;
        final File[] files = directory.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClasses(file,
                            packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    try {
                        Class c = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                        if (c.isAnonymousClass())
                            continue;
                        classes.add(c);
                    } catch (final Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return classes;
    }
}
