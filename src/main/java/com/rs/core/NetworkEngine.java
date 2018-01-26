package com.rs.core;

import com.rs.core.cores.DefaultThreadFactory;
import com.rs.core.net.Session;
import com.rs.core.net.SessionLimiter;
import com.rs.core.net.decoders.impl.WorldPacketsDecoder;
import com.rs.core.net.io.InputStream;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author John (FuzzyAvacado) on 12/22/2015.
 */
@Getter(AccessLevel.PRIVATE)
public class NetworkEngine extends SimpleChannelHandler {

    private static final int SERVER_WORKERS_COUNT = Runtime.getRuntime().availableProcessors() >= 6 ? Runtime.getRuntime().availableProcessors() - (Runtime.getRuntime().availableProcessors() >= 12 ? 7 : 5) : 1;

    private final DefaultThreadFactory defaultThreadFactory;
    private final ChannelGroup channels;
    private final SessionLimiter sessionLimiter;
    private final ExecutorService serverWorkerChannelExecutor;
    private final ExecutorService serverBossChannelExecutor;

    @Setter(AccessLevel.PRIVATE)
    private ServerBootstrap bootstrap;

    @Setter(AccessLevel.PRIVATE)
    private int maxConnections;

    public NetworkEngine() {
        this.defaultThreadFactory = new DefaultThreadFactory("Decoder Pool", Thread.MAX_PRIORITY - 1);
        this.serverWorkerChannelExecutor = Runtime.getRuntime().availableProcessors() >= 6 ?
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - (Runtime.getRuntime().availableProcessors() >= 12 ? 7 : 5), getDefaultThreadFactory()) :
                Executors.newSingleThreadExecutor(getDefaultThreadFactory());
        this.serverBossChannelExecutor = Executors.newSingleThreadExecutor(getDefaultThreadFactory());
        this.sessionLimiter = new SessionLimiter();
        this.channels = new DefaultChannelGroup();
    }

    public void init(int maxConnections) {
        setMaxConnections(maxConnections);
        setBootstrap(new ServerBootstrap(new NioServerSocketChannelFactory(getServerBossChannelExecutor(), getServerWorkerChannelExecutor(), SERVER_WORKERS_COUNT)));
        getBootstrap().getPipeline().addLast("handler", this);
        getBootstrap().setOption("reuseAddress", true);
        getBootstrap().setOption("child.tcpNoDelay", true);
        getBootstrap().setOption("child.TcpAckFrequency", true);
        getBootstrap().setOption("child.keepAlive", true);
        getBootstrap().bind(new InetSocketAddress(GameConstants.PORT_ID));
        Logger.info(NetworkEngine.class, "Networking deployed on port " + GameConstants.PORT_ID);
    }

    public int getConnectedChannelsSize() {
        return getChannels() == null ? 0 : getChannels().size();
    }

    public void shutdown() {
        getChannels().close().awaitUninterruptibly();
        getBootstrap().releaseExternalResources();
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        getChannels().add(e.getChannel());
        getSessionLimiter().addConnection(e.getChannel());
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        getChannels().remove(e.getChannel());
        getSessionLimiter().removeConnection(e.getChannel());
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        if (getSessionLimiter().getConnections(e.getChannel()) >= getMaxConnections()) {
            Logger.info(this.getClass(), "Several connections from " + e.getChannel().getRemoteAddress().toString() + "! Stopping them...");
            e.getChannel().close();
            return;
        }
        ctx.setAttachment(new Session(e.getChannel()));
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        Object sessionObject = ctx.getAttachment();
        if (sessionObject != null && sessionObject instanceof Session) {
            Session session = (Session) sessionObject;
            if (session.getDecoder() == null)
                return;
            if (session.getDecoder() instanceof WorldPacketsDecoder)
                session.getWorldPackets().getPlayer().finish();
        }
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        if (!(e.getMessage() instanceof ChannelBuffer)) {
            e.getChannel().close();
            return;
        }
        Object sessionObject = ctx.getAttachment();
        if (sessionObject == null || !(sessionObject instanceof Session)) {
            return;
        }
        try {
            Session session = (Session) sessionObject;
            if (session.getDecoder() == null)
                return;
            ChannelBuffer buf = (ChannelBuffer) e.getMessage();
            if (buf == null) {
                Logger.info(this.getClass(), "Channel Buffer is Null!");
                return;
            }
            buf.markReaderIndex();
            int avail = buf.readableBytes();
            if (avail < 1 || avail > GameConstants.RECEIVE_DATA_LIMIT) {
                Logger.info(this.getClass(), "" + session.getIP() + " has been kicked for packet flooding.");
                session.getChannel().close();
                return;
            }
            byte[] buffer = new byte[avail];
            buf.readBytes(buffer);
            session.getDecoder().decode(new InputStream(buffer));
        } catch (Throwable er) {
            er.printStackTrace();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent ee) {

    }
}
