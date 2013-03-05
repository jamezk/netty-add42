package org.kavanaghj.services.netty.handlers;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created: 28/02/13
 * Time: 16:29
 */
public class Add42Handler extends SimpleChannelUpstreamHandler {

    private static final Logger log = LoggerFactory.getLogger(Add42Handler.class);

    private static final String ADD42_PATH = "/add42/";


    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        HttpRequest request = (HttpRequest) e.getMessage();
        String url = getPath(request);
        log.debug("Received fetch request for {}", url);
        if(HttpMethod.GET.equals(request.getMethod()) && isRequestingAdd42(url)) {
            add42AndWriteResponse(url, e);
        } else {
            writeNotFoundWithCloseHeader(e);
        }
    }

    private void add42AndWriteResponse(String url, MessageEvent e) {
        Channel channel = e.getChannel();
        long num = Long.parseLong(url.substring(url.indexOf(ADD42_PATH)+ADD42_PATH.length()));
        num += 42;
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
        ChannelBuffer buf = ChannelBuffers.dynamicBuffer(256);
        buf.writeBytes(String.valueOf(num).getBytes());
        response.setContent(buf);
        channel.write(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void writeNotFoundWithCloseHeader(MessageEvent e) {

    }

    private boolean isRequestingAdd42(String url) {
        return url.startsWith(ADD42_PATH);
    }

    private String getPath(HttpRequest request) {
        return request.getUri();
    }


}
