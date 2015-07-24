package com.github.florent37.carpaccio;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by florentchampigny on 24/07/15.
 */
public class CarpaccioHelperTest {

    @Before
    public void setUp() throws Exception {
        Log.ENABLE_LOG = false;
        CarpaccioHelper.LOG_FAILURES = true;
    }

    @Test
    public void testConstructCarpaccioHelper() throws Exception {
        new CarpaccioHelper();
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
        Class[] expectedClasses = new Class[]{String.class,Integer.class,Float.class};

        Object[] objects = new Object[]{"florent",Integer.valueOf(1),Float.valueOf(2.0f)};

        Class[] outClasses = CarpaccioHelper.getClasses(objects);
        assertArrayEquals(expectedClasses, outClasses);
    }

    @Test
    public void testGetClassesWithHeaderClass() throws Exception {
        Class[] expectedClasses = new Class[]{View.class, String.class,Integer.class,Float.class};

        Object[] objects = new Object[]{"florent",Integer.valueOf(1),Float.valueOf(2.0f)};

        Class[] outClasses = CarpaccioHelper.getClassesWithHeaderClass(objects, View.class);
        assertArrayEquals(expectedClasses, outClasses);
    }

    @Test
    public void testGetArguments() throws Exception {
        Class viewClass = TextView.class;
        TextView textView = Mockito.mock(TextView.class);

        String[] args = new String[]{"value1","value2"};

        Object[] outArgs = CarpaccioHelper.getArgumentsWithView(viewClass, (View)textView, args);

        Object[] expectedArgs = new Object[]{textView,"value1","value2"};

        assertArrayEquals(expectedArgs,outArgs);
        assertTrue(outArgs[0] instanceof TextView);
    }

    @Test
    public void testGetArguments_fail() throws Exception {
        Class viewClass = ImageView.class;
        TextView textView = Mockito.mock(TextView.class);

        String[] args = new String[]{"value1","value2"};

        Object[] outArgs = CarpaccioHelper.getArgumentsWithView(viewClass, (View)textView, args);

        Object[] expectedArgs = new Object[]{textView,"value1","value2"};

        assertArrayEquals(expectedArgs, outArgs);
        assertTrue(outArgs[0] instanceof TextView);
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
    public void testGetAttributes_empty() throws Exception {
        String[] expectedStrings = new String[]{};

        String tag = "myFunction()";
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
        public String getFamily(String family){return "hello "+family;}
    }

    public class TestObjectFail{
        public void getName(View view, String argument1){throw new UnknownError();}
        public String getPseudo(){throw new UnknownError();}
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
        verify(spyObject, never()).getFamily(anyString());
        verify(spyObject, never()).getPseudo();
    }

    @Test
    public void testCallFunction_fail() throws Exception {
        View mockView = Mockito.mock(View.class);

        TestObjectFail spyObject = Mockito.spy(new TestObjectFail());

        assertFalse(CarpaccioHelper.callFunction(spyObject, "getName", mockView, new String[]{"florent"}));

    }

    @Test
    public void testCallFunction1() throws Exception {
        TestObject spyObject = Mockito.spy(new TestObject());

        String pseudo = CarpaccioHelper.callFunction(spyObject, "getPseudo");

        assertEquals("florent", pseudo);
        verify(spyObject, atLeastOnce()).getPseudo();
        verify(spyObject, never()).getName(any(View.class), anyString());
        verify(spyObject, never()).getFamily(anyString());
    }

    @Test
    public void testCallFunction1_fail() throws Exception {
        TestObject spyObject = Mockito.spy(new TestObject());

        String pseudo = CarpaccioHelper.callFunction(spyObject, "getEmptyNooooo");
        assertNull(pseudo);

        verify(spyObject, never()).getPseudo();
        verify(spyObject, never()).getName(any(View.class), anyString());
        verify(spyObject, never()).getFamily(anyString());
    }

    @Test
    public void testCallFunction2() throws Exception {
        TestObject spyObject = Mockito.spy(new TestObject());

        String[] args = new String[]{"florent"};

        String pseudo = CarpaccioHelper.callFunction(spyObject, "getFamily",args);

        assertEquals("hello florent", pseudo);

        verify(spyObject, atLeastOnce()).getFamily(eq("florent"));
        verify(spyObject, never()).getPseudo();
        verify(spyObject, never()).getName(any(View.class), anyString());
    }

    @Test
    public void testCallFunction2_fail() throws Exception {
        TestObject spyObject = Mockito.spy(new TestObject());

        String[] args = new String[]{"florent","florent"};

        String pseudo = CarpaccioHelper.callFunction(spyObject, "getFamily",args);

        assertNull(pseudo);

        verify(spyObject, never()).getFamily(eq("florent"));
        verify(spyObject, never()).getPseudo();
        verify(spyObject, never()).getName(any(View.class), anyString());
    }

    @Test
    public void testCallFunction2_fail2() throws Exception {
        TestObjectFail spyObject = Mockito.spy(new TestObjectFail());

        String[] args = new String[]{"florent","florent"};

        String pseudo = CarpaccioHelper.callFunction(spyObject, "getFamily",args);

        assertNull(pseudo);

        verify(spyObject, never()).getPseudo();
        verify(spyObject, never()).getName(any(View.class), anyString());
    }
}