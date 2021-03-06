/*
 * Copyright 2010-2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.springfaces.mvc.internal;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.springfaces.mvc.FacesContextSetter;

/**
 * Tests for {@link MvcNavigationActionListener}.
 * 
 * @author Phillip Webb
 */
@RunWith(MockitoJUnitRunner.class)
public class MvcNavigationActionListenerTest {

	private MvcNavigationActionListener listener;

	@Mock
	private ActionListener delegate;

	@Mock
	private ActionEvent event;

	@Mock
	private FacesContext facesContext;

	private Map<Object, Object> attributes = new HashMap<Object, Object>();

	@Before
	public void setup() {
		this.listener = new MvcNavigationActionListener(this.delegate);
		given(this.facesContext.getAttributes()).willReturn(this.attributes);
		FacesContextSetter.setCurrentInstance(this.facesContext);
	}

	@After
	public void cleanup() {
		FacesContextSetter.setCurrentInstance(null);
	}

	@Test
	public void shouldStoreActionEvent() throws Exception {
		this.listener.processAction(this.event);
		assertTrue(this.attributes.size() == 1);
		assertSame(this.event, MvcNavigationActionListener.getLastActionEvent(this.facesContext));
	}

	@Test
	public void shouldCallDelegate() throws Exception {
		this.listener.processAction(this.event);
		verify(this.delegate).processAction(this.event);
	}
}
