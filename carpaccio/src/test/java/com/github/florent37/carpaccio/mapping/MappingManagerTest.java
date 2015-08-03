package com.github.florent37.carpaccio.mapping;

import android.view.View;
import android.widget.TextView;

import com.github.florent37.carpaccio.Carpaccio;
import com.github.florent37.carpaccio.model.CarpaccioAction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by florentchampigny on 24/07/15.
 */
public class MappingManagerTest {

    @Mock MappingManager.MappingManagerCallback callback;

    MappingManager mappingManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mappingManager = new MappingManager();
        mappingManager.setMappingManagerCallback(callback);
    }

    @Test
    public void testIsCallMapping() throws Exception {
        String[] call = new String[]{"$user"};
        assertTrue(MappingManager.isCallMapping(call));
    }

    @Test
    public void testIsCallMapping_multiple() throws Exception {
        String[] call = new String[]{"$user1","$user2"}; //only 1 mapping
        assertFalse(MappingManager.isCallMapping(call));
    }

    @Test
    public void testIsCallMapping_false() throws Exception {
        String[] call = new String[]{"user1"};
        assertFalse(MappingManager.isCallMapping(call));
    }

    @Test
    public void testIsCallMapping_empty() throws Exception {
        String[] call = new String[]{};
        assertFalse(MappingManager.isCallMapping(call));
    }

    @Test
    public void testGetFunctionName() throws Exception {
        assertEquals("getName", MappingManager.getFunctionName("getName()"));
    }

    @Test
    public void testGetFunctionName2() throws Exception {
        assertEquals("getName", MappingManager.getFunctionName("name"));
    }

    public class User{
        String name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "nameToString";
        }
    }

    public class Controller {
        public void setText(TextView textView, String text) {
            textView.setText(text);
        }
    }

    @Test
    public void testMapObject() throws Exception {
        User user = new User("florent");
        String name = "user";

        CarpaccioAction carpaccioAction = new CarpaccioAction("setText($user.getName())");
        View view = mock(View.class);
        {
            ArrayList list = new ArrayList();
            list.add(new MappingWaiting(view,carpaccioAction,"user.getName()","user"));
            mappingManager.mappingWaitings.put("user", list);
        }

        mappingManager.mapObject(name,user);

        assertTrue(mappingManager.mappedObjects.containsKey(name));
        assertEquals(user, mappingManager.mappedObjects.get(name));

        verify(callback,atLeastOnce()).callActionOnView(eq(carpaccioAction), eq(view));
        assertEquals("florent", carpaccioAction.getValues()[0]);
    }

    @Test
    public void testMapObject2() throws Exception {
        User user = new User("florent");
        String name = "user";

        View view = mock(View.class);
        CarpaccioAction carpaccioAction = new CarpaccioAction("setText($user.name)");
        {
            ArrayList list = new ArrayList();
            list.add(new MappingWaiting(view,carpaccioAction,"user.getName()","user"));
            mappingManager.mappingWaitings.put("user", list);
        }

        mappingManager.mapObject(name, user);

        assertTrue(mappingManager.mappedObjects.containsKey(name));
        assertEquals(user, mappingManager.mappedObjects.get(name));

        verify(callback,atLeastOnce()).callActionOnView(eq(carpaccioAction), eq(view));
        assertEquals("florent", carpaccioAction.getValues()[0]);
    }

    @Test
    public void testMapObject_noFunction() throws Exception {
        User user = new User("florent");
        String name = "user";

        View view = mock(View.class);
        CarpaccioAction carpaccioAction = new CarpaccioAction("setText($user)");
        {
            ArrayList list = new ArrayList();
            list.add(new MappingWaiting(view,carpaccioAction,"user","user"));
            mappingManager.mappingWaitings.put("user", list);
        }

        mappingManager.mapObject(name,user);

        assertTrue(mappingManager.mappedObjects.containsKey(name));
        assertEquals(user, mappingManager.mappedObjects.get(name));

        verify(callback,atLeastOnce()).callActionOnView(eq(carpaccioAction), eq(view));
        verify(callback,atLeastOnce()).callActionOnView(eq(carpaccioAction), eq(view));
        assertEquals("nameToString", carpaccioAction.getValues()[0]);
    }

    @Test
    public void testMapObject_fail() throws Exception {
        User user = new User("florent");
        String name = "user";

        View view = mock(View.class);
        CarpaccioAction carpaccioAction = new CarpaccioAction("setTexteu($user.getName())");
        {
            ArrayList list = new ArrayList();
            list.add(new MappingWaiting(view,carpaccioAction,"setTexteu","user"));
            mappingManager.mappingWaitings.put("user", list);
        }


        mappingManager.mapObject(name, user);

        assertTrue(mappingManager.mappedObjects.containsKey(name));
        assertEquals(user, mappingManager.mappedObjects.get(name));

        verify(callback,atLeastOnce()).callActionOnView(eq(carpaccioAction), eq(view));
    }

    @Test
    public void testCallMapping() throws Exception {
        View view = mock(View.class);

        CarpaccioAction carpaccioAction = new CarpaccioAction("setText($user)");
        mappingManager.callMappingOnView(carpaccioAction,view,null);

        assertTrue(mappingManager.mappingWaitings.containsKey("user"));

        MappingWaiting addedWaiting = mappingManager.mappingWaitings.get("user").get(0);
        assertNotNull(addedWaiting);
        assertEquals("user", addedWaiting.objectName);
        assertEquals("user", addedWaiting.call);
        assertEquals(carpaccioAction, addedWaiting.carpaccioAction);
        assertEquals(view, addedWaiting.view);
    }

    @Test
    public void testCallMapping_withFunction() throws Exception {
        View view = mock(View.class);

        CarpaccioAction carpaccioAction = new CarpaccioAction("setText($user.getName())");
        mappingManager.callMappingOnView(carpaccioAction,view,null);

        assertTrue(mappingManager.mappingWaitings.containsKey("user"));

        MappingWaiting addedWaiting = mappingManager.mappingWaitings.get("user").get(0);
        assertNotNull(addedWaiting);
        assertEquals("user", addedWaiting.objectName);
        assertEquals("user.getName()", addedWaiting.call);
        assertEquals(carpaccioAction, addedWaiting.carpaccioAction);
        assertEquals(view, addedWaiting.view);
    }

    @Test
    public void testGetMappingManagerCallback() throws Exception {
        assertEquals(callback,mappingManager.getMappingManagerCallback());
    }

    @Test
    public void testSetMappingManagerCallback() throws Exception {
        mappingManager.setMappingManagerCallback(callback);
        assertEquals(callback,mappingManager.mappingManagerCallback);
    }

    public class SubClassToEvaluate{
        public String getUrl(){
            return "www.MyImage";
        }

        public String toString(){
            return "IamAnImage";
        }
    }

    public class ObjectToEvaluate{

        protected SubClassToEvaluate image = new SubClassToEvaluate();

        public String toString(){
            return "ThisIsMyToString";
        }

        public String getName(){
            return "florent";
        }

        public int getCount(){
            return 3;
        }

        public SubClassToEvaluate getImage() {
            return image;
        }
    }

    @Test
    public void testEvaluateExpression(){
        ObjectToEvaluate object = new ObjectToEvaluate();

        String value = mappingManager.evaluate(object, "object.getName()");

        assertEquals("florent",value);
    }


    @Test
    public void testEvaluateExpression_reduce(){
        ObjectToEvaluate object = new ObjectToEvaluate();

        String value = mappingManager.evaluate(object, "object.name");

        assertEquals("florent",value);
    }

    @Test
    public void testEvaluateExpression_toString(){
        ObjectToEvaluate object = new ObjectToEvaluate();

        String value = mappingManager.evaluate(object,"object");

        assertEquals("ThisIsMyToString",value);
    }

    @Test
    public void testEvaluateExpression_multiple_toString(){
        ObjectToEvaluate object = new ObjectToEvaluate();

        String value = mappingManager.evaluate(object, "object.image");

        assertEquals("IamAnImage",value);
    }

    @Test
    public void testEvaluateExpression_multiple_value(){
        ObjectToEvaluate object = new ObjectToEvaluate();

        String value = mappingManager.evaluate(object, "object.image.getUrl()");

        assertEquals("www.MyImage",value);
    }

    @Test
    public void testEvaluateExpression_int(){
        ObjectToEvaluate object = new ObjectToEvaluate();

        String value = mappingManager.evaluate(object, "object.count");

        assertEquals("3",value);
    }
}