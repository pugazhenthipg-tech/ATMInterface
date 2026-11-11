# ATM Interface

A Java console ATM simulator with PIN authentication, balance checks, deposits, withdrawals, and transaction history.

## Features

- 4-digit PIN authentication (demo PIN: `1234`, locks after 3 wrong attempts)
- Deposit and withdrawal with a per-transaction limit (`50,000.00`)
- Opening demo balance of `5,000.00`
- Timestamped transaction history
- Non-interactive `--demo` mode

## Compile and Run

```bash
javac ATMInterface.java
java ATMInterface
```

Run demo mode:

```bash
java ATMInterface --demo
```
