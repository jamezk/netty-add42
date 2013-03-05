package org.kavanaghj.services.netty.handlers;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import static org.kavanaghj.services.netty.HttpServer.ServerChannelGroup.getAllChannels;
/**
 * Convenience handler for registering channels when they have been created
 * @author james.kavanagh
 *
 */
public class ChannelRegistrationHandler extends SimpleChannelHandler {
	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		getAllChannels().add(e.getChannel());
	}
}
