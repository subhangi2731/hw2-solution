# hw2

The homework will be based on this project named "Expense Tracker",where users will be able to add/remove daily transaction. 

## Compile

To compile the code from terminal, use the following command:
```
cd src
javac ExpenseTrackerApp.java
java ExpenseTrackerApp
```

You should be able to view the GUI of the project upon successful compilation. 

## Java Version
This code is compiled with ```openjdk 17.0.7 2023-04-18```. Please update your JDK accordingly if you face any incompatibility issue.

## Features
- Add a new transaction: First specify the amount and category. Then click on the Add transaction button. Adds the new transaction to the list and updates the total cost.
- Filter the transaction list by either amount or category: First specify the amount or category to be matched. Then click the corresponding Filter button. Highlights the matching transactions in the list.

## Undo Functionality Added
- An intuitive "Remove Transaction" button has been integrated into the user interface, allowing users to select a specific transaction from the displayed table and remove it with a single click. This feature enables seamless deletion of individual transaction records from both the visual interface and the underlying model, ensuring that all related calculations such as total expenses, filtered views, or highlights are automatically updated in real-time.


- To complement this removal action, an Undo mechanism has been implemented. Unlike a traditional “undo” that reverses the most recent action blindly, this system allows users to remove any specifically selected transaction and still retain the ability to restore it shortly after. This adds an extra layer of flexibility and control for users who may delete the wrong entry by mistake.


## Key Functional Highlights of Undo Functionality
- The removed transaction is stored temporarily in memory (e.g. using a variable or stack in the model).


- The Undo button becomes enabled immediately after a transaction is removed.


- Clicking Undo restores the most recently deleted transaction to the data model and updates the view accordingly.


- The view is refreshed to reflect the current state of the transaction list after every add, remove, or undo action.