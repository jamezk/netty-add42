package org.kavanaghj.services.netty;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import static org.kavanaghj.services.netty.HttpServer.ServerChannelGroup.getAllChannels;

public class HttpServer {
	
	private static final Logger log = LoggerFactory.getLogger(HttpServer.class);
	
	private final int port;
	private final String host;
    private final String channelPipelineFactoryName;

	private ChannelFactory channelFactory;

	public HttpServer(String host, int port, String channelFactory) {
		this.host = host;
        this.port = port;
        this.channelPipelineFactoryName = channelFactory;
	}
	
	public void run() {
		channelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
															Executors.newCachedThreadPool());
		log.info("Starting plain http server on host {} with ports {}", host, port);
        log.info("Using factory {}", channelPipelineFactoryName);
		createBootStrapper(port, host, channelFactory);
	}

	private ServerBootstrap createBootStrapper(int port,
                                               String host,
									           ChannelFactory channelFactory) {
		ServerBootstrap bootstrapper = null;
		bootstrapper = new ServerBootstrap(channelFactory);
		bootstrapper.setPipelineFactory(createPipelineFactory());
		getAllChannels().add(bootstrapper.bind(new InetSocketAddress(host, port)));

		return bootstrapper;
	}
	
	public void shutdown() {
		ChannelGroupFuture future = getAllChannels().close();
		future.awaitUninterruptibly();
		channelFactory.releaseExternalResources();
	}

    private ChannelPipelineFactory createPipelineFactory() {
        final ChannelPipelineFactory factory;
        try {
            factory = (ChannelPipelineFactory) Class.forName(channelPipelineFactoryName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Problem instantiating factory class >> "+ channelPipelineFactoryName,e);
        }
        return factory;
    }
	
	public static class ServerChannelGroup {
		private static ChannelGroup channels;
		
		static {
			channels = new DefaultChannelGroup("content-server");
		}
		
		public static ChannelGroup getAllChannels() {
			return channels;
		}
	}

    public static void main(String[] args) throws UnknownHostException {
        String host = args[0].trim();
        int port = Integer.parseInt(args[1]);
        String channelFactoryName = args[2];

        final HttpServer server = new HttpServer(host, port, channelFactoryName);
        server.run();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                server.shutdown();
            }
        });
    }




}
