Test couchbase adapter (multi-threaded)

Run with:
gradle run -Pmyargs=<sync>,<threads>

Where
sync = 1 - Synchronous, 2 = Asynchronous
threads = Total number of threads

e.g.
gradle run -Pmyargs=1,20

Currently processes 1 million inserts.