package com.rs.core.net;

import com.rs.core.file.managers.IPBanFileManager;
import com.rs.core.net.decoders.Decoder;
import com.rs.core.net.decoders.impl.ClientPacketsDecoder;
import com.rs.core.net.decoders.impl.GrabPacketsDecoder;
import com.rs.core.net.decoders.impl.LoginPacketsDecoder;
import com.rs.core.net.decoders.impl.WorldPacketsDecoder;
import com.rs.core.net.encoders.Encoder;
import com.rs.core.net.encoders.impl.GrabPacketsEncoder;
import com.rs.core.net.encoders.impl.LoginPacketsEncoder;
import com.rs.core.net.encoders.impl.WorldPacketsEncoder;
import com.rs.core.net.io.OutputStream;
import com.rs.player.Player;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

public class Session {

	private final Channel channel;
	private Decoder decoder;
	private Encoder encoder;

	public Session(final Channel channel) {
		this.channel = channel;
		if (IPBanFileManager.isBanned(getIP())) {
			channel.disconnect();
			return;
		}
		setDecoder(0);
	}

	public final ChannelFuture write(final OutputStream outStream) {
		if (channel.isConnected()) {
			final ChannelBuffer buffer = ChannelBuffers.copiedBuffer(
					outStream.getBuffer(), 0, outStream.getOffset());
			synchronized (channel) {
				return channel.write(buffer);
			}
		}
		return null;
	}

	public final ChannelFuture write(final ChannelBuffer outStream) {
		if (outStream == null)
			return null;
		if (channel.isConnected()) {
			synchronized (channel) {
				return channel.write(outStream);
			}
		}
		return null;
	}

	public final Channel getChannel() {
		return channel;
	}

	public final Decoder getDecoder() {
		return decoder;
	}

	public final void setDecoder(final int stage) {
		setDecoder(stage, null);
	}

	public GrabPacketsDecoder getGrabPacketsDecoder() {
		return (GrabPacketsDecoder) decoder;
	}

	public final Encoder getEncoder() {
		return encoder;
	}

	public final void setEncoder(final int stage) {
		setEncoder(stage, null);
	}

	public final void setDecoder(final int stage, final Object attachement) {
		switch (stage) {
		case 0:
			decoder = new ClientPacketsDecoder(this);
			break;
		case 1:
			decoder = new GrabPacketsDecoder(this);
			break;
		case 2:
			decoder = new LoginPacketsDecoder(this);
			break;
		case 3:
			decoder = new WorldPacketsDecoder(this, (Player) attachement);
			break;
		case -1:
		default:
			decoder = null;
			break;
		}
	}

	public final void setEncoder(final int stage, final Object attachement) {
		switch (stage) {
		case 0:
			encoder = new GrabPacketsEncoder(this);
			break;
		case 1:
			encoder = new LoginPacketsEncoder(this);
			break;
		case 2:
			encoder = new WorldPacketsEncoder(this, (Player) attachement);
			break;
		case -1:
		default:
			encoder = null;
			break;
		}
	}

	public LoginPacketsEncoder getLoginPackets() {
		return (LoginPacketsEncoder) encoder;
	}

	public GrabPacketsEncoder getGrabPackets() {
		return (GrabPacketsEncoder) encoder;
	}

	public WorldPacketsEncoder getWorldPackets() {
		return (WorldPacketsEncoder) encoder;
	}

	public String getIP() {
		return channel == null ? "" : channel.getRemoteAddress().toString()
				.split(":")[0].replace("/", "");

	}

	public String getLocalAddress() {
		return channel.getLocalAddress().toString();
	}
}
