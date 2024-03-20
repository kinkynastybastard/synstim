package com.synstim.generate;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitVariablePort;
import com.jsyn.unitgen.Add;
import com.jsyn.unitgen.Circuit;

public class PulseGenerator extends Circuit {
	public String channel_name;
	public StimOscillator stim;
	public LowFrequencyOscillator fm;
	public LowFrequencyOscillator am;
	public LowFrequencyOscillator pwm;
	
	public UnitInputPort frequency;
	public Add fm_modulator;
	
	public UnitInputPort amplitude;
	public Add am_modulator;
	
	public UnitInputPort pulsewidth;
	public Add pwm_modulator;
	
	public UnitVariablePort phase;
	
	public UnitOutputPort output;
	
	public PulseGenerator(String name) {
		this.channel_name = name;
		this.add(fm = new LowFrequencyOscillator());
		fm.off();
		this.add(am = new LowFrequencyOscillator());
		am.off();
		this.add(pwm = new LowFrequencyOscillator());
		pwm.off();
		this.add(stim = new StimOscillator());
		stim.off();
		//FM
		this.add(fm_modulator = new Add());
		this.addPort(frequency = fm_modulator.inputA);
		this.fm_modulator.output.connect(stim.frequency);
		//AM
		this.add(am_modulator = new Add());
		this.addPort(amplitude = am_modulator.inputA);
		this.am_modulator.output.connect(stim.amplitude);
		//PWM
		this.add(pwm_modulator = new Add());
		this.addPort(pulsewidth = pwm_modulator.inputA);
		this.pwm_modulator.output.connect(stim.pulsewidth);
				
		this.addPort(phase = stim.phase);	
		
		this.addPort(output = stim.output);				
	}
	
	public void waveform_select(int w) {
		stim.waveformselect.set(w);
	}
	
    public void on() {
    	stim.on();
    }
    
    public void off() {
    	stim.off();
    }
    
    public void fm_on() {
    	fm.on();
    	fm.output.connect(fm_modulator.inputB);
    }
    
    public void fm_off() {
    	fm.off();	
    	fm.output.disconnect(fm_modulator.inputB);
    }
    
    public void am_on() {
    	am.on();
    	am.output.connect(am_modulator.inputB);
    }
    
    public void am_off() {
    	am.off();	
    	am.output.disconnect(am_modulator.inputB);
    }
    
    public void pwm_on() {
    	pwm.on();
    	pwm.output.connect(pwm_modulator.inputB);
    }
    
    public void pwm_off() {
    	pwm.off();	
    	pwm.output.disconnect(pwm_modulator.inputB);
    }
    
	
}
