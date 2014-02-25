package com.tesco.adapter.core;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import com.couchbase.client.CouchbaseClient;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;


/**
 * Created with IntelliJ IDEA.
 * User: xy66
 * Date: 17/02/14
 * Time: 09:08
 * Main class which kicks off adapter to load data
 */
public class Controller {

    private static final Logger logger = getLogger("Controller");
    private static CouchWriter writer;
    private static final String mode = "SYNC";

    public Controller() {
    }

    public static void main(String[] args) throws IOException {

        Integer count = 1;
        logger.info("Firing up...");

        //DBCollection productCollection = getCollection(getTempCollectionName(PRODUCT_COLLECTION));
        //DBCollection pimHierarchyCollection = getCollection(getTempCollectionName(PIM_HIERARCHY_COLLECTION));
        //DBCollection commercialHierarchyCollection = getCollection(getTempCollectionName(COMMERCIAL_HIERARCHY_COLLECTION));


        ArrayList<URI> nodes = new ArrayList<URI>();
        // Add one or more nodes of your cluster (exchange the IP with yours)
        //nodes.add(URI.create(couchURI));
        nodes.add(URI.create("http://127.0.0.1:8091/pools"));

        // Try to connect to the client
        CouchbaseClient CBClient = null;
        try {
            CBClient = new CouchbaseClient(nodes, "products", "");
            logger.info("Connected to Couchbase!");
        } catch (Exception e) {
            logger.error("Error connecting to Couchbase: " + e.getMessage());
            System.exit(1);
        }

        logger.info("Ingestion starting - Creating threads");

        //Date d = new Date();
        if (mode == "SYNC")  {
            while (count < 21) {
                   new Thread(new CouchWriter(CBClient,count.toString())).start();
                   count++;
            }
        } else {

            while (count < 11) {
                new Thread(new CouchWriterAsync(CBClient,count.toString())).start();
                count++;
            }
        }
        //Date d2 = new Date();
        //Long compare = (d2.getTime() - d.getTime()) / 1000;  //get seconds
        //logger.info("Total Ingestion complete, time elapsed: " + compare.toString() + " s");
        /*
        Controller controller = new Controller(productCollection, pimHierarchyCollection,
                commercialHierarchyCollection,
                Configuration.getRmsDataPath(),
                Configuration.getRmsEanDataPath(),
                Configuration.getPimHierarchyDataPath(),
                Configuration.getPimUdaDataPath(),
                Configuration.getPimItemDataPath(),
                Configuration.getPimAttachmentDataPath(),
                Configuration.getPimISBNDataPath(),
                Configuration.getSonettoDataPath(),
                Configuration.getSonettoExtraDataPath(),
                Configuration.getCommercialHierarchyDivisionDataPath(),
                Configuration.getCommercialHierarchyGroupDataPath(),
                Configuration.getCommercialHierarchyDepartmentDataPath(),
                Configuration.getCommercialHierarchyClassDataPath(),
                Configuration.getCommercialHierarchySubclassDataPath(),
                Configuration.getFeatureToggles());
                */

       // ControllerCoordination controllerCoordination = new ControllerCoordination(controller);
       // controllerCoordination.processData();

        //Shutdown connection and stop process
        //CBClient.shutdown();
    }
}
