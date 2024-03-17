package com.synstim.generate;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitVariablePort;
import com.jsyn.unitgen.UnitOscillator;


public class StimOscillator extends UnitOscillator {
	public UnitInputPort pulsewidth;
	public UnitVariablePort waveformselect;
	
	public StimOscillator() {
		addPort(pulsewidth = new UnitInputPort("pulsewidth"));
		addPort(waveformselect = new UnitVariablePort("waveformselect"));
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
	@Override
	public void generate(int start, int limit) {
        double[] frequencies = frequency.getValues();
        double[] amplitudes = amplitude.getValues();
        double[] widths = pulsewidth.getValues();
        double[] outputs = output.getValues();
        double currentPhase = phase.getValue();
		
        switch((int) waveformselect.getValue()) {
    		case 0: {//bPulse
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
    		case 1: {//iPulse
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
    		case 2: {//sine
	            for (int i = start; i < limit; i++) {
	                double phaseIncrement = convertFrequencyToPhaseIncrement(frequencies[i]);	           
	                currentPhase = incrementWrapPhase(currentPhase, phaseIncrement);
	                outputs[i] = Math.sin(currentPhase * Math.PI) * amplitudes[i];
	                //System.out.println(outputs[i]);
	            }
	            phase.setValue(currentPhase);
	            break;
    		}
        }
	}
}
