package com.synstim.generate;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.Add;
import com.jsyn.unitgen.Circuit;
import com.synstim.main.SynStim;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

public class PulseGenerator extends Circuit {
	public JPanel pulsegen_panel;
	public String channel_name;
	public StimOscillator osc;
	public LowFrequencyOscillator fm;
	public LowFrequencyOscillator am;
	public LowFrequencyOscillator pwm;
	public Add fm_modulator;
	public Add am_modulator;
	public Add pwm_modulator;
	public UnitOutputPort output;
	
	public PulseGenerator(String channel_name) {
		this.channel_name = channel_name;
		//FM
		this.add(fm = new LowFrequencyOscillator("FM", SynStim.LFO_FREQ_MIN, SynStim.LFO_FREQ_MAX, SynStim.FM_LFO_DEPTH_MIN, SynStim.FM_LFO_DEPTH_MAX, SynStim.LFO_DC_MIN, SynStim.LFO_DC_MAX));
		fm.off();
		this.add(fm_modulator = new Add());
		this.fm_modulator.inputB.connect(fm.output);
		
		//AM
		this.add(am = new LowFrequencyOscillator("AM", SynStim.LFO_FREQ_MIN, SynStim.LFO_FREQ_MAX, SynStim.AM_LFO_DEPTH_MIN, SynStim.AM_LFO_DEPTH_MAX, SynStim.LFO_DC_MIN, SynStim.LFO_DC_MAX));
		am.off();
		this.add(am_modulator = new Add());
		this.am_modulator.inputB.connect(am.output);
		
		//PWM
		this.add(pwm = new LowFrequencyOscillator("PWM", SynStim.LFO_FREQ_MIN, SynStim.LFO_FREQ_MAX, SynStim.PWM_LFO_DEPTH_MIN, SynStim.PWM_LFO_DEPTH_MAX, SynStim.LFO_DC_MIN, SynStim.LFO_DC_MAX));
		pwm.off();
		this.add(pwm_modulator = new Add());
		this.pwm_modulator.inputB.connect(pwm.output);
				
		this.add(osc = new StimOscillator(channel_name, fm_modulator, am_modulator, pwm_modulator));
		osc.off();
		this.addPort(output = osc.output);		
		
		//GUI Setup
		CC centering = new CC();
		centering.alignX("center").spanX();
		pulsegen_panel = new JPanel();
		pulsegen_panel.setLayout(new MigLayout("wrap 5"));
		pulsegen_panel.add(new JLabel(channel_name), centering);
		pulsegen_panel.add(osc.osc_panel, "wrap");
		pulsegen_panel.add(fm.lfo_panel, "wrap");
		pulsegen_panel.add(am.lfo_panel, "wrap");
		pulsegen_panel.add(pwm.lfo_panel, "wrap");
	}
	
	public void waveform_select(int w) {
		osc.waveformselect.set(w);
	}
	
    public void on() {
    	osc.on();
    }
    
    public void off() {
    	osc.off();
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
