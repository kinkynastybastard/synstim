package com.synstim.misc;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.swing.DoubleBoundedRangeSlider;
import com.jsyn.swing.PortControllerFactory;
import com.jsyn.unitgen.LinearRamp;
import com.synstim.generate.PulseGenerator;
import com.synstim.main.SynStim;

public class SynStimController {
	public String channel_name;
	public LinearRamp LR_frequency;
	public LinearRamp LR_amplitude;
	public LinearRamp LR_pulsewidth;
	public LinearRamp LR_phase;
	public LinearRamp LR_fm_frequency;
	public LinearRamp LR_fm_depth;
	public LinearRamp LR_fm_dutycycle;
	public LinearRamp LR_fm_phase;
	public LinearRamp LR_am_frequency;
	public LinearRamp LR_am_depth;
	public LinearRamp LR_am_dutycycle;
	public LinearRamp LR_am_phase;
	public LinearRamp LR_pwm_frequency;
	public LinearRamp LR_pwm_depth;
	public LinearRamp LR_pwm_dutycycle;
	public LinearRamp LR_pwm_phase;
	
	public DoubleBoundedRangeSlider DBRS_frequency;
	public DoubleBoundedRangeSlider DBRS_amplitude;
	public DoubleBoundedRangeSlider DBRS_pulsewidth;
	public DoubleBoundedRangeSlider DBRS_phase;
	public DoubleBoundedRangeSlider DBRS_fm_frequency;
	public DoubleBoundedRangeSlider DBRS_fm_depth;
	public DoubleBoundedRangeSlider DBRS_fm_dutycycle;
	public DoubleBoundedRangeSlider DBRS_fm_phase;
	public DoubleBoundedRangeSlider DBRS_am_frequency;
	public DoubleBoundedRangeSlider DBRS_am_depth;
	public DoubleBoundedRangeSlider DBRS_am_dutycycle;
	public DoubleBoundedRangeSlider DBRS_am_phase;
	public DoubleBoundedRangeSlider DBRS_pwm_frequency;
	public DoubleBoundedRangeSlider DBRS_pwm_depth;
	public DoubleBoundedRangeSlider DBRS_pwm_dutycycle;
	public DoubleBoundedRangeSlider DBRS_pwm_phase;
		
