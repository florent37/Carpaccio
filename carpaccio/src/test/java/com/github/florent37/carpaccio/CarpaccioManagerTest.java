package com.github.florent37.carpaccio;

import android.view.View;
import android.widget.TextView;

import com.github.florent37.carpaccio.mapping.MappingManager;
import com.github.florent37.carpaccio.model.CarpaccioAction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by florentchampigny on 24/07/15.
 */
public class CarpaccioManagerTest {

    @Mock
    MappingManager mappingManager;

    CarpaccioManager carpaccioManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Log.ENABLE_LOG = false;
        carpaccioManager = new CarpaccioManager(mappingManager);
    }

    @Test
    public void testAddView() throws Exception {
        View view = mock(View.class);
        doReturn("setColor()").when(view).getTag();
        carpaccioManager.addView(view);

        assertTrue(carpaccioManager.carpaccioViews.contains(view));
    }

    @Test
    public void testAddViewMultiple() throws Exception {
        View view = mock(View.class);

        carpaccioManager.addView(view);

        assertTrue(carpaccioManager.carpaccioViews.contains(view));
    }

    @Test
    public void testIsCarpaccioControlledView() throws Exception {
        View view = mock(View.class);
        doReturn("setColor()").when(view).getTag();
        assertTrue(carpaccioManager.isCarpaccioControlledView(view));
    }

    @Test
    public void testIsCarpaccioControlledView_textView() throws Exception {
        TextView view = mock(TextView.class);
        doReturn("$user.getName()").when(view).getText();
        assertTrue(carpaccioManager.isCarpaccioControlledView(view));
    }

    @Test
    public void testIsCarpaccioControlledView_column() throws Exception {
        View view = mock(View.class);
        doReturn("setColor();").when(view).getTag();
        assertTrue(carpaccioManager.isCarpaccioControlledView(view));
    }

    @Test
    public void testIsCarpaccioControlledView_multiple() throws Exception {
        View view = mock(View.class);
        doReturn("setColor();setText();").when(view).getTag();
        assertTrue(carpaccioManager.isCarpaccioControlledView(view));
    }

    @Test
    public void testIsCarpaccioControlledView_false() throws Exception {
        View view = mock(View.class);
        doReturn("setColor").when(view).getTag();
        assertFalse(carpaccioManager.isCarpaccioControlledView(view));
    }

    @Test
    public void testIsCarpaccioControlledView_false_emptyTag() throws Exception {
        View view = mock(View.class);
        doReturn(null).when(view).getTag();
        assertFalse(carpaccioManager.isCarpaccioControlledView(view));
    }

    @Test
    public void testRegisterController() throws Exception {
        Object controller = mock(Object.class);

        carpaccioManager.registerController(controller);

        assertTrue(carpaccioManager.registerControllers.contains(controller));
    }

    @Test
    public void testRegisterControllers() throws Exception {
        assertTrue(carpaccioManager.registerControllers.isEmpty());

        carpaccioManager.registerControllers("java.util.ArrayList");

        assertFalse(carpaccioManager.registerControllers.isEmpty());

        assertTrue(carpaccioManager.registerControllers.get(0) instanceof java.util.ArrayList);
    }

    @Test
    public void testRegisterControllers_multiple() throws Exception {
        assertTrue(carpaccioManager.registerControllers.isEmpty());

        carpaccioManager.registerControllers("java.util.ArrayList;java.util.HashMap;");

        assertFalse(carpaccioManager.registerControllers.isEmpty());

        assertTrue(carpaccioManager.registerControllers.get(0) instanceof java.util.ArrayList);
        assertTrue(carpaccioManager.registerControllers.get(1) instanceof java.util.HashMap);
    }

    @Test
    public void testRegisterControllers_fail() throws Exception {
        assertTrue(carpaccioManager.registerControllers.isEmpty());

        carpaccioManager.registerControllers("java.ttttttiiillll.ArrayList");

        assertTrue(carpaccioManager.registerControllers.isEmpty());
    }

    @Test
    public void testRegisterControllers_fail_multiple() throws Exception {
        assertTrue(carpaccioManager.registerControllers.isEmpty());

        carpaccioManager.registerControllers("java.ttttttiiillll.ArrayList;java.util.HashMap;");

        assertFalse(carpaccioManager.registerControllers.isEmpty());
        assertTrue(carpaccioManager.registerControllers.get(0) instanceof java.util.HashMap);
    }

    @Test
    public void testRegisterControllers_multiple_whiteSpace() throws Exception {
        assertTrue(carpaccioManager.registerControllers.isEmpty());

        carpaccioManager.registerControllers("             java.util.ArrayList;     \n    java.util.HashMap;     \n          ");
        assertFalse(carpaccioManager.registerControllers.isEmpty());

        assertTrue(carpaccioManager.registerControllers.get(0) instanceof java.util.ArrayList);
        assertTrue(carpaccioManager.registerControllers.get(1) instanceof java.util.HashMap);
    }

    @Test
    public void testRegisterControllers_multiple_commaSeparator() throws Exception {
        assertTrue(carpaccioManager.registerControllers.isEmpty());

        carpaccioManager.registerControllers("java.util.ArrayList;java.util.HashMap");
        assertFalse(carpaccioManager.registerControllers.isEmpty());

        assertTrue(carpaccioManager.registerControllers.get(0) instanceof java.util.ArrayList);
        assertTrue(carpaccioManager.registerControllers.get(1) instanceof java.util.HashMap);
    }

    public class Controller {
        public void setText(TextView textView, String text) {
            textView.setText(text);
        }

        public void setColor(View view, int color) {
        }
    }

    @Test
    public void testExecuteActionsOnViews() throws Exception {
        Controller controller = spy(new Controller());

        TextView textView = mock(TextView.class);
        doReturn("setText(florent)").when(textView).getTag();

        carpaccioManager.carpaccioViews.add(textView);
        carpaccioManager.registerControllers.add(controller);

        carpaccioManager.executeActionsOnViews();

        verify(controller, atLeastOnce()).setText(eq(textView), eq("florent"));
        verify(textView, atLeastOnce()).setText(eq("florent"));
    }

    @Test
    public void testExecuteActionsOnView() throws Exception {
        Controller controller = spy(new Controller());

        TextView textView = mock(TextView.class);
        doReturn("setColor(1)").when(textView).getTag();

        carpaccioManager.carpaccioViews.add(textView);
        carpaccioManager.registerControllers.add(controller);

        carpaccioManager.executeActionsOnView(textView);

        verify(controller, atLeastOnce()).setColor(eq(textView), eq(1));
    }

    @Test
    public void testExecuteActionsOnView_list() throws Exception {
        Controller controller = spy(new Controller());

        TextView textView = mock(TextView.class);

        List<CarpaccioAction> carpaccioActionList = new ArrayList<>();
        carpaccioActionList.add(new CarpaccioAction("setText(florent)"));
        doReturn(carpaccioActionList).when(textView).getTag();

        carpaccioManager.carpaccioViews.add(textView);
        carpaccioManager.registerControllers.add(controller);

        carpaccioManager.executeActionsOnView(textView);

        verify(controller, atLeastOnce()).setText(eq(textView), eq("florent"));
    }

    @Test
    public void testExecuteActionsOnViews2() throws Exception {
        Controller controller = spy(new Controller());

        TextView textView = mock(TextView.class);
        doReturn("setFont(florent)").when(textView).getTag();

        carpaccioManager.carpaccioViews.add(textView);
        carpaccioManager.registerControllers.add(controller);

        carpaccioManager.executeActionsOnViews();

        verify(controller, never()).setText(eq(textView), eq("florent"));
        verify(textView, never()).setText(eq("florent"));
    }

    @Test
    public void testMapObject() throws Exception {
        String name = "name";
        Object object = new Object();

        carpaccioManager.mapObject(name, object);
        verify(carpaccioManager.mappingManager, atLeastOnce()).mapObject(eq(name), eq(object));
    }

}