package org.kavanaghj.services.netty.handlers;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class ServerErrorHandler extends SimpleChannelHandler {
	
	private static final Logger log = LoggerFactory.getLogger(SimpleChannelHandler.class);	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		log.error("Problem occurred....", e.getCause());
		Channel channel = e.getChannel();
		if(channel != null && channel.isConnected()) {
			HttpResponse err = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            ChannelBuffers.copiedBuffer(e.getCause().getMessage(), Charset.forName("ISO-8859-1"));
            channel.write(err).addListener(ChannelFutureListener.CLOSE);
		}
	}
}
