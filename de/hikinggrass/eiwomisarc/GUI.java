package de.hikinggrass.eiwomisarc;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class GUI {

	private JFrame frame;

	private static Core core;
	private static JTextField textSerial;
	private static JTextField textBaud;
	private static JSlider sliderR;
	private static JSlider sliderG;
	private static JSlider sliderB;
	private static JSlider sliderSpeed;
	private static JTextField textNumberOfLEDStripes;
	private static JSlider sliderRSingle;
	private static JSlider sliderGSingle;
	private static JSlider sliderBSingle;
	private static JComboBox singleColorComboBox;
	private static JButton btnFading;
	private static JSlider sliderFading;
	private static JButton btnDeactivateLED;

	private static void init() {
		byte count = (byte) Integer.parseInt(textNumberOfLEDStripes.getText());

		if (core == null) {
			core = new Core(textSerial.getText(), Integer.parseInt(textBaud.getText()));
			for (int i = 0; i < count * 5; i++) {
				singleColorComboBox.addItem(new StripeLED((byte) ((i / 5) + 1), (byte) ((i % 5) + 1), (byte) 0,
						(byte) 0, (byte) 0, true));
			}
			singleColorComboBox.setEnabled(true);
			sliderRSingle.setEnabled(true);
			sliderGSingle.setEnabled(true);
			sliderBSingle.setEnabled(true);
			btnDeactivateLED.setEnabled(true);

		}

		byte[] buffer = { 0x00, count };

		core.writeToSerialPort(new KaiLED(buffer).getBuffer());
	}

	private static void demo() {
		byte[] buffer = { (byte) 0xff };

		if (core != null) {
			core.writeToSerialPort(new KaiLED(buffer).getBuffer());
		}
	}

	private static void fading() {
		byte speed = (byte) sliderFading.getValue();

		byte[] buffer = { 0x03, speed };

		if (core != null) {
			core.writeToSerialPort(new KaiLED(buffer).getBuffer());
		}
	}

	private static void deactivateFading() {
		// deactivate fading
		byte[] buffer = { 0x13 };

		if (core != null) {
			core.writeToSerialPort(new KaiLED(buffer).getBuffer());
		}
	}

	private static void globalColor() {
		byte r = (byte) sliderR.getValue();
		byte g = (byte) sliderG.getValue();
		byte b = (byte) sliderB.getValue();

		byte[] buffer = { 0x01, r, g, b };

		if (core != null) {
			core.writeToSerialPort(new KaiLED(buffer).getBuffer());
		}
	}

	private static void ledSelected() {
		StripeLED stripeLED = (StripeLED) singleColorComboBox.getSelectedItem();
		String btnText = stripeLED.toString();
		if (stripeLED.isActivated()) {
			btnDeactivateLED.setText(btnText + " deaktivieren");
		} else {
			btnDeactivateLED.setText(btnText + " aktivieren");
		}
	}

	private static void changeLEDState() {
		StripeLED stripeLED = (StripeLED) singleColorComboBox.getSelectedItem();
		if (stripeLED.isActivated()) {
			// disable LED
			stripeLED.setActivated(false);

		} else {
			// enable LED
			stripeLED.setActivated(true);

		}
		if (singleColorComboBox.getItemCount() >= 5) {
			String[] states = new String[singleColorComboBox.getItemCount() / 5];
			for (int i = 0; i < states.length; i++) {
				states[i] = "";
			}
			for (int i = 0; i < singleColorComboBox.getItemCount(); i++) {
				stripeLED = (StripeLED) singleColorComboBox.getItemAt(i);
				if (stripeLED.isActivated()) {
					states[i / 5] += "1";
				} else {
					states[i / 5] += "0";
				}
			}
			byte[] buffer = new byte[states.length + 1];
			buffer[0] = 0x05;
			for (int i = 0; i < states.length; i++) {
				buffer[i + 1] = (byte) Integer.parseInt(states[i], 2);
			}
			if (core != null) {
				core.writeToSerialPort(new KaiLED(buffer).getBuffer());
			}
		}

		ledSelected();
	}

	private static void singleColor() {
		byte r = (byte) sliderRSingle.getValue();
		byte g = (byte) sliderGSingle.getValue();
		byte b = (byte) sliderBSingle.getValue();

		byte[] buffer = new byte[singleColorComboBox.getItemCount() * 3 + 1];
		buffer[0] = 0x04;
		StripeLED stripeLED = (StripeLED) singleColorComboBox.getSelectedItem();
		stripeLED.setColor(r, g, b);
		for (int i = 0; i < singleColorComboBox.getItemCount() * 3; i++) {
			stripeLED = (StripeLED) singleColorComboBox.getItemAt(i / 3);
			switch (i % 3) {
			case 0:
				buffer[i + 1] = stripeLED.getR();
				break;
			case 1:
				buffer[i + 1] = stripeLED.getG();
				break;
			case 2:
				buffer[i + 1] = stripeLED.getB();
				break;
			}

		}

		if (core != null) {
			core.writeToSerialPort(new KaiLED(buffer).getBuffer());
		}
	}

	private static void lauflicht() {
		byte s = (byte) sliderSpeed.getValue();

		s = (byte) sliderSpeed.getValue();
		byte[] buffer = { 0x02, s };

		if (core != null) {
			core.writeToSerialPort(new KaiLED(buffer).getBuffer());
		}
	}

	private static void deactivateLEDSequencer() {
		byte[] buffer = { 0x12 };
		if (core != null) {
			core.writeToSerialPort(new KaiLED(buffer).getBuffer());
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "eiwomisarc");

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("eiwomisarc");
		frame.setBounds(100, 100, 450, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnInitialisierung = new JButton("Initialisierung");
		btnInitialisierung.setBounds(6, 37, 145, 29);
		btnInitialisierung.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				init();
			}
		});
		frame.getContentPane().add(btnInitialisierung);

		sliderR = new JSlider();
		sliderR.setValue(0);
		sliderR.setMaximum(127);
		sliderR.setBounds(146, 78, 190, 29);
		sliderR.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				globalColor();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderR);

		sliderG = new JSlider();
		sliderG.setMaximum(127);
		sliderG.setValue(0);
		sliderG.setBounds(146, 119, 190, 29);
		sliderG.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				globalColor();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderG);

		sliderB = new JSlider();
		sliderB.setValue(0);
		sliderB.setMaximum(127);
		sliderB.setBounds(146, 160, 190, 29);
		sliderB.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				globalColor();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderB);

		JLabel lblR = new JLabel("R");
		lblR.setBounds(136, 83, 15, 16);
		frame.getContentPane().add(lblR);

		JLabel lblG = new JLabel("G");
		lblG.setBounds(136, 119, 15, 16);
		frame.getContentPane().add(lblG);

		JLabel lblB = new JLabel("B");
		lblB.setBounds(136, 158, 15, 16);
		frame.getContentPane().add(lblB);

		sliderSpeed = new JSlider();
		sliderSpeed.setMaximum(255);
		sliderSpeed.setMinimum(1);
		sliderSpeed.setValue(1);
		sliderSpeed.setBounds(146, 197, 190, 29);
		sliderSpeed.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				lauflicht();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderSpeed);

		JLabel lblSpeed = new JLabel("Speed");
		lblSpeed.setBounds(111, 201, 48, 16);
		frame.getContentPane().add(lblSpeed);

		textSerial = new JTextField("/dev/ttyUSB0");
		textSerial.setBounds(153, 36, 110, 28);
		frame.getContentPane().add(textSerial);
		textSerial.setColumns(10);

		textBaud = new JTextField("9600");
		textBaud.setBounds(275, 36, 61, 28);
		frame.getContentPane().add(textBaud);
		textBaud.setColumns(10);

		JLabel lblNewLabel = new JLabel("Serial Port");
		lblNewLabel.setBounds(174, 8, 77, 16);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblBaudrate = new JLabel("Baudrate");
		lblBaudrate.setBounds(275, 8, 61, 16);
		frame.getContentPane().add(lblBaudrate);

		textNumberOfLEDStripes = new JTextField();
		textNumberOfLEDStripes.setText("1");
		textNumberOfLEDStripes.setBounds(354, 36, 61, 28);
		frame.getContentPane().add(textNumberOfLEDStripes);
		textNumberOfLEDStripes.setColumns(10);

		JLabel lblAnzahlLeisten = new JLabel("Anzahl Leisten");
		lblAnzahlLeisten.setBounds(344, 8, 100, 16);
		frame.getContentPane().add(lblAnzahlLeisten);

		JLabel lblGlobaleFarbe = new JLabel("Globale Farbe:");
		lblGlobaleFarbe.setBounds(18, 119, 95, 16);
		frame.getContentPane().add(lblGlobaleFarbe);

		JLabel lblLauflicht = new JLabel("Lauflicht:");
		lblLauflicht.setBounds(18, 197, 61, 16);
		frame.getContentPane().add(lblLauflicht);

		JButton btnLauflichtDeaktivieren = new JButton("Lauflicht deaktivieren");
		btnLauflichtDeaktivieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deactivateLEDSequencer();
			}
		});
		btnLauflichtDeaktivieren.setBounds(6, 229, 180, 29);
		frame.getContentPane().add(btnLauflichtDeaktivieren);

		singleColorComboBox = new JComboBox();
		singleColorComboBox.setEnabled(false);
		singleColorComboBox.setBounds(102, 337, 164, 27);
		singleColorComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ledSelected();
			}
		});
		frame.getContentPane().add(singleColorComboBox);

		JLabel lblNewLabel_1 = new JLabel("Einzelfarbe:");
		lblNewLabel_1.setBounds(18, 341, 84, 16);
		frame.getContentPane().add(lblNewLabel_1);

		sliderRSingle = new JSlider();
		sliderRSingle.setEnabled(false);
		sliderRSingle.setValue(0);
		sliderRSingle.setMaximum(127);
		sliderRSingle.setBounds(28, 361, 190, 29);
		sliderRSingle.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				singleColor();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderRSingle);

		sliderGSingle = new JSlider();
		sliderGSingle.setEnabled(false);
		sliderGSingle.setValue(0);
		sliderGSingle.setMaximum(127);
		sliderGSingle.setBounds(28, 402, 190, 29);
		sliderGSingle.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				singleColor();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderGSingle);

		sliderBSingle = new JSlider();
		sliderBSingle.setEnabled(false);
		sliderBSingle.setValue(0);
		sliderBSingle.setMaximum(127);
		sliderBSingle.setBounds(28, 443, 190, 29);
		sliderBSingle.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				singleColor();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderBSingle);

		JLabel label = new JLabel("R");
		label.setBounds(18, 366, 15, 16);
		frame.getContentPane().add(label);

		JLabel label_1 = new JLabel("G");
		label_1.setBounds(18, 402, 15, 16);
		frame.getContentPane().add(label_1);

		JLabel label_2 = new JLabel("B");
		label_2.setBounds(18, 441, 15, 16);
		frame.getContentPane().add(label_2);

		btnFading = new JButton("Farb\u00FCberg\u00E4nge ausschalten");
		btnFading.setBounds(6, 296, 208, 29);
		btnFading.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deactivateFading();
			}
		});
		frame.getContentPane().add(btnFading);

		sliderFading = new JSlider();
		sliderFading.setMinimum(1);
		sliderFading.setValue(0);
		sliderFading.setMaximum(255);
		sliderFading.setEnabled(false);
		sliderFading.setBounds(225, 270, 190, 29);
		sliderFading.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				fading();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.getContentPane().add(sliderFading);

		JLabel lblNewLabel_2 = new JLabel("Farb\u00FCberg\u00E4nge Geschwindigkeit:");
		lblNewLabel_2.setBounds(16, 270, 208, 16);
		frame.getContentPane().add(lblNewLabel_2);

		JButton btnDemoModus = new JButton("Demo Modus");
		btnDemoModus.setBounds(327, 229, 117, 29);
		btnDemoModus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				demo();
			}
		});
		frame.getContentPane().add(btnDemoModus);

		btnDeactivateLED = new JButton("LED deaktivieren");
		btnDeactivateLED.setEnabled(false);
		btnDeactivateLED.setBounds(212, 361, 226, 29);
		btnDeactivateLED.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				changeLEDState();
			}
		});
		frame.getContentPane().add(btnDeactivateLED);
	}
}
