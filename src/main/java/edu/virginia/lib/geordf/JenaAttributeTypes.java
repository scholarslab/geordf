package edu.virginia.lib.geordf;

import java.net.URI;

import org.geotools.feature.AttributeTypeBuilder;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.PropertyDescriptor;

public class JenaAttributeTypes {

	static public AttributeType uriAttributeType(URI uri) {
		AttributeTypeBuilder builder = new AttributeTypeBuilder();
		builder.setName(uri.toString());
		builder.setBinding(URI.class);
		builder.setNillable(false);
		return builder.buildType();
	}
	static public AttributeType stringAttributeType() {
		AttributeTypeBuilder builder = new AttributeTypeBuilder();
		builder.setName("stringType");
		builder.setBinding(String.class);
		builder.setNillable(false);
		return builder.buildType();
	}
	public static AttributeType uriAttributeType(String s) {
		AttributeTypeBuilder builder = new AttributeTypeBuilder();
		builder.setName(s.toString());
		builder.setBinding(URI.class);
		builder.setNillable(false);
		return builder.buildType();
	}

}
