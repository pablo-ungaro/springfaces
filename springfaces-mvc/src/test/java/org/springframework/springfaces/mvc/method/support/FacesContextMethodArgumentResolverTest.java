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
package org.springframework.springfaces.mvc.method.support;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.springfaces.mvc.SpringFacesMocks.mockMethodParameter;

import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.springfaces.mvc.FacesContextSetter;

/**
 * Tests for {@link FacesContextMethodArgumentResolver}.
 * 
 * @author Phillip Webb
 */
public class FacesContextMethodArgumentResolverTest {

	@Mock
	private FacesContext facesContext;

	@Mock
	private ExternalContext externalContext;

	@Mock
	private PartialViewContext partialViewContext;

	@Mock
	private Application application;

	@Mock
	private ResourceHandler resourceHandler;

	@Mock
	private ExceptionHandler exceptionHandler;

	@Mock
	private UIViewRoot viewRoot;

	private Locale locale = Locale.UK;

	private FacesContextMethodArgumentResolver resolver = new FacesContextMethodArgumentResolver();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		setupMocks(true);
		FacesContextSetter.setCurrentInstance(this.facesContext);
	}

	@After
	public void cleanup() {
		FacesContextSetter.setCurrentInstance(null);
	}

	@Test
	public void shouldNotSupportWhenNoFacesContext() throws Exception {
		FacesContextSetter.setCurrentInstance(null);
		assertFalse(this.resolver.supportsParameter(mockMethodParameter(FacesContext.class)));
		assertFalse(this.resolver.supportsParameter(mockMethodParameter(ExternalContext.class)));
		assertFalse(this.resolver.supportsParameter(mockMethodParameter(PartialViewContext.class)));
		assertFalse(this.resolver.supportsParameter(mockMethodParameter(Application.class)));
		assertFalse(this.resolver.supportsParameter(mockMethodParameter(ResourceHandler.class)));
		assertFalse(this.resolver.supportsParameter(mockMethodParameter(ExceptionHandler.class)));
		assertFalse(this.resolver.supportsParameter(mockMethodParameter(UIViewRoot.class)));
		assertFalse(this.resolver.supportsParameter(mockMethodParameter(Locale.class)));
	}

	@Test
	public void shouldResolveFacesContext() throws Exception {
		MethodParameter parameter = mockMethodParameter(FacesContext.class);
		assertTrue(this.resolver.supportsParameter(parameter));
		assertSame(this.facesContext, this.resolver.resolveArgument(parameter, null, null, null));
	}

	@Test
	public void shouldResolveExternalContext() throws Exception {
		MethodParameter parameter = mockMethodParameter(ExternalContext.class);
		assertTrue(this.resolver.supportsParameter(parameter));
		assertSame(this.externalContext, this.resolver.resolveArgument(parameter, null, null, null));
	}

	@Test
	public void shouldResolvePartialViewContext() throws Exception {
		MethodParameter parameter = mockMethodParameter(PartialViewContext.class);
		assertTrue(this.resolver.supportsParameter(parameter));
		assertSame(this.partialViewContext, this.resolver.resolveArgument(parameter, null, null, null));
	}

	@Test
	public void shouldResolveApplication() throws Exception {
		MethodParameter parameter = mockMethodParameter(Application.class);
		assertTrue(this.resolver.supportsParameter(parameter));
		assertSame(this.application, this.resolver.resolveArgument(parameter, null, null, null));
	}

	@Test
	public void shouldResolveResourceHandler() throws Exception {
		MethodParameter parameter = mockMethodParameter(ResourceHandler.class);
		assertTrue(this.resolver.supportsParameter(parameter));
		assertSame(this.resourceHandler, this.resolver.resolveArgument(parameter, null, null, null));
	}

	@Test
	public void shouldResolveExceptionHandler() throws Exception {
		MethodParameter parameter = mockMethodParameter(ExceptionHandler.class);
		assertTrue(this.resolver.supportsParameter(parameter));
		assertSame(this.exceptionHandler, this.resolver.resolveArgument(parameter, null, null, null));
	}

	@Test
	public void shouldResolveUIViewRoot() throws Exception {
		MethodParameter parameter = mockMethodParameter(UIViewRoot.class);
		assertTrue(this.resolver.supportsParameter(parameter));
		assertSame(this.viewRoot, this.resolver.resolveArgument(parameter, null, null, null));
	}

	@Test
	public void shouldResolveLocale() throws Exception {
		MethodParameter parameter = mockMethodParameter(Locale.class);
		assertTrue(this.resolver.supportsParameter(parameter));
		assertSame(this.locale, this.resolver.resolveArgument(parameter, null, null, null));
	}

	@Test
	public void shouldNotSupportWhenNoViewRoot() throws Exception {
		setupMocks(false);
		assertFalse(this.resolver.supportsParameter(mockMethodParameter(UIViewRoot.class)));
		assertFalse(this.resolver.supportsParameter(mockMethodParameter(Locale.class)));
	}

	private void setupMocks(boolean hasViewRoot) {
		reset(this.facesContext, this.application, this.viewRoot);
		given(this.facesContext.getExternalContext()).willReturn(this.externalContext);
		given(this.facesContext.getPartialViewContext()).willReturn(this.partialViewContext);
		given(this.facesContext.getApplication()).willReturn(this.application);
		given(this.facesContext.getExceptionHandler()).willReturn(this.exceptionHandler);
		if (hasViewRoot) {
			given(this.facesContext.getViewRoot()).willReturn(this.viewRoot);
		}
		given(this.application.getResourceHandler()).willReturn(this.resourceHandler);
		given(this.viewRoot.getLocale()).willReturn(this.locale);
	}

}
