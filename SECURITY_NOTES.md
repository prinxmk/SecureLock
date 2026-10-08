# SecureLock Security Notes

## What this release can and cannot do

A third-party APK cannot safely claim to replace Android's own PIN/password/pattern credential system on a normal consumer device.

SecureLock is intentionally designed as an additional security layer.

### Boot

BOOT_COMPLETED / LOCKED_BOOT_COMPLETED starts a foreground monitoring service where Android permits it.

### Background operation

The service uses START_STICKY. Some phone manufacturers may still terminate or restrict background work. Users should set SecureLock battery usage to unrestricted where available.

### Accessibility

Accessibility is optional in Phase 1 and is not silently enabled. The owner must explicitly enable the service in Android Settings.

### Overlay

The app requests the ability to draw over other apps only through the normal Android settings screen. It does not attempt to grant itself that permission.

### PIN storage

The PIN is not stored in plaintext. Phase 1 stores a random salt and SHA-256 digest locally.

For a production release, a memory-hard password KDF (for example PBKDF2 with a high iteration count) plus Android Keystore-backed key material should replace this basic implementation.

### Recovery

Do not add a hidden master PIN. A future recovery mechanism should be explicit and auditable.

### Device Owner mode

The strongest future architecture is a managed-device/device-owner mode. It can provide capabilities that ordinary third-party applications cannot. It should be offered as an explicit provisioning option rather than attempting to bypass Android's security model.
