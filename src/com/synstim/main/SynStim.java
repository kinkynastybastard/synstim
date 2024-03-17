package com.synstim.main;

import java.awt.BorderLayout;
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

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.scope.AudioScope;
import com.jsyn.scope.AudioScopeProbe;
import com.jsyn.swing.JAppletFrame;
import com.jsyn.swing.PortControllerFactory;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.LinearRamp;
import com.synstim.generate.PulseGenerator;
import com.synstim.generate.StimOscillator;

public class SynStim extends JApplet {
	public static final String MODES[]				= {"Channel", "FM", "AM", "PWM"};
	public static final String CHANNELS[]			= {"A", "B"};
	public static final String WAVEFORMS_PULSEGEN[] = {"Biphasic Balanced", "Biphasic Impulse", "Sine"};
	public static final String WAVEFORMS_LFO[] 		= {"Sine", "Triangle", "Ramp Up", "Ramp Down", "Pulse"};
	public static final double PULSEGEN_FREQ_MIN 	= 25;
	public static final double PULSEGEN_FREQ_MEDIAN	= 220;
	public static final double PULSEGEN_FREQ_MAX	= 1000;
	public static final double PULSEGEN_AMP_MIN		= 0.0;
	public static final double PULSEGEN_AMP_MEDIAN	= 0.5;
	public static final double PULSEGEN_AMP_MAX		= 1.0;
	public static final double PULSEGEN_PW_MIN		= 70;
	public static final double PULSEGEN_PW_MEDIAN	= 120;
	public static final double PULSEGEN_PW_MAX		= 500;
	public static final double LFO_FREQ_MIN			= 0.05;
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
	
	public LinearRamp LRA_frequency;
	public LinearRamp LRA_amplitude;
	public LinearRamp LRA_pulsewidth;
	public LinearRamp LRA_phase;
	public LinearRamp LRA_fm_frequency;
	public LinearRamp LRA_fm_depth;
	public LinearRamp LRA_fm_dutycycle;
	public LinearRamp LRA_fm_phase;
	public LinearRamp LRA_am_frequency;
	public LinearRamp LRA_am_depth;
	public LinearRamp LRA_am_dutycycle;
	public LinearRamp LRA_am_phase;
	public LinearRamp LRA_pwm_frequency;
	public LinearRamp LRA_pwm_depth;
	public LinearRamp LRA_pwm_dutycycle;
	public LinearRamp LRA_pwm_phase;
	
	public LinearRamp LRB_frequency;
	public LinearRamp LRB_amplitude;
	public LinearRamp LRB_pulsewidth;
	public LinearRamp LRB_phase;
	public LinearRamp LRB_fm_frequency;
	public LinearRamp LRB_fm_depth;
	public LinearRamp LRB_fm_dutycycle;
	public LinearRamp LRB_fm_phase;
	public LinearRamp LRB_am_frequency;
	public LinearRamp LRB_am_depth;
	public LinearRamp LRB_am_dutycycle;
	public LinearRamp LRB_am_phase;
	public LinearRamp LRB_pwm_frequency;
	public LinearRamp LRB_pwm_depth;
	public LinearRamp LRB_pwm_dutycycle;
	public LinearRamp LRB_pwm_phase;
	  
	Synthesizer syn;
	LineOut lineout;
	AudioScope scope;
	JPanel centerPanel;
	JPanel chanApanel;
	JPanel chanBpanel;
	PulseGenerator chanA;
	PulseGenerator chanB;
	
