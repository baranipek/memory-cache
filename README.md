# memory-cache
Implement an in memory cache. The cache’s purpose is to allow the results of expensive or long running calculations or data retrievals to be stored in memory to avoid unnecessary duplication of effort.

The cache must provide a method to request the value for a key. 
If the cache does not contain the requested data it should load it from an underlying data source, and then cache it for future requests.
If the cache exceeds a set number of items in size then the least recently requested items should be removed.
It should use generics so it can be instantiated for different types of keys and values.

# Test 

Please run mvn clean test 
