#!/bin/sh

if [ -z "$1" ]; then
  echo "You must supply a host"
  exit
fi
if [ -n "$2" ]; then
  echo "You must supply a port"
fi

pipelineFactory=org.kavanaghj.services.netty.pipeline.factories.Add42PipelineFactory

echo Starting Server....
java -Xmx512m -Xms512m -cp 'lib/*':lib:. org.kavanaghj.services.netty.HttpServer $1 $2 $pipelineFactory
