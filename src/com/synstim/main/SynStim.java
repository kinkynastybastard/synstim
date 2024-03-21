package com.synstim.main;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.scope.AudioScope;
import com.jsyn.scope.AudioScopeProbe;
import com.jsyn.unitgen.LineOut;
import com.synstim.generate.PulseGenerator;
import com.synstim.gui.PulseGeneratorGUI;
import com.synstim.gui.SynStimGUI;
import com.synstim.misc.SynStimController;

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
	JPanel centerPanel;
	JPanel chanApanel;
	JPanel chanBpanel;
	PulseGenerator chanA;
	PulseGenerator chanB;
	SynStimController ctrlChanA;
	SynStimController ctrlChanB;
	PulseGeneratorGUI chanAGUI;
	PulseGeneratorGUI chanBGUI;
	
	
	public void start() {
		syn = JSyn.createSynthesizer();
		
		scope = new AudioScope(syn);
		syn.add(lineout = new LineOut());
		syn.add(chanA = new PulseGenerator("A"));
		syn.add(chanB = new PulseGenerator("B"));
		chanA.output.connect(0, lineout.input, 0);
		chanB.output.connect(0, lineout.input, 1);
		ctrlChanA = new SynStimController(syn, chanA, "A");
		ctrlChanB = new SynStimController(syn, chanB, "B");
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
    

	public static void main(String[] args) {
		SynStim synstim = new SynStim();
		JFrame frame = new JFrame("SynStim v0.5.05");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        synstim.start();
        SynStimGUI stimGUI = new SynStimGUI(synstim.chanA, synstim.chanB, synstim.ctrlChanA, synstim.ctrlChanB, synstim.scope);
        frame.getContentPane().add(stimGUI);
        frame.setVisible(true);
        frame.validate();
	}
}
