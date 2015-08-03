package com.github.florent37.carpaccio;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.florent37.carpaccio.model.CarpaccioAction;
import com.github.florent37.carpaccio.model.ObjectAndMethod;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by florentchampigny on 24/07/15.
 */
public class CarpaccioHelperTest {

    @Before
    public void setUp() throws Exception {
        CarpaccioLogger.ENABLE_LOG = false;
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
        Class[] expectedClasses = new Class[]{String.class, Integer.class, Float.class};

        Object[] objects = new Object[]{"florent", Integer.valueOf(1), Float.valueOf(2.0f)};

        Class[] outClasses = CarpaccioHelper.getClasses(objects);
        assertArrayEquals(expectedClasses, outClasses);
    }

    @Test
    public void testGetClassesWithHeaderClass() throws Exception {
        Class[] expectedClasses = new Class[]{View.class, String.class, Integer.class, Float.class};

        Object[] objects = new Object[]{"florent", Integer.valueOf(1), Float.valueOf(2.0f)};

        Class[] outClasses = CarpaccioHelper.getClassesWithHeaderClass(objects, View.class);
        assertArrayEquals(expectedClasses, outClasses);
    }

    @Test
    public void testGetArguments() throws Exception {
        Class viewClass = TextView.class;
        TextView textView = mock(TextView.class);

        String[] args = new String[]{"value1", "value2"};

        Object[] outArgs = CarpaccioHelper.getArgumentsWithView((View) textView, new Class[]{TextView.class, String.class, String.class}, args);

        Object[] expectedArgs = new Object[]{textView, "value1", "value2"};

        assertArrayEquals(expectedArgs, outArgs);
        assertTrue(outArgs[0] instanceof TextView);
    }

    @Test
    public void testGetArguments_numeric() throws Exception {
        Class viewClass = TextView.class;
        TextView textView = mock(TextView.class);

        String[] args = new String[]{"value1", "1", "1.2"};

        Object[] outArgs = CarpaccioHelper.getArgumentsWithView((View) textView, new Class[]{TextView.class, String.class, Integer.class, Float.class}, args);

        Object[] expectedArgs = new Object[]{textView, "value1", 1, 1.2f};

        assertArrayEquals(expectedArgs, outArgs);
        assertTrue(outArgs[0] instanceof TextView);
    }

    @Test
    public void testGetArguments_fail() throws Exception {
        Class viewClass = ImageView.class;
        TextView textView = mock(TextView.class);

        String[] args = new String[]{"value1", "value2"};

        Object[] outArgs = CarpaccioHelper.getArgumentsWithView((View) textView, new Class[]{TextView.class, String.class, String.class}, args);

        Object[] expectedArgs = new Object[]{textView, "value1", "value2"};

        assertArrayEquals(expectedArgs, outArgs);
        assertTrue(outArgs[0] instanceof TextView);
    }

    @Test
    public void testGetFunctionName() throws Exception {
        String tag = "myFunction(arg1,arg2)";
        assertEquals("myFunction", CarpaccioHelper.getFunctionName(tag));
    }