	public SynStimController(Synthesizer syn, PulseGenerator channel, String name) {
		this.channel_name = name;
		syn.add(LR_frequency = new LinearRamp());
		syn.add(LR_amplitude= new LinearRamp());
		syn.add(LR_pulsewidth= new LinearRamp());
		syn.add(LR_fm_frequency= new LinearRamp());
		syn.add(LR_fm_depth= new LinearRamp());
		syn.add(LR_fm_dutycycle= new LinearRamp());
		syn.add(LR_fm_phase= new LinearRamp()); 
		syn.add(LR_am_frequency= new LinearRamp());
		syn.add(LR_am_depth= new LinearRamp());
		syn.add(LR_am_dutycycle= new LinearRamp());
		syn.add(LR_am_phase= new LinearRamp()); 
		syn.add(LR_pwm_frequency= new LinearRamp());
		syn.add(LR_pwm_depth= new LinearRamp());
		syn.add(LR_pwm_dutycycle= new LinearRamp());
		syn.add(LR_pwm_phase= new LinearRamp());
		
		setupLinearRamps(LR_frequency, SynStim.PULSEGEN_FREQ_MIN, SynStim.PULSEGEN_FREQ_MEDIAN, SynStim.PULSEGEN_FREQ_MAX, "Frequency", channel.frequency);
		setupLinearRamps(LR_amplitude, SynStim.PULSEGEN_AMP_MIN, SynStim.PULSEGEN_AMP_MEDIAN, SynStim.PULSEGEN_AMP_MAX, "Amplitude", channel.amplitude);
		setupLinearRamps(LR_pulsewidth, SynStim.PULSEGEN_PW_MIN, SynStim.PULSEGEN_PW_MEDIAN, SynStim.PULSEGEN_PW_MAX, "Pulsewidth", channel.pulsewidth);
		
		setupLinearRamps(LR_fm_frequency, SynStim.LFO_FREQ_MIN, SynStim.LFO_FREQ_MEDIAN, SynStim.LFO_FREQ_MAX, "FM Frequency", channel.fm.frequency);
		setupLinearRamps(LR_fm_depth, SynStim.FM_LFO_DEPTH_MIN, SynStim.FM_LFO_DEPTH_MEDIAN, SynStim.FM_LFO_DEPTH_MAX, "FM Depth", channel.fm.depth);
		setupLinearRamps(LR_fm_dutycycle, SynStim.LFO_DC_MIN, SynStim.LFO_DC_MEDIAN, SynStim.LFO_DC_MAX, "FM Pulse DC", channel.fm.dutycycle);
		//setupLinearRamps(LR_fm_phase, SynStim.PHASE_MIN, SynStim.PHASE_MEDIAN, SynStim.PHASE_MAX, "FM Phase", chanA.fm.phase);
		
		setupLinearRamps(LR_am_frequency, SynStim.LFO_FREQ_MIN, SynStim.LFO_FREQ_MEDIAN, SynStim.LFO_FREQ_MAX, "AM Frequency", channel.am.frequency);
		setupLinearRamps(LR_am_depth, SynStim.AM_LFO_DEPTH_MIN, SynStim.AM_LFO_DEPTH_MEDIAN, SynStim.AM_LFO_DEPTH_MAX, "AM Depth", channel.am.depth);
		setupLinearRamps(LR_am_dutycycle, SynStim.LFO_DC_MIN, SynStim.LFO_DC_MEDIAN, SynStim.LFO_DC_MAX, "AM Pulse DC", channel.am.dutycycle);
		//setupLinearRamps(LR_am_phase, SynStim.PHASE_MIN, SynStim.PHASE_MEDIAN, SynStim.PHASE_MAX, "AM Phase", chanA.am.phase);
		
		setupLinearRamps(LR_pwm_frequency, SynStim.LFO_FREQ_MIN, SynStim.LFO_FREQ_MEDIAN, SynStim.LFO_FREQ_MAX, "PWM Frequency", channel.pwm.frequency);
		setupLinearRamps(LR_pwm_depth, SynStim.PWM_LFO_DEPTH_MIN, SynStim.PWM_LFO_DEPTH_MEDIAN, SynStim.PWM_LFO_DEPTH_MAX, "PWM Depth", channel.pwm.depth);
		setupLinearRamps(LR_pwm_dutycycle, SynStim.LFO_DC_MIN, SynStim.LFO_DC_MEDIAN, SynStim.LFO_DC_MAX, "PWM Pulse DC", channel.pwm.dutycycle);
		//setupLinearRamps(LR_pwm_phase, SynStim.PHASE_MIN, SynStim.PHASE_MEDIAN, SynStim.PHASE_MAX, "PWM Phase", chanA.pwm.phase);
		
		DBRS_frequency = PortControllerFactory.createExponentialPortSlider(LR_frequency.input);
		DBRS_amplitude = PortControllerFactory.createExponentialPortSlider(LR_amplitude.input);
		DBRS_pulsewidth = PortControllerFactory.createExponentialPortSlider(LR_pulsewidth.input);
		//DBRS_phase = PortControllerFactory.createExponentialPortSlider(LR_phase.input);
		DBRS_fm_frequency = PortControllerFactory.createExponentialPortSlider(LR_fm_frequency.input);
		DBRS_fm_depth = PortControllerFactory.createExponentialPortSlider(LR_fm_depth.input);
		DBRS_fm_dutycycle = PortControllerFactory.createExponentialPortSlider(LR_fm_dutycycle.input);
		//DBRS_fm_phase = PortControllerFactory.createExponentialPortSlider(LR_fm_phase.input);
		DBRS_am_frequency = PortControllerFactory.createExponentialPortSlider(LR_am_frequency.input);
		DBRS_am_depth = PortControllerFactory.createExponentialPortSlider(LR_am_depth.input);
		DBRS_am_dutycycle = PortControllerFactory.createExponentialPortSlider(LR_am_dutycycle.input);
		//DBRS_am_phase = PortControllerFactory.createExponentialPortSlider(LR_am_phase.input);
		DBRS_pwm_frequency = PortControllerFactory.createExponentialPortSlider(LR_pwm_frequency.input);
		DBRS_pwm_depth = PortControllerFactory.createExponentialPortSlider(LR_pwm_depth.input);
		DBRS_pwm_dutycycle = PortControllerFactory.createExponentialPortSlider(LR_pwm_dutycycle.input);
		//DBRS_pwm_phase = PortControllerFactory.createExponentialPortSlider(LR_pwm_phase.input);
	}
	
	public void setupLinearRamps(LinearRamp ramp, double min, double median, double max, String name, UnitInputPort in) {
		ramp.input.setup(min, median, max);
		ramp.input.setName(name);
		ramp.time.set(0.1);
		ramp.output.connect(in);
	}
}
