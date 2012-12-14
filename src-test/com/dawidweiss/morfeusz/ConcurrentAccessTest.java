package com.dawidweiss.morfeusz;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.put.util.lang.VirtualVmClassLoader;

import junit.framework.TestCase;


/**
 * Tests the concurrent access to the native
 * loaded shared library.
 */
public class ConcurrentAccessTest extends TestCase {
    /**
     * Required by JUnit.
     */
    public ConcurrentAccessTest(String s) {
        super(s);
    }

    /**
     * Test a single-word analysis and availability of
     * the morphological analysis table and properties.
     */
    public void testCrossClassloaderConcurrentAccess()
        throws Exception {
        
        // create a 'virtual' class loader that will allow us
        // to load two singletons.
        String [] sandboxedClasses = new String [] {
            "com.dawidweiss.morfeusz."
        };

        VirtualVmClassLoader loader1 = new VirtualVmClassLoader(
            Thread.currentThread().getContextClassLoader(), Arrays.asList(sandboxedClasses));
        VirtualVmClassLoader loader2 = new VirtualVmClassLoader(
                        Thread.currentThread().getContextClassLoader(), Arrays.asList(sandboxedClasses));        

        Class clazz1 = loader1.loadClass(Morfeusz.class.getName());
        Class clazz2 = loader2.loadClass(Morfeusz.class.getName());

        assertNotSame(clazz1, clazz2);

        // ok, now instantiate Morfeusz class
        Method [] methods = clazz1.getMethods();
        Method getInstance1 = null;
        for (int i=0;i<methods.length;i++) {
            if ("getInstance".equals(methods[i].getName()) &&
                Modifier.isStatic(methods[i].getModifiers())) {
                getInstance1 = methods[i];
                break;
            }
        }
        assertNotNull(getInstance1);
        clazz1.getDeclaredMethod("getInstance", new Class [] {});
        Object morfeusz1 = getInstance1.invoke(null, (Object[]) null);
        assertNotNull(morfeusz1);

        try {        
            Method getInstance2 = clazz2.getDeclaredMethod("getInstance", new Class [] {});
            getInstance2.invoke(null, (Object[]) null);
            fail("Shared object loaded from two class loaders.");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof UnsatisfiedLinkError) {
                // this is what is expected - the shared object
                // has been loaded by another class loader.
            } else {
                fail("Unexpected exception when loading the second shared object.");
            }
        }
    }
}
