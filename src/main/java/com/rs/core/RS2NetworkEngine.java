package com.rs.core;

import com.rs.RS2Launcher;
import com.rs.core.cores.DefaultThreadFactory;
import com.rs.core.net.Session;
import com.rs.core.net.SessionLimiter;
import com.rs.core.net.decoders.impl.WorldPacketsDecoder;
import com.rs.core.net.io.InputStream;
import com.rs.core.settings.GameConstants;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;
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
public class RS2NetworkEngine extends SimpleChannelHandler {

    private static final int SERVER_WORKERS_COUNT = Runtime.getRuntime().availableProcessors() >= 6 ? Runtime.getRuntime().availableProcessors() - (Runtime.getRuntime().availableProcessors() >= 12 ? 7 : 5) : 1;

    private static ChannelGroup channels;
    private static ServerBootstrap bootstrap;
    private static SessionLimiter sessionLimiter;

    private static ExecutorService serverWorkerChannelExecutor;
    private static ExecutorService serverBossChannelExecutor;

    public static void deploy() {
        final DefaultThreadFactory decoderThreadFactory = new DefaultThreadFactory("Decoder Pool", Thread.MAX_PRIORITY - 1);
        serverWorkerChannelExecutor = Runtime.getRuntime().availableProcessors() >= 6 ? Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - (Runtime.getRuntime().availableProcessors() >= 12 ? 7 : 5), decoderThreadFactory) : Executors.newSingleThreadExecutor(decoderThreadFactory);
        serverBossChannelExecutor = Executors.newSingleThreadExecutor(decoderThreadFactory);
        create();
        Logger.info(RS2NetworkEngine.class, "Networking deployed on port " + GameConstants.PORT_ID);
    }

    private static void create() {
        try {
            sessionLimiter = new SessionLimiter();
            channels = new DefaultChannelGroup();
            bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(serverBossChannelExecutor, serverWorkerChannelExecutor, SERVER_WORKERS_COUNT));
            bootstrap.getPipeline().addLast("handler", new RS2NetworkEngine());
            bootstrap.setOption("reuseAddress", true);
            bootstrap.setOption("child.tcpNoDelay", true);
            bootstrap.setOption("child.TcpAckFrequency", true);
            bootstrap.setOption("child.keepAlive", true);
            bootstrap.bind(new InetSocketAddress(GameConstants.PORT_ID));
        } catch (Throwable t) {
            Logger.handle(t);
            RS2Launcher.shutdown();
        }
    }

    public static int getConnectedChannelsSize() {
        return channels == null ? 0 : channels.size();
    }

    public static void shutdown() {
        channels.close().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        channels.add(e.getChannel());
        sessionLimiter.addConnection(e.getChannel());
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        channels.remove(e.getChannel());
        sessionLimiter.removeConnection(e.getChannel());
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        if (sessionLimiter.getConnections(e.getChannel()) >= SettingsManager.getSettings().MAX_CONNECTIONS) {
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
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent ee) throws Exception {

    }
}
