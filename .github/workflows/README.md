# GitHub Actions — iOS Build

This workflow builds the **Needs vs. Wants** iOS app on Apple's macOS runners so you
don't need your own Mac.

## What the workflow does

| Job | Trigger | Output |
|---|---|---|
| `build` | Push to `main`/`master`, PRs touching `ios/`, or manual | `.app` bundle (Simulator) |
| `release` | *(commented out — see below)* | Signed `.ipa` for TestFlight |

## How to use it

### 1. Push this repo to GitHub

If you haven't already:

```bash
git init
git add .
git commit -m "feat: add iOS source + GitHub Actions CI"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/needs-vs-wants.git
git push -u origin main
```

### 2. Run the workflow

- Go to **Actions → iOS Build → Run workflow** in your GitHub repo.
- It builds for the **iPhone 15 Simulator** (~3-4 minutes).
- Download the artifact `NeedsVsWants-Simulator` when done.

### 3. Test the build without a Mac

#### Option A: Appetize.io (Browser simulator)
1. Go to [appetize.io/upload](https://appetize.io/upload)
2. Drag the `.app` folder into the uploader (they accept `.app` bundles)
3. Run it in your browser — share the link with anyone

> ⚠️ Appetize.io free tier has session limits. Great for quick demos.

#### Option B: BrowserStack / AWS Device Farm
1. Subscribe to [BrowserStack App Live](https://www.browserstack.com/app-live) or
   [AWS Device Farm](https://aws.amazon.com/device-farm/)
2. Upload the `.app` bundle
3. Interact with it on real iPhones via your browser

#### Option C: Ask a friend with a Mac
Give them the `.app` bundle; they can drop it into their iOS Simulator and run it.

---

## Going further: Build a real `.ipa` for iPhone / TestFlight

The simulator build proves the code compiles. To install on a real iPhone or ship to
TestFlight, you need an **Apple Developer Program** account ($99/year).

### Steps

1. **Enroll** at [developer.apple.com](https://developer.apple.com)
2. **Create** an App ID (`com.needsvswants.ios`) + Distribution certificate + Provisioning profile
3. **Export** your certificate as a `.p12` file with a password
4. **Base64-encode** the files:
   ```bash
   base64 -i Certificates.p12 | pbcopy      # copy to clipboard
   base64 -i AppStore.mobileprovision | pbcopy
   ```
5. **Add GitHub Secrets** (Settings → Secrets and variables → Actions):
   | Secret | Value |
   |---|---|
   | `BUILD_CERTIFICATE_BASE64` | base64 of your `.p12` |
   | `P12_PASSWORD` | password for the `.p12` |
   | `BUILD_PROVISION_PROFILE` | base64 of `.mobileprovision` |
   | `DEVELOPMENT_TEAM` | Your Apple Team ID (10 chars) |

6. **Uncomment** the `release:` job in `.github/workflows/ios.yml` (remove the `#` prefix
   on lines 69-138).

7. **Push** — the workflow will now produce a signed `.ipa` on every `main` push.

### Upload to TestFlight automatically (optional)

Add the [Apple App Store Connect action](https://github.com/marketplace/actions/upload-to-app-store-connect)
to the release job and it will upload straight to TestFlight after every successful build.

---

## Troubleshooting

| Problem | Fix |
|---|---|
| "XcodeGen not found" | The `brew install xcodegen` step should handle this; re-run the workflow. |
| Build fails with SwiftData errors | Make sure the runner uses Xcode 15+ (the workflow selects `Xcode_15.4.app`). |
| `.app` not found after build | Check the `Package .app for artifacts` step logs; the DerivedData path may shift between Xcode versions. |
| Code signing fails (release) | Double-check your Team ID, certificate expiry, and provisioning profile App ID match `com.needsvswants.ios`. |
