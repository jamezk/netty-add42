netty-add42
===========

Http service built using Netty that adds 42 to a number supplied

From the command line build using Maven i.e.

    mvn clean assembly:assembly

This produces a zip file in the target directory called __netty-adder-server-0.0.1-SNAPSHOT-server.zip__
Unzip this file to an empty directory and then make the expanded start script executable

    chmod 755 start-server.sh

Next execute the start script with as follows:

    ./start-server.sh {host} {port}

Ideally __host__ should be the external facing port of the machine

This should then produce output similar to the following:

    main - INFO -> Starting plain http server on host 181.254.56.201 with ports 8080
    main - INFO -> Using factory org.kavanaghj.services.netty.pipeline.factories.Add42PipelineFactory

Now test by using a browser and navigating to:

    http://{host}:{port}/add42/123

This should produce an output of __165__
