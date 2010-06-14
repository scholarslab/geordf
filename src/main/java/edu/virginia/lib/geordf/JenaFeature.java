package edu.virginia.lib.geordf;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.geotools.feature.AttributeImpl;
import org.geotools.feature.NameImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;

import com.hp.hpl.jena.graph.Factory;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

public class JenaFeature implements Feature {

	private Collection<Triple> coll;
	private Node viewpoint;

	public JenaFeature(Collection<Triple> coll) {
		this.coll = coll;
		this.viewpoint = coll.iterator().next().getSubject();
	}

	@Override
	public BoundingBox getBounds() {
		return getDefaultGeometryProperty().getBounds();
	}

	@Override
	public GeometryAttribute getDefaultGeometryProperty() {
		return (GeometryAttribute) getProperty("http://www.opengis.net/gml/3.2#Location");
	}

	@Override
	public FeatureId getIdentifier() {
		return new FeatureIdImpl(viewpoint.getURI() + ':' + coll.hashCode());
	}

	@Override
	public FeatureType getType() {
		try {
			return new JenaFeatureType(viewpoint.getURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setDefaultGeometryProperty(GeometryAttribute geometryAttribute) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Property> getProperties() {
		return getProperties("");
	}

	@Override
	public Collection<Property> getProperties(Name name) {
		return getProperties(name.getURI());
	}

	@Override
	public Collection<Property> getProperties(String name) {
		Collection<Property> s = new HashSet<Property>();
		Iterator<Triple> iter = coll.iterator();
		while (iter.hasNext()) {
			Triple t = iter.next();
			try {
				String stringuri = t.getPredicate().getURI();
				if (stringuri == name || name == "") {
					URI predicateuri = new URI(stringuri);
					AttributeType predicate = JenaAttributeTypes
							.uriAttributeType(predicateuri);
					Node object = t.getObject();
					if (object.isLiteral()) {
						String value = object.getLiteral().getLexicalForm();
						Property p = (Property) (new AttributeImpl(value,
								predicate, null));
						s.add(p);
					} else if (object.isURI()) {
						URI value = new URI(object.getURI());
						Property p = (Property) (new AttributeImpl(value,
								predicate, null));
						s.add(p);
					}
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return s;
	}

	@Override
	public Property getProperty(Name name) {
		return getProperty(name.getURI());
	}

	@Override
	public Property getProperty(String name) {
		return getProperties(name).toArray(new Property[0])[0];
	}

	@Override
	public Collection<? extends Property> getValue() {
		return getProperties();
	}

	@Override
	public void setValue(Collection<Property> values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void validate() throws IllegalAttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public AttributeDescriptor getDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name getName() {
		return new NameImpl(viewpoint.getURI());
	}

	@Override
	public Map<Object, Object> getUserData() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isNillable() {
		return false;
	}

	@Override
	public void setValue(Object newValue) {
		throw new UnsupportedOperationException();
	}
	
	public Collection<Triple> getTriples() {
		return coll;
	}
	
	public Graph getGraph() {
		Graph g = Factory.createGraphMem();
		for (Triple t : coll) {
			g.add(t);
		}
		return g;
	}

}
