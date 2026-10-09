# SecureLock Phase 1.1 — Reliability Foundation

GitHub-ready Java/Android project, minimum Android 9 (API 28), package `com.securelock.app`.

## Changes in 1.1
- Screen-on/off/user-present broadcasts are registered dynamically by the foreground service. Android does not reliably deliver screen broadcasts to manifest receivers.
- Screen-off sets a pending-lock flag; screen-on attempts to launch SecureLock when a PIN is configured and enabled.
- Boot receiver records boot/package replacement events and starts the foreground service.
- Ongoing notification opens the dashboard.
- Diagnostic dashboard shows PIN status, service-start timestamp, screen-lock setting, overlay, Accessibility status, boot-event count, and last event.
- Accessibility is not silently re-enabled: Android requires manual user approval.

## Build
1. Upload all project contents to a GitHub repository root.
2. Push to `main`/`master`, or run Actions → Build SecureLock Phase 1.1 APK → Run workflow.
3. Download debug or release APK from the workflow Artifacts.

## Install
The package name is unchanged. If Android accepts the signing key, install over the old version. If it reports a signature conflict, uninstall old SecureLock and install the new APK; uninstalling erases local settings, so set your PIN again.

## Test after install
1. Open SecureLock 1.1 and set/reconfirm a 6–12 digit PIN.
2. Confirm the ongoing notification appears.
3. Enable Accessibility in Android Settings; return and refresh diagnostics.
4. Keep screen-off locking enabled. Turn screen off with Power, wait 5 seconds, turn it on; SecureLock should attempt to launch after SCREEN_ON. Android background-launch restrictions can still prevent this on some firmware.
5. Reboot and wait 60 seconds; check notification, open dashboard and refresh diagnostics.
6. Inspect boot event count, service start, and last event.
7. If Accessibility is disabled after reboot, manually re-enable it. Apps cannot override this Android security setting.

## Limitations
SecureLock is an additional application-level layer, not the system credential screen. Ordinary apps cannot disable Home/Recents, silently re-enable Accessibility, or guarantee an Activity launches over all Android screens. Boot delivery may be delayed or blocked by OEM firmware. For stronger kiosk controls, Device Owner provisioning is a separate future path.
