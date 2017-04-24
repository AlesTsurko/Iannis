+ IannisMIDIInManagerController {

  didDisableMIDIInput {

  }

  didUpdateMIDIChannel {arg newValue;

  }

  didSelectNewDevice {arg device;
    this.midiManager.map[\noteOn].free();
    this.midiManager.map[\noteOff].free();

    if (this.midiManager.midiInputEnabled) {
      this.parentController.node.voices??{this.parentController.node.voices = nil!127};
      
      // NoteON
      this.midiManager.map[\noteOn] = MIDIFunc.noteOn({arg val, num, chan, src;
        if (this.midiManager.selectedDevice.uid == src) {
          if ((this.midiManager.channel == 0) || (this.midiManager.channel == (chan+1))) {
            var values = this.parentController.node.getState.getPairs;
            
            values = values.addAll([\freq, num.midicps, \velocity, val]);

            this.parentController.node.voices[num] = Synth(
              this.parentController.synthDefName,
              values, 
              this.parentController.node
            );
          }
        }
      });

      // NoteOFF
      this.midiManager.map[\noteOff] = MIDIFunc.noteOff({arg val, num, chan, src;
        if (this.midiManager.selectedDevice.uid == src) {
          if ((this.midiManager.channel == 0) || (this.midiManager.channel == (chan+1))) {
            this.parentController.node.voices[num].release();
          }
        }
      });

    }
  }

  didUpdateMIDISources {
    var devicesNames = IannisMIDIClient.sources.collect(_.name);
    AppClock.sched(0, {
      midiSourcesMenu.items = devicesNames.insert(0, "None");
    });
  }
}
