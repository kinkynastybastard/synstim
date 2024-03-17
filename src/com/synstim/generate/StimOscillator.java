package com.synstim.generate;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitVariablePort;
import com.jsyn.unitgen.UnitOscillator;

public class StimOscillator extends UnitOscillator {
	//generates the base audio tone used for estim purposes 
	public UnitInputPort pulsewidth;			//port for adjusting the pulsewidth
	public UnitVariablePort waveformselect;		//port for selecting the waveform
	
	public StimOscillator() {
		addPort(pulsewidth = new UnitInputPort("pulsewidth"));
		addPort(waveformselect = new UnitVariablePort("waveformselect"));
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
