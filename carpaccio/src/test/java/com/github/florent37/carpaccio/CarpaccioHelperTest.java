package com.github.florent37.carpaccio;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by florentchampigny on 24/07/15.
 */
public class CarpaccioHelperTest {

    @Before
    public void setUp() throws Exception {
        CarpaccioHelper.ENABLE_LOG = false;
    }

    @Test
    public void testConstruct() throws Exception {
        Object object = CarpaccioHelper.construct("java.util.ArrayList");
        assertNotNull(object);
        assertTrue(object instanceof ArrayList);
    }

    @Test
    public void testConstruct_error() throws Exception {
        Object object = CarpaccioHelper.construct("java.util.ArrayListEU");
        assertNull(object);
    }

    @Test
    public void testGetClasses() throws Exception {
        Class[] expectedClasses = new Class[]{View.class, String.class,Integer.class,Float.class};

        Object[] objects = new Object[]{"florent",Integer.valueOf(1),Float.valueOf(2.0f)};

        Class[] outClasses = CarpaccioHelper.getClasses(objects);
        assertArrayEquals(expectedClasses, outClasses);
    }

    @Test
    public void testGetArguments() throws Exception {

    }

    @Test
    public void testGetFunctionName() throws Exception {
        String tag = "myFunction(arg1,arg2)";
        assertEquals("myFunction",CarpaccioHelper.getFunctionName(tag));
    }

    @Test
    public void testGetAttributes() throws Exception {
        String[] expectedStrings = new String[]{"arg1","arg2"};

        String tag = "myFunction(arg1,arg2)";
        assertArrayEquals(expectedStrings, CarpaccioHelper.getAttributes(tag));
    }

    @Test
    public void testTrim() throws Exception {
        String[] inString = new String[]{"     string1     ","     string2     "};

        String[] expectedStrings = new String[]{"string1","string2"};

        assertArrayEquals(expectedStrings, CarpaccioHelper.trim(inString));
    }

    public class TestObject{
        public void getName(View view, String argument1){}
        public String getPseudo(){return "florent";}
    }

    @Test
    public void testCallFunctionOnObjects() throws Exception {
        View mockView = Mockito.mock(View.class);

        TestObject spyObject1 = Mockito.spy(new TestObject());
        TestObject spyObject2 = Mockito.spy(new TestObject());

        String[] args = new String[]{"value1"};

        List<Object> objects = new ArrayList<>();
        objects.add(spyObject1);
        objects.add(spyObject2);

        boolean called = CarpaccioHelper.callFunctionOnObjects(objects, "getName", mockView, args);

        assertTrue(called);

        verify(spyObject1, atLeastOnce()).getName(eq(mockView), eq("value1"));
        verify(spyObject2, never()).getName(eq(mockView),eq("value1"));
    }

    @Test
    public void testCallFunctionOnObjects_second() throws Exception {
        View mockView = Mockito.mock(View.class);

        Object spyObject1 = Mockito.spy(new Object());
        TestObject spyObject2 = Mockito.spy(new TestObject());

        String[] args = new String[]{"value1"};

        List<Object> objects = new ArrayList<>();
        objects.add(spyObject1);
        objects.add(spyObject2);

        boolean called = CarpaccioHelper.callFunctionOnObjects(objects, "getName", mockView, args);

        assertTrue(called);

        verify(spyObject2, atLeastOnce()).getName(eq(mockView),eq("value1"));
    }

    @Test
    public void testCallFunctionOnObjects_null() throws Exception {
        CarpaccioHelper.callFunctionOnObjects(null, null, null, null); //test not crash
    }

    @Test
    public void testCallFunction() throws Exception {
        View mockView = Mockito.mock(View.class);

        TestObject spyObject = Mockito.spy(new TestObject());

        String[] args = new String[]{"value1"};

        boolean called = CarpaccioHelper.callFunction(spyObject,"getName",mockView,args);

        assertTrue(called);

        verify(spyObject, atLeastOnce()).getName(eq(mockView), eq("value1"));
    }

    @Test
    public void testCallFunction1() throws Exception {
        TestObject spyObject = Mockito.spy(new TestObject());

        String pseudo = CarpaccioHelper.callFunction(spyObject, "getPseudo");

        assertEquals("florent", pseudo);
        verify(spyObject, atLeastOnce()).getPseudo();
        verify(spyObject, never()).getName(any(View.class), anyString());
    }

    @Test
    public void testCallFunction1_fail() throws Exception {
        TestObject spyObject = Mockito.spy(new TestObject());

        String pseudo = CarpaccioHelper.callFunction(spyObject, "getEmptyNooooo");
        assertNull(pseudo);

        verify(spyObject, never()).getPseudo();
        verify(spyObject, never()).getName(any(View.class),anyString());
    }
}