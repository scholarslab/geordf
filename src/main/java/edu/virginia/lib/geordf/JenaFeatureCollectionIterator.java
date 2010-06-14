package edu.virginia.lib.geordf;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.geotools.feature.FeatureIterator;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;

public class JenaFeatureCollectionIterator implements FeatureIterator<Feature> {

	private static final Logger LOGGER = Logging.getLogger("edu.virginia.lib.geordf.JenaFeatureCollectionIterator");
	private Collection<Feature> features;
	private Iterator<Feature> iter;
	
	public JenaFeatureCollectionIterator(JenaFeatureCollection coll) {
		LOGGER.info("Entering JenaFeatureCollectionIterator(JenaFeatureCollection coll)");
		LOGGER.info("with JenaFeatureCollection of size: " + coll.size());
		this.features = coll.features;
		this.iter = features.iterator();
	}
	
	public JenaFeatureCollectionIterator(Collection<Feature> coll) {
		this.features = coll;
		this.iter = features.iterator();
	}
	
	@Override
	public void close() {
	}

	@Override
	public boolean hasNext() {
		LOGGER.info("Entering hasNext()");
		return iter.hasNext();
	}

	@Override
	public Feature next() throws NoSuchElementException {
		Feature n = iter.next();
		LOGGER.info("JenaFeatureCollectionIterator.next() produced: " + n.getName());
		return n;
	}

}
