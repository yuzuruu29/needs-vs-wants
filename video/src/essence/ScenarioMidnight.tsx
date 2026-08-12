import React from "react";
import { AbsoluteFill, interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, FONT_STACK, SERIF_STACK } from "../theme";
import { PaperBackground } from "../fx/PaperBackground";
import { GoldDust } from "../fx/GoldDust";
import { CONTENT } from "../layout";
import { LogScreen } from "../ui/LogScreen";
import { MIDNIGHT } from "./timing";
import { CanvasPhone, CloseLockup, FadeUp, Kicker, prog } from "./blocks";
import { InsightCard, MomentHeader, ScenarioScene, TimeChip } from "./scenario-blocks";
import { FlipSequence, NightVeil, SwingTag, TiltIn } from "./flash";
import { ScenarioMidnightAudio } from "./audio2";

const W = 1080;
const H = 1920;
const PHONE = { x: W / 2, y: 1176, height: 1180 };

/**
 * "The 11:59 Sale" — a midnight flash sale meets the Max 24-hour hold.
 * Night paper, split-flap clock, the urge typed but never sealed, a fast
 * sunrise, and the money still there in the morning.
 */
export const ScenarioMidnight: React.FC = () => {
  const frame = useCurrentFrame();
  const boot = interpolate(frame, [0, 8], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  // Global dusk: full night through the urge, sunrise during the hold lapse.
  const night = interpolate(
    frame,
    [MIDNIGHT.hold + 54, MIDNIGHT.hold + 150],
    [1, 0],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp", easing: EASE_EDIT },
  );

  return (
    <AbsoluteFill style={{ width: W, height: H, background: COLORS.background }}>
      <PaperBackground />
      <NightVeil night={night} />
      <GoldDust
        frame={frame}
        count={12}
        area={{ x: 0, y: 160, w: W, h: 1500 }}
        opacity={0.32 + (1 - night) * 0.16}
      />

      <AbsoluteFill style={{ opacity: boot }}>
        <ScenarioScene from={MIDNIGHT.hook} duration={MIDNIGHT.urge - MIDNIGHT.hook} fadeIn={4}>
          <MidnightHook />
        </ScenarioScene>

        <ScenarioScene from={MIDNIGHT.urge} duration={MIDNIGHT.hold - MIDNIGHT.urge}>
          <MomentHeader chip="11:59 PM" line="The urge arrives on schedule." accent={COLORS.crimson} />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <UrgeDemo />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={MIDNIGHT.hold} duration={MIDNIGHT.morning - MIDNIGHT.hold}>
          <HoldLapse />
        </ScenarioScene>

        <ScenarioScene from={MIDNIGHT.morning} duration={MIDNIGHT.insight - MIDNIGHT.morning}>
          <MorningHeader />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <MorningDemo />
          </CanvasPhone>
          <KeptPill />
        </ScenarioScene>

        <ScenarioScene from={MIDNIGHT.insight} duration={MIDNIGHT.close - MIDNIGHT.insight}>
          <div style={{ position: "absolute", top: 620, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
            <InsightCard
              eyebrow="The morning test"
              headline="The urge didn't survive the night."
              sub="The money did."
              stats={[
                { label: "Urge", value: "₱1,499.00", accent: COLORS.crimson },
                { label: "Spent", value: "₱0.00", accent: COLORS.green },
                { label: "Kept", value: "₱1,499.00", accent: COLORS.goldDeep },
              ]}
            />
          </div>
        </ScenarioScene>

        <ScenarioScene from={MIDNIGHT.close} duration={MIDNIGHT.total - MIDNIGHT.close} fadeOut={10}>
          <GoldDust frame={frame} count={20} area={{ x: 60, y: 300, w: 960, h: 1300 }} opacity={0.8} scale={1.1} />
          <div style={{ position: "absolute", top: 636, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
            <CloseLockup sealAt={8} scale={0.9} tagline="Sleep on it. Literally." />
          </div>
        </ScenarioScene>
      </AbsoluteFill>

      <ScenarioMidnightAudio />
    </AbsoluteFill>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */

const MidnightHook: React.FC = () => {
  return (
    <AbsoluteFill>
      <FadeUp at={4} style={{ position: "absolute", top: 250, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <Kicker text="Flash sale · tonight only" align="center" size={26} />
      </FadeUp>
      <FadeUp at={14} style={{ position: "absolute", top: 366, left: 0, right: 0, textAlign: "center", padding: "0 80px" }}>
        <div
          style={{
            fontFamily: SERIF_STACK,
            fontSize: 62,
            fontWeight: 700,
            lineHeight: 1.16,
            color: COLORS.ink,
          }}
        >
          Midnight knows
          <br />
          your weaknesses.
        </div>
      </FadeUp>

      <div style={{ position: "absolute", top: 760, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <FlipSequence
          sequence={["11:59", "12:00"]}
          startAt={42}
          stepFrames={30}
          cellW={104}
          cellH={148}
          fontSize={92}
        />
      </div>

      {/* sale tags swinging into frame */}
      <div style={{ position: "absolute", top: 1010, left: 208 }}>
        <SwingTag label="-50%" at={26} color={COLORS.crimson} stringLen={110} phase={0.4} />
      </div>
      <div style={{ position: "absolute", top: 985, left: 648 }}>
        <SwingTag label="₱1,499" at={36} color={COLORS.goldDeep} stringLen={78} phase={2.1} />
      </div>

      <FadeUp at={70} style={{ position: "absolute", top: 1420, left: 0, right: 0, textAlign: "center" }}>
        <div
          style={{
            fontFamily: FONT_STACK,
            fontSize: 30,
            fontWeight: 600,
            letterSpacing: 4,
            color: COLORS.muted,
            textTransform: "uppercase",
          }}
        >
          A cart at midnight. A cooler head by eight.
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

/** Typed, selected WANT, and then the Max hold card instead of a seal. */
const UrgeDemo: React.FC = () => {
  const f = useCurrentFrame();
  const ITEM = "Gaming mouse";
  const COST = "1499";
  const typeItemStart = 10;
  const typeCostStart = 42;
  const chipAt = 62;
  const holdAt = 88;

  const itemChars = Math.max(0, Math.min(ITEM.length, Math.floor((f - typeItemStart) / 2.2)));
  const costChars = Math.max(0, Math.min(COST.length, Math.floor((f - typeCostStart) / 3.2)));
  const holdP = prog(f, holdAt, holdAt + 16);

  return (
    <div style={{ position: "relative", width: CONTENT.W, height: CONTENT.H }}>
      <LogScreen
        width={CONTENT.W}
        height={CONTENT.H}
        data={{
          dateLabel: "FRIDAY · 11:59 PM",
          filledLabel: "1 / 20",
          itemValue: ITEM.slice(0, itemChars),
          costValue: COST.slice(0, costChars),
          focusedField: f >= typeCostStart - 3 ? "cost" : f >= typeItemStart - 3 ? "item" : null,
          needSelected: false,
          wantSelected: f >= chipAt,
          ledgerTimeW: 92,
          rows: [
            { row: { id: "dinner", time: "9:14", item: "Dinner", cost: "₱185.00", type: "NEED" } },
          ],
        }}
      />
      {/* Max pre-seal hold card rises over the form */}
      {holdP > 0.01 ? (
        <div style={{ position: "absolute", left: 18, right: 18, top: 236 }}>
          <TiltIn p={holdP}>
            <div
              style={{
                background: COLORS.card,
                borderRadius: 24,
                border: "1.5px solid rgba(232,169,42,0.55)",
                boxShadow: "0 30px 60px -26px rgba(26,26,26,0.5)",
                padding: "26px 28px 24px",
              }}
            >
              <div style={{ display: "flex", alignItems: "center", gap: 14 }}>
                <div
                  style={{
                    padding: "7px 16px",
                    borderRadius: 10,
                    background: "rgba(232,169,42,0.16)",
                    border: `1.5px solid ${COLORS.goldDeep}`,
                    fontFamily: FONT_STACK,
                    fontSize: 22,
                    fontWeight: 700,
                    letterSpacing: 2.6,
                    color: COLORS.goldDeep,
                  }}
                >
                  MAX
                </div>
                <div
                  style={{
                    fontFamily: FONT_STACK,
                    fontSize: 23,
                    fontWeight: 600,
                    letterSpacing: 2.4,
                    color: COLORS.crimson,
                    textTransform: "uppercase",
                  }}
                >
                  Want coach
                </div>
              </div>
              <div
                style={{
                  marginTop: 16,
                  fontFamily: SERIF_STACK,
                  fontSize: 42,
                  fontWeight: 700,
                  color: COLORS.ink,
                  lineHeight: 1.12,
                }}
              >
                24h hold suggested
              </div>
              <div
                style={{
                  marginTop: 12,
                  fontFamily: FONT_STACK,
                  fontSize: 26,
                  lineHeight: 1.45,
                  color: COLORS.sub,
                }}
              >
                Flash sales rely on speed.
                <br />
                You don't have to.
              </div>
              <div
                style={{
                  marginTop: 18,
                  display: "inline-flex",
                  padding: "13px 24px",
                  borderRadius: 14,
                  background: "rgba(11,107,58,0.1)",
                  border: `2px solid ${COLORS.green}`,
                  fontFamily: FONT_STACK,
                  fontSize: 25,
                  fontWeight: 600,
                  color: COLORS.green,
                }}
              >
                Ask Max before sealing
              </div>
            </div>
          </TiltIn>
        </div>
      ) : null}
    </div>
  );
};

const HoldLapse: React.FC = () => {
  const f = useCurrentFrame();
  const sunrise = prog(f, 54, 150);
  return (
    <AbsoluteFill>
      <FadeUp at={6} style={{ position: "absolute", top: 400, left: 0, right: 0, textAlign: "center" }}>
        <div style={{ fontFamily: SERIF_STACK, fontSize: 66, fontWeight: 700, color: COLORS.ink }}>
          Hold it for one night.
        </div>
      </FadeUp>

      <div style={{ position: "absolute", top: 700, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <FlipSequence
          sequence={["12:00", "01:00", "03:00", "05:00", "06:30", "08:00"]}
          startAt={36}
          stepFrames={18}
          cellW={100}
          cellH={142}
          fontSize={88}
        />
      </div>

      {/* warm sunrise band rising with the veil fade */}
      <div
        style={{
          position: "absolute",
          left: 0,
          right: 0,
          bottom: 0,
          height: 900,
          background:
            "linear-gradient(0deg, rgba(244,201,104,0.4) 0%, rgba(244,201,104,0) 78%)",
          opacity: sunrise,
        }}
      />

      <FadeUp at={116} style={{ position: "absolute", top: 1180, left: 0, right: 0, textAlign: "center", padding: "0 100px" }}>
        <div
          style={{
            fontFamily: SERIF_STACK,
            fontStyle: "italic",
            fontSize: 44,
            lineHeight: 1.4,
            color: COLORS.goldDeep,
          }}
        >
          The sale will still be there.
          <br />
          The urge won't.
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

const MorningHeader: React.FC = () => {
  const f = useCurrentFrame();
  const swap = prog(f, 92, 104);
  return (
    <div
      style={{
        position: "absolute",
        top: 148,
        left: 0,
        right: 0,
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        gap: 26,
      }}
    >
      <FadeUp at={4}>
        <TimeChip label="8:00 AM" accent={COLORS.green} />
      </FadeUp>
      <div style={{ position: "relative", height: 76, width: "100%", textAlign: "center" }}>
        <FadeUp at={14} style={{ position: "absolute", inset: 0, opacity: 1 - swap, transform: `translateY(${swap * -12}px)` }}>
          <div style={{ fontFamily: SERIF_STACK, fontSize: 58, fontWeight: 700, color: COLORS.ink }}>
            Still want it?
          </div>
        </FadeUp>
        <div style={{ position: "absolute", inset: 0, opacity: swap, transform: `translateY(${(1 - swap) * 14}px)` }}>
          <div style={{ fontFamily: SERIF_STACK, fontSize: 58, fontWeight: 700, color: COLORS.green }}>
            Turns out, no.
          </div>
        </div>
      </div>
    </div>
  );
};

/** Saturday morning: the row that never happened. */
const MorningDemo: React.FC = () => (
  <LogScreen
    width={CONTENT.W}
    height={CONTENT.H}
    data={{
      dateLabel: "SATURDAY · 8:00 AM",
      filledLabel: "1 / 20",
      itemValue: "",
      costValue: "",
      focusedField: null,
      needSelected: false,
      wantSelected: false,
      ledgerTimeW: 92,
      rows: [
        { row: { id: "dinner", time: "9:14", item: "Dinner", cost: "₱185.00", type: "NEED" } },
      ],
    }}
  />
);

const KeptPill: React.FC = () => {
  const f = useCurrentFrame();
  const p = prog(f, 44, 60);
  return (
    <div style={{ position: "absolute", top: 512, right: 74 }}>
      <TiltIn p={p}>
        <div
          style={{
            padding: "16px 28px",
            borderRadius: 18,
            background: COLORS.card,
            border: `2px solid ${COLORS.green}`,
            boxShadow: "0 20px 40px -20px rgba(11,107,58,0.45)",
            fontFamily: FONT_STACK,
            fontSize: 30,
            fontWeight: 700,
            color: COLORS.green,
            transform: "rotate(3deg)",
            fontVariantNumeric: "tabular-nums",
          }}
        >
          Still yours: ₱1,499.00
        </div>
      </TiltIn>
    </div>
  );
};
