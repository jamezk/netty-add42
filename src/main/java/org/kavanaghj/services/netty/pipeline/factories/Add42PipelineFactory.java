package org.kavanaghj.services.netty.pipeline.factories;

import org.kavanaghj.services.netty.handlers.Add42Handler;
import org.kavanaghj.services.netty.handlers.ChannelRegistrationHandler;
import org.kavanaghj.services.netty.handlers.ServerErrorHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import static org.jboss.netty.channel.Channels.pipeline;

/**
 * Created: 04/03/13
 * Time: 10:42
 */
public class Add42PipelineFactory implements ChannelPipelineFactory{
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();
        pipeline.addLast("channel-register", new ChannelRegistrationHandler());
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("content-handler", new Add42Handler());
        pipeline.addLast("error-handler", new ServerErrorHandler());
        return pipeline;
    }
}
