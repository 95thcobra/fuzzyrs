package com.rs.server.net.encoders.impl;

import com.rs.server.net.Session;
import com.rs.server.net.encoders.Encoder;
import com.rs.server.net.io.OutputStream;
import com.rs.player.Player;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

public final class LoginPacketsEncoder extends Encoder {

	public LoginPacketsEncoder(final Session connection) {
		super(connection);
	}

	public final void sendStartUpPacket() {
		final OutputStream stream = new OutputStream(1);
		stream.writeByte(0);
		session.write(stream);
	}

	public final void sendClientPacket(final int opcode) {
		final OutputStream stream = new OutputStream(1);
		stream.writeByte(opcode);
		final ChannelFuture future = session.write(stream);
		if (future != null) {
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			session.getChannel().close();
		}
	}

	public final void sendLoginDetails(final Player player) {
		final OutputStream stream = new OutputStream();
		stream.writePacketVarByte(null, 2);
		stream.writeByte(player.getRank().getMessageIcon());
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeByte(1);
		stream.writeByte(0);
		stream.writeShort(player.getIndex());
		stream.writeByte(1);
		stream.write24BitInteger(0);
		stream.writeByte(1); // is member worldtask
		stream.writeString(player.getDisplayName());
		stream.endPacketVarByte();
		session.write(stream);
	}
}
