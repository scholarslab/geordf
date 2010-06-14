package edu.virginia.lib.geordf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.logging.Logger;

import net.opengis.wfs.FeatureCollectionType;

import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.impl.RDFWriterFImpl;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;

public class RdfXmlOutputFormat extends WFSGetFeatureOutputFormat {

	private static final Logger LOGGER = Logging.getLogger("edu.virginia.lib.geordf.RdfXmlOutputFormat");
	Model model = ModelFactory.createMemModelMaker().createModel(this.toString());
	
	public RdfXmlOutputFormat() {
		super("rdfxml");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	 public String getMimeType(Object value, Operation operation)
	            throws ServiceException {
	      // return the mime type of the format here, the parent
	      // class returns 'text/xml'
	      return "application/rdf+xml";
	 }

    @Override
    protected boolean canHandleInternal(Operation operation) {
        return super.canHandleInternal(operation);
    }
    
	@Override
	protected void write(FeatureCollectionType featureCollection,
			OutputStream output, Operation getFeature) throws IOException,
			ServiceException {
		//create a writer
	     BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( output ) );

	     // create a serializer
	     RDFWriter rdfwriter= new RDFWriterFImpl().getWriter("RDF/XML");
	     
	     // assemble a Jena Model with all of the triples
		try {
			FeatureCollection<FeatureType, JenaFeature> fcjena = (FeatureCollection<FeatureType, JenaFeature>) featureCollection
					.getFeature().get(0);
			for (FeatureIterator<JenaFeature> iter = fcjena.features(); iter
					.hasNext();) {
				JenaFeature i = iter.next();
				Model m = ModelFactory.createModelForGraph(i
						.getGraph());
				LOGGER.info("Composed JenaFeature " + i);
				model.add(m);

			}
		} catch (java.lang.ClassCastException e) {
			// that wasn't a JenaFeatureCollection but an ordinary feature
			// collection
			FeatureCollection<FeatureType, SimpleFeature> fcsimple = (FeatureCollection<FeatureType, SimpleFeature>) featureCollection
					.getFeature().get(0);
			ModelMaker m = ModelFactory.createMemModelMaker();
			for (FeatureIterator<SimpleFeature> iter = fcsimple.features(); iter
					.hasNext();) {
				SimpleFeature i = iter.next();
				Model featuremodel = getModelfromSimpleFeature(m, i);
				LOGGER.info("Composed SimpleFeature " + i);
				model.add(featuremodel);
			}
		}
		rdfwriter.write(model, writer, null);
	}

	private Model getModelfromSimpleFeature(ModelMaker maker, SimpleFeature feature) {
		Model m = maker.createFreshModel();
		Resource subject = m.createResource(feature.getID().toString());
		for (Object attr : feature.getAttributes()) {
			Property predicate = m.createProperty(attr.getClass().getName());
			String object = attr.toString();
			m.add(m.createStatement(subject, predicate, object));
		}
		return m;
	}
}
