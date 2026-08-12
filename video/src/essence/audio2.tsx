import React from "react";
import { BARKADA, MIDNIGHT, MILKTEA, STREAK, SUBS } from "./timing";
import { Bed, Hit } from "./audio";

/**
 * Audio beds for scenario pack 2. Same soft sample rules (D70): quiet
 * music + pad, sparse hits sitting slightly above the bed. Signature
 * moments get their own texture — flip ticks, milestone dings, the
 * subscription card that falls with a click and no ceremony.
 */

export const ScenarioMidnightAudio: React.FC = () => (
  <>
    <Bed total={MIDNIGHT.total} music={0.16} pad={0.24} ducks={[[MIDNIGHT.urge + 84, MIDNIGHT.urge + 130]]} />

    {/* HOOK — the clock flips at midnight */}
    <Hit at={2} file="s-whoosh.wav" volume={0.3} frames={22} />
    <Hit at={MIDNIGHT.hook + 44} file="s-click.wav" volume={0.4} frames={14} />
    <Hit at={MIDNIGHT.hook + 52} file="s-switch.wav" volume={0.4} frames={16} />
    <Hit at={MIDNIGHT.hook + 28} file="s-pop.wav" volume={0.32} frames={20} />
    <Hit at={MIDNIGHT.hook + 38} file="s-pop.wav" volume={0.3} frames={20} />

    {/* URGE — typing, the chip, then the hold card (no seal) */}
    <Hit at={MIDNIGHT.urge + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={MIDNIGHT.urge + 14} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={MIDNIGHT.urge + 24} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={MIDNIGHT.urge + 34} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={MIDNIGHT.urge + 46} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={MIDNIGHT.urge + 62} file="s-switch.wav" volume={0.46} frames={18} />
    <Hit at={MIDNIGHT.urge + 90} file="s-pop.wav" volume={0.42} frames={24} />
    <Hit at={MIDNIGHT.urge + 104} file="s-ding.wav" volume={0.26} frames={32} />

    {/* HOLD — flip lapse ticks + sunrise swell */}
    <Hit at={MIDNIGHT.hold + 38} file="s-click.wav" volume={0.34} frames={12} />
    <Hit at={MIDNIGHT.hold + 56} file="s-click.wav" volume={0.32} frames={12} />
    <Hit at={MIDNIGHT.hold + 74} file="s-click.wav" volume={0.34} frames={12} />
    <Hit at={MIDNIGHT.hold + 92} file="s-click.wav" volume={0.32} frames={12} />
    <Hit at={MIDNIGHT.hold + 110} file="s-click.wav" volume={0.34} frames={12} />
    <Hit at={MIDNIGHT.hold + 126} file="s-success.wav" volume={0.24} frames={48} />

    {/* MORNING — quiet relief */}
    <Hit at={MIDNIGHT.morning + 2} file="s-whoosh.wav" volume={0.24} frames={20} />
    <Hit at={MIDNIGHT.morning + 48} file="s-pop.wav" volume={0.34} frames={20} />
    <Hit at={MIDNIGHT.morning + 96} file="s-ding.wav" volume={0.3} frames={34} />

    {/* INSIGHT + CLOSE */}
    <Hit at={MIDNIGHT.insight + 6} file="s-pop.wav" volume={0.36} frames={22} />
    <Hit at={MIDNIGHT.insight + 42} file="s-ding.wav" volume={0.28} frames={34} />
    <Hit at={MIDNIGHT.close + 8} file="s-seal.wav" volume={0.42} frames={38} />
    <Hit at={MIDNIGHT.close + 14} file="s-success.wav" volume={0.34} frames={52} />
    <Hit at={MIDNIGHT.close + 50} file="s-ding.wav" volume={0.3} frames={36} />
  </>
);

export const ScenarioMilkTeaAudio: React.FC = () => (
  <>
    <Bed total={MILKTEA.total} ducks={[[MILKTEA.multiply + 100, MILKTEA.multiply + 170]]} />

    {/* HOOK */}
    <Hit at={2} file="s-whoosh.wav" volume={0.3} frames={22} />
    <Hit at={MILKTEA.hook + 12} file="s-pop.wav" volume={0.36} frames={22} />
    <Hit at={MILKTEA.hook + 28} file="s-switch.wav" volume={0.38} frames={16} />

    {/* MULTIPLY — grid wave + two odometer runs */}
    <Hit at={MILKTEA.multiply + 12} file="s-whoosh.wav" volume={0.3} frames={24} />
    <Hit at={MILKTEA.multiply + 26} file="s-softclick.wav" volume={0.34} frames={12} />
    <Hit at={MILKTEA.multiply + 38} file="s-softclick.wav" volume={0.36} frames={12} />
    <Hit at={MILKTEA.multiply + 50} file="s-softclick.wav" volume={0.34} frames={12} />
    <Hit at={MILKTEA.multiply + 80} file="s-ding.wav" volume={0.3} frames={32} />
    <Hit at={MILKTEA.multiply + 112} file="s-whoosh.wav" volume={0.3} frames={22} />
    <Hit at={MILKTEA.multiply + 162} file="s-ding.wav" volume={0.36} frames={36} />

    {/* LOG */}
    <Hit at={MILKTEA.log + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={MILKTEA.log + 16} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={MILKTEA.log + 28} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={MILKTEA.log + 44} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={MILKTEA.log + 62} file="s-switch.wav" volume={0.46} frames={18} />
    <Hit at={MILKTEA.log + 78} file="s-seal.wav" volume={0.42} frames={38} />

    {/* SEE — donut + gold circle draw */}
    <Hit at={MILKTEA.see + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={MILKTEA.see + 46} file="s-ding.wav" volume={0.32} frames={34} />
    <Hit at={MILKTEA.see + 66} file="s-softclick.wav" volume={0.32} frames={14} />

    {/* INSIGHT + CLOSE */}
    <Hit at={MILKTEA.insight + 6} file="s-pop.wav" volume={0.36} frames={22} />
    <Hit at={MILKTEA.insight + 42} file="s-ding.wav" volume={0.28} frames={34} />
    <Hit at={MILKTEA.close + 8} file="s-seal.wav" volume={0.42} frames={38} />
    <Hit at={MILKTEA.close + 14} file="s-success.wav" volume={0.34} frames={52} />
    <Hit at={MILKTEA.close + 50} file="s-ding.wav" volume={0.3} frames={36} />
  </>
);

export const ScenarioStreakAudio: React.FC = () => (
  <>
    <Bed total={STREAK.total} ducks={[[STREAK.montage + 190, STREAK.montage + 240]]} />

    {/* HOOK */}
    <Hit at={2} file="s-whoosh.wav" volume={0.3} frames={22} />
    <Hit at={STREAK.hook + 45} file="s-switch.wav" volume={0.46} frames={18} />

    {/* MONTAGE — rows in rhythm + milestone dings (7/14/21/30) */}
    <Hit at={STREAK.montage + 34} file="s-softclick.wav" volume={0.38} frames={14} />
    <Hit at={STREAK.montage + 66} file="s-softclick.wav" volume={0.4} frames={14} />
    <Hit at={STREAK.montage + 98} file="s-softclick.wav" volume={0.38} frames={14} />
    <Hit at={STREAK.montage + 130} file="s-softclick.wav" volume={0.4} frames={14} />
    <Hit at={STREAK.montage + 162} file="s-softclick.wav" volume={0.38} frames={14} />
    <Hit at={STREAK.montage + 194} file="s-softclick.wav" volume={0.4} frames={14} />
    <Hit at={STREAK.montage + 58} file="s-ding.wav" volume={0.26} frames={30} />
    <Hit at={STREAK.montage + 113} file="s-ding.wav" volume={0.28} frames={30} />
    <Hit at={STREAK.montage + 169} file="s-ding.wav" volume={0.3} frames={30} />
    <Hit at={STREAK.montage + 240} file="s-success.wav" volume={0.3} frames={46} />

    {/* APP */}
    <Hit at={STREAK.app + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={STREAK.app + 46} file="s-ding.wav" volume={0.32} frames={34} />

    {/* INSIGHT + CLOSE */}
    <Hit at={STREAK.insight + 6} file="s-pop.wav" volume={0.36} frames={22} />
    <Hit at={STREAK.insight + 42} file="s-ding.wav" volume={0.28} frames={34} />
    <Hit at={STREAK.close + 8} file="s-seal.wav" volume={0.42} frames={38} />
    <Hit at={STREAK.close + 14} file="s-success.wav" volume={0.34} frames={52} />
    <Hit at={STREAK.close + 50} file="s-ding.wav" volume={0.3} frames={36} />
  </>
);

export const ScenarioBarkadaAudio: React.FC = () => (
  <>
    <Bed total={BARKADA.total} ducks={[[BARKADA.allowed + 10, BARKADA.allowed + 50]]} />

    {/* HOOK — clink + the roll */}
    <Hit at={2} file="s-whoosh.wav" volume={0.3} frames={22} />
    <Hit at={BARKADA.hook + 26} file="s-ding.wav" volume={0.36} frames={34} />
    <Hit at={BARKADA.hook + 40} file="s-whoosh.wav" volume={0.22} frames={26} />

    {/* SPLIT — total lands, four shares fly */}
    <Hit at={BARKADA.split + 8} file="s-pop.wav" volume={0.4} frames={22} />
    <Hit at={BARKADA.split + 32} file="s-switch.wav" volume={0.4} frames={16} />
    <Hit at={BARKADA.split + 58} file="s-softclick.wav" volume={0.34} frames={12} />
    <Hit at={BARKADA.split + 64} file="s-softclick.wav" volume={0.36} frames={12} />
    <Hit at={BARKADA.split + 70} file="s-softclick.wav" volume={0.34} frames={12} />
    <Hit at={BARKADA.split + 76} file="s-softclick.wav" volume={0.36} frames={12} />
    <Hit at={BARKADA.split + 110} file="s-ding.wav" volume={0.3} frames={32} />

    {/* LOG */}
    <Hit at={BARKADA.log + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={BARKADA.log + 16} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={BARKADA.log + 30} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={BARKADA.log + 46} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={BARKADA.log + 66} file="s-switch.wav" volume={0.46} frames={18} />
    <Hit at={BARKADA.log + 82} file="s-seal.wav" volume={0.42} frames={38} />

    {/* ALLOWED — the warm beat */}
    <Hit at={BARKADA.allowed + 14} file="s-seal.wav" volume={0.36} frames={36} />
    <Hit at={BARKADA.allowed + 30} file="s-success.wav" volume={0.28} frames={46} />

    {/* INSIGHT + CLOSE */}
    <Hit at={BARKADA.insight + 6} file="s-pop.wav" volume={0.36} frames={22} />
    <Hit at={BARKADA.insight + 42} file="s-ding.wav" volume={0.28} frames={34} />
    <Hit at={BARKADA.close + 8} file="s-seal.wav" volume={0.42} frames={38} />
    <Hit at={BARKADA.close + 14} file="s-success.wav" volume={0.34} frames={52} />
    <Hit at={BARKADA.close + 50} file="s-ding.wav" volume={0.3} frames={36} />
  </>
);

export const ScenarioSubsAudio: React.FC = () => (
  <>
    <Bed total={SUBS.total} ducks={[[SUBS.audit + 30, SUBS.audit + 100]]} />

    {/* HOOK — cards land */}
    <Hit at={2} file="s-whoosh.wav" volume={0.3} frames={22} />
    <Hit at={SUBS.hook + 28} file="s-softclick.wav" volume={0.36} frames={12} />
    <Hit at={SUBS.hook + 40} file="s-softclick.wav" volume={0.38} frames={12} />
    <Hit at={SUBS.hook + 52} file="s-softclick.wav" volume={0.36} frames={12} />
    <Hit at={SUBS.hook + 64} file="s-softclick.wav" volume={0.38} frames={12} />

    {/* STACK — sum steps + the year roll */}
    <Hit at={SUBS.stack + 16} file="s-click.wav" volume={0.34} frames={12} />
    <Hit at={SUBS.stack + 36} file="s-click.wav" volume={0.34} frames={12} />
    <Hit at={SUBS.stack + 56} file="s-click.wav" volume={0.34} frames={12} />
    <Hit at={SUBS.stack + 76} file="s-click.wav" volume={0.36} frames={12} />
    <Hit at={SUBS.stack + 118} file="s-whoosh.wav" volume={0.28} frames={22} />
    <Hit at={SUBS.stack + 156} file="s-ding.wav" volume={0.34} frames={34} />

    {/* LOG */}
    <Hit at={SUBS.log + 2} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={SUBS.log + 16} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={SUBS.log + 30} file="s-tap.wav" volume={0.42} frames={20} />
    <Hit at={SUBS.log + 48} file="s-tap.wav" volume={0.4} frames={20} />
    <Hit at={SUBS.log + 62} file="s-switch.wav" volume={0.46} frames={18} />
    <Hit at={SUBS.log + 78} file="s-seal.wav" volume={0.42} frames={38} />

    {/* AUDIT — flip, fall, save */}
    <Hit at={SUBS.audit + 38} file="s-switch.wav" volume={0.4} frames={16} />
    <Hit at={SUBS.audit + 62} file="s-whoosh.wav" volume={0.26} frames={20} />
    <Hit at={SUBS.audit + 100} file="s-ding.wav" volume={0.3} frames={32} />
    <Hit at={SUBS.audit + 112} file="s-pop.wav" volume={0.34} frames={20} />

    {/* INSIGHT + CLOSE */}
    <Hit at={SUBS.insight + 6} file="s-pop.wav" volume={0.36} frames={22} />
    <Hit at={SUBS.insight + 42} file="s-ding.wav" volume={0.28} frames={34} />
    <Hit at={SUBS.close + 8} file="s-seal.wav" volume={0.42} frames={38} />
    <Hit at={SUBS.close + 14} file="s-success.wav" volume={0.34} frames={52} />
    <Hit at={SUBS.close + 50} file="s-ding.wav" volume={0.3} frames={36} />
  </>
);
