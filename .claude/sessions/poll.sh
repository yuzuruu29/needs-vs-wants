#!/usr/bin/env bash
# Poll this session's mailbox and print a summary of new (unhandled) messages.
#
# Usage:
#   .claude/sessions/poll.sh <subject>              # poll inbox for new mail
#   .claude/sessions/poll.sh status <subject>       # print the shared status table
#   .claude/sessions/poll.sh heartbeat <subject> "task"   # write this session's heartbeat
#
# Mailbox:  new mail = exit 0, none = exit 3.
# Heartbeat writes/updates the subject's row in a shared status.md so any
# session can see what all sessions are up to.

set -euo pipefail

SUBJECT="${1:?usage: poll.sh <subject>}"
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
INBOX="$ROOT/.claude/sessions/inbox/$SUBJECT.md"
OUTBOX="$ROOT/.claude/sessions/outbox/$SUBJECT.md"
STATUS="$ROOT/.claude/sessions/status.md"

# --- heartbeat subcommand: write this session's current task + timestamp ---
if [ "$SUBJECT" = "heartbeat" ]; then
  H_SUBJECT="${2:?usage: poll.sh heartbeat <subject> \"task\"}"
  H_TASK="${3:-idle}"
  H_TS="$(date '+%Y-%m-%d %H:%M')"
  # Rebuild the table: keep every `| x |` data row except this subject's old row,
  # then append this subject's fresh row. Header is always rewritten cleanly.
  tmp="$(mktemp)"
  if [ -f "$STATUS" ]; then
    # Data rows start with "| <subject> |" where subject != "Subject" (header).
    grep -E '^\| [^|]+\|' "$STATUS" \
      | grep -v '^| Subject' \
      | grep -v "| $H_SUBJECT |" > "$tmp" || true
  fi
  { echo "| Subject | Current task | Last seen |"; echo "|---|---|---|"; cat "$tmp"; printf '| %s | %s | %s |\n' "$H_SUBJECT" "$H_TASK" "$H_TS"; } > "$STATUS"
  rm -f "$tmp"
  echo "heartbeat: $H_SUBJECT updated"
  exit 0
fi

# --- status subcommand: print the shared status table ---
if [ "$SUBJECT" = "status" ]; then
  if [ -f "$STATUS" ]; then cat "$STATUS"; else echo "(no heartbeats yet)"; fi
  exit 0
fi

mkdir -p "$(dirname "$INBOX")" "$(dirname "$OUTBOX")"
[ -f "$INBOX" ] || { echo "inbox: <empty>"; exit 3; }

# A message is "new" if it has a Status line that is new/pending/needs-reply,
# or if it has NOT been marked done (no [x] and no "## Done" belongs to it).
unhandled=0
in_done=0
while IFS= read -r line; do
  case "$line" in
    "## Done"*) in_done=1 ;;
    "## "*)
      in_done=0
      ;;
  esac
  if [ "$in_done" = "0" ]; then
    case "$line" in
      \#\#\ *)
        echo "MESSAGE: $line"
        unhandled=1
        ;;
      "Reply:"*) ;;
      "  -"*) echo "        $line" ;;
    esac
  fi
done < "$INBOX"

if [ "$unhandled" = "0" ]; then
  echo "inbox: <no new mail>"
  exit 3
fi
echo "inbox: <has new mail>"
exit 0