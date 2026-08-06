import React from "react";
import { Sequence, staticFile, interpolate } from "remotion";
import { Audio } from "@remotion/media";
import { PROMO_T as T, PROMO_TOTAL_FRAMES } from "../promoTiming";

/**
 * Soft boutique audio for the 45s marketing promo.
 *
 * Design rules:
 * - Real soft UI samples (Remotion CDN + Mixkit), not raw FFmpeg sine beeps
 * - No paper-scratch / page-turn (user disliked) — use soft whoosh / switch / pop
 * - Typing + popup hits slightly more present than the music bed
 * - Still sparse — no stamp spam
 *
 * Assets: public/audio/soft/
 */

const S = (file: string) => staticFile(`audio/soft/${file}`);

const clamp = {
  extrapolateLeft: "clamp" as const,
  extrapolateRight: "clamp" as const,
};

/** Short SFX with gentle in/out so nothing pops. */
const SoftHit: React.FC<{
  from: number;
  file: string;
  volume?: number;
  frames?: number;
}> = ({ from, file, volume = 0.35, frames = 30 }) => (
  <Sequence from={from} durationInFrames={frames}>
    <Audio
      src={S(file)}
      volume={(f) => {
        const env = interpolate(f, [0, 2, frames - 8, frames - 1], [0, 1, 1, 0], clamp);
        return volume * env;
      }}
    />
  </Sequence>
);

export const PromoAudioBed: React.FC = () => {
  return (
    <>
      {/* ── Continuous soft music bed (quiet) ─────────────────────── */}
      <Sequence from={0} durationInFrames={PROMO_TOTAL_FRAMES}>
        <Audio
          src={S("soft-music.wav")}
          volume={(f) => {
            let v = interpolate(
              f,
              [0, 24, T.seal, T.guard, T.close, PROMO_TOTAL_FRAMES - 1],
              [0.12, 0.2, 0.18, 0.16, 0.22, 0.12],
              clamp,
            );
            // Duck under louder SFX moments
            if (f >= T.seal + 30 && f <= T.seal + 160) v *= 0.68;
            if (f >= T.guard + 60 && f <= T.guard + 120) v *= 0.75;
            if (f >= T.close && f <= T.close + 50) v *= 0.82;
            v *= interpolate(
              f,
              [PROMO_TOTAL_FRAMES - 40, PROMO_TOTAL_FRAMES - 1],
              [1, 0.35],
              clamp,
            );
            return v;
          }}
        />
      </Sequence>

      <Sequence from={0} durationInFrames={PROMO_TOTAL_FRAMES}>
        <Audio
          src={S("soft-pad.wav")}
          volume={(f) =>
            interpolate(
              f,
              [0, 30, PROMO_TOTAL_FRAMES - 40, PROMO_TOTAL_FRAMES - 1],
              [0.12, 0.3, 0.28, 0.1],
              clamp,
            )
          }
        />
      </Sequence>

      {/* ── HOOK — soft whoosh (no paper scratch) ─────────────────── */}
      <SoftHit from={4} file="s-whoosh.wav" volume={0.32} frames={24} />

      {/* ── PURPOSE — chip lands + soft resolve (pop-ups) ─────────── */}
      <SoftHit from={T.purpose + 2} file="s-whoosh.wav" volume={0.28} frames={22} />
      <SoftHit from={T.purpose + 36} file="s-switch.wav" volume={0.42} frames={18} />
      <SoftHit from={T.purpose + 52} file="s-switch.wav" volume={0.44} frames={18} />
      <SoftHit from={T.purpose + 70} file="s-pop.wav" volume={0.4} frames={24} />

      {/* ── SEE — whoosh, ding, CTA click ─────────────────────────── */}
      <SoftHit from={T.see + 2} file="s-whoosh.wav" volume={0.3} frames={22} />
      <SoftHit from={T.see + 90} file="s-ding.wav" volume={0.34} frames={36} />
      <SoftHit from={T.see + 115} file="s-click.wav" volume={0.4} frames={16} />

      {/* ── SEAL — typing taps (louder) + classify + seal ─────────── */}
      <SoftHit from={T.seal + 2} file="s-whoosh.wav" volume={0.3} frames={24} />
      {/* Typing: a few clear taps, not a full keyboard roll */}
      <SoftHit from={T.seal + 28} file="s-tap.wav" volume={0.42} frames={26} />
      <SoftHit from={T.seal + 42} file="s-tap.wav" volume={0.44} frames={26} />
      <SoftHit from={T.seal + 56} file="s-tap.wav" volume={0.4} frames={24} />
      <SoftHit from={T.seal + 78} file="s-tap.wav" volume={0.46} frames={26} />
      <SoftHit from={T.seal + 92} file="s-tap.wav" volume={0.44} frames={24} />
      {/* WANT chip */}
      <SoftHit from={T.seal + 108} file="s-switch.wav" volume={0.48} frames={18} />
      {/* Seal popup / lock-in */}
      <SoftHit from={T.seal + 118} file="s-seal.wav" volume={0.42} frames={40} />
      <SoftHit from={T.seal + 280} file="s-pop.wav" volume={0.38} frames={24} />

      {/* ── GUARD — dialog / confirm popups ───────────────────────── */}
      <SoftHit from={T.guard + 4} file="s-whoosh.wav" volume={0.26} frames={20} />
      <SoftHit from={T.guard + 70} file="s-pop.wav" volume={0.42} frames={22} />
      <SoftHit from={T.guard + 95} file="s-click.wav" volume={0.44} frames={16} />

      {/* ── BENEFITS — soft card ticks (no paper) ─────────────────── */}
      <SoftHit from={T.benefits + 4} file="s-whoosh.wav" volume={0.26} frames={22} />
      <SoftHit from={T.benefits + 16} file="s-softclick.wav" volume={0.36} frames={14} />
      <SoftHit from={T.benefits + 28} file="s-softclick.wav" volume={0.38} frames={14} />
      <SoftHit from={T.benefits + 40} file="s-softclick.wav" volume={0.4} frames={14} />
      <SoftHit from={T.benefits + 52} file="s-softclick.wav" volume={0.42} frames={14} />

      {/* ── CLOSE — success + ding ────────────────────────────────── */}
      <SoftHit from={T.close + 6} file="s-success.wav" volume={0.36} frames={48} />
      <SoftHit from={T.close + 40} file="s-ding.wav" volume={0.32} frames={36} />
    </>
  );
};
