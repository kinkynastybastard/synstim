package com.synstim.main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JApplet;
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
import com.synstim.generate.StimOscillator;
import com.synstim.gui.PulseGeneratorGUI;
import com.synstim.misc.SynStimController;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

public class SynStim extends JApplet {
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
	JPanel centerPanel;
	JPanel chanApanel;
	JPanel chanBpanel;
	PulseGenerator chanA;
	PulseGenerator chanB;
	SynStimController ctrlChanA;
	SynStimController ctrlChanB;
	PulseGeneratorGUI chanAGUI;
	PulseGeneratorGUI chanBGUI;
	
	
	@Override
	public void start() {
		syn = JSyn.createSynthesizer();
		syn.add(lineout = new LineOut());
		syn.add(chanA = new PulseGenerator("A"));
		syn.add(chanB = new PulseGenerator("B"));
		chanA.output.connect(0, lineout.input, 0);
		chanB.output.connect(0, lineout.input, 1);
		ctrlChanA = new SynStimController(syn, chanA, "A");
		ctrlChanB = new SynStimController(syn, chanB, "B");
		syn.start();
    	lineout.start();
    	setupGUI();
    	scope.start();
	}
	

	
    @Override
    public void stop() {
    	syn.stop();
    	lineout.stop();
    	scope.stop();
    }
    
    private void setupGUI() {
    	UIManager.put("Label.font", new FontUIResource(new Font("Dialog", Font.PLAIN, SynStim.FONTSIZE_TITLE)));
    	UIManager.put("ComboBox.font", new FontUIResource(new Font("Dialog", Font.PLAIN, SynStim.FONTSIZE_LABEL)));
    	UIManager.put("ToggleButton.font", new FontUIResource(new Font("Dialog", Font.PLAIN, SynStim.FONTSIZE_LABEL)));
    	UIManager.put("TitledBorder.font", new FontUIResource(new Font("Dialog", Font.PLAIN, SynStim.FONTSIZE_LABEL)));
		setLayout(new MigLayout("wrap 2, fill"));
		CC centering = new CC();
		centering.alignX("center").spanX();
		add(new JLabel("SynStim"), "span 3, center");
		add(chanAGUI = new PulseGeneratorGUI(chanA, ctrlChanA, "A"),"cell 1 1");
		add(chanBGUI = new PulseGeneratorGUI(chanB, ctrlChanB, "B"),"cell 2 1");
		scope = new AudioScope(syn);
		AudioScopeProbe probeA = scope.addProbe(chanA.output);
		AudioScopeProbe probeB = scope.addProbe(chanB.output);
		probeA.setAutoScaleEnabled(true);
		probeB.setAutoScaleEnabled(true);
		//probeA.setVerticalScale(0);
		//probeB.setVerticalScale(0);
		scope.setTriggerMode(AudioScope.TriggerMode.NORMAL);
		scope.getView().setControlsVisible(false);
		add(scope.getView(), centering);
		validate();
    }

	public static void main(String[] args) {
		SynStim synstim = new SynStim();
		JAppletFrame frame = new JAppletFrame("SynStim v0.5.01", synstim);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.test();
        frame.validate();
	}
}
