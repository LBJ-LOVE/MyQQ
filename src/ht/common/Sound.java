package ht.common;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class Sound {
	String path=new String("sound\\notify.mid");
	Sequence seq;
	Sequencer midi;
	public Sound(){
		try {
			seq = MidiSystem.getSequence(new File(path));
			midi=MidiSystem.getSequencer();//创建一个音乐设备播放器，初始化参数
			midi.open();//打开播放器
			midi.setSequence(seq);//传入要播放的文件
			midi.start();
			midi.setLoopCount(1);//循环播放次数
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}