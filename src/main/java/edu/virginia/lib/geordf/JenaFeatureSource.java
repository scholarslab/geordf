package edu.virginia.lib.geordf;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.DataAccess;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.GraphUtil;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class JenaFeatureSource implements
		FeatureSource<JenaFeatureType, Feature> {

	private static final Node LOCATIONPREDICATE = Node
			.createURI("http://www.opengis.net/gml/3.2#Location");
	private JenaDataAccess store;
	private Name name;
	private Graph graph;
	private static final Logger LOGGER = Logging.getLogger("edu.virginia.lib.geordf.JenaFeatureSource");

	public JenaFeatureSource(JenaDataAccess store, Name name) {
		LOGGER.info("Entering JenaFeatureSource(JenaDataAccess store, Name name)");
		LOGGER.info("with graph name: " + name.toString());
		this.store = store;
		this.name = name;
		Store sdbstore = store.getStore();
		this.graph = SDBFactory.connectDefaultGraph(sdbstore/*, name.toString()*/);
		if (graph.isEmpty()) {
			LOGGER.info("Graph is empty!");
		}
		else {
			LOGGER.info("Graph contains: " + graph.size() + " triples:");
			for (Triple t : GraphUtil.findAll(graph).toList()) {
				LOGGER.info(t.toString());
			}
		}
	}

	@Override
	public void addFeatureListener(FeatureListener listener) {
		// Copied from app-schema
		throw new UnsupportedOperationException();
	}

	@Override
	public ReferencedEnvelope getBounds() throws IOException {
		return getFeatures().getBounds();
	}

	@Override
	public ReferencedEnvelope getBounds(Query query) throws IOException {
		// temporarily ignoring query
		return new ReferencedEnvelope();
	}

	@Override
	public int getCount(Query query) throws IOException {
		// temporarily ignoring query
		return getFeatures().size();
	}

	@Override
	public DataAccess<JenaFeatureType, Feature> getDataStore() {
		return store;
	}

	@Override
	public FeatureCollection<JenaFeatureType, Feature> getFeatures()
			throws IOException {
		LOGGER.info("Entering getFeatures()");
		List<Node> featurenodes = new ArrayList<Node>();
		List<Triple> tripleswithlocation = graph.find(Node.ANY, LOCATIONPREDICATE,
				Node.ANY).toList();
		LOGGER.info("Found " + tripleswithlocation.size() + " triples with LOCATIONPREDICATE");
		for (Triple t : tripleswithlocation) {
			featurenodes.add(t.getSubject());
		}
		Collection<Feature> features = new HashSet<Feature>();
		for (Node n : featurenodes ) {
			ExtendedIterator<Triple> j = graph.find(n, Node.ANY, Node.ANY);
			features.add(new JenaFeature(j.toSet()));
			LOGGER.info("Created JenaFeature with: ");
			for (Triple t : j.toSet())
				LOGGER.info(t.toString());
		}
		return new JenaFeatureCollection(features);
	}

	@Override
	public FeatureCollection<JenaFeatureType, Feature> getFeatures(Filter filter)
			throws IOException {
		LOGGER.info("Entering getFeatures(Filter filter)");
		// temporarily ignore filter
		return getFeatures();
	}

	@Override
	public FeatureCollection<JenaFeatureType, Feature> getFeatures(Query query)
			throws IOException {
		LOGGER.info("Entering getFeatures(Query query)");
		// temporarily ignore query
		return getFeatures();
	}

	@Override
	public ResourceInfo getInfo() {
		// Copied from app-schema
		throw new UnsupportedOperationException();
	}

	private Name getNameO() {
		return name;
	}
	
	public Name getName() {
		return new NameImpl(getNameO().toString().replaceAll("/", "").replaceAll(":", ""));
	}

	@Override
	public QueryCapabilities getQueryCapabilities() {
		// Copied from app-schema
		return new QueryCapabilities();
	}

	@Override
	public JenaFeatureType getSchema() {
		LOGGER.info("Entering getSchema()");
		try {
			LOGGER.info("Trying to use URI: " + getName().getURI() + " to create a JenaFeatureType");
			return new JenaFeatureType(getName().getURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Set<Key> getSupportedHints() {
		// Copied from app-schema
		return Collections.emptySet();
	}

	@Override
	public void removeFeatureListener(FeatureListener listener) {
		// Copied from app-schema
		throw new UnsupportedOperationException();
	}

}
