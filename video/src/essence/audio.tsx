import React from "react";
import { Sequence, interpolate, staticFile } from "remotion";
import { Audio } from "@remotion/media";
import { AD, PAYDAY, PETSA, REEL, SQ } from "./timing";

/**
 * Soft boutique audio for "The Choice" campaign — same sample library and
 * mixing rules as the D70 promo bed: sparse real UI samples over a quiet
 * music + pad bed, nothing harsh, hits sit slightly above the bed.
 */

const S = (file: string) => staticFile(`audio/soft/${file}`);

const CLAMP = {
  extrapolateLeft: "clamp" as const,
  extrapolateRight: "clamp" as const,
};

export const Hit: React.FC<{
  at: number;
  file: string;
  volume?: number;
  frames?: number;
}> = ({ at, file, volume = 0.36, frames = 28 }) => (
  <Sequence from={at} durationInFrames={frames}>
    <Audio
      src={S(file)}
      volume={(f) => {
        const env = interpolate(f, [0, 2, frames - 8, frames - 1], [0, 1, 1, 0], CLAMP);
        return volume * env;
      }}
    />
  </Sequence>
);

/** Continuous music + pad bed with head/tail fades and optional duck spans. */
export const Bed: React.FC<{
  total: number;
  music?: number;
  pad?: number;
  ducks?: Array<[number, number]>;
}> = ({ total, music = 0.19, pad = 0.27, ducks = [] }) => (
  <>
    <Sequence durationInFrames={total}>
      <Audio
        src={S("soft-music.wav")}
        loop
        volume={(f) => {
          let v = interpolate(
            f,
            [0, 26, total - 46, total - 1],
            [0, music, music, 0.02],
            CLAMP,
          );
          for (const [a, b] of ducks) {
            if (f >= a && f <= b) v *= 0.7;
          }
          return v;
        }}
      />
    </Sequence>
    <Sequence durationInFrames={total}>
      <Audio
        src={S("soft-pad.wav")}
        loop
        volume={(f) =>
          interpolate(f, [0, 30, total - 40, total - 1], [0.1, pad, pad, 0.04], CLAMP)
        }
      />
    </Sequence>
  </>
);

/* ────────────────────────────────────────────────────────────────────────── */

