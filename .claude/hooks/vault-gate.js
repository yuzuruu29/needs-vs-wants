#!/usr/bin/env node
/*
 * vault-gate.js — PreToolUse hook enforcing the Second Brain rule.
 *
 * Rule being enforced (project CLAUDE.md / AGENTS.md):
 *   Before ANY code write/edit, the agent MUST have read the Obsidian Second Brain.
 *
 * Mechanism:
 *   - Fires on Read | Write | Edit | NotebookEdit.
 *   - On READ of any path under the Second Brain vault  -> stamps .claude/.vault-gate
 *     with today's date. (Reading the vault IS the rule; it unlocks the gate.)
 *   - On WRITE/EDIT/NotebookEdit of CODE (non-exempt)  -> ALLOWED only if the gate
 *     is stamped for today. Otherwise BLOCKS (exit 2) with an instructive message.
 *   - Exempt paths (always allowed, never gated):
 *       * anything under the Second Brain vault
 *       * the instruction files (CLAUDE.md, AGENTS.md)
 *       * settings files and this hook itself
 *
 * Escape hatch: set env DISABLE_VAULT_GATE=1 to bypass (troubleshooting only).
 */

const fs = require('fs');
const path = require('path');

const ROOT = path.join(__dirname, '..', '..');
const MARKER = path.join(ROOT, '.claude', '.vault-gate');
const VAULT_FRAG = ['obsidian vault', 'second brain'];

function norm(p) {
  return (p || '').replace(/[\\/]+/g, '/').toLowerCase();
}

function isVault(p) {
  const n = norm(p);
  return VAULT_FRAG.every((f) => n.includes(f));
}

function isExempt(p) {
  const n = norm(p);
  if (isVault(p)) return true;
  if (n.endsWith('/claude.md') || n.endsWith('/agents.md')) return true;
  if (n.includes('/.claude/settings')) return true;
  if (n.includes('/.claude/hooks/')) return true;
  if (n.endsWith('/.claude/.vault-gate')) return true;
  return false;
}

function today() {
  const d = new Date();
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const dd = String(d.getDate()).padStart(2, '0');
  return `${d.getFullYear()}-${mm}-${dd}`;
}

let raw = '';
process.stdin.setEncoding('utf8');
process.stdin.on('data', (c) => (raw += c));
process.stdin.on('end', () => {
  if (process.env.DISABLE_VAULT_GATE === '1') process.exit(0);

  let req;
  try {
    req = JSON.parse(raw || '{}');
  } catch (e) {
    process.exit(0); // malformed input: never block on a parse failure
  }

  const tool = req.tool_name || '';
  const input = req.tool_input || {};
  const p = input.file_path || input.path || input.notebook_path || '';

  try { fs.mkdirSync(path.join(ROOT, '.claude'), { recursive: true }); } catch (e) {}

  // Read of the vault stamps the gate (and is always allowed).
  if (tool === 'Read') {
    if (isVault(p)) {
      try { fs.writeFileSync(MARKER, today()); } catch (e) {}
    }
    process.exit(0);
  }

  // Write/Edit/NotebookEdit:
  const codeTools = ['Write', 'Edit', 'NotebookEdit'];
  if (!codeTools.includes(tool)) process.exit(0);

  // Writing to the vault = recording findings; stamps gate, always allowed.
  if (isVault(p)) {
    try { fs.writeFileSync(MARKER, today()); } catch (e) {}
    process.exit(0);
  }

  if (isExempt(p)) process.exit(0);

  let stampedFor = '';
  try { stampedFor = fs.readFileSync(MARKER, 'utf8').trim(); } catch (e) {}

  if (stampedFor === today()) process.exit(0);

  const msg = [
    '[vault-gate] BLOCKED: the Second Brain rule is not satisfied for code edits.',
    `You are editing: ${p}`,
    '',
    'Required before any code edit (per project CLAUDE.md / AGENTS.md):',
    '  1. Read the Second Brain index:',
    '       C:/Obsidian Vault/Second Brain/Memory/00 Memory Layer/Memory Index.md',
    '  2. Read the project notes:',
    '       C:/Obsidian Vault/Second Brain/Projects/Needs vs Wants/{Summary,Tasks,Decisions}.md',
    '  3. (Architecture/cross-surface work) consult the graphify graph.',
    '',
    'Reading any path under the vault automatically unlocks in-project code edits.',
    'If the vault is genuinely unreachable, STOP and tell the user; do not bypass.',
    '(Troubleshooting escape: env DISABLE_VAULT_GATE=1)',
    '',
  ].join('\n');

  // Print the reasoning block (goes back to the model), then block with exit 2.
  process.stdout.write(msg + '\n');
  process.exit(2);
});
