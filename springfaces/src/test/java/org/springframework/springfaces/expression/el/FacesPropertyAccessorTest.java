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
package org.springframework.springfaces.expression.el;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import javax.el.ELContext;
import javax.faces.context.FacesContext;

import org.junit.After;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.springfaces.FacesContextSetter;

/**
 * Test for {@link FacesPropertyAccessor}.
 * 
 * @author Phillip Webb
 */
public class FacesPropertyAccessorTest {

	private FacesPropertyAccessor facesPropertyAccessor = new FacesPropertyAccessor();
	private EvaluationContext context = mock(EvaluationContext.class);
	private Object target = new Object();

	@After
	public void cleanupFacesContext() {
		FacesContextSetter.setCurrentInstance(null);
	}

	@Test
	public void shouldWorkWithoutFacesContext() throws Exception {
		assertNull(this.facesPropertyAccessor.getElContext(this.context, this.target));
	}

	@Test
	public void shouldGetElContextFromFacesContext() throws Exception {
		FacesContext facesContext = mock(FacesContext.class);
		FacesContextSetter.setCurrentInstance(facesContext);
		ELContext elContext = mock(ELContext.class);
		given(facesContext.getELContext()).willReturn(elContext);
		assertSame(elContext, this.facesPropertyAccessor.getElContext(this.context, elContext));
	}
}
