#!/usr/bin/env bash
# Studio-grade procedural stems for the How It Works promo (offline — no API keys).
# Layered partials, strike transients, sympathetic resonance, reverb tails.
# Output: video/public/audio/*.wav  (frame-exact placement in AudioBed.tsx)
set -euo pipefail

OUT="$(cd "$(dirname "$0")/.." && pwd)/public/audio"
mkdir -p "$OUT"
SR=44100

# Shared mastering chain pieces (light peak control; final LUFS via Remotion mix + post loudnorm).
LIMIT="alimiter=limit=0.92:attack=5:release=50"
SOFT="lowpass=f=12000,highpass=f=40"

echo "→ Generating boutique audio stems into $OUT"

# ─────────────────────────────────────────────────────────────────────────────
# 1. Warm ambient pad — 15s F♯ minor-ish stack with slow tremolo + soft reverb
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y -f lavfi -i \
  "aevalsrc=exprs='\
0.028*sin(2*PI*92.50*t)*(0.75+0.25*sin(2*PI*0.07*t))+\
0.032*sin(2*PI*185.00*t)*(0.72+0.28*sin(2*PI*0.11*t+0.4))+\
0.030*sin(2*PI*233.08*t)*(0.70+0.30*sin(2*PI*0.09*t+1.1))+\
0.026*sin(2*PI*277.18*t)*(0.68+0.32*sin(2*PI*0.13*t+2.0))+\
0.018*sin(2*PI*369.99*t)*(0.65+0.35*sin(2*PI*0.08*t+0.7))+\
0.012*sin(2*PI*554.37*t)*(0.60+0.40*sin(2*PI*0.06*t+1.6))\
':s=${SR}:d=15" \
  -af "lowpass=f=2800,aecho=0.85:0.75:80|160|320:0.28|0.18|0.10,afade=t=in:st=0:d=0.8,afade=t=out:st=14.2:d=0.8,${LIMIT}" \
  -c:a pcm_s16le "$OUT/pad.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 2. Room tone — quiet pink noise, very low, stereo-feel mono bed
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y -f lavfi -i "anoisesrc=colour=pink:amplitude=0.018:duration=15:seed=7" \
  -af "lowpass=f=1800,highpass=f=60,aecho=0.6:0.5:40:0.12,${LIMIT}" \
  -c:a pcm_s16le "$OUT/room.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 3. Soft warm piano pluck — A3 with strike transient + partials + body resonance
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y -f lavfi -i \
  "aevalsrc=exprs='\
0.18*sin(2*PI*220*t)*exp(-1.35*t)+\
0.10*sin(2*PI*440*t)*exp(-2.1*t)+\
0.055*sin(2*PI*660*t)*exp(-2.9*t)+\
0.030*sin(2*PI*880*t)*exp(-3.6*t)+\
0.018*sin(2*PI*1100*t)*exp(-4.2*t)+\
0.09*exp(-95*t)*sin(2*PI*2200*t)+\
0.04*exp(-0.55*t)*sin(2*PI*110*t)\
':s=${SR}:d=1.6" \
  -af "lowpass=f=5200,aecho=0.7:0.6:45|90:0.22|0.12,afade=t=out:st=1.2:d=0.4,${LIMIT}" \
  -c:a pcm_s16le "$OUT/piano-pluck.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 4. Paper rustle — bandpassed noise with natural envelope
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y -f lavfi -i "anoisesrc=colour=white:amplitude=0.55:duration=0.85:seed=11" \
  -af "highpass=f=700,lowpass=f=5800,afade=t=in:st=0:d=0.08,afade=t=out:st=0.42:d=0.4,${LIMIT}" \
  -c:a pcm_s16le "$OUT/paper.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 5. Muted UI click — soft high tick with tiny body
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y -f lavfi -i \
  "aevalsrc=exprs='0.26*sin(2*PI*1850*t)*exp(-130*t)+0.08*sin(2*PI*920*t)*exp(-80*t)':s=${SR}:d=0.06" \
  -af "lowpass=f=6000,${LIMIT}" -c:a pcm_s16le "$OUT/click.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 6. Keyboard tap — crisp key with slight noise edge
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y \
  -f lavfi -i "aevalsrc=exprs='0.28*sin(2*PI*1380*t)*exp(-100*t)+0.09*sin(2*PI*2760*t)*exp(-140*t)':s=${SR}:d=0.08" \
  -f lavfi -i "anoisesrc=colour=white:amplitude=0.12:duration=0.08:seed=19" \
  -filter_complex "[1:a]highpass=f=2000,lowpass=f=8000,afade=t=out:st=0.01:d=0.04[n];[0:a][n]amix=inputs=2:duration=first:weights=1 0.35,${LIMIT}" \
  -c:a pcm_s16le "$OUT/tap.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 7. Whoosh — swelling then settling (donut arc draw)
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y -f lavfi -i "anoisesrc=colour=white:amplitude=0.48:duration=0.95:seed=23" \
  -af "highpass=f=200,lowpass=f=2800,afade=t=in:st=0:d=0.28,afade=t=out:st=0.48:d=0.45,aecho=0.6:0.5:50:0.15,${LIMIT}" \
  -c:a pcm_s16le "$OUT/whoosh.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 8. Subdued cha-ching — soft double-tone register bell
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y -f lavfi -i \
  "aevalsrc=exprs='\
0.085*sin(2*PI*1318.5*t)*exp(-5.2*t)+\
0.070*sin(2*PI*1975.5*t)*exp(-6.5*t)+\
0.040*sin(2*PI*2637*t)*exp(-8*t)+\
if(lt(t,0.08),0,0.055*sin(2*PI*1568*t)*exp(-5.5*(t-0.08)))\
':s=${SR}:d=0.85" \
  -af "lowpass=f=7000,aecho=0.65:0.55:70:0.18,afade=t=out:st=0.55:d=0.3,${LIMIT}" \
  -c:a pcm_s16le "$OUT/ching.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 9. Seal stamp — low thump + paper edge noise
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y \
  -f lavfi -i "aevalsrc=exprs='0.48*exp(-24*t)*sin(2*PI*88*t)+0.18*exp(-36*t)*sin(2*PI*175*t)+0.08*exp(-50*t)*sin(2*PI*320*t)':s=${SR}:d=0.5" \
  -f lavfi -i "anoisesrc=colour=brown:amplitude=0.22:duration=0.5:seed=31" \
  -filter_complex "[1:a]highpass=f=400,lowpass=f=2200,afade=t=out:st=0.05:d=0.12[n];[0:a][n]amix=inputs=2:duration=first:weights=1 0.4,lowpass=f=1100,aecho=0.5:0.4:30:0.12,afade=t=out:st=0.32:d=0.16,${LIMIT}" \
  -c:a pcm_s16le "$OUT/stamp.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 10. Meter tick — soft blip
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y -f lavfi -i \
  "aevalsrc=exprs='0.18*sin(2*PI*880*t)*exp(-140*t)+0.06*sin(2*PI*1760*t)*exp(-180*t)':s=${SR}:d=0.05" \
  -af "${LIMIT}" -c:a pcm_s16le "$OUT/tick.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 11. Soft alert chime — two-tone G5 → E5 with resonance
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y -f lavfi -i \
  "aevalsrc=exprs='\
0.14*sin(2*PI*783.99*t)*exp(-2.4*t)+\
0.08*sin(2*PI*1567.98*t)*exp(-3.2*t)+\
0.11*sin(2*PI*659.26*t)*exp(-2.4*t)+\
0.06*sin(2*PI*1318.51*t)*exp(-3.2*t)\
':s=${SR}:d=1.1" \
  -af "lowpass=f=4500,aecho=0.7:0.6:90|180:0.2|0.1,afade=t=out:st=0.75:d=0.35,${LIMIT}" \
  -c:a pcm_s16le "$OUT/chime.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 12. Confirm tap — soft thock
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y -f lavfi -i \
  "aevalsrc=exprs='0.38*exp(-20*t)*sin(2*PI*140*t)+0.12*exp(-35*t)*sin(2*PI*380*t)+0.05*exp(-60*t)*sin(2*PI*720*t)':s=${SR}:d=0.2" \
  -af "lowpass=f=1400,aecho=0.45:0.4:25:0.1,${LIMIT}" \
  -c:a pcm_s16le "$OUT/confirm.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 13. Resolving chord — warm F♯ major-ish stack, slow attack, long tail
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y -f lavfi -i \
  "aevalsrc=exprs='\
0.055*sin(2*PI*92.50*t)*(1-exp(-3*t))*exp(-0.35*t)+\
0.065*sin(2*PI*185.00*t)*(1-exp(-3.2*t))*exp(-0.38*t)+\
0.060*sin(2*PI*233.08*t)*(1-exp(-3.4*t))*exp(-0.40*t)+\
0.055*sin(2*PI*277.18*t)*(1-exp(-3.5*t))*exp(-0.42*t)+\
0.045*sin(2*PI*369.99*t)*(1-exp(-3.6*t))*exp(-0.45*t)+\
0.030*sin(2*PI*554.37*t)*(1-exp(-3.8*t))*exp(-0.50*t)+\
0.018*sin(2*PI*739.99*t)*(1-exp(-4*t))*exp(-0.55*t)\
':s=${SR}:d=3.2" \
  -af "lowpass=f=3000,aecho=0.8:0.7:140|280|420:0.25|0.15|0.08,afade=t=in:st=0:d=0.28,afade=t=out:st=2.6:d=0.55,${LIMIT}" \
  -c:a pcm_s16le "$OUT/chord.wav"

# ─────────────────────────────────────────────────────────────────────────────
# 14. Page settle — soft paper breath + low thud
# ─────────────────────────────────────────────────────────────────────────────
ffmpeg -hide_banner -loglevel error -y \
  -f lavfi -i "anoisesrc=colour=brown:amplitude=0.32:duration=1.15:seed=41" \
  -f lavfi -i "aevalsrc=exprs='0.12*exp(-12*t)*sin(2*PI*55*t)':s=${SR}:d=1.15" \
  -filter_complex "[0:a]lowpass=f=800,afade=t=in:st=0:d=0.08,afade=t=out:st=0.65:d=0.45[p];[1:a][p]amix=inputs=2:duration=first:weights=0.6 1,${LIMIT}" \
  -c:a pcm_s16le "$OUT/settle.wav"

COUNT=$(ls -1 "$OUT"/*.wav 2>/dev/null | wc -l | tr -d ' ')
echo "✓ Generated ${COUNT} stems in $OUT"
ls -lh "$OUT"/*.wav | awk '{print $5, $9}'
