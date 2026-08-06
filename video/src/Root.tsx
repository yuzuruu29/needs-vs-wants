import "./index.css";
import { Composition } from "remotion";
import { loadFont as loadInter } from "@remotion/google-fonts/Inter";
import { loadFont as loadPlayfairSC } from "@remotion/google-fonts/PlayfairDisplaySC";
import { HowItWorks } from "./HowItWorks";
import { NeedsVsWantsPromo } from "./NeedsVsWantsPromo";
import { GoogleFlowPromptImpulseTrap } from "./GoogleFlowPromptImpulseTrap";
import { PROMO_TOTAL_FRAMES } from "./promoTiming";

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
    </>
  );
};
