package com.kay.javabean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyEditorSupport;
import java.util.Arrays;

public class BeanInfoDemo {

		public static void main(String[] args) throws IntrospectionException {

				Person bean = new Person();

				BeanInfo beanInfo = Introspector.getBeanInfo(Person.class, Object.class);
				Arrays.stream(beanInfo.getPropertyDescriptors())
						.forEach(propertyDescriptor -> {
								System.out.println(propertyDescriptor.getName());
								propertyDescriptor.setPropertyEditorClass(StringToIntegerPropertyEditor.class);
						});

		}

		static class StringToIntegerPropertyEditor extends PropertyEditorSupport {

				@Override
				public void setAsText(String text) throws IllegalArgumentException {
						Integer value = Integer.valueOf(text);
						setValue(value);
				}
		}

}
