# SecureLock Android

SecureLock is an Android application-level security layer designed for phones where the built-in screen-lock settings may be malfunctioning.

## Important security distinction

SecureLock **does not replace Android's underlying system credential system**. A normal third-party Android application cannot become the operating system's true lock screen without device-owner/system-level privileges.

SecureLock instead provides:

- PIN authentication
- Persistent foreground security monitoring
- Boot/reboot startup
- Screen-off lock trigger
- Accessibility-service integration for stronger application monitoring
- Failed-attempt throttling
- Local salted SHA-256 PIN verification
- No cloud account required for the core lock

## Android compatibility

- Minimum SDK: 28 (Android 9)
- Target SDK: 35
- Java
- Dependency-light
- Intended for GitHub Actions compilation

## Build with GitHub

1. Create a new GitHub repository.
2. Upload all files in this project to the repository root.
3. Push to `main` or `master`, or manually run:
   `Actions -> Build SecureLock APK -> Run workflow`.
4. Download the APK from the workflow's Artifacts section.

## First setup on the phone

1. Install SecureLock.
2. Open it.
3. Grant notification permission when requested.
4. Set a 6–12 digit PIN.
5. Test "Test SecureLock".
6. Open Android Accessibility settings and enable SecureLock if you want the monitoring layer.
7. Allow "Display over other apps" if your Android version/device permits it.
8. Do not remove SecureLock from battery optimization if your manufacturer aggressively stops background services; instead, set SecureLock to "Unrestricted" battery use where that option exists.
9. Reboot and confirm that the SecureLock notification returns.

## Current phase

Phase 1 provides the core security foundation and boot monitoring.

The following should be implemented as subsequent hardened phases rather than pretending Phase 1 can bypass Android security:

- Protected application selection
- Stronger app-launch interception
- Device Policy / device-owner deployment mode
- Biometric authentication
- Intruder camera capture
- Encrypted security event database
- Recovery workflow
- Tamper-resistance improvements
- Optional enterprise/kiosk mode

## Why SecureLock may not automatically appear immediately after reboot

Modern Android versions deliberately restrict background activity launches. The boot receiver therefore starts a foreground security service, which is the reliable background mechanism. An app cannot guarantee that Android will display an arbitrary activity over the system UI immediately after every reboot.

For a genuinely device-wide kiosk/lock experience, SecureLock should later be deployed as a **Device Owner / managed-device application**. That is a different security model and may require provisioning the phone.

## Privacy

The core Phase 1 implementation does not upload the PIN or security data to a server.
