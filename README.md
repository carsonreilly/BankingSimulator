# BankingSimulator

This is a banking simulator that works on the output terminal.

Multi threading is used to control the different "agents" (deposit, withdraw, audit). The lock will transfer to different agent threads based on randomized time.

Random amounts will be deposited and withdrawn. If too much is requested to be withdrawn, then the transaction will be blocked.

Transactions and flaged transaction will then be logged into an Excel (.csv) file.