	@Override
	public void start() {
		syn = JSyn.createSynthesizer();
		syn.add(lineout = new LineOut());
		syn.add(chanA = new PulseGenerator("A"));
		syn.add(chanB = new PulseGenerator("B"));
		chanA.output.connect(0, lineout.input, 0);
		chanB.output.connect(0, lineout.input, 1);
		
		syn.add(LRA_frequency = new LinearRamp());
		syn.add(LRA_amplitude= new LinearRamp());
		syn.add(LRA_pulsewidth= new LinearRamp());
		syn.add(LRA_fm_frequency= new LinearRamp());
		syn.add(LRA_fm_depth= new LinearRamp());
		syn.add(LRA_fm_dutycycle= new LinearRamp());
		syn.add(LRA_fm_phase= new LinearRamp()); //new addition
		syn.add(LRA_am_frequency= new LinearRamp());
		syn.add(LRA_am_depth= new LinearRamp());
		syn.add(LRA_am_dutycycle= new LinearRamp());
		syn.add(LRA_am_phase= new LinearRamp()); //new addition
		syn.add(LRA_pwm_frequency= new LinearRamp());
		syn.add(LRA_pwm_depth= new LinearRamp());
		syn.add(LRA_pwm_dutycycle= new LinearRamp());
		syn.add(LRA_pwm_phase= new LinearRamp()); //new addition
		syn.add(LRB_frequency = new LinearRamp());
		syn.add(LRB_amplitude= new LinearRamp());
		syn.add(LRB_pulsewidth= new LinearRamp());
		syn.add(LRB_fm_frequency= new LinearRamp());
		syn.add(LRB_fm_depth= new LinearRamp());
		syn.add(LRB_fm_dutycycle= new LinearRamp());
		syn.add(LRB_fm_phase= new LinearRamp()); //new addition
		syn.add(LRB_am_frequency= new LinearRamp());
		syn.add(LRB_am_depth= new LinearRamp());
		syn.add(LRB_am_dutycycle= new LinearRamp());
		syn.add(LRB_am_phase= new LinearRamp()); //new addition
		syn.add(LRB_pwm_frequency= new LinearRamp());
		syn.add(LRB_pwm_depth= new LinearRamp());
		syn.add(LRB_pwm_dutycycle= new LinearRamp());
		syn.add(LRB_pwm_phase= new LinearRamp()); //new addition
		
		setupLinearRamps(LRA_frequency, SynStim.PULSEGEN_FREQ_MIN, SynStim.PULSEGEN_FREQ_MEDIAN, SynStim.PULSEGEN_FREQ_MAX, "Channel A Frequency", chanA.frequency);
		setupLinearRamps(LRA_amplitude, SynStim.PULSEGEN_AMP_MIN, SynStim.PULSEGEN_AMP_MEDIAN, SynStim.PULSEGEN_AMP_MAX, "Channel A Amplitude", chanA.amplitude);
		setupLinearRamps(LRA_pulsewidth, SynStim.PULSEGEN_PW_MIN, SynStim.PULSEGEN_PW_MEDIAN, SynStim.PULSEGEN_PW_MAX, "Channel A Pulsewidth", chanA.pulsewidth);
		
		setupLinearRamps(LRA_fm_frequency, SynStim.LFO_FREQ_MIN, SynStim.LFO_FREQ_MEDIAN, SynStim.LFO_FREQ_MAX, "FM Frequency", chanA.fm.frequency);
		setupLinearRamps(LRA_fm_depth, SynStim.FM_LFO_DEPTH_MIN, SynStim.FM_LFO_DEPTH_MEDIAN, SynStim.FM_LFO_DEPTH_MAX, "FM Depth", chanA.fm.depth);
		setupLinearRamps(LRA_fm_dutycycle, SynStim.LFO_DC_MIN, SynStim.LFO_DC_MEDIAN, SynStim.LFO_DC_MAX, "FM Pulse DC", chanA.fm.dutycycle);
		//setupLinearRamps(LRA_fm_phase, SynStim.PHASE_MIN, SynStim.PHASE_MEDIAN, SynStim.PHASE_MAX, "FM Phase", chanA.fm.phase);
		
		setupLinearRamps(LRA_am_frequency, SynStim.LFO_FREQ_MIN, SynStim.LFO_FREQ_MEDIAN, SynStim.LFO_FREQ_MAX, "AM Frequency", chanA.am.frequency);
		setupLinearRamps(LRA_am_depth, SynStim.AM_LFO_DEPTH_MIN, SynStim.AM_LFO_DEPTH_MEDIAN, SynStim.AM_LFO_DEPTH_MAX, "AM Depth", chanA.am.depth);
		setupLinearRamps(LRA_am_dutycycle, SynStim.LFO_DC_MIN, SynStim.LFO_DC_MEDIAN, SynStim.LFO_DC_MAX, "AM Pulse DC", chanA.am.dutycycle);
		
		setupLinearRamps(LRA_pwm_frequency, SynStim.LFO_FREQ_MIN, SynStim.LFO_FREQ_MEDIAN, SynStim.LFO_FREQ_MAX, "PWM Frequency", chanA.pwm.frequency);
		setupLinearRamps(LRA_pwm_depth, SynStim.PWM_LFO_DEPTH_MIN, SynStim.PWM_LFO_DEPTH_MEDIAN, SynStim.PWM_LFO_DEPTH_MAX, "PWM Depth", chanA.pwm.depth);
		setupLinearRamps(LRA_pwm_dutycycle, SynStim.LFO_DC_MIN, SynStim.LFO_DC_MEDIAN, SynStim.LFO_DC_MAX, "PWM Pulse DC", chanA.pwm.dutycycle);
		
		setupLinearRamps(LRB_frequency, SynStim.PULSEGEN_FREQ_MIN, SynStim.PULSEGEN_FREQ_MEDIAN, SynStim.PULSEGEN_FREQ_MAX, "Channel B Frequency", chanB.frequency);
		setupLinearRamps(LRB_amplitude, SynStim.PULSEGEN_AMP_MIN, SynStim.PULSEGEN_AMP_MEDIAN, SynStim.PULSEGEN_AMP_MAX, "Channel B Amplitude", chanB.amplitude);
		setupLinearRamps(LRB_pulsewidth, SynStim.PULSEGEN_PW_MIN, SynStim.PULSEGEN_PW_MEDIAN, SynStim.PULSEGEN_PW_MAX, "Channel B Pulsewidth", chanB.pulsewidth);
		
		setupLinearRamps(LRB_fm_frequency, SynStim.LFO_FREQ_MIN, SynStim.LFO_FREQ_MEDIAN, SynStim.LFO_FREQ_MAX, "FM Frequency", chanB.fm.frequency);
		setupLinearRamps(LRB_fm_depth, SynStim.FM_LFO_DEPTH_MIN, SynStim.FM_LFO_DEPTH_MEDIAN, SynStim.FM_LFO_DEPTH_MAX, "FM Depth", chanB.fm.depth);
		setupLinearRamps(LRB_fm_dutycycle, SynStim.LFO_DC_MIN, SynStim.LFO_DC_MEDIAN, SynStim.LFO_DC_MAX, "FM Pulse DC", chanB.fm.dutycycle);
		
		setupLinearRamps(LRB_am_frequency, SynStim.LFO_FREQ_MIN, SynStim.LFO_FREQ_MEDIAN, SynStim.LFO_FREQ_MAX, "AM Frequency", chanB.am.frequency);
		setupLinearRamps(LRB_am_depth, SynStim.AM_LFO_DEPTH_MIN, SynStim.AM_LFO_DEPTH_MEDIAN, SynStim.AM_LFO_DEPTH_MAX, "AM Depth", chanB.am.depth);
		setupLinearRamps(LRB_am_dutycycle, SynStim.LFO_DC_MIN, SynStim.LFO_DC_MEDIAN, SynStim.LFO_DC_MAX, "AM Pulse DC", chanB.am.dutycycle);
		
		setupLinearRamps(LRB_pwm_frequency, SynStim.LFO_FREQ_MIN, SynStim.LFO_FREQ_MEDIAN, SynStim.LFO_FREQ_MAX, "PWM Frequency", chanB.pwm.frequency);
		setupLinearRamps(LRB_pwm_depth, SynStim.PWM_LFO_DEPTH_MIN, SynStim.PWM_LFO_DEPTH_MEDIAN, SynStim.PWM_LFO_DEPTH_MAX, "PWM Depth", chanB.pwm.depth);
		setupLinearRamps(LRB_pwm_dutycycle, SynStim.LFO_DC_MIN, SynStim.LFO_DC_MEDIAN, SynStim.LFO_DC_MAX, "PWM Pulse DC", chanB.pwm.dutycycle);
		
		syn.start();
    	lineout.start();
    	 
    	setupGUI();
    	scope.start();
	}
	
