package edu.virginia.lib.geordf;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.GraphUtil;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class JenaFeatureCollection implements FeatureCollection<JenaFeatureType, Feature> {
	
	private static final Logger LOGGER = Logging.getLogger("edu.virginia.lib.geordf.JenaFeatureCollection");
	protected Collection<Feature> features;
	
	public JenaFeatureCollection(Collection<Feature> c) {
		LOGGER.info("Entering JenaFeatureCollection(Collection<Feature> c)");
		LOGGER.info("with Collection<Feature> size: " + c.size());
		this.features = c;
	}

	@Override
	public void accepts(FeatureVisitor visitor, ProgressListener progress)
			throws IOException {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public boolean add(Feature obj) {
		if (features.add(obj)) return true;
		else return false;
	}

	@Override
	public boolean addAll(Collection<? extends Feature> collection) {
		if (features.addAll(collection)) return true;
		else return false;
	}

	@Override
	public boolean addAll(FeatureCollection<? extends JenaFeatureType, ? extends Feature> resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addListener(CollectionListener listener)
			throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		features.clear();
	}

	@Override
	public void close(FeatureIterator<Feature> close) {
		close.close();
	}

	@Override
	public void close(Iterator<Feature> close) {
	}

	@Override
	public boolean contains(Object o) {
		return features.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> o) {
		return features.containsAll(o);
	}

	@Override
	public FeatureIterator<Feature> features() {
		LOGGER.info("Entering features()");
		return new JenaFeatureCollectionIterator(this);
	}

	@Override
	public ReferencedEnvelope getBounds() {
		// stubbed in for the moment
		LOGGER.info("Entering getBounds()");
		return new ReferencedEnvelope();
	}

	@Override
	public String getID() {
		LOGGER.info("Entering getID()");
		LOGGER.info("getID() returns: " + "JenaFeatureCollection: " + features.hashCode());
		return "JenaFeatureCollection: " + features.hashCode();
	}

	@Override
	public JenaFeatureType getSchema() {
		LOGGER.info("Entering getSchema()");
		try {
			return new JenaFeatureType("");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean isEmpty() {
		LOGGER.info("Entering isEmpty()");
		LOGGER.info("isEmpty() returns: " + features.isEmpty());
		return features.isEmpty();
	}

	@Override
	public Iterator<Feature> iterator() {
		LOGGER.info("Entering iterator()");
		return features.iterator();
	}

	@Override
	public void purge() {
		features.clear();
	}

	@Override
	public boolean remove(Object o) {
		return features.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return features.removeAll(c);
	}

	@Override
	public void removeListener(CollectionListener listener)
			throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return features.retainAll(c);
	}

	@Override
	public int size() {
		LOGGER.info("Entering size()");
		LOGGER.info("size() returns: " + features.size());
		return features.size();
	}

	@Override
	public FeatureCollection<JenaFeatureType, Feature> sort(SortBy order) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FeatureCollection<JenaFeatureType, Feature> subCollection(Filter filter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		LOGGER.info("Entering toArray()");
		return features.toArray();
	}

	@Override
	public <O> O[] toArray(O[] a) {
		LOGGER.info("Entering toArray(O[] a)");
		return features.toArray(a);
	}

}
