---
name: expense-cli
description: Track personal expenses using the expense CLI tool. Use when the user mentions spending money, making a purchase, or wants to record/view expenses. Supports adding expenses with amount, currency, category, description and listing/filtering expenses by date.
---

# Expense CLI Tool

Use this skill to interact with the expense tracking CLI tool installed at `/usr/local/bin/expense`.

## Prerequisites
Before using this skill, verify that the execution environment is capable of running a `.jar` file:

- A compatible Java Runtime Environment (JRE) or Java Development Kit (JDK) is installed.
- The installed Java version is equal to or higher than the version used to compile the `.jar`.
- The `java` command is available in the system PATH.
- The target `.jar` file exists and is readable.
- The `.jar` is executable (contains a valid `Main-Class` entry in `META-INF/MANIFEST.MF`).

If any of these conditions are not met, abort execution and return a clear error message with remediation guidance.
if environment requirements are met, check if the `expense` command is available: 

```bash
which expense
```

If the command is not found, use the **bin-builder** skill to build and install it:

```bash
./wrap-jar.sh
```

## Available Commands

### Add Expense
```bash
expense add --amount <amount> --currency <currency> --category <category> [--description "text"] [--createdAt "dd-MM-yyyy HH:mm"]
```

**Required flags:**
- `--amount`: Numeric value (e.g., 360000)
- `--currency`: Currency code - VND, USD, YEN, EUR, GBP, etc.
- `--category`: One of FOOD, RENT, TRANSPORTATION, UTILITIES, ENTERTAINMENT, OTHER

**Optional flags:**
- `--description`: Text description of the expense
- `--createdAt`: Custom date/time in format "dd-MM-yyyy HH:mm" (defaults to now)

### List Expenses
```bash
expense list [--time dd-MM-yyyy]
```

- Without `--time`: Lists all expenses
- With `--time`: Lists expenses for specific date (format: dd-MM-yyyy)

## Categories Reference

| Category | Use For |
|----------|---------|
| FOOD | Meals, groceries, snacks, restaurants |
| RENT | Housing, rent payments |
| TRANSPORTATION | Fuel, public transit, taxis, vehicle costs |
| UTILITIES | Electricity, water, internet, phone bills |
| ENTERTAINMENT | Movies, games, subscriptions, hobbies |
| OTHER | Anything that doesn't fit above |

## Workflow

1. **When user mentions spending/purchasing something:**
   - Ask for: amount, currency (default: VND), category, description
   - Execute: `expense add --amount <amount> --currency <currency> --category <category> --description "<description>"`
   - Confirm success

2. **When user asks to view expenses:**
   - Check if they want: all expenses, or specific date
   - Execute: `expense list` or `expense list --time <date>`
   - Present results clearly

3. **Data location:** All expenses stored in `~/my_expenses.csv`

## Currency Defaults

- If user is Tung (Vietnam timezone), default to **VND** unless specified otherwise
- Always confirm currency if ambiguous

## Date Formats

- CLI expects: `dd-MM-yyyy` (e.g., 01-03-2026)
- Convert user's natural dates to this format

## Installation / Build

To build and install the CLI tool, run:

```bash
./skills/expense-cli/bin-builder/scripts/wrap-jar.sh
```

This script will:
1. Build the JAR file with `mvn package`
2. Copy the JAR to `~/expense-cli.jar`
3. Create a wrapper script at `/usr/local/bin/expense`
4. Make the wrapper executable

After running the script, the `expense` command will be available system-wide.
