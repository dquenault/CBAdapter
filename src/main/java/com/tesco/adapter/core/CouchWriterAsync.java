package com.tesco.adapter.core;

import com.couchbase.client.CouchbaseClient;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.internal.OperationCompletionListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesco.adapter.dao.Product;
import org.slf4j.Logger;
import java.util.concurrent.CountDownLatch;
import java.util.Date;
import java.util.Random;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created with IntelliJ IDEA.
 * User: xy66
 * Date: 25/02/14
 * Time: 17:32
 * Async version of writer
 */
public class CouchWriterAsync implements Runnable{

      private static final Logger logger = getLogger("CouchWriterAsync");
        private final String JSONdata;
        private final CouchbaseClient client;
        private final String thread;
        private ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

        public CouchWriterAsync(CouchbaseClient CBClient, String threadNumber){
            this.JSONdata = "";
            this.client = CBClient;
            this.thread = threadNumber;
        }

        public void run(){

            final CountDownLatch latch = new CountDownLatch(100000);
            try {
                Date d = new Date();
                for (int i = 0; i < 100000; i++) {
                    String key = new Random().toString();
                    Product prod = new Product(key);
                    String productJSON = mapper.writeValueAsString(prod);

                    OperationFuture<Boolean> future = client.set(prod.getId(),productJSON);
                    future.addListener(new OperationCompletionListener() {
                        @Override
                        public void onComplete(OperationFuture<?> future) throws Exception {
                            latch.countDown();
                        }
                    });
                }
                Date d2 = new Date();
                Long compare = (d2.getTime() - d.getTime()) / 1000;  //get seconds
                logger.info("Thread: " + thread + ". Ingestion complete, time elapsed: " + compare.toString() + " s");
                latch.await();
                Date d3 = new Date();
                Long compare2 = (d3.getTime() - d2.getTime()) / 1000;  //get seconds
                Long compare3 = (d3.getTime() - d.getTime()) / 1000;  //get seconds
                logger.info("Thread: " + thread + ". Async notification complete, total time elapsed: " + compare3.toString()
                        + " s.  Time between async and latch: "  + compare2.toString()  + " s");
            } catch (Exception ex) {
                logger.error("Exception occurred: "+ ex);

            }

            logger.info("Done");
        }

    }
