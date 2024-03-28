package com.synstim.main;

import java.awt.Font;


import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.scope.AudioScope;
import com.jsyn.scope.AudioScopeProbe;
import com.jsyn.swing.DoubleBoundedRangeSlider;
import com.jsyn.swing.JAppletFrame;
import com.jsyn.swing.PortControllerFactory;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.LinearRamp;
import com.synstim.generate.PulseGenerator;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

public class SynStim {
	public static final int    FONTSIZE_LABEL		= 16;
	public static final int    FONTSIZE_TITLE		= 40;
	public static final String MODES[]				= {"Channel", "FM", "AM", "PWM"};
	public static final String CHANNELS[]			= {"A", "B"};
	public static final String WAVEFORMS_PULSEGEN[] = {"Biphasic Balanced", "Biphasic Impulse", "Sine"};
	public static final String WAVEFORMS_LFO[] 		= {"Sine", "Triangle", "Ramp Up", "Ramp Down", "Pulse"};
	public static final double PULSEGEN_FREQ_MIN 	= 25;
	public static final double PULSEGEN_FREQ_MEDIAN	= 200;
	public static final double PULSEGEN_FREQ_MAX	= 1000;	
	public static final double PULSEGEN_AMP_MIN		= 0.0;
	public static final double PULSEGEN_AMP_MEDIAN	= 0.5;
	public static final double PULSEGEN_AMP_MAX		= 1.0;
	public static final double PULSEGEN_PW_MIN		= 70;
	public static final double PULSEGEN_PW_MEDIAN	= 120;

	public static final double PULSEGEN_PW_MAX		= 500;
	public static final double LFO_FREQ_MIN			= 0.01;
	public static final double LFO_FREQ_MEDIAN		= .5;
	public static final double LFO_FREQ_MAX			= 4;
	public static final double LFO_DC_MIN			= 0.0;
	public static final double LFO_DC_MEDIAN		= 0.5;
	public static final double LFO_DC_MAX			= 1.0;
							   
	public static final double FM_LFO_DEPTH_MIN		= 0;
	public static final double FM_LFO_DEPTH_MEDIAN	= 50;
	public static final double FM_LFO_DEPTH_MAX		= 100;
	public static final double AM_LFO_DEPTH_MIN		= 0.0;
	public static final double AM_LFO_DEPTH_MEDIAN	= 0.5;
	public static final double AM_LFO_DEPTH_MAX		= 1.0;
	public static final double PWM_LFO_DEPTH_MIN	= 50;
	public static final double PWM_LFO_DEPTH_MEDIAN	= 150;
	public static final double PWM_LFO_DEPTH_MAX	= 550;
	public static final double PHASE_MIN			= -1.0;
	public static final double PHASE_MEDIAN			= 0.0;
	public static final double PHASE_MAX			= 1.0;
	  
	Synthesizer syn;
	LineOut lineout;
	AudioScope scope;
	PulseGenerator chanA;
	PulseGenerator chanB;
	
	
	public void start() {
    	UIManager.put("Label.font", new FontUIResource(new Font("Dialog", Font.PLAIN, SynStim.FONTSIZE_TITLE)));
    	UIManager.put("ComboBox.font", new FontUIResource(new Font("Dialog", Font.PLAIN, SynStim.FONTSIZE_LABEL)));
    	UIManager.put("ToggleButton.font", new FontUIResource(new Font("Dialog", Font.PLAIN, SynStim.FONTSIZE_LABEL)));
    	UIManager.put("TitledBorder.font", new FontUIResource(new Font("Dialog", Font.PLAIN, SynStim.FONTSIZE_LABEL)));
		syn = JSyn.createSynthesizer();
		scope = new AudioScope(syn);
		syn.add(lineout = new LineOut());
		syn.add(chanA = new PulseGenerator("A"));
		syn.add(chanB = new PulseGenerator("B"));
		chanA.output.connect(0, lineout.input, 0);
		chanB.output.connect(0, lineout.input, 1);
		syn.start();
    	lineout.start();
    	AudioScopeProbe probeA = scope.addProbe(chanA.output);
		AudioScopeProbe probeB = scope.addProbe(chanB.output);
		probeA.setAutoScaleEnabled(true);
		probeB.setAutoScaleEnabled(true);
		probeA.setVerticalScale(0);
		probeB.setVerticalScale(0);
		scope.setTriggerMode(AudioScope.TriggerMode.NORMAL);
		scope.getView().setControlsVisible(false);
    	scope.start();
	}
	
    public void stop() {
    	syn.stop();
    	lineout.stop();
    	scope.stop();
    }
    
    private JPanel setupGUI() {
    	JPanel panel = new JPanel(new MigLayout("wrap 2, fill"));
		CC centering = new CC();
		centering.alignX("center").spanX();
		panel.add(new JLabel("SynStim"), "span 3, center");
		panel.add(chanA.pulsegen_panel);
		panel.add(chanB.pulsegen_panel);
		panel.add(scope.getView(), centering);
		return panel;
    }

	public static void main(String[] args) {
		SynStim synstim = new SynStim();
		JFrame frame = new JFrame("SynStim v0.6.01");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		synstim.start();
		frame.getContentPane().add(synstim.setupGUI());
        frame.setVisible(true);
        frame.validate();
	}
}
