package com.tesco.adapter.core;

import com.tesco.adapter.dao.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.CouchbaseClient;
import org.slf4j.Logger;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created with IntelliJ IDEA.
 * User: xy66
 * Date: 25/02/14
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public class CouchWriter implements Runnable{
    private static final Logger logger = getLogger("CouchWriter");
    private final String JSONdata;
    private final CouchbaseClient client;
    private final Integer thread;
    private final Integer totalThreads;
    private ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    public CouchWriter(CouchbaseClient CBClient, int threadNumber, int threads){
        this.JSONdata = "";
        this.client = CBClient;
        this.thread = threadNumber;
        this.totalThreads = threads;
    }

    public void write(Product product){

          try {
              String productJSON = mapper.writeValueAsString(product);
              if (client.get(product.getId()) == null) {
                      client.set(product.getId(), productJSON).get();
              }
              //Straight write, no get first
              //client.set(product.getId(), productJSON).get();
          } catch (Exception ex){
              logger.error(ex.toString());
          }
    }


    public void run(){

        Date startTime = new Date();
        int total = 1000000 / totalThreads; // TODO: Needs to be put into parameters

        for (int i = 1; i < total; i++ ) {
            UUID key = UUID.randomUUID();
            Product prod = new Product(key.toString());
            this.write(prod);
        }

        Date endTime = new Date();
        Long durationSeconds = (endTime.getTime() - startTime.getTime()) / 1000;  //get seconds
        logger.info("Thread: " + thread.toString() + ". Ingestion complete, time elapsed: " + durationSeconds.toString() + " s");
    }

}
