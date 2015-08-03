package com.github.florent37.carpaccio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.florent37.carpaccio.mapping.MappingManager;
import com.github.florent37.carpaccio.model.CarpaccioAction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
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
        CarpaccioLogger.ENABLE_LOG = false;
        Carpaccio.IN_EDIT_MODE = false;
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
        doReturn("setText(florent)").when(textView).getTag();

        carpaccioManager.carpaccioViews.add(textView);
        carpaccioManager.registerControllers.add(controller);

        carpaccioManager.executeActionsOnViews();

        verify(controller, atLeastOnce()).setText(eq(textView), eq("florent"));
        verify(textView, atLeastOnce()).setText(eq("florent"));
    }

    Object tmpTag;

    @Test
    public void testExecuteActionsOnViews_text() throws Exception {
        Controller controller = spy(new Controller());

        TextView textView = mock(TextView.class);
        doReturn("$user.name").when(textView).getText();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                tmpTag = invocation.getArguments()[0];
                return null;
            }
        }).when(textView).setTag(any());


        carpaccioManager.carpaccioViews.add(textView);
        carpaccioManager.registerControllers.add(controller);

        carpaccioManager.executeActionsOnViews();

        assertNotNull(tmpTag);
    }

    @Test
    public void testExecuteActionsOnViews_text_tagString() throws Exception {
        Controller controller = spy(new Controller());

        TextView textView = mock(TextView.class);
        doReturn("$user.name").when(textView).getText();
        doReturn("setFont(font)").when(textView).getTag();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                tmpTag = invocation.getArguments()[0];
                return null;
            }
        }).when(textView).setTag(any());

        carpaccioManager.carpaccioViews.add(textView);
        carpaccioManager.registerControllers.add(controller);

        carpaccioManager.executeActionsOnViews();

        assertEquals("setFont(font);setText($user.name)", tmpTag);
    }

    @Test
    public void testExecuteActionsOnViews_text_tagList() throws Exception {
        Controller controller = spy(new Controller());

        TextView textView = mock(TextView.class);
        doReturn("$user.name").when(textView).getText();

        List<CarpaccioAction> actions = new ArrayList<>();
        actions.add(new CarpaccioAction("setFont(font)"));

        doReturn(actions).when(textView).getTag();

        carpaccioManager.carpaccioViews.add(textView);
        carpaccioManager.registerControllers.add(controller);

        carpaccioManager.executeActionsOnViews();

        List<CarpaccioAction> expected = new ArrayList<>();
        expected.add(new CarpaccioAction("setFont(font)"));
        expected.add(new CarpaccioAction("setText($user.name)"));

        assertEquals(expected,textView.getTag());
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
    public void testExecuteActionsOnView_mapping() throws Exception {
        Controller controller = spy(new Controller());

        TextView textView = mock(TextView.class);
        doReturn("setText($user.name)").when(textView).getTag();
        doNothing().when(mappingManager).callMappingOnView(any(CarpaccioAction.class),any(View.class),anyObject());

        carpaccioManager.carpaccioViews.add(textView);
        carpaccioManager.registerControllers.add(controller);

        carpaccioManager.executeActionsOnView(textView);

        verify(mappingManager, atLeastOnce()).callMappingOnView(any(CarpaccioAction.class), any(View.class), anyObject());
        verify(controller, never()).setText(eq(textView), anyString());
    }

    @Test
    public void testExecuteActionsOnView_mapping_EDIT_MODE() throws Exception {
        Controller controller = spy(new Controller());

        Carpaccio.IN_EDIT_MODE = true;

        TextView textView = mock(TextView.class);
        doReturn("setText($user.name)").when(textView).getTag();
        doNothing().when(mappingManager).callMappingOnView(any(CarpaccioAction.class), any(View.class), anyObject());

        carpaccioManager.carpaccioViews.add(textView);
        carpaccioManager.registerControllers.add(controller);

        carpaccioManager.executeActionsOnView(textView);

        verify(controller, atLeastOnce()).setText(eq(textView), eq("$user.name"));
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

    @Test
    public void testMapList() throws Exception {
        List list = new ArrayList();

        RecyclerView.Adapter adapter = mock(RecyclerView.Adapter.class);

        carpaccioManager.registerAdapters.put("users", adapter);

        try {
            carpaccioManager.mapList("users", list);
        }catch (Exception e){
            //adapter.notifyDataSetChanged() throws exception on tests
        }
        verify(mappingManager).mapList(eq("users"), eq(list));
    }

    @Test
    public void testGetMappedList() throws Exception {
        carpaccioManager.getMappedList("users");
        verify(mappingManager).getMappedList(eq("users"));
    }

    @Test
    public void testGetMappedList_null() throws Exception {
        carpaccioManager.mappingManager = null;
        verify(mappingManager,never()).getMappedList(eq("users"));
    }

    @Test
    public void testRegisterAdapter() throws Exception {
        RecyclerView.Adapter adapter = mock(RecyclerView.Adapter.class);

        carpaccioManager.registerAdapter("users", adapter);

        assertEquals(adapter, carpaccioManager.registerAdapters.get("users"));
    }

    @Test
    public void findCarpaccioControlledViews() throws Exception {
        View view = mock(View.class);

        doReturn("setText(florent)").when(view).getTag();

        carpaccioManager.findCarpaccioControlledViews(view);

        assertTrue(carpaccioManager.carpaccioViews.contains(view));
    }

    @Test
    public void findCarpaccioControlledViews_viewGroup() throws Exception {

        ViewGroup viewGroup = mock(ViewGroup.class);
        doReturn(1).when(viewGroup).getChildCount();
        View view = mock(View.class);
        doReturn(view).when(viewGroup).getChildAt(0);

        doReturn("setText(florent)").when(view).getTag();

        carpaccioManager.findCarpaccioControlledViews(viewGroup);

        assertTrue(carpaccioManager.carpaccioViews.contains(view));
        assertFalse(carpaccioManager.carpaccioViews.contains(viewGroup));
    }

    @Test
    public void testAddChildViews() throws Exception {
        ViewGroup viewGroup = mock(ViewGroup.class);
        doReturn(1).when(viewGroup).getChildCount();
        View view = mock(View.class);
        doReturn(view).when(viewGroup).getChildAt(0);
        doReturn("setText(florent)").when(view).getTag();

        carpaccioManager.addChildViews(viewGroup);

        assertTrue(carpaccioManager.carpaccioSubViews.get(viewGroup).contains(view));
    }

    @Test
    public void testBindViews() throws Exception {
        ViewGroup viewGroup = mock(ViewGroup.class);
        View view = mock(View.class);

        doReturn(new Object()).when(mappingManager).getMappedListsObject("users",0);

        carpaccioManager.carpaccioSubViews.put(viewGroup, Arrays.asList(view));

        carpaccioManager.bindView(viewGroup, "users", 0);

        verify(mappingManager, atLeastOnce()).getMappedListsObject(eq("users"), eq(0));
        verify(view,atLeastOnce()).getTag();

    }
}