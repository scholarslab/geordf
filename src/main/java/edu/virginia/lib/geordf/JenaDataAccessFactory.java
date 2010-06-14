package edu.virginia.lib.geordf;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

import com.hp.hpl.jena.sdb.SDBFactory;

//Test to use RDF to deploy non-tabular data to OWS

public class JenaDataAccessFactory implements DataAccessFactory {

	public static final String DBTYPE_STRING = "RDF";

	public static final DataAccessFactory.Param DBTYPE = new DataAccessFactory.Param(
			"dbtype", String.class, "Fixed value '" + DBTYPE_STRING + "'",
			true, DBTYPE_STRING);

	public static final DataAccessFactory.Param SDBDESC = new DataAccessFactory.Param(
			"sdbdesc", String.class,
			"Filepath to an Jena SDB store description file", true);

	public JenaDataAccessFactory() {
	}

	@Override
	public DataAccess<? extends FeatureType, ? extends Feature> createDataStore(
			Map<String, Serializable> params) throws IOException {
		String sdbdesc = (String) JenaDataAccessFactory.SDBDESC.lookUp(params);
		return new JenaDataAccess(SDBFactory.connectStore(sdbdesc));
	}

	@Override
	public boolean canProcess(Map<String, Serializable> params) {
		if (params == null) {
            return false; // throw new NullPointerException("params");
        }
		if (params.containsKey(SDBDESC.key)) {
			return true;
		}
		return false;
	}

	@Override
	public Param[] getParametersInfo() {
		return new DataStoreFactorySpi.Param[] { JenaDataAccessFactory.DBTYPE,
				JenaDataAccessFactory.SDBDESC };
	}

	@Override
	public String getDisplayName() {
		return "Jena RDF SDB Datastore";
	}

	@Override
	public boolean isAvailable() {
		// Copied from app-schema
		return true;
	}

	public DataStore createNewDataStore(Map params) throws IOException {
		// we aren't yet supporting creating new stores from the API
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDescription() {
		return "Uses a Jena SDB triplestore to produce features";
	}

	@Override
	public Map<Key, ?> getImplementationHints() {
		// Copied from app-schema
		return Collections.emptyMap();
	}

}
