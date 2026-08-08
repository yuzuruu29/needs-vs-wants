# Cross-Session Coordination Protocol

This directory lets multiple independent Claude Code sessions talk to each other
through shared files on disk. Claude Code has **no built-in cross-session
messaging** — this mailing protocol is the coordination layer.

## Concept

Each session has a **subject identity** (what workstream it owns: `summary`,
`paywall`, `history`, `platform`, ...). Messages are routed by subject.

```
sessions/
  inbox/
    <subject>.md     # messages addressed to <subject>
  outbox/
    <subject>.md     # messages <subject> has sent (for audit trail)
  README.md          # this file
```

## How a message flows

1. **Sender** appends to `inbox/<recipient>.md` (and copies to its own
   `outbox/<sender>.md`).
2. **Recipient** polls its inbox (via a durable cron, typically every 60–120s).
3. Recipient reads new messages, acts on them, and appends a reply to
   `inbox/<sender>.md`.
4. Recipient marks handled messages with a `[x]` checkbox or moves them to a
   `## Done` section, so it doesn't re-process them.

## Message format

Each message is a top-level heading with a header block:

```markdown
## YYYY-MM-DD HH:MM — from <sender>

- **Status:** new | done | needs-reply
- **Thread:** <optional short thread id to group replies>
- **Related:** <files/commits touched, optional>

<Body>

Reply: <!-- appended below by the recipient -->
```

## Rules of engagement

- **One identity per session.** If you restart a workstream, reuse the same
  subject name so routing stays stable.
- **Poll, don't push.** Sessions wake on their cron; they do not wait on each
  other's live state.
- **Never edit another session's running files.** Only the mailbox directory and
  memory are shared; everything else is owned by the session that's working on it.
- **Check for a `## Pending` section** at the top of your inbox on every wake —
  that's unhandled work.
- **Report facts, not opinions.** When you act on a message, write what you did
  and any files/commits you changed so the sender can verify.

## One-time setup per session

Every session that participates must register itself by running a durable cron
that polls its own inbox. From the session, call the scheduled-task helper:

- Subject: `<your-subject-name>`
- Interval: every 1–2 minutes

On each wake, the session reads `inbox/<subject>.md`, processes any `[ ]`/new
messages, and replies. Sessions that are idle (no new inbox items) should just
report "no new mail" and stop — do not burn tokens narrating.

## What this does NOT do

- No live chat — messages are only picked up on the next poll (up to the cron interval).
- No presence detection — a session must be running its cron to receive mail.
- No cross-session agent/task registry — `SendMessage`, `Agent`, and `TaskList`
  only reach agents inside the *same* session.