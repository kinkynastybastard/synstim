package com.synstim.generate;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitVariablePort;
import com.jsyn.unitgen.UnitOscillator;

public class LowFrequencyOscillator extends UnitOscillator {
	public UnitInputPort depth;
	public UnitInputPort dutycycle;
	public UnitVariablePort waveformselect;
	
	public LowFrequencyOscillator() {
		addPort(depth = this.amplitude);
		addPort(dutycycle = new UnitInputPort("dutycycle"));
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
