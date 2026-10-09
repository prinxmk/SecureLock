# Security notes
- Application-level layer only; not a replacement for Android system credentials.
- Screen state is dynamically monitored by the foreground service.
- Boot count and event history are persisted locally.
- Accessibility is detected, never silently enabled.
- PIN is salted and hashed with SHA-256; a production build should use a reviewed, memory-hard password KDF and Android Keystore-backed design.
- Home and Recents cannot be disabled by ordinary apps.
- Device Owner / managed-device mode should be an explicit future setup option.
