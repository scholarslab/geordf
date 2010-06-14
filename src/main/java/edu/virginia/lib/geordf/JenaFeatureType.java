package edu.virginia.lib.geordf;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.feature.NameImpl;
import org.geotools.util.LocalName;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

public class JenaFeatureType implements FeatureType {
	
	private static final Logger LOGGER = Logging.getLogger("edu.virginia.lib.geordf.JenaFeatureType");
	private final URI uri;
	
	public JenaFeatureType(URI u) {
		LOGGER.info("Entering JenaFeatureType(URI u)");
		this.uri = u;
	}
	
	public JenaFeatureType(String u) throws URISyntaxException {
		LOGGER.info("Entering JenaFeatureType(String u)");
		this.uri = new URI(u);
	}

	@Override
	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		LOGGER.info("Entering getCoordinateReferenceSystem()");
		return null;
	}

	@Override
	public GeometryDescriptor getGeometryDescriptor() {
		return null;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public Class<Collection<Property>> getBinding() {
		LOGGER.info("Entering getBinding()");
		return (Class<Collection<Property>>) new Object();
	}

	@Override
	public PropertyDescriptor getDescriptor(Name name) {
		return getDescriptor(name.toString());
	}

	@Override
	public PropertyDescriptor getDescriptor(String name) {
		return (PropertyDescriptor) JenaAttributeTypes
		.uriAttributeType(uri.toString()) ;
	}

	@Override
	public Collection<PropertyDescriptor> getDescriptors() {
		return new HashSet<PropertyDescriptor>();
	}

	@Override
	public boolean isInline() {
		return false;
	}

	@Override
	public AttributeType getSuper() {
		// No parent type for us
		return null;
	}

	@Override
	public InternationalString getDescription() {
		return new LocalName(uri.toString()).toInternationalString();
	}

	private Name getNameO() {
		return new NameImpl(uri.toString());
	}
	
	@Override
	public Name getName() {
		return new NameImpl(getNameO().toString().replaceAll("/", "").replaceAll(":", ""));
	}

	@Override
	public List<Filter> getRestrictions() {
		return new ArrayList<Filter>();
	}

	@Override
	public Map<Object, Object> getUserData() {
		return new HashMap<Object,Object>();
	}

	@Override
	public boolean isAbstract() {
		return false;
	}

}
