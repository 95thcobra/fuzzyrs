package com.rs.core.utils;

import com.rs.player.Player;

public class MachineInformation {

    private final int os;
    private final boolean x64Arch;
    private final int osVersion;
    private final int javaVersion;
    private final int javaVersionBuild;
    private final int javaVersionBuild2;
    private final boolean hasApplet;
    private final int heap;
    private final int availableProcessors;
    private final int ram;
    private final int cpuClockFrequency;
    private final int cpuInfo3;
    private final int cpuInfo4;
    private final int cpuInfo5;
    private int osVendor;

    public MachineInformation(final int os, final boolean x64Arch,
                              final int osVersion, final int osVendor, final int javaVersion,
                              final int javaVersionBuild, final int javaVersionBuild2,
                              final boolean hasApplet, final int heap,
                              final int availableProcessor, final int ram,
                              final int cpuClockFrequency, final int cpuInfo3,
                              final int cpuInfo4, final int cpuInfo5) {
        this.os = os;
        this.x64Arch = x64Arch;
        this.osVersion = osVersion;
        this.javaVersion = javaVersion;
        this.javaVersionBuild = javaVersionBuild;
        this.javaVersionBuild2 = javaVersionBuild2;
        this.hasApplet = hasApplet;
        this.heap = heap;
        this.availableProcessors = availableProcessor;
        this.ram = ram;
        this.cpuClockFrequency = cpuClockFrequency;
        this.cpuInfo3 = cpuInfo3;
        this.cpuInfo4 = cpuInfo4;
        this.cpuInfo5 = cpuInfo5;
    }

    public String getVersion() {
        return javaVersion + "." + javaVersionBuild + "." + javaVersionBuild2;
    }

    public void sendSuggestions(final Player player) {
        String suggestion = null;
        String title = null;
        if (javaVersion < 6) {
            title = "Client Issues";
            suggestion = "You seem to be using java version: " + getVersion()
                    + ".<br>You should update to jre6.";
        }/*
         * else if(javaVersionBuild != 0 || javaVersionBuild2 < 31) { title =
		 * "Outdated Java Version"; suggestion =
		 * "Your java seems outdated: "+getVersion
		 * ()+".<br>You should update your to 6.0.31.";
		 *//*
			 * }else if (hasApplet && ((availableProcessors <= (x64Arch ? 2 :
			 * 1)) || ram <= (x64Arch ? 1024 : 512) || cpuClockFrequency <=
			 * 1500)) { title = "Weak Specs"; suggestion =
			 * "Your computer seems to have weak specs. You'd better download desktop client for better perfomance."
			 * ; }
			 */
        if (title != null) {
            player.getInterfaceManager().sendInterface(405);
            player.getPackets().sendIComponentText(405, 16, title);
            player.getPackets().sendIComponentText(405, 17, suggestion);
        }
    }

}
