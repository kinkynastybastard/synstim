package com.synstim.generate;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitVariablePort;
import com.jsyn.swing.DoubleBoundedRangeModel;
import com.jsyn.swing.PortModelFactory;
import com.jsyn.swing.RotaryTextController;
import com.jsyn.unitgen.Add;
import com.jsyn.unitgen.UnitOscillator;
import com.synstim.main.SynStim;

import net.miginfocom.swing.MigLayout;

public class StimOscillator extends UnitOscillator {
	public String name;
	public DoubleBoundedRangeModel freq_model;
	public RotaryTextController freq_knob;
	public Add freq_modulator;
	public DoubleBoundedRangeModel amp_model;
	public RotaryTextController amp_knob;
	public Add amp_modulator;
	public DoubleBoundedRangeModel pulsewidth_model;
	public RotaryTextController pulsewidth_knob; 
	public Add pulsewidth_modulator;
	public UnitInputPort pulsewidth;			//port for adjusting the pulsewidth
	public UnitVariablePort waveformselect;		//port for selecting the waveform
	public JPanel osc_panel;
	
	public StimOscillator(String name, Add fm_mod, Add am_mod, Add pwm_mod) {
		this.name = name;
		addPort(pulsewidth = new UnitInputPort("pulsewidth"));
		addPort(waveformselect = new UnitVariablePort("waveformselect"));	
		waveformselect.set(0);
		fm_mod.inputA.setup(SynStim.PULSEGEN_FREQ_MIN, SynStim.PULSEGEN_FREQ_MEDIAN, SynStim.PULSEGEN_FREQ_MAX);
		am_mod.inputA.setup(SynStim.PULSEGEN_AMP_MIN, SynStim.PULSEGEN_AMP_MEDIAN, SynStim.PULSEGEN_AMP_MAX);
		pwm_mod.inputA.setup(SynStim.PULSEGEN_PW_MIN, SynStim.PULSEGEN_PW_MEDIAN, SynStim.PULSEGEN_PW_MAX);
		freq_knob = setupLinearPortKnob(fm_mod.inputA, freq_model, "Frequency");
		amp_knob = setupLinearPortKnob(am_mod.inputA, amp_model, "Amplitude");
		pulsewidth_knob = setupLinearPortKnob(pwm_mod.inputA, pulsewidth_model, "Pulsewidth");
		fm_mod.output.connect(frequency);
		am_mod.output.connect(amplitude);
		pwm_mod.output.connect(pulsewidth);
		osc_panel = new JPanel(new MigLayout("wrap 5"));
		osc_panel.add(freq_knob);
		osc_panel.add(amp_knob);
		osc_panel.add(pulsewidth_knob);
    	JComboBox wvsel = new JComboBox(SynStim.WAVEFORMS_PULSEGEN);
    	wvsel.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent itemEvent) {
        		if (wvsel.getSelectedItem() == SynStim.WAVEFORMS_PULSEGEN[0]) {
        			waveformselect.set(0);
        		}
        		else if  (wvsel.getSelectedItem() == SynStim.WAVEFORMS_PULSEGEN[1]) {
        			waveformselect.set(1);
        		}
        		else if  (wvsel.getSelectedItem() == SynStim.WAVEFORMS_PULSEGEN[2]) {
        			waveformselect.set(2);
        		}
           		else {
         			System.err.print("***ERROR***");
         			System.exit(0);
         		}
        	}
    	});
    	osc_panel.add(wvsel, "aligny bottom");
        JToggleButton onfoff = new JToggleButton("ON/OFF");
        onfoff.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent itemEvent) {
        		if(isEnabled()) {
        			setEnabled(false);	
        		}
        		else {
        			setEnabled(true);
        		}
        	}
        });
        osc_panel.add(onfoff, "aligny bottom, wrap");
	}
	
	public void waveform_select(int w) {
		waveformselect.set(w);					//0 for balanced pulse, 1 for impulse pulse, 2 for sine
	}
    public void on() {
    	setEnabled(true);
    }
    public void off() {
    	setEnabled(false);
    }
    private RotaryTextController setupLinearPortKnob(UnitInputPort port, DoubleBoundedRangeModel model, String label) {
        model = PortModelFactory.createLinearModel(port);
        RotaryTextController knob = new RotaryTextController(model, 10);
        knob.setBorder(BorderFactory.createTitledBorder(label));
        knob.setTitle(label);
        return knob;
    }
	@Override
	public void generate(int start, int limit) {
        double[] frequencies = frequency.getValues();
        double[] amplitudes = amplitude.getValues();
        double[] widths = pulsewidth.getValues();
        double[] outputs = output.getValues();
        double currentPhase = phase.getValue();
		
        switch((int) waveformselect.getValue()) {        	
    		case 0: {							//bPulse
    			for (int i = start; i < limit; i++) {	    				
	                double phaseIncrement = convertFrequencyToPhaseIncrement(frequencies[i]);
	                currentPhase = incrementWrapPhase(currentPhase, phaseIncrement);
	                double ampl = amplitudes[i];
	                double scale = widths[i] / (1 / frequencies[i] * 1000000);
	            	if ((currentPhase >= -0.5 - scale ) && (currentPhase <= -0.5 + scale ))
	            		outputs[i] = ampl;
	            	else if ((currentPhase >= 0.5 - scale ) && (currentPhase <= 0.5 + scale ))
	            		outputs[i] = -ampl;
	            	else 
	            		outputs[i] = 0;
    			}
	            phase.setValue(currentPhase);
	            break;
    		}
    		case 1: {							//iPulse
	            for (int i = start; i < limit; i++) {
	                double phaseIncrement = convertFrequencyToPhaseIncrement(frequencies[i]);
	                currentPhase = incrementWrapPhase(currentPhase, phaseIncrement);
	                double ampl = amplitudes[i];
	                double scale = widths[i] * 2 / (1 / frequencies[i] * 1000000);
	            	if (currentPhase + 1 < scale )
	            		outputs[i] = ampl;
	            	else if ((currentPhase + 1  >= scale ) && (currentPhase + 1 < scale * 2))
	            		outputs[i] = -ampl;
	            	else 
	            		outputs[i] = 0;
	            }
	            phase.setValue(currentPhase);
	            break;
    		}
    		case 2: {							//sine
	            for (int i = start; i < limit; i++) {
	                double phaseIncrement = convertFrequencyToPhaseIncrement(frequencies[i]);	           
	                currentPhase = incrementWrapPhase(currentPhase, phaseIncrement);
	                outputs[i] = Math.sin(currentPhase * Math.PI) * amplitudes[i];
	            }
	            phase.setValue(currentPhase);
	            break;
    		}
        }
	}
}
