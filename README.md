Inventory & POS System (Java)
A console-based Inventory and Point-of-Sale application built with Object-Oriented Programming in Java. It manages products, applies discounts and taxes, generates itemised bills, and produces low-stock and daily sales reports.
Course: 23CSR303 – Object Oriented Programming with Java
Assignment: II – Specification 6: Inventory & POS System
---
Features
Add, update and remove products (electronics and groceries)
Stock management with controlled updates (`addStock`, `reduceStock`)
Cart building and detailed invoice generation
Flat percentage discounts and coupon-code discounts
Category-specific tax and discount rules
Expiry handling for groceries (near-expiry markdowns, expired items blocked)
Variable-weight billing for loose items (e.g. rice, tomatoes in KG)
Low-stock report and daily sales report
---
Project Structure
```
InventoryPOSSystem/
└── src/
    └── PosSystem/
        ├── Product.java
        ├── Electronics.java
        ├── Grocery.java
        ├── InventoryManager.java
        └── POSAppMain.java
```
Class	Description
`Product`	Base class with `id`, `name`, `basePrice`, `stockQty`; stock handling, discount methods and billing
`Electronics`	Extends `Product`; adds brand, category, warranty and GST rate with category-specific pricing
`Grocery`	Extends `Product`; adds expiry date, unit type, organic flag and storage type; handles variable weight
`InventoryManager`	Manages the product list, cart, invoicing, stock updates and reports
`POSAppMain`	Main class that tests the whole system
---
OOP Concepts Demonstrated
Concept	Where
Inheritance	`Electronics` and `Grocery` extend `Product`
Method Overloading	`priceAfterDiscount(double percent)` and `priceAfterDiscount(String couponCode)`; `generateInvoice()` and `generateInvoice(String couponCode)`
Method Overriding	`getTaxRate()`, `getCategoryDiscount()`, `getCategory()`, `display()`; `Grocery` also overrides `reduceStock()`
Polymorphism	`InventoryManager` stores everything as `List<Product>` and calls the overridden methods to compute totals and bills
Encapsulation	All fields are `private`; stock changes only through `addStock()` / `reduceStock()`; `setBasePrice()` validates input
---
Pricing Rules
Item	Tax	Discount
Electronics	18% GST	Mobile 5%, Accessories 10%, others 0%
Grocery (loose, sold by KG/LITRE)	0%	30% if expiring in 2 days or less, 10% if in 5 days or less
Grocery (packaged, sold by PCS)	5%	Same near-expiry markdown
Coupons: `SAVE10` (10%), `FEST20` (20%), `NEW5` (5%). A coupon discount is added on top of the category discount.
---
How to Run
Eclipse
Create a new Java Project.
Create a package named `PosSystem` under `src`.
Copy all five `.java` files into the package.
Right-click `POSAppMain.java` → Run As → Java Application.
Command line
```bash
javac -d out src/PosSystem/*.java
java -cp out PosSystem.POSAppMain
```
Requirements: JDK 8 or later.
---
Sample Output (excerpt)
```
==================== SMART MART ====================
Invoice No: 1001
Coupon: SAVE10 (10.0% off)
---------------------------------------------------------------
Laptop            1.00 x  52000.00 | Disc  10.0% | Tax 18.0% |    55224.00
Smartphone        2.00 x  25000.00 | Disc  15.0% | Tax 18.0% |    50150.00
Rice              2.50 x     60.00 | Disc  10.0% | Tax  0.0% |      135.00
Milk              2.00 x     30.00 | Disc  40.0% | Tax  5.0% |       37.80
Tomato            1.50 x     40.00 | Disc  20.0% | Tax  0.0% |       48.00
Cannot sell Bread: product expired on 2026-10-29
---------------------------------------------------------------
Subtotal :   89519.00
Tax      :   16075.80
TOTAL    :  105594.80
===============================================================

----- LOW STOCK REPORT (threshold <= 5.0) -----
Earbuds -> 5.0 left
Milk -> 2.0 left
```
Expiry dates in the demo are set relative to the day the program is run, so the markdowns and dates in your output will match your own run date.
---
Author
Department of Computer Science and Engineering
Karpagam College of Engineering (Autonomous)