export const EssenceAdAudio: React.FC = () => (
  <>
    <Bed
      total={AD.total}
      ducks={[
        [AD.log + 90, AD.log + 130],
        [AD.guard + 10, AD.guard + 40],
      ]}
    />

    {/* HOOK — two stamps land */}
    <Hit at={4} file="s-whoosh.wav" volume={0.3} frames={24} />
    <Hit at={AD.hook + 38} file="s-switch.wav" volume={0.42} frames={18} />
    <Hit at={AD.hook + 52} file="s-switch.wav" volume={0.46} frames={18} />
    <Hit at={AD.hook + 94} file="s-softclick.wav" volume={0.3} frames={14} />

    {/* ESSENCE — strike, trainer line, receipt verdict */}
    <Hit at={AD.essence + 2} file="s-whoosh.wav" volume={0.26} frames={22} />
    <Hit at={AD.essence + 48} file="s-softclick.wav" volume={0.34} frames={14} />
    <Hit at={AD.essence + 60} file="s-ding.wav" volume={0.28} frames={34} />
    <Hit at={AD.essence + 74} file="s-seal.wav" volume={0.38} frames={38} />

    {/* LOG — typing, the call, the seal */}
    <Hit at={AD.log + 2} file="s-whoosh.wav" volume={0.28} frames={22} />
    <Hit at={AD.log + 18} file="s-tap.wav" volume={0.4} frames={22} />
    <Hit at={AD.log + 28} file="s-tap.wav" volume={0.42} frames={22} />
    <Hit at={AD.log + 38} file="s-tap.wav" volume={0.4} frames={22} />
    <Hit at={AD.log + 56} file="s-tap.wav" volume={0.42} frames={22} />
    <Hit at={AD.log + 66} file="s-tap.wav" volume={0.4} frames={22} />
    <Hit at={AD.log + 78} file="s-switch.wav" volume={0.48} frames={18} />
    <Hit at={AD.log + 96} file="s-seal.wav" volume={0.44} frames={40} />
    <Hit at={AD.log + 116} file="s-pop.wav" volume={0.34} frames={22} />

    {/* SEE — the ring resolves */}
    <Hit at={AD.see + 2} file="s-whoosh.wav" volume={0.28} frames={22} />
    <Hit at={AD.see + 48} file="s-ding.wav" volume={0.34} frames={36} />
    <Hit at={AD.see + 74} file="s-softclick.wav" volume={0.3} frames={14} />

    {/* GUARD — dialog, conscious confirm */}
    <Hit at={AD.guard + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={AD.guard + 16} file="s-pop.wav" volume={0.42} frames={22} />
    <Hit at={AD.guard + 72} file="s-click.wav" volume={0.44} frames={16} />
    <Hit at={AD.guard + 86} file="s-seal.wav" volume={0.4} frames={38} />

    {/* PROOF — four soft card ticks */}
    <Hit at={AD.proof + 4} file="s-whoosh.wav" volume={0.24} frames={20} />
    <Hit at={AD.proof + 14} file="s-softclick.wav" volume={0.34} frames={14} />
    <Hit at={AD.proof + 26} file="s-softclick.wav" volume={0.36} frames={14} />
    <Hit at={AD.proof + 38} file="s-softclick.wav" volume={0.38} frames={14} />
    <Hit at={AD.proof + 50} file="s-softclick.wav" volume={0.4} frames={14} />

    {/* CLOSE — seal, chord, sheen */}
    <Hit at={AD.close + 10} file="s-seal.wav" volume={0.42} frames={40} />
    <Hit at={AD.close + 16} file="s-success.wav" volume={0.34} frames={52} />
    <Hit at={AD.close + 52} file="s-ding.wav" volume={0.3} frames={36} />
    <Hit at={AD.close + 70} file="s-whoosh.wav" volume={0.18} frames={22} />
  </>
);

export const EssenceReelAudio: React.FC = () => (
  <>
    <Bed total={REEL.total} ducks={[[REEL.log + 90, REEL.log + 130]]} />

    <Hit at={4} file="s-whoosh.wav" volume={0.3} frames={24} />
    <Hit at={REEL.hook + 38} file="s-switch.wav" volume={0.42} frames={18} />
    <Hit at={REEL.hook + 52} file="s-switch.wav" volume={0.46} frames={18} />

    <Hit at={REEL.log + 2} file="s-whoosh.wav" volume={0.28} frames={22} />
    <Hit at={REEL.log + 18} file="s-tap.wav" volume={0.4} frames={22} />
    <Hit at={REEL.log + 28} file="s-tap.wav" volume={0.42} frames={22} />
    <Hit at={REEL.log + 38} file="s-tap.wav" volume={0.4} frames={22} />
    <Hit at={REEL.log + 56} file="s-tap.wav" volume={0.42} frames={22} />
    <Hit at={REEL.log + 78} file="s-switch.wav" volume={0.48} frames={18} />
    <Hit at={REEL.log + 96} file="s-seal.wav" volume={0.44} frames={40} />

    <Hit at={REEL.see + 2} file="s-whoosh.wav" volume={0.28} frames={22} />
    <Hit at={REEL.see + 48} file="s-ding.wav" volume={0.34} frames={36} />

    <Hit at={REEL.guard + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={REEL.guard + 16} file="s-pop.wav" volume={0.42} frames={22} />
    <Hit at={REEL.guard + 72} file="s-click.wav" volume={0.44} frames={16} />
    <Hit at={REEL.guard + 86} file="s-seal.wav" volume={0.4} frames={38} />

    <Hit at={REEL.close + 10} file="s-seal.wav" volume={0.42} frames={40} />
    <Hit at={REEL.close + 16} file="s-success.wav" volume={0.34} frames={52} />
    <Hit at={REEL.close + 52} file="s-ding.wav" volume={0.3} frames={36} />
  </>
);

export const ScenarioPaydayAudio: React.FC = () => (
  <>
    <Bed total={PAYDAY.total} ducks={[[PAYDAY.m3 + 70, PAYDAY.m3 + 110]]} />

    {/* HOOK — fast stamp, per the 2-second rule */}
    <Hit at={2} file="s-whoosh.wav" volume={0.3} frames={22} />
    <Hit at={PAYDAY.hook + 9} file="s-switch.wav" volume={0.46} frames={18} />
    <Hit at={PAYDAY.hook + 30} file="s-softclick.wav" volume={0.3} frames={14} />
    <Hit at={PAYDAY.hook + 56} file="s-softclick.wav" volume={0.28} frames={14} />

    {/* 10:12 AM — the latte */}
    <Hit at={PAYDAY.m1 + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={PAYDAY.m1 + 16} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={PAYDAY.m1 + 26} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={PAYDAY.m1 + 36} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={PAYDAY.m1 + 46} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={PAYDAY.m1 + 62} file="s-switch.wav" volume={0.46} frames={18} />
    <Hit at={PAYDAY.m1 + 78} file="s-seal.wav" volume={0.42} frames={38} />
    <Hit at={PAYDAY.m1 + 98} file="s-pop.wav" volume={0.3} frames={20} />

    {/* 12:40 PM — lunch */}
    <Hit at={PAYDAY.m2 + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={PAYDAY.m2 + 16} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={PAYDAY.m2 + 28} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={PAYDAY.m2 + 40} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={PAYDAY.m2 + 50} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={PAYDAY.m2 + 62} file="s-switch.wav" volume={0.46} frames={18} />
    <Hit at={PAYDAY.m2 + 78} file="s-seal.wav" volume={0.42} frames={38} />
    <Hit at={PAYDAY.m2 + 98} file="s-pop.wav" volume={0.3} frames={20} />

    {/* 8:47 PM — the flash sale, with a held breath before the call */}
    <Hit at={PAYDAY.m3 + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={PAYDAY.m3 + 16} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={PAYDAY.m3 + 26} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={PAYDAY.m3 + 40} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={PAYDAY.m3 + 48} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={PAYDAY.m3 + 84} file="s-switch.wav" volume={0.48} frames={18} />
    <Hit at={PAYDAY.m3 + 100} file="s-seal.wav" volume={0.46} frames={40} />
    <Hit at={PAYDAY.m3 + 120} file="s-pop.wav" volume={0.32} frames={20} />

    {/* Evening summary */}
    <Hit at={PAYDAY.summary + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={PAYDAY.summary + 46} file="s-ding.wav" volume={0.34} frames={36} />
    <Hit at={PAYDAY.summary + 84} file="s-softclick.wav" volume={0.28} frames={14} />

    {/* Insight + close */}
    <Hit at={PAYDAY.insight + 6} file="s-pop.wav" volume={0.36} frames={22} />
    <Hit at={PAYDAY.insight + 42} file="s-ding.wav" volume={0.28} frames={34} />
    <Hit at={PAYDAY.close + 8} file="s-seal.wav" volume={0.42} frames={38} />
    <Hit at={PAYDAY.close + 14} file="s-success.wav" volume={0.34} frames={52} />
    <Hit at={PAYDAY.close + 50} file="s-ding.wav" volume={0.3} frames={36} />
  </>
);

export const ScenarioPetsaAudio: React.FC = () => (
  <>
    <Bed total={PETSA.total} ducks={[[PETSA.day3 + 30, PETSA.day3 + 110]]} />

    {/* HOOK */}
    <Hit at={2} file="s-whoosh.wav" volume={0.3} frames={22} />
    <Hit at={PETSA.hook + 9} file="s-switch.wav" volume={0.46} frames={18} />
    <Hit at={PETSA.hook + 28} file="s-softclick.wav" volume={0.3} frames={14} />
    <Hit at={PETSA.hook + 58} file="s-ding.wav" volume={0.24} frames={32} />

    {/* Set the line */}
    <Hit at={PETSA.set + 2} file="s-whoosh.wav" volume={0.24} frames={20} />
    <Hit at={PETSA.set + 20} file="s-pop.wav" volume={0.38} frames={22} />

    {/* Day 1 — under the line */}
    <Hit at={PETSA.day1 + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={PETSA.day1 + 16} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={PETSA.day1 + 28} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={PETSA.day1 + 42} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={PETSA.day1 + 52} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={PETSA.day1 + 62} file="s-switch.wav" volume={0.46} frames={18} />
    <Hit at={PETSA.day1 + 78} file="s-seal.wav" volume={0.42} frames={38} />
    <Hit at={PETSA.day1 + 98} file="s-pop.wav" volume={0.3} frames={20} />

    {/* Day 3 — the refusal (no seal, and that is the point) */}
    <Hit at={PETSA.day3 + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={PETSA.day3 + 34} file="s-pop.wav" volume={0.42} frames={22} />
    <Hit at={PETSA.day3 + 96} file="s-click.wav" volume={0.44} frames={16} />
    <Hit at={PETSA.day3 + 118} file="s-ding.wav" volume={0.26} frames={34} />

    {/* Day 5 — still standing */}
    <Hit at={PETSA.day5 + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={PETSA.day5 + 46} file="s-ding.wav" volume={0.34} frames={36} />

    {/* Insight + close */}
    <Hit at={PETSA.insight + 6} file="s-pop.wav" volume={0.36} frames={22} />
    <Hit at={PETSA.insight + 42} file="s-ding.wav" volume={0.28} frames={34} />
    <Hit at={PETSA.close + 8} file="s-seal.wav" volume={0.42} frames={38} />
    <Hit at={PETSA.close + 14} file="s-success.wav" volume={0.34} frames={52} />
    <Hit at={PETSA.close + 50} file="s-ding.wav" volume={0.3} frames={36} />
  </>
);

export const EssenceSquareAudio: React.FC = () => (
  <>
    <Bed total={SQ.total} music={0.17} pad={0.24} />

    <Hit at={4} file="s-whoosh.wav" volume={0.28} frames={24} />
    <Hit at={SQ.hook + 38} file="s-switch.wav" volume={0.42} frames={18} />
    <Hit at={SQ.hook + 52} file="s-switch.wav" volume={0.46} frames={18} />

    <Hit at={SQ.split + 2} file="s-whoosh.wav" volume={0.26} frames={22} />
    <Hit at={SQ.split + 46} file="s-ding.wav" volume={0.34} frames={36} />

    <Hit at={SQ.seal + 2} file="s-whoosh.wav" volume={0.24} frames={20} />
    <Hit at={SQ.seal + 22} file="s-seal.wav" volume={0.44} frames={40} />

    <Hit at={SQ.close + 10} file="s-seal.wav" volume={0.4} frames={38} />
    <Hit at={SQ.close + 16} file="s-success.wav" volume={0.34} frames={52} />
    <Hit at={SQ.close + 50} file="s-ding.wav" volume={0.28} frames={36} />
  </>
);
