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
    private static int mode;
    private static int threads;

    public Controller() {
    }

    public static void main(String[] args) throws IOException {

        // Check how many arguments were passed in
        if(args.length == 0)
        {
            System.out.println("Proper Usage is: java Controller SYNC/ASYNC NoOfThreads");
            System.exit(0);
        }

        if (args[0] == null){
            mode = 1; //sync
        } else {
            mode = Integer.parseInt(args[0]);
        }

        if (args[1] == null){
            threads = 1;
        } else {
            threads = Integer.parseInt(args[1]);
        }

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
        logger.info("mode " + mode + " " + threads);

        //Date d = new Date();
        if (mode == 1)  {
            for (int i = 0; i < threads; i++) {
                   logger.info("Thread " + i + " started");
                   new Thread(new CouchWriter(CBClient,i,threads)).start();
            }
        } else {  //async (mode 2)

            for (int i = 0; i < threads; i++) {
                new Thread(new CouchWriterAsync(CBClient,i)).start();
            }
        }
        //Date d2 = new Date();
        //Long compare = (d2.getTime() - d.getTime()) / 1000;  //get seconds
        //logger.info("Total Ingestion complete, time elapsed: " + compare.toString() + " s");


        //Shutdown connection and stop process  TODO: Check threads complete
        //CBClient.shutdown();
    }
}
