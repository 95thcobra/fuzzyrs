package com.rs.core.utils.tools;

import com.rs.server.file.impl.IPBanFileManager;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.content.Magic;
import com.rs.player.controlers.JailController;
import com.rs.world.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Panel extends JFrame {

    private final JPanel contentPane;
    private final JTextField textField;

    /**
     * Create the frame.
     */
    public Panel() {
        setTitle("Control Panel");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 408, 456);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textField = new JTextField();
        textField.setBounds(87, 25, 194, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        final JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(156, 11, 92, 14);
        contentPane.add(lblUsername);

        final JLabel lblPunishment = new JLabel("Punishment");
        lblPunishment.setBounds(73, 60, 104, 14);
        contentPane.add(lblPunishment);

		/*
         * JProgressBar progressBar = new JProgressBar();
		 * progressBar.setBounds(10, 372, 372, 35);
		 * contentPane.add(progressBar); progressBar.setIndeterminate(true);
		 */

        final JButton btnIpban = new JButton("IPBan");
        btnIpban.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                IPban();
            }
        });
        btnIpban.setBounds(0, 85, 89, 23);
        contentPane.add(btnIpban);

        final JButton btnMute = new JButton("Mute");
        btnMute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                mute();
            }
        });
        btnMute.setBounds(99, 85, 89, 23);
        contentPane.add(btnMute);

        final JButton btnBan = new JButton("Ban");
        btnBan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ban();
            }
        });
        btnBan.setBounds(0, 119, 89, 23);
        contentPane.add(btnBan);

        final JButton btnJail = new JButton("Jail");
        btnJail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                jail();
            }
        });
        btnJail.setBounds(0, 153, 89, 23);
        contentPane.add(btnJail);

        final JButton btnKill = new JButton("Kill");
        btnKill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                kill();
            }
        });
        btnKill.setBounds(99, 119, 89, 23);
        contentPane.add(btnKill);

        final JButton btnFreezeunfreeze = new JButton("Freeze");
        btnFreezeunfreeze.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                freeze();
            }
        });
        btnFreezeunfreeze.setBounds(99, 153, 89, 23);
        contentPane.add(btnFreezeunfreeze);

        final JLabel lblItemManagment = new JLabel("Item Managment");
        lblItemManagment.setBounds(261, 60, 104, 14);
        contentPane.add(lblItemManagment);

        final JButton btnGiveItem = new JButton("Give Item");
        btnGiveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                giveItem();
            }
        });
        btnGiveItem.setBounds(204, 85, 89, 23);
        contentPane.add(btnGiveItem);

        final JButton btnTakeItem = new JButton("Take Item");
        btnTakeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                takeItem();
            }
        });
        btnTakeItem.setBounds(303, 85, 89, 23);
        contentPane.add(btnTakeItem);

        final JButton btnGiveAll = new JButton("Give All");
        btnGiveAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                giveAll();
            }
        });
        btnGiveAll.setBounds(204, 119, 89, 23);
        contentPane.add(btnGiveAll);

        final JButton btnTakeAll = new JButton("Take All");
        btnTakeAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                takeAll();
            }
        });
        btnTakeAll.setBounds(303, 119, 89, 23);
        contentPane.add(btnTakeAll);

        final JLabel lblTeleportation = new JLabel("Teleportation");
        lblTeleportation.setBounds(73, 206, 104, 14);
        contentPane.add(lblTeleportation);

        final JButton btnTeleport = new JButton("Teleport");
        btnTeleport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                teleport();
            }
        });
        btnTeleport.setBounds(57, 231, 89, 23);
        contentPane.add(btnTeleport);

        final JButton btnSendhome = new JButton("Tele All");
        btnSendhome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                teleAll();
            }
        });
        btnSendhome.setBounds(57, 265, 89, 23);
        contentPane.add(btnSendhome);

        final JLabel lblFunPanel = new JLabel("Fun Panel");
        lblFunPanel.setBounds(261, 177, 56, 14);
        contentPane.add(lblFunPanel);

        final JButton btnMakeDance = new JButton("Make Dance");
        btnMakeDance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                makeDance();
            }
        });
        btnMakeDance.setBounds(231, 202, 114, 23);
        contentPane.add(btnMakeDance);

        final JButton btnDanceAll = new JButton("Dance All");
        btnDanceAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                danceAll();
            }
        });
        btnDanceAll.setBounds(231, 236, 114, 23);
        contentPane.add(btnDanceAll);

        final JButton btnForceChat = new JButton("Force Chat");
        btnForceChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                forceChat();
            }
        });
        btnForceChat.setBounds(231, 270, 114, 23);
        contentPane.add(btnForceChat);

        final JButton btnSmite = new JButton("Smite");
        btnSmite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                smite();
            }
        });
        btnSmite.setBounds(231, 304, 114, 23);
        contentPane.add(btnSmite);

        final JButton btnFuckUp = new JButton("Fuck Up");
        btnFuckUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                fuckUp();
            }
        });
        btnFuckUp.setBounds(231, 338, 114, 23);
        contentPane.add(btnFuckUp);

        final JButton btnNewButton = new JButton("Shutdown Server");
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shutdown();
            }
        });
        btnNewButton.setBounds(10, 299, 194, 62);
        contentPane.add(btnNewButton);

    }

    /**
     * Launch the application.
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                final Panel frame = new Panel();
                frame.setVisible(true);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    public String getUsernameInput() {
        return textField.getText();
    }

    public void ban() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            target.getSession().getChannel().close();
            World.removePlayer(target);
            System.out.println("Console: Successfully Banned " + name + ".");
            JOptionPane.showMessageDialog(null, "Successfully Banned " + name,
                    "Console", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out
                    .println("Console: "
                            + Utils.formatPlayerNameForDisplay(name)
                            + " Doesn't Exist");
        }
    }

    public void forceChat() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            final String chat = JOptionPane
                    .showInputDialog("What Do You Want Him To Say?");
            target.setNextForceTalk(new ForceTalk(chat));
            System.out.println("Console: Forcing " + name + " To Say " + chat
                    + "!");
            JOptionPane.showMessageDialog(null, "Forcing " + name + " To Say "
                    + chat + "!", "Console", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out
                    .println("Console: "
                            + Utils.formatPlayerNameForDisplay(name)
                            + " Doesn't Exist");
        }
    }

    public void shutdown() {
        final String sht = JOptionPane.showInputDialog("Shudown Delay?");
        final int delay = Integer.parseInt(sht);
        World.safeShutdown(false, delay);
        System.out.println("Console: Shutting Server Down!");
        JOptionPane.showMessageDialog(null, "Shutting Server Down!", "Console",
                JOptionPane.PLAIN_MESSAGE);
    }

    public void makeDance() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            target.setNextAnimation(new Animation(7071));
            System.out.println("Console: You Have Made " + name + " Dance!");
            JOptionPane.showMessageDialog(null, "You Have Made " + name
                    + " Dance!", "Console", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out
                    .println("Console: "
                            + Utils.formatPlayerNameForDisplay(name)
                            + " Doesn't Exist");
        }
    }

    public void smite() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            target.setPrayerDelay(999999999);
            System.out.println("Console: You Have Made Smited " + name + "!");
            JOptionPane.showMessageDialog(null,
                    "You Have Smited " + name + "!", "Console",
                    JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out
                    .println("Console: "
                            + Utils.formatPlayerNameForDisplay(name)
                            + " Doesn't Exist");
        }
    }

    public void fuckUp() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            target.setPrayerDelay(999999999);
            target.addFreezeDelay(999999999);
            for (int i = 0; i < 10; i++) {
                target.getCombatDefinitions().getBonuses()[i] = 0;
            }
            for (int i = 14; i < target.getCombatDefinitions().getBonuses().length; i++) {
                target.getCombatDefinitions().getBonuses()[i] = 0;
            }
            System.out
                    .println("Console: You Have Made Fucked Up " + name + "!");
            JOptionPane.showMessageDialog(null, "You Have Fucked Up " + name
                    + "!", "Console", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out
                    .println("Console: "
                            + Utils.formatPlayerNameForDisplay(name)
                            + " Doesn't Exist");
        }
    }

    public void IPban() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            final boolean loggedIn = true;
            IPBanFileManager.ban(target, loggedIn);
            System.out.println("Console: Successfully IP Banned " + name + ".");
            JOptionPane.showMessageDialog(null, "Successfully IP Banned "
                    + name, "Console", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist!",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out.println("Console: "
                    + Utils.formatPlayerNameForDisplay(name)
                    + " Doesn't Exist!");
        }
    }

    public void mute() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            target.setMuted(Utils.currentTimeMillis() + (48 * 60 * 60 * 1000));
            System.out.println("Console: Muted " + name + ".");
            JOptionPane.showMessageDialog(null, "Successfully Muted " + name,
                    "Console", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist!",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out.println("Console: "
                    + Utils.formatPlayerNameForDisplay(name)
                    + " Doesn't Exist!");
        }
    }

    public void kill() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            target.applyHit(new Hit(target, target.getHitpoints(),
                    Hit.HitLook.REGULAR_DAMAGE));
            target.stopAll();
            System.out.println("Console: Killed " + name + ".");
            JOptionPane.showMessageDialog(null, "Successfully Killed " + name,
                    "Console", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist!",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out.println("Console: "
                    + Utils.formatPlayerNameForDisplay(name)
                    + " Doesn't Exist!");
        }
    }

    public void jail() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            target.setJailed(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
            target.getControllerManager().startController(JailController.class);
            System.out.println("Console: Jailed " + name + ".");
            JOptionPane.showMessageDialog(null, "Successfully Jailed " + name,
                    "Console", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist!",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out.println("Console: "
                    + Utils.formatPlayerNameForDisplay(name)
                    + " Doesn't Exist!");
        }
    }

    public void freeze() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            target.addFreezeDelay(999999999);
            System.out.println("Console: Frozen " + name + ".");
            JOptionPane.showMessageDialog(null, "Successfully Frozen " + name,
                    "Console", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist!",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out.println("Console: "
                    + Utils.formatPlayerNameForDisplay(name)
                    + " Doesn't Exist!");
        }
    }

    public void giveItem() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            final String id = JOptionPane.showInputDialog("Item Id");
            final String quantity = JOptionPane.showInputDialog("Item Amount");
            final int item = Integer.parseInt(id);
            final int amount = Integer.parseInt(quantity);
            target.getInventory().addItem(item, amount);
            System.out.println("Console: Given Item To " + name + ".");
            JOptionPane.showMessageDialog(null, "Given Item To " + name,
                    "Console", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist!",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out.println("Console: "
                    + Utils.formatPlayerNameForDisplay(name)
                    + " Doesn't Exist!");
        }
    }

    public void teleport() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            final String x = JOptionPane.showInputDialog("Coordinate X");
            final String y = JOptionPane.showInputDialog("Coordinate Y");
            final String h = JOptionPane.showInputDialog("Height Level");
            final int coordx = Integer.parseInt(x);
            final int coordy = Integer.parseInt(y);
            final int height = Integer.parseInt(h);
            Magic.sendNormalTeleportSpell(target, 0, 0, new WorldTile(coordx,
                    coordy, height));
            System.out.println("Console: Teleported " + name + " To " + coordx
                    + ", " + coordy + ", " + height);
            JOptionPane.showMessageDialog(null, "Console: Teleported " + name
                            + " To " + coordx + ", " + coordy + ", " + height,
                    "Console", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist!",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out.println("Console: "
                    + Utils.formatPlayerNameForDisplay(name)
                    + " Doesn't Exist!");
        }
    }

    public void teleAll() {
        final String x = JOptionPane.showInputDialog("Coordinate X");
        final String y = JOptionPane.showInputDialog("Coordinate Y");
        final String h = JOptionPane.showInputDialog("Height Level");
        final int coordx = Integer.parseInt(x);
        final int coordy = Integer.parseInt(y);
        final int height = Integer.parseInt(h);
        for (final Player teleall : World.getPlayers()) {
            Magic.sendNormalTeleportSpell(teleall, 0, 0, new WorldTile(coordx,
                    coordy, height));
        }
        System.out.println("Console: Teleported Everyone To " + coordx + ", "
                + coordy + ", " + height);
        JOptionPane.showMessageDialog(null, "Console: Teleported Everyone To "
                        + coordx + ", " + coordy + ", " + height, "Console",
                JOptionPane.PLAIN_MESSAGE);
    }

    public void danceAll() {
        for (final Player danceAll : World.getPlayers()) {
            danceAll.setNextAnimation(new Animation(7071));
        }
        System.out.println("Console: Making Everyone Dance!");
        JOptionPane.showMessageDialog(null, "Console: Making Everyone Dance!",
                "Console", JOptionPane.PLAIN_MESSAGE);
    }

    public void takeItem() {
        final String name = getUsernameInput();
        final Player target = World.getPlayerByDisplayName(name);
        if (target != null) {
            final String id = JOptionPane.showInputDialog("Item Id");
            final String quantity = JOptionPane.showInputDialog("Item Amount");
            final int item = Integer.parseInt(id);
            final int amount = Integer.parseInt(quantity);
            target.getInventory().deleteItem(item, amount);
            System.out.println("Console: Taken Item " + item + " From " + name
                    + ".");
            JOptionPane.showMessageDialog(null, "Taken Item " + item + " From "
                    + name, "Console", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, name + " Doesn't Exist!",
                    "Console", JOptionPane.ERROR_MESSAGE);
            System.out.println("Console: "
                    + Utils.formatPlayerNameForDisplay(name)
                    + " Doesn't Exist!");
        }
    }

    public void giveAll() {
        final String id = JOptionPane.showInputDialog("Item Id");
        final String quantity = JOptionPane.showInputDialog("Item Amount");
        final int item = Integer.parseInt(id);
        final int amount = Integer.parseInt(quantity);
        for (final Player giveall : World.getPlayers()) {
            giveall.getInventory().addItem(item, amount);
        }
        System.out.println("Console: Given Item " + item + " To All Players");
        JOptionPane.showMessageDialog(null, "Given Item " + item
                + " To All Players", "Console", JOptionPane.PLAIN_MESSAGE);
    }

    public void takeAll() {
        final String id = JOptionPane.showInputDialog("Item Id");
        final String quantity = JOptionPane.showInputDialog("Item Amount");
        final int item = Integer.parseInt(id);
        final int amount = Integer.parseInt(quantity);
        for (final Player takeall : World.getPlayers()) {
            takeall.getInventory().deleteItem(item, amount);
        }
        System.out.println("Console: Taken Item " + item + " From All Players");
        JOptionPane.showMessageDialog(null, "Taken Item " + item
                + " From All Players", "Console", JOptionPane.PLAIN_MESSAGE);
    }
}