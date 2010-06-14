package edu.virginia.lib.geordf;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.data.DataAccess;
import org.geotools.data.FeatureSource;
import org.geotools.data.ServiceInfo;
import org.geotools.feature.NameImpl;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.type.Name;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;

public class JenaDataAccess implements DataAccess<JenaFeatureType, Feature> {

	private static final Logger LOGGER = Logging
			.getLogger("edu.virginia.lib.geordf.JenaDataAccess");
	private final Store store;

	public JenaDataAccess(Store s) {
		this.store = s;
	}

	protected Store getStore() {
		return store;
	}

	@Override
	public void dispose() {
		store.close();
	}

	@Override
	public FeatureSource<JenaFeatureType, Feature> getFeatureSource(
			Name typeName) throws IOException {
		LOGGER.info("Entering getFeatureSource(Name typeName)");
		return new JenaFeatureSource(this, new NameImpl(typeName.getNamespaceURI()));
	}

	@Override
	public List<Name> getNames() throws IOException {
		LOGGER.info("Entering getNames()");
		List<Name> names = new ArrayList<Name>();
		for (Iterator<Node> i = SDBFactory.connectGraphStore(store)
				.listGraphNodes(); i.hasNext();) {
			Node n = i.next();
			names.add(new NameImpl(n.getURI()));
			LOGGER.info("Found graph name: " + n.toString());
		}
		return names;
	}

	@Override
	public JenaFeatureType getSchema(Name name) throws IOException {
		LOGGER.info("Entering getSchema(Name name)");
		try {
			return new JenaFeatureType(name.getURI());
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void createSchema(JenaFeatureType featureType) throws IOException {
		// Copied from app-schema
		throw new UnsupportedOperationException();
	}

	@Override
	public ServiceInfo getInfo() {
		// Copied from app-schema
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateSchema(Name typeName, JenaFeatureType featureType)
			throws IOException {
		// Copied from app-schema
		throw new UnsupportedOperationException();
	}

	public String toString() {
		return "JenaDataAccess with Store " + getStore().toString();
	}

}
