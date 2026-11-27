# Arabic typography

- Use the bundled Thmanyah Sans for Arabic UI; preserve the existing Latin fonts, monospace codes and document fonts.
- Read docs/ARABIC_TYPOGRAPHY.md before changing typography or the pinned font input.
- Use ArabicFonts.watch for separately created windows; use the Arabic dialog builders for new dialogs. Service-owned views need watchRoot.
- Preserve text, spans, selection, accessibility font sizes, weights and RTL behavior. Do not restyle third-party Android widgets or PDF page/text-mode content.
- Do not commit raw fonts or keys. Keep acquisition at build time and runtime typography offline. The packaged-font format follows the user's SahlTV implementation; obfuscation is not proof of license compliance.
- Run typography tests, Android unit tests/lint/build and the final APK asset check. Submit a PR and merge automatically after relevant CI passes.

# GitHub workflow

- Across all chats in this project, when pushing changes to GitHub, automatically push a branch, open a pull request, and merge it into `main` without asking for confirmation (never push directly to `main`).

# Planning before editing

- For any task that modifies code or files (unless I say "skip plan"):
  - Inspect the relevant files first without editing anything.
  - Output a concise plan (goal, exact files to change, risks, and test steps).
  - End with "Plan ready. Reply 'Proceed' to execute." and STOP.
  - Only edit files and run tests after I reply "Proceed".
