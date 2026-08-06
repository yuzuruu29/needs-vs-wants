import React from "react";
import { Sequence, staticFile, interpolate } from "remotion";
import { Audio } from "@remotion/media";

const A = (file: string) => staticFile(`audio/${file}`);

/**
 * Full audio design, placed frame-exact on the timeline.
 * Music bed / room tone run continuously; every SFX lands on its beat.
 * Pad ducks slightly under decisive SFX for a boutique master.
 */
export const AudioBed: React.FC = () => {
  return (
    <>
      {/* continuous bed — pad slightly quieter under dense SFX regions via multi-band volume curve */}
      <Sequence from={0} durationInFrames={450}>
        <Audio
          src={A("pad.wav")}
          volume={(f) => {
            // Base bed level; duck under log typing, stamp, budget chime, close chord
            let v = 0.72;
            // hook piano moment — let piano lead
            if (f < 40) v *= interpolate(f, [0, 6, 30, 45], [0.55, 0.55, 0.85, 1], { extrapolateLeft: "clamp", extrapolateRight: "clamp" });
            // typing block
            if (f >= 155 && f <= 220) v *= 0.62;
            // stamp
            if (f >= 238 && f <= 258) v *= 0.5;
            // budget ticks + chime
            if (f >= 270 && f <= 320) v *= 0.58;
            // resolving chord owns the close
            if (f >= 368 && f <= 430) v *= interpolate(f, [368, 380, 430, 449], [0.45, 0.35, 0.55, 0.7], { extrapolateLeft: "clamp", extrapolateRight: "clamp" });
            return v;
          }}
        />
      </Sequence>
      <Sequence from={0} durationInFrames={450}>
        <Audio
          src={A("room.wav")}
          volume={(f) =>
            interpolate(f, [0, 20, 420, 449], [0.35, 0.48, 0.52, 0.42], {
              extrapolateLeft: "clamp",
              extrapolateRight: "clamp",
            })
          }
        />
      </Sequence>

      {/* S1 — HOOK: piano pluck, paper rustle, one muted UI click */}
      <Sequence from={0} durationInFrames={48}>
        <Audio
          src={A("piano-pluck.wav")}
          volume={(f) =>
            interpolate(f, [0, 2, 36, 48], [0, 1, 0.9, 0], {
              extrapolateLeft: "clamp",
              extrapolateRight: "clamp",
            })
          }
        />
      </Sequence>
      <Sequence from={0} durationInFrames={28}>
        <Audio src={A("paper.wav")} volume={0.7} />
      </Sequence>
      <Sequence from={8} durationInFrames={8}>
        <Audio src={A("click.wav")} volume={0.82} />
      </Sequence>

      {/* S2 — SUMMARY: whoosh as arcs draw, subdued cha-ching, CTA pulse click */}
      <Sequence from={66} durationInFrames={32}>
        <Audio src={A("whoosh.wav")} volume={0.78} />
      </Sequence>
      <Sequence from={92} durationInFrames={28}>
        <Audio src={A("ching.wav")} volume={0.88} />
      </Sequence>
      <Sequence from={122} durationInFrames={8}>
        <Audio src={A("click.wav")} volume={0.88} />
      </Sequence>

      {/* S3 — LOG: keyboard taps while typing, decisive stamp on seal */}
      <Sequence from={160} durationInFrames={8}>
        <Audio src={A("tap.wav")} volume={0.88} />
      </Sequence>
      <Sequence from={164} durationInFrames={8}>
        <Audio src={A("tap.wav")} volume={0.86} />
      </Sequence>
      <Sequence from={168} durationInFrames={8}>
        <Audio src={A("tap.wav")} volume={0.9} />
      </Sequence>
      <Sequence from={200} durationInFrames={8}>
        <Audio src={A("tap.wav")} volume={0.88} />
      </Sequence>
      <Sequence from={206} durationInFrames={8}>
        <Audio src={A("tap.wav")} volume={0.86} />
      </Sequence>
      <Sequence from={212} durationInFrames={8}>
        <Audio src={A("tap.wav")} volume={0.9} />
      </Sequence>
      <Sequence from={242} durationInFrames={18}>
        <Audio src={A("stamp.wav")} volume={1} />
      </Sequence>

      {/* S4 — BUDGET: meter ticks, gentle chime when the line is crossed, confirm tap */}
      <Sequence from={274} durationInFrames={6}>
        <Audio src={A("tick.wav")} volume={0.68} />
      </Sequence>
      <Sequence from={284} durationInFrames={6}>
        <Audio src={A("tick.wav")} volume={0.74} />
      </Sequence>
      <Sequence from={294} durationInFrames={6}>
        <Audio src={A("tick.wav")} volume={0.8} />
      </Sequence>
      <Sequence from={300} durationInFrames={32}>
        <Audio src={A("chime.wav")} volume={0.82} />
      </Sequence>
      <Sequence from={328} durationInFrames={10}>
        <Audio src={A("confirm.wav")} volume={0.95} />
      </Sequence>

      {/* S5 — CLOSE: resolving chord + paper settle, hold on room tone */}
      <Sequence from={372} durationInFrames={78}>
        <Audio
          src={A("chord.wav")}
          volume={(f) =>
            interpolate(f, [0, 8, 60, 78], [0, 1, 0.95, 0.35], {
              extrapolateLeft: "clamp",
              extrapolateRight: "clamp",
            })
          }
        />
      </Sequence>
      <Sequence from={384} durationInFrames={36}>
        <Audio src={A("settle.wav")} volume={0.78} />
      </Sequence>
    </>
  );
};