	public void setupLinearRamps(LinearRamp ramp, double min, double median, double max, String name, UnitInputPort in) {
		ramp.input.setup(min, median, max);
		ramp.input.setName(name);
		ramp.time.set(0.1);
		ramp.output.connect(in);
	}
	
    @Override
    public void stop() {
    	syn.stop();
    	lineout.stop();
    	scope.stop();
    }
    
    private void setupGUI() {
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, new JLabel("SynStim"));
		scope = new AudioScope(syn);
		AudioScopeProbe probeA = scope.addProbe(chanA.output);
		probeA.setAutoScaleEnabled(true);
		probeA.setVerticalScale(0);
		AudioScopeProbe probeB = scope.addProbe(chanB.output);
		probeB.setAutoScaleEnabled(true);
		probeB.setVerticalScale(0);
		scope.setTriggerMode(AudioScope.TriggerMode.NORMAL);
		scope.getView().setControlsVisible(false);
		add(BorderLayout.SOUTH, scope.getView());
		chanApanel = new JPanel();
		chanApanel.setLayout(new GridLayout(4,0));
		chanApanel.add(setupOscGUI(chanA.stim, LRA_frequency, LRA_amplitude, LRA_pulsewidth, LRA_phase, "A"));
		chanApanel.add(setupOscGUI(chanA, LRA_fm_frequency, LRA_fm_depth, LRA_fm_dutycycle, LRA_fm_phase, 0));
		chanApanel.add(setupOscGUI(chanA, LRA_am_frequency, LRA_am_depth, LRA_am_dutycycle, LRA_am_phase, 1));
		chanApanel.add(setupOscGUI(chanA, LRA_pwm_frequency, LRA_pwm_depth, LRA_pwm_dutycycle, LRA_pwm_phase, 2));
		add(BorderLayout.WEST, chanApanel);
	    chanBpanel = new JPanel();
		chanBpanel.setLayout(new GridLayout(4,0));
		chanBpanel.add(setupOscGUI(chanB.stim, LRB_frequency, LRB_amplitude, LRB_pulsewidth, LRB_phase, "B"));
		chanBpanel.add(setupOscGUI(chanB, LRB_fm_frequency, LRB_fm_depth, LRB_fm_dutycycle, LRB_fm_phase, 0));
		chanBpanel.add(setupOscGUI(chanB, LRB_am_frequency, LRB_am_depth, LRB_am_dutycycle, LRB_am_phase, 1));
		chanBpanel.add(setupOscGUI(chanB, LRB_pwm_frequency, LRB_pwm_depth, LRB_pwm_dutycycle, LRB_pwm_phase, 2));
		add(BorderLayout.EAST, chanBpanel);
		validate();
    }
    
    private JPanel setupOscGUI(StimOscillator stim, LinearRamp freq, LinearRamp amp, LinearRamp pw, LinearRamp phase, String chan) {
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	addPortControllers(panel, freq, amp, pw, phase);
    	stim.waveformselect.set(0);
    	JComboBox waveformselect = new JComboBox(SynStim.WAVEFORMS_PULSEGEN);
    	waveformselect.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent itemEvent) {
        		if (waveformselect.getSelectedItem() == SynStim.WAVEFORMS_PULSEGEN[0]) {
        			stim.waveformselect.set(0);
        		}
        		else if  (waveformselect.getSelectedItem() == SynStim.WAVEFORMS_PULSEGEN[1]) {
        			stim.waveformselect.set(1);
        		}
        		else if  (waveformselect.getSelectedItem() == SynStim.WAVEFORMS_PULSEGEN[2]) {
        			stim.waveformselect.set(2);
        		}
           		else {
         			System.err.print("***ERROR***");
         			System.exit(0);
         		}
        	}
    	});
    	panel.add(waveformselect);
        JToggleButton onfoff = new JToggleButton("Channel " + chan + " ON/OFF");
        onfoff.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent itemEvent) {
        		if(stim.isEnabled()) {
        			stim.setEnabled(false);	
        		}
        		else {
        			stim.setEnabled(true);
        		}
        	}
        });
        panel.add(onfoff);
        return panel;
	}
        
    private JPanel setupOscGUI(PulseGenerator pg, LinearRamp freq, LinearRamp depth, LinearRamp dc, LinearRamp ph, int select) {
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	addPortControllers(panel, freq, depth, dc, ph);
    	switch(select) {
	    	case 0 : { //FM
	    		pg.fm.waveformselect.set(0);
	        	JComboBox fm_waveformselect = new JComboBox(SynStim.WAVEFORMS_LFO);
	        	fm_waveformselect.addItemListener(new ItemListener() {
	            	public void itemStateChanged(ItemEvent itemEvent) {
	            		if (fm_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[0]) {
	            			pg.fm.waveformselect.set(0);
	            		}
	            		else if  (fm_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[1]) {
	            			pg.fm.waveformselect.set(1);
	            		}
	            		else if  (fm_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[2]) {
	            			pg.fm.waveformselect.set(2);
	            		}
	            		else if  (fm_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[3]) {
	            			pg.fm.waveformselect.set(3);
	            		}
	            		else if  (fm_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[4]) {
	            			pg.fm.waveformselect.set(4);
	            		}
	               		else {
	             			System.err.print("***ERROR***");
	             			System.exit(0);
	             		}
	            	}
	        	});
	        	panel.add(fm_waveformselect);
	            JToggleButton fm_onfoff = new JToggleButton("FM ON/OFF");
	            fm_onfoff.addItemListener(new ItemListener() {
	            	public void itemStateChanged(ItemEvent itemEvent) {
	            		if(pg.fm.isEnabled()) {
	            			pg.fm_off();			
	            		}
	            		else {
	            			pg.fm_on();
	            		}
	            	}
	            });
	            panel.add(fm_onfoff);
	    	}
	    	break;
	    	case 1 : { //AM
	    		pg.am.waveformselect.set(0);
	        	JComboBox am_waveformselect = new JComboBox(SynStim.WAVEFORMS_LFO);
	        	am_waveformselect.addItemListener(new ItemListener() {
	            	public void itemStateChanged(ItemEvent itemEvent) {
	            		if (am_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[0]) {
	            			pg.am.waveformselect.set(0);
	            		}
	            		else if  (am_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[1]) {
	            			pg.am.waveformselect.set(1);
	            		}
	            		else if  (am_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[2]) {
	            			pg.am.waveformselect.set(2);
	            		}
	            		else if  (am_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[3]) {
	            			pg.am.waveformselect.set(3);
	            		}
	            		else if  (am_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[4]) {
	            			pg.am.waveformselect.set(4);
	            		}
	               		else {
	             			System.err.print("***ERROR***");
	             			System.exit(0);
	             		}
	            	}
	        	});
	        	panel.add(am_waveformselect);
	            JToggleButton am_onfoff = new JToggleButton("AM ON/OFF");
	            am_onfoff.addItemListener(new ItemListener() {
	            	public void itemStateChanged(ItemEvent itemEvent) {
	            		if(pg.am.isEnabled()) {
	            			pg.am_off();			
	            		}
	            		else {
	            			pg.am_on();
	            		}
	            	}
	            });
	            panel.add(am_onfoff);
	    	}
	    	break;
	    	case 2 : { //PWM
	    		pg.pwm.waveformselect.set(0);
	        	JComboBox pwm_waveformselect = new JComboBox(SynStim.WAVEFORMS_LFO);
	        	pwm_waveformselect.addItemListener(new ItemListener() {
	            	public void itemStateChanged(ItemEvent itemEvent) {
	            		if (pwm_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[0]) {
	            			pg.pwm.waveformselect.set(0);
	            		}
	            		else if  (pwm_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[1]) {
	            			pg.pwm.waveformselect.set(1);
	            		}
	            		else if  (pwm_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[2]) {
	            			pg.pwm.waveformselect.set(2);
	            		}
	            		else if  (pwm_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[3]) {
	            			pg.pwm.waveformselect.set(3);
	            		}
	            		else if  (pwm_waveformselect.getSelectedItem() == SynStim.WAVEFORMS_LFO[4]) {
	            			pg.pwm.waveformselect.set(4);
	            		}
	               		else {
	             			System.err.print("***ERROR***");
	             			System.exit(0);
	             		}
	            	}
	        	});
	        	panel.add(pwm_waveformselect);
	            JToggleButton pwm_onfoff = new JToggleButton("PWM ON/OFF");
	            pwm_onfoff.addItemListener(new ItemListener() {
	            	public void itemStateChanged(ItemEvent itemEvent) {
	            		if(pg.pwm.isEnabled()) {
	            			pg.pwm_off();			
	            		}
	            		else {
	            			pg.pwm_on();
	            		}
	            	}
	            });
	            panel.add(pwm_onfoff);
	    	}
	    	break;
    	} 	
        return panel;
	}

    private void addPortControllers(JPanel panel, LinearRamp f, LinearRamp ad, LinearRamp pwdc, LinearRamp ph) {
    	panel.add(PortControllerFactory.createExponentialPortSlider(f.input));
    	panel.add(PortControllerFactory.createExponentialPortSlider(ad.input));
    	panel.add(PortControllerFactory.createExponentialPortSlider(pwdc.input));
    	//panel.add(PortControllerFactory.createExponentialPortSlider(ph.input));
    }

	public static void main(String[] args) {
		SynStim synstim = new SynStim();
		JAppletFrame frame = new JAppletFrame("SynStim v0.5", synstim);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.test();
        frame.validate();

	}

}
