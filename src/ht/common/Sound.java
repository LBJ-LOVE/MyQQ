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
			midi=MidiSystem.getSequencer();//����һ�������豸����������ʼ������
			midi.open();//�򿪲�����
			midi.setSequence(seq);//����Ҫ���ŵ��ļ�
			midi.start();
			midi.setLoopCount(1);//ѭ�����Ŵ���
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}