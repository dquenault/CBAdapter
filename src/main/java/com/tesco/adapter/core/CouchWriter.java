package com.tesco.adapter.core;

import com.tesco.adapter.dao.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.CouchbaseClient;
import org.slf4j.Logger;

import java.util.Date;
import java.util.Random;

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
    private final String thread;
    private ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    public CouchWriter(CouchbaseClient CBClient, String threadNumber){
        this.JSONdata = "";
        this.client = CBClient;
        this.thread = threadNumber;
    }

    public String write(Product product){

          try {
              String productJSON = mapper.writeValueAsString(product);
              if (client.get(product.getId()) == null) {
                      client.set(product.getId(), productJSON).get();
              }
              //Straight write, no get first
              //client.set(product.getId(), productJSON).get();
          } catch (Exception ex){
              return ex.toString();
          }

          return "Success";
    }


    public void run(){
        int count = 1;
        Date d = new Date();
        while (count < 50000) {
            String key = new Random().toString();
            Product prod = new Product(key);
            String result = this.write(prod);

            //logger.info(result);
            count++;
        }
        Date d2 = new Date();
        Long compare = (d2.getTime() - d.getTime()) / 1000;  //get seconds
        logger.info("Thread: " + thread + ". Ingestion complete, time elapsed: " + compare.toString() + " s");
    }

}
