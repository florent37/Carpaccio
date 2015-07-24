package com.github.florent37.carpaccio.mapping;

import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        assertEquals("getName", MappingManager.getFunctionName("user.getName()"));
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

        View view = mock(View.class);
        {
            mappingManager.mappingWaitings.put("user", Arrays.asList(
                    new MappingWaiting(view,"setText","user.getName()","user")
            ));
        }

        mappingManager.mapObject(name,user);

        assertTrue(mappingManager.mappedObjects.containsKey(name));
        assertEquals(user, mappingManager.mappedObjects.get(name));

        verify(callback,atLeastOnce()).callFunctionOnControllers(eq("setText"),eq(view),eq(new String[]{"florent"}));
    }

    @Test
    public void testMapObject_noFunction() throws Exception {
        User user = new User("florent");
        String name = "user";

        View view = mock(View.class);
        {
            mappingManager.mappingWaitings.put("user", Arrays.asList(
                    new MappingWaiting(view,"setText","user","user")
            ));
        }

        mappingManager.mapObject(name,user);

        assertTrue(mappingManager.mappedObjects.containsKey(name));
        assertEquals(user, mappingManager.mappedObjects.get(name));

        verify(callback,atLeastOnce()).callFunctionOnControllers(eq("setText"),eq(view),eq(new String[]{"nameToString"}));
    }

    @Test
    public void testMapObject_fail() throws Exception {
        User user = new User("florent");
        String name = "user";

        View view = mock(View.class);
        {
            mappingManager.mappingWaitings.put("user", Arrays.asList(
                    new MappingWaiting(view,"setTexteu","user.getName()","user")
            ));
        }

        mappingManager.mapObject(name,user);

        assertTrue(mappingManager.mappedObjects.containsKey(name));
        assertEquals(user, mappingManager.mappedObjects.get(name));

        verify(callback,never()).callFunctionOnControllers(eq("setText"),eq(view),eq(new String[]{"florent"}));
    }

    @Test
    public void testCallMapping() throws Exception {
        View view = mock(View.class);

        mappingManager.callMapping("setText",view,new String[]{"$user"});

        assertTrue(mappingManager.mappingWaitings.containsKey("user"));

        MappingWaiting addedWaiting = mappingManager.mappingWaitings.get("user").get(0);
        assertNotNull(addedWaiting);
        assertEquals("user", addedWaiting.objectName);
        assertEquals("setText", addedWaiting.function);
        assertEquals("user",addedWaiting.call);
        assertEquals(view, addedWaiting.view);
    }

    @Test
    public void testCallMapping_withFunction() throws Exception {
        View view = mock(View.class);

        mappingManager.callMapping("setText",view,new String[]{"$user.getName()"});

        assertTrue(mappingManager.mappingWaitings.containsKey("user"));

        MappingWaiting addedWaiting = mappingManager.mappingWaitings.get("user").get(0);
        assertNotNull(addedWaiting);
        assertEquals("user", addedWaiting.objectName);
        assertEquals("setText",addedWaiting.function);
        assertEquals("user.getName()",addedWaiting.call);
        assertEquals(view,addedWaiting.view);
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
}