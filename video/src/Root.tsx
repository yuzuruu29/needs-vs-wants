import "./index.css";
import { Composition } from "remotion";
import { loadFont as loadInter } from "@remotion/google-fonts/Inter";
import { loadFont as loadPlayfairSC } from "@remotion/google-fonts/PlayfairDisplaySC";
import { HowItWorks } from "./HowItWorks";
import { NeedsVsWantsPromo } from "./NeedsVsWantsPromo";
import { GoogleFlowPromptImpulseTrap } from "./GoogleFlowPromptImpulseTrap";
import { PROMO_TOTAL_FRAMES } from "./promoTiming";
import { EssenceAd } from "./essence/EssenceAd";
import { EssenceReel } from "./essence/EssenceReel";
import { EssenceSquare } from "./essence/EssenceSquare";
import { ScenarioPayday } from "./essence/ScenarioPayday";
import { ScenarioPetsa } from "./essence/ScenarioPetsa";
import { ScenarioMidnight } from "./essence/ScenarioMidnight";
import { ScenarioMilkTea } from "./essence/ScenarioMilkTea";
import { ScenarioStreak } from "./essence/ScenarioStreak";
import { ScenarioBarkada } from "./essence/ScenarioBarkada";
import { ScenarioSubs } from "./essence/ScenarioSubs";
import {
  AD,
  BARKADA,
  MIDNIGHT,
  MILKTEA,
  PAYDAY,
  PETSA,
  REEL,
  SQ,
  STREAK,
  SUBS,
} from "./essence/timing";

// Fonts are loaded once at bundle time (delayRender handled by the package).
export const Inter = loadInter("normal", {
  weights: ["400", "500", "600", "700"],
}).fontFamily;
export const PlayfairSC = loadPlayfairSC("normal", {
  weights: ["400", "700"],
}).fontFamily;

export const RemotionRoot: React.FC = () => {
  return (
    <>
      <Composition
        id="GoogleFlowPromptImpulseTrap"
        component={GoogleFlowPromptImpulseTrap}
        durationInFrames={450}
        fps={30}
        width={1440}
        height={2560}
      />
      <Composition
        id="HowItWorks"
        component={HowItWorks}
        durationInFrames={450}
        fps={30}
        width={1440}
        height={2560}
      />
      <Composition
        id="NeedsVsWantsPromo"
        component={NeedsVsWantsPromo}
        durationInFrames={PROMO_TOTAL_FRAMES}
        fps={30}
        width={1920}
        height={1080}
      />
      <Composition
        id="NeedsVsWantsPromoVertical"
        component={NeedsVsWantsPromo}
        durationInFrames={PROMO_TOTAL_FRAMES}
        fps={30}
        width={1080}
        height={1920}
        defaultProps={{ portrait: true }}
      />
      {/* "The Choice" campaign — essence + how-to, three cuts */}
      <Composition
        id="EssenceAd"
        component={EssenceAd}
        durationInFrames={AD.total}
        fps={30}
        width={1920}
        height={1080}
      />
      <Composition
        id="EssenceReel"
        component={EssenceReel}
        durationInFrames={REEL.total}
        fps={30}
        width={1080}
        height={1920}
      />
      <Composition
        id="EssenceSquare"
        component={EssenceSquare}
        durationInFrames={SQ.total}
        fps={30}
        width={1080}
        height={1080}
      />
      {/* Real-life scenario cuts */}
      <Composition
        id="ScenarioPayday"
        component={ScenarioPayday}
        durationInFrames={PAYDAY.total}
        fps={30}
        width={1080}
        height={1920}
      />
      <Composition
        id="ScenarioPetsa"
        component={ScenarioPetsa}
        durationInFrames={PETSA.total}
        fps={30}
        width={1080}
        height={1920}
      />
      {/* Scenario pack 2 — bigger set pieces */}
      <Composition
        id="ScenarioMidnight"
        component={ScenarioMidnight}
        durationInFrames={MIDNIGHT.total}
        fps={30}
        width={1080}
        height={1920}
      />
      <Composition
        id="ScenarioMilkTea"
        component={ScenarioMilkTea}
        durationInFrames={MILKTEA.total}
        fps={30}
        width={1080}
        height={1920}
      />
      <Composition
        id="ScenarioStreak"
        component={ScenarioStreak}
        durationInFrames={STREAK.total}
        fps={30}
        width={1080}
        height={1920}
      />
      <Composition
        id="ScenarioBarkada"
        component={ScenarioBarkada}
        durationInFrames={BARKADA.total}
        fps={30}
        width={1080}
        height={1920}
      />
      <Composition
        id="ScenarioSubs"
        component={ScenarioSubs}
        durationInFrames={SUBS.total}
        fps={30}
        width={1080}
        height={1920}
      />
    </>
  );
};
