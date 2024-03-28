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
import com.jsyn.unitgen.UnitOscillator;
import com.synstim.main.SynStim;

import net.miginfocom.swing.MigLayout;

public class LowFrequencyOscillator extends UnitOscillator {
	public String name;
	public DoubleBoundedRangeModel freq_model;
	public RotaryTextController freq_knob;
	public DoubleBoundedRangeModel depth_model;
	public RotaryTextController depth_knob;
	public DoubleBoundedRangeModel dutycycle_model;
	public RotaryTextController dutycycle_knob;
	public UnitInputPort depth;
	public UnitInputPort dutycycle;
	public UnitVariablePort waveformselect;
	public JPanel lfo_panel;
	
	public LowFrequencyOscillator(String name, double fmin, double fmax, double dmin, double dmax, double dcmin, double dcmax) {
		this.name = name;
		addPort(depth = this.amplitude);
		addPort(dutycycle = new UnitInputPort("dutycycle"));
		addPort(waveformselect = new UnitVariablePort("waveformselect"));
		freq_knob = setupLinearPortKnob(this.frequency, freq_knob, freq_model, fmin, fmax, "Frequency");
		depth_knob = setupLinearPortKnob(this.depth, depth_knob, depth_model, dmin, dmax, "Depth");
		dutycycle_knob = setupLinearPortKnob(this.dutycycle, dutycycle_knob, dutycycle_model, dcmin, dcmax, "Duty Cycle");	
		lfo_panel = new JPanel(new MigLayout("wrap 5"));
		lfo_panel.add(freq_knob);
		lfo_panel.add(depth_knob);
		lfo_panel.add(dutycycle_knob);
		this.waveformselect.set(0);
		dutycycle_knob.setVisible(false);
		JComboBox wfsel = new JComboBox(SynStim.WAVEFORMS_LFO);
		wfsel.addItemListener(new ItemListener() {
	    	public void itemStateChanged(ItemEvent itemEvent) {
	    		dutycycle_knob.setVisible(false);
	    		if (wfsel.getSelectedItem() == SynStim.WAVEFORMS_LFO[0]) {
	    			waveformselect.set(0);
	    		}
	    		else if  (wfsel.getSelectedItem() == SynStim.WAVEFORMS_LFO[1]) {
	    			waveformselect.set(1);
	    		}
	    		else if  (wfsel.getSelectedItem() == SynStim.WAVEFORMS_LFO[2]) {
	    			waveformselect.set(2);
	    		}
	    		else if  (wfsel.getSelectedItem() == SynStim.WAVEFORMS_LFO[3]) {
	    			waveformselect.set(3);
	    		}
	    		else if  (wfsel.getSelectedItem() == SynStim.WAVEFORMS_LFO[4]) {
	    			waveformselect.set(4);
	    			dutycycle_knob.setVisible(true);
	    		}
	       		else {
	     			System.err.print("***ERROR***");
	     			System.exit(0);
	     		}
	    	}
    	});
    	lfo_panel.add(wfsel, "aligny bottom");
        JToggleButton onfoff = new JToggleButton(this.name + " ON/OFF");
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
        lfo_panel.add(onfoff, "aligny bottom, wrap");
		
	}
	public void waveform_select(int w) {
		waveformselect.set(w);
	}
    public void on() {
    	setEnabled(true);
    }
    public void off() {
    	setEnabled(false);
    }
    private RotaryTextController setupLinearPortKnob(UnitInputPort port, RotaryTextController knob, DoubleBoundedRangeModel model, double min, double max, String label) {
    	port.setMinimum(min);
    	port.setMaximum(max);
        model = PortModelFactory.createLinearModel(port);
        knob = new RotaryTextController(model, 10);
        knob.setBorder(BorderFactory.createTitledBorder(label));
        knob.setTitle(label);
        return knob;
    }

	@Override
	public void generate(int start, int limit) {
        double[] frequencies = frequency.getValues();
        double[] amplitudes = amplitude.getValues();
        double[] dutycycles = dutycycle.getValues();
        double[] outputs = output.getValues();
        double currentPhase = phase.getValue();
        switch((int) waveformselect.getValue()) {
    	   	case 0: {//sine
	            for (int i = start; i < limit; i++) {
	                double phaseIncrement = convertFrequencyToPhaseIncrement(frequencies[i]);	           
	                currentPhase = incrementWrapPhase(currentPhase, phaseIncrement);
	                outputs[i] = Math.sin(currentPhase * Math.PI) * amplitudes[i];
	            }
	            phase.setValue(currentPhase);
	            break;
    	   	}  
    	   	case 1: {//triangle
				for (int i = start; i < limit; i++) {
					double phaseIncrement = convertFrequencyToPhaseIncrement(frequencies[i]);
					currentPhase = incrementWrapPhase(currentPhase, phaseIncrement);
					outputs[i] = (Math.PI / 2 * Math.asin(Math.sin(Math.PI * currentPhase))) * amplitudes[i];
				}
				phase.setValue(currentPhase);
				break;
    	   	}
    	   	case 2: {//rampup
				for (int i = start; i < limit; i++) {
					double phaseIncrement = convertFrequencyToPhaseIncrement(frequencies[i]);
					currentPhase = incrementWrapPhase(currentPhase, phaseIncrement);
					outputs[i] =  currentPhase * amplitudes[i];
				}	
				phase.setValue(currentPhase);
				break;
    	   	}
    	   	case 3: {//rampdown
    	   		for (int i = start; i < limit; i++) {
				double phaseIncrement = convertFrequencyToPhaseIncrement(frequencies[i]);
				currentPhase = incrementWrapPhase(currentPhase, phaseIncrement);
				outputs[i] =  -1 * currentPhase * amplitudes[i];
				}
				phase.setValue(currentPhase);
				break;
    	   	}
    	   	case 4: {//pulse
    	   		for (int i = start; i < limit; i++) {
				double phaseIncrement = convertFrequencyToPhaseIncrement(frequencies[i]);
				currentPhase = incrementWrapPhase(currentPhase, phaseIncrement);
            	if ((currentPhase + 1) /2 < dutycycles[i]) 
            		outputs[i] =  1 * amplitudes[i];
            	else
            		outputs[i] =  -1 * amplitudes[i]; 
    	   		}
    	   		phase.setValue(currentPhase);
    	   		break;
    	   	}
        }
	}
}
