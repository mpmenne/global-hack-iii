package com.globalhack;

/**
 * Created with IntelliJ IDEA.
 * User: mpmenne
 * Date: 12/6/14
 * Time: 8:47 AM
 * To change this template use File | Settings | File Templates.
 */

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExampleRepository extends MongoRepository<Phone, String> {


}