    @Test
    public void testGetAttributes() throws Exception {
        String[] expectedStrings = new String[]{"arg1", "arg2"};

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
    public void testRemoveTag() throws Exception {

        List<CarpaccioAction> carpaccioActions = new ArrayList<>();
        View view = mock(View.class);
        doReturn(carpaccioActions).when(view).getTag();

        carpaccioActions.add(new CarpaccioAction("setColor(red)"));
        carpaccioActions.add(new CarpaccioAction("setText(florent)"));
        carpaccioActions.add(new CarpaccioAction("setBackground(blue)"));

        List<CarpaccioAction> expected = new ArrayList<>();
        expected.add(new CarpaccioAction("setColor(red)"));
        expected.add(new CarpaccioAction("setBackground(blue)"));

        assertEquals(expected, CarpaccioHelper.removeTag(view, "setText(florent)"));
    }

    @Test
    public void testRemoveTag_null() throws Exception {
        View view = mock(View.class);
        doReturn(null).when(view).getTag();

        assertNull(CarpaccioHelper.removeTag(view, "setText"));
    }

    @Test
    public void testTrim() throws Exception {
        String[] inString = new String[]{"     string1     ", "     string2     "};

        String[] expectedStrings = new String[]{"string1", "string2"};

        assertArrayEquals(expectedStrings, CarpaccioHelper.trim(inString));
    }

    public class TestObject {
        public void getName(View view, String argument1) {
        }

        public String getPseudo() {
            return "florent";
        }

        public String getFamily(String family) {
            return "hello " + family;
        }
    }

    public class TestObjectFail {
        public void getName(View view, String argument1) {
            throw new UnknownError();
        }

        public String getPseudo() {
            throw new UnknownError();
        }
    }

    @Test
    public void testCallFunctionOnObjects() throws Exception {
        View mockView = mock(View.class);

        TestObject spyObject = spy(new TestObject());

        String[] args = new String[]{"value1"};

        Method method = CarpaccioHelper.callFunction(spyObject, "getName", mockView, args);

        assertNotNull(method);

        verify(spyObject, atLeastOnce()).getName(eq(mockView), eq("value1"));
    }


    @Test
    public void testCallMethod() throws Exception {
        TestObject testObject = spy(new TestObject());
        View view = mock(View.class);
        String arg1 = "florent";

        Method method = testObject.getClass().getMethod("getName", new Class[]{View.class, String.class});

        assertNotNull(CarpaccioHelper.callMethod(testObject, method, "getName", view, new Object[]{arg1}));
        verify(testObject,atLeastOnce()).getName(eq(view),eq(arg1));
    }

    @Test
    public void testCallMethod_fail() throws Exception {
        TestObject testObject = spy(new TestObject());
        View view = mock(View.class);
        String arg1 = "florent";

        Method method = null;

        assertNull(CarpaccioHelper.callMethod(testObject, method, "getName", view, new Object[]{arg1}));
        verify(testObject,never()).getName(eq(view), eq(arg1));
    }

    @Test
    public void testCallFunction() throws Exception {
        TestObject spyObject = spy(new TestObject());

        String pseudo = CarpaccioHelper.callFunction(spyObject, "getPseudo");

        assertEquals("florent", pseudo);
        verify(spyObject, atLeastOnce()).getPseudo();
        verify(spyObject, never()).getName(any(View.class), anyString());
        verify(spyObject, never()).getFamily(anyString());
    }

    @Test
    public void testCallFunction_fail() throws Exception {
        TestObject spyObject = spy(new TestObject());

        String pseudo = CarpaccioHelper.callFunction(spyObject, "getEmptyNooooo");
        assertNull(pseudo);

        verify(spyObject, never()).getPseudo();
        verify(spyObject, never()).getName(any(View.class), anyString());
        verify(spyObject, never()).getFamily(anyString());
    }

    @Test
    public void testCallFunction1() throws Exception {
        TestObject spyObject = spy(new TestObject());

        String[] args = new String[]{"florent"};

        String pseudo = CarpaccioHelper.callFunction(spyObject, "getFamily", args);

        assertEquals("hello florent", pseudo);

        verify(spyObject, atLeastOnce()).getFamily(eq("florent"));
        verify(spyObject, never()).getPseudo();
        verify(spyObject, never()).getName(any(View.class), anyString());
    }

    @Test
    public void testCallFunction1_fail() throws Exception {
        TestObject spyObject = spy(new TestObject());

        String[] args = new String[]{"florent", "florent"};

        String pseudo = CarpaccioHelper.callFunction(spyObject, "getFamily", args);

        assertNull(pseudo);

        verify(spyObject, never()).getFamily(eq("florent"));
        verify(spyObject, never()).getPseudo();
        verify(spyObject, never()).getName(any(View.class), anyString());
    }

    @Test
    public void testCallFunction2_fail2() throws Exception {
        TestObjectFail spyObject = spy(new TestObjectFail());

        String[] args = new String[]{"florent", "florent"};

        String pseudo = CarpaccioHelper.callFunction(spyObject, "getFamily", args);

        assertNull(pseudo);

        verify(spyObject, never()).getPseudo();
        verify(spyObject, never()).getName(any(View.class), anyString());
    }

    @Test
    public void testIsNumber() throws Exception {
        assertTrue(CarpaccioHelper.isNumber(Integer.class));
        assertTrue(CarpaccioHelper.isNumber(Float.class));
        assertTrue(CarpaccioHelper.isNumber(Double.class));
        assertTrue(CarpaccioHelper.isNumber(Long.class));
    }

    @Test
    public void testFindParentOfClass() throws Exception {
        LinearLayout linearLayout = mock(LinearLayout.class);
        ViewGroup viewGroup = mock(ViewGroup.class);
        View view = mock(View.class);
        doReturn(viewGroup).when(view).getParent();
        doReturn(linearLayout).when(viewGroup).getParent();

        assertEquals(linearLayout, CarpaccioHelper.findParentOfClass(view, LinearLayout.class));
    }

    @Test
    public void testFindParentOfClass_fail() throws Exception {
        LinearLayout linearLayout = mock(LinearLayout.class);
        ViewGroup viewGroup = mock(ViewGroup.class);
        View view = mock(View.class);
        doReturn(viewGroup).when(view).getParent();
        doReturn(linearLayout).when(viewGroup).getParent();

        assertNull(CarpaccioHelper.findParentOfClass(view, RelativeLayout.class));
    }

    @Test
    public void testFindParentCarpaccio() throws Exception {
        Carpaccio carpaccio = mock(Carpaccio.class);
        ViewGroup viewGroup = mock(ViewGroup.class);
        View view = mock(View.class);
        doReturn(viewGroup).when(view).getParent();
        doReturn(carpaccio).when(viewGroup).getParent();

        assertEquals(carpaccio, CarpaccioHelper.findParentCarpaccio(view));
    }

    @Test
    public void registerToParentCarpaccio() throws Exception {
        Carpaccio carpaccio = mock(Carpaccio.class);
        ViewGroup viewGroup = mock(ViewGroup.class);
        View view = mock(View.class);
        doReturn(viewGroup).when(view).getParent();
        doReturn(carpaccio).when(viewGroup).getParent();

        CarpaccioHelper.registerToParentCarpaccio(view);

        verify(carpaccio, atLeastOnce()).addCarpaccioView(eq(view));
    }

    public class Controller1 {
        public void setText(View view, String text) {
        }
    }

    public class Controller2 {
        public void setColor(View view, String color) {
        }

        public void setText(View view, String text, String color) {
        }
    }

    @Test
    public void testFindObjectWithThisMethod() throws Exception {
        Controller1 testObject = new Controller1();
        Controller2 testObjectfail = new Controller2();

        ObjectAndMethod objectAndMethod = CarpaccioHelper.findObjectWithThisMethod(Arrays.asList(testObjectfail, testObject), "setText", 2);

        assertNotNull(objectAndMethod);
        assertEquals(testObject, objectAndMethod.getObject());
        assertEquals("setText", objectAndMethod.getMethod().getName());
        assertEquals(2, objectAndMethod.getMethod().getParameterTypes().length);
    }

    @Test
    public void testFindObjectWithThisMethod_fail() throws Exception {
        Controller2 testObjectfail1 = new Controller2();
        Controller2 testObjectfail2 = new Controller2();

        List<Object> arrayList = new ArrayList<>();
        arrayList.add(testObjectfail1);
        arrayList.add(testObjectfail2);

        ObjectAndMethod objectAndMethod = CarpaccioHelper.findObjectWithThisMethod(arrayList, "setText", 2);

        assertNull(objectAndMethod);
    }

    @Test
    public void testStringToInt() throws Exception {
        assertEquals(Integer.valueOf(1), CarpaccioHelper.stringToInt("1"));
    }

    @Test
    public void testStringToInt_fail() throws Exception {
        assertNull(CarpaccioHelper.stringToInt("aa"));
    }

    @Test
    public void testStringToDouble() throws Exception {
        assertEquals(Double.valueOf(2), CarpaccioHelper.stringToDouble("2"));
    }

    @Test
    public void testStringToDouble_fail() throws Exception {
        assertNull(CarpaccioHelper.stringToDouble("aa"));
    }


    @Test
    public void testStringToFloat() throws Exception {
        assertEquals(Float.valueOf(2.1f), CarpaccioHelper.stringToFloat("2.1"));
    }

    @Test
    public void testStringToFloat_fail() throws Exception {
        assertNull(CarpaccioHelper.stringToFloat("aa"));
    }


    @Test
    public void testStringToLong() throws Exception {
        assertEquals(Long.valueOf(3), CarpaccioHelper.stringToLong("3"));
    }

    @Test
    public void testStringToNumber() throws Exception {
        assertEquals(Integer.valueOf(3),CarpaccioHelper.stringToNumber("3",Integer.class));
        assertEquals(Long.valueOf(3),CarpaccioHelper.stringToNumber("3",Long.class));
        assertEquals(Float.valueOf(3),CarpaccioHelper.stringToNumber("3",Float.class));
        assertEquals(Double.valueOf(3),CarpaccioHelper.stringToNumber("3",Double.class));
        assertNull(CarpaccioHelper.stringToNumber("3",String.class));
    }

}