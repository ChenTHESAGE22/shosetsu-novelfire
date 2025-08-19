# Novelfire Shosetsu Repository (Multi-language ready)

This package contains a **Novelfire** extension source file (Kotlin) and a simple `repo.json`.
It's prepared so you can upload it to GitHub and enable GitHub Pages to expose `repo.json`.

## Files included
- `Novelfire.kt` — Kotlin source code for a Tachiyomi-style scraper (multi-language ready).
- `repo.json` — A minimal repository descriptor (edit after upload if needed).
- `README.md` — This file.

## Quick upload steps (GitHub web UI)
1. Create a new public repository on GitHub (e.g. `shosetsu-novelfire`).
2. Click "Add file" → "Upload files" and upload the three files from this package.
3. Commit the files to the `main` branch.
4. In the repository settings, enable **GitHub Pages**:
   - Under "Pages", set the source to `main` branch and `/ (root)`.
   - Save — GitHub will show a URL like `https://yourusername.github.io/shosetsu-novelfire/`
5. Your `repo.json` will be available at:
   ```
   https://yourusername.github.io/shosetsu-novelfire/repo.json
   ```
6. In the Shosetsu app, go to Sources/Repositories and add the URL above.

## Notes & building a real extension
- **Important**: Many readers (Shosetsu, Tachiyomi) expect compiled extension packages (`.shosetsu` or `.apk`/`.aes` depending on platform). This package contains only the **source** Kotlin file.
- If Shosetsu requires a compiled extension, follow the Tachiyomi extension build instructions:
  1. Use the Tachiyomi-extensions template (Gradle/Kotlin).
  2. Place `Novelfire.kt` under the appropriate package path.
  3. Build an extension JAR/APK and host the compiled extension file; then update `repo.json` to point to the compiled artifact.
- If you want, I can provide step-by-step instructions to compile the Kotlin source into a Shosetsu-compatible extension.

## If Novelfire changes layout
- The selectors in `Novelfire.kt` include multiple fallbacks. If Novelfire updates their HTML structure, the extension may need selector updates.
- I can help maintain/fix the selectors if you encounter errors.

## Support
Reply here and tell me when you've uploaded the files to GitHub (or paste your repo link) and I'll give the exact `repo.json` URL you should add into Shosetsu.
