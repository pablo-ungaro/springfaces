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
package org.springframework.springfaces.mvc.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.validation.DataBinder;

/**
 * Tests for {@link ReverseDataBinder}.
 * 
 * @author Phillip Webb
 */
public class ReverseDataBinderTest {

	Log logger = LogFactory.getLog(getClass());

	private static final Date D01_12_2009;
	static {
		Calendar c = Calendar.getInstance();
		c.set(2009, Calendar.DECEMBER, 1, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		D01_12_2009 = c.getTime();
	}

	private static final String S01_12_2009 = "2009/01/12";

	private FormattingConversionService conversionService;

	@Before
	public void setup() {
		this.conversionService = new FormattingConversionService();
		this.conversionService.addConverter(new CustomTypeToStringConverter());
		this.conversionService.addConverter(new StringToCustomTypeConverter());
	}

	/**
	 * Setup the databinder with a customer date editor and a conversion service
	 * @param dataBinder
	 */
	private void initBinder(DataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy/dd/MM");
		df.setLenient(false);
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));
		dataBinder.setConversionService(this.conversionService);
	}

	@Test
	public void shouldReverseConvertWithCustomEditor() throws Exception {
		doTestReverseConvert(S01_12_2009, D01_12_2009);
	}

	@Test
	public void shouldReverseConvertForDefaultType() throws Exception {
		doTestReverseConvert("1234", new Integer(1234));
	}

	@Test
	public void shouldReverseConvertWithNull() throws Exception {
		doTestReverseConvert(null, null);
	}

	private void doTestReverseConvert(String value, Object expected) throws Exception {
		DataBinder dataBinder = new DataBinder(null);
		initBinder(dataBinder);
		Object converted = (value == null ? null : dataBinder.convertIfNecessary(value, expected.getClass()));
		assertEquals(expected, converted);
		ReverseDataBinder reverseDataBinder = new ReverseDataBinder(dataBinder);
		String reversed = reverseDataBinder.reverseConvert(converted);
		assertEquals(value, reversed);
	}

	@Test
	public void shouldReverseBind() throws Exception {
		Sample target = new Sample();
		DataBinder dataBinder = new DataBinder(target);
		initBinder(dataBinder);
		MutablePropertyValues pvs = new MutablePropertyValues();
		pvs.addPropertyValue("customTypeValue", "custom");
		pvs.addPropertyValue("dateValue", S01_12_2009);
		pvs.addPropertyValue("integerValue", "123");
		pvs.addPropertyValue("stringValue", "string");
		dataBinder.bind(pvs);

		assertEquals(new Integer(123), target.getIntegerValue());
		assertEquals("string", target.getStringValue());
		assertEquals(D01_12_2009, target.getDateValue());
		assertEquals(new CustomType("custom"), target.getCustomTypeValue());

		ReverseDataBinder reverseDataBinder = new ReverseDataBinder(dataBinder);
		PropertyValues result = reverseDataBinder.reverseBind();
		for (int i = 0; i < result.getPropertyValues().length; i++) {
			PropertyValue pv = result.getPropertyValues()[i];
			this.logger.info(pv.getName() + "=" + pv.getValue());
		}
		assertEquals(pvs, result);
	}

	@Test
	public void shouldFailReverseBindIfBindWillFail() throws Exception {
		Sample target = new Sample();
		DataBinder dataBinder = new DataBinder(target);
		dataBinder.setRequiredFields(new String[] { "integerValue" });
		ReverseDataBinder reverseDataBinder = new ReverseDataBinder(dataBinder);
		try {
			reverseDataBinder.reverseBind();
			fail();
		} catch (IllegalStateException e) {
			assertEquals(
					"Unable to reverse bind from target 'target', the properties 'PropertyValues: length=0' will result in binding errors "
							+ "when re-bound [Field error in object 'target' on field 'integerValue': rejected value []; codes "
							+ "[required.target.integerValue,required.integerValue,required.java.lang.Integer,required]; arguments "
							+ "[org.springframework.context.support.DefaultMessageSourceResolvable: codes [target.integerValue,integerValue]; "
							+ "arguments []; default message [integerValue]]; default message [Field 'integerValue' is required]]",
					e.getMessage());
		}
	}

	@Test
	public void shouldReverseBindWithDefaultValues() throws Exception {
		doTestReverseBindWithDefaultValues(false, false);
	}

	@Test
	public void shouldReverseBindWithDefaultValuesNotSkipped() throws Exception {
		doTestReverseBindWithDefaultValues(true, false);
	}

	@Test
	public void shouldReverseBindWithDefaultValuesNoConstructor() throws Exception {
		doTestReverseBindWithDefaultValues(false, true);
	}

	private void doTestReverseBindWithDefaultValues(boolean dontSkip, boolean noConstructor) throws Exception {
		Sample target = noConstructor ? new SampleWithoutDefaultConstructor("") : new Sample();
		target.setIntegerValue(new Integer(123));
		DataBinder dataBinder = new DataBinder(target);
		ReverseDataBinder reverseDataBinder = new ReverseDataBinder(dataBinder);
		if (dontSkip) {
			// Only set when skipped to test default is true
			reverseDataBinder.setSkipDefaultValues(false);
		}
		PropertyValues result = reverseDataBinder.reverseBind();
		boolean fullBindExpected = dontSkip || noConstructor;
		assertEquals(fullBindExpected ? 2 : 1, result.getPropertyValues().length);
		assertEquals("123", result.getPropertyValue("integerValue").getValue());
		if (fullBindExpected) {
			assertEquals("default", result.getPropertyValue("stringValue").getValue());
		}
	}

	// FIXME test with custom converter

	public static class Sample {
		private Date dateValue;

		private Integer integerValue;

		private String stringValue = "default";

		private CustomType customTypeValue;

		public Date getDateValue() {
			return this.dateValue;
		}

		public void setDateValue(Date dateValue) {
			this.dateValue = dateValue;
		}

		public Integer getIntegerValue() {
			return this.integerValue;
		}

		public void setIntegerValue(Integer integerValue) {
			this.integerValue = integerValue;
		}

		public String getStringValue() {
			return this.stringValue;
		}

		public void setStringValue(String stringValue) {
			this.stringValue = stringValue;
		}

		public String getNonMutable() {
			return "";
		}

		public CustomType getCustomTypeValue() {
			return this.customTypeValue;
		}

		public void setCustomTypeValue(CustomType customTypeValue) {
			this.customTypeValue = customTypeValue;
		}
	}

	public static class SampleWithoutDefaultConstructor extends Sample {
		public SampleWithoutDefaultConstructor(String argument) {
			super();
		}
	}

	public static class ThrowingPropertyEditor extends PropertyEditorSupport {
		@Override
		public String getAsText() {
			throw new RuntimeException("test error");
		}
	}

	public static final class CustomType {
		private String value;

		private CustomType(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}

		@Override
		public int hashCode() {
			return this.value.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof CustomType) {
				return this.value.equals(((CustomType) obj).value);
			}
			return super.equals(obj);
		}
	}

	public static final class CustomTypeToStringConverter implements Converter<CustomType, String> {
		public String convert(CustomType source) {
			return source == null ? null : source.toString();
		}
	}

	public static final class StringToCustomTypeConverter implements Converter<String, CustomType> {
		public CustomType convert(String source) {
			return source == null ? null : new CustomType(source);
		}
	}
}
