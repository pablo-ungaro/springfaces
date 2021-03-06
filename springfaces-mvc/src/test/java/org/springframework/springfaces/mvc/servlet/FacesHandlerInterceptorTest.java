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
package org.springframework.springfaces.mvc.servlet;

import org.junit.Ignore;

@Ignore
public class FacesHandlerInterceptorTest {

	// FIXME incorporate handler test

	// private Method method = ReflectionUtils.findMethod(getClass(), "method");
	//
	// public void method() {
	// }
	//
	// @Test
	// public void shouldSupportNullHandlerBean() throws Exception {
	// assertNull(HandlerUtils.getHandlerBean(null));
	// }
	//
	// @Test
	// public void shouldReturnHandlerIfNotHandlerMethod() throws Exception {
	// Object handler = new Object();
	// assertSame(handler, HandlerUtils.getHandlerBean(handler));
	// }
	//
	// @Test
	// public void shouldUnwrapHandlerMethodFromBeanName() throws Exception {
	// BeanFactory beanFactory = mock(BeanFactory.class);
	// Object handler = new Object();
	// given(beanFactory.containsBean("beanName")).willReturn(true);
	// given(beanFactory.getBean("beanName")).willReturn(handler);
	// HandlerMethod handlerMethod = new HandlerMethod("beanName", beanFactory, method);
	// assertSame(handler, HandlerUtils.getHandlerBean(handlerMethod));
	// }
	//
	// @Test
	// public void shouldUnwrapHandlerMethod() throws Exception {
	// Object handler = new Object();
	// HandlerMethod handlerMethod = new HandlerMethod(handler, method);
	// assertSame(handler, HandlerUtils.getHandlerBean(handlerMethod));
	// }
}
