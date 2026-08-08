#!/usr/bin/env bash
# Poll this session's mailbox and print a summary of new (unhandled) messages.
#
# Usage:  .claude/sessions/poll.sh <subject>
# Example:.claude/sessions/poll.sh summary
#
# Prints a compact digest of unhandled messages. The session cron runs this
# every interval and acts on the output. Exit 0 = new mail, 3 = no new mail.

set -euo pipefail

SUBJECT="${1:?usage: poll.sh <subject>}"
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
INBOX="$ROOT/.claude/sessions/inbox/$SUBJECT.md"
OUTBOX="$ROOT/.claude/sessions/outbox/$SUBJECT.md"

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