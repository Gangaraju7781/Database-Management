Team Members:

Paul Menexas                                        U78320172       pmenexas@bu.edu
Abbireddy, BhaskarDurgaVeeraVenkata Ganga Raju      U89625488  	    bhaskar9@bu.edu
Shweta Baindur 					   					U73181758 		shwetab@bu.edu 

Please run the below commands to setup the mysql database and tables: 

CREATE DATABASE bank;

show databases;

use bank;

CREATE TABLE user_table(

    userid INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 

    db_username VARCHAR(255),
    db_password VARCHAR(255),
	firstname VARCHAR(255),
    lastname VARCHAR(255),
	email VARCHAR(25),
    dateofbirth VARCHAR(20),
	gender VARCHAR(20),
    nationality VARCHAR(40),
    ssn VARCHAR(40),
    phone VARCHAR(40),
	address VARCHAR(255),
    banker_or_customer VARCHAR(1),
    lastLoggedInDate VARCHAR(255));


CREATE TABLE transaction(

    time_stamp VARCHAR(255),
    userid VARCHAR(255),
	transaction_log VARCHAR(255));
 

CREATE TABLE accounts(

    accountid INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    isOpen VARCHAR(25),
    userid VARCHAR(255),
	balance VARCHAR(255),
    realizedGains VARCHAR(255),
    type varchar(20));
    

CREATE TABLE loan(

    userid VARCHAR(255),
	loanid INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    principal VARCHAR(255),
	interestRate VARCHAR(255),
    numYears varchar(255),
    numPeriodsPerYears varchar(255),
    requestedDate varchar(255),
    isOpen varchar(255));

create table stock(

    ticker VARCHAR(255),
    company_name VARCHAR(255));
    

create table stockPrice(

	stockprice_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    ticker VARCHAR(255),
    price VARCHAR(255),
    stockPriceDate VARCHAR(255));
    

create table positions(

	userid VARCHAR(255),
    isOpen VARCHAR(255),
    openDate VARCHAR(255),
    ticker VARCHAR(255),
    quantity VARCHAR(255),
    averageCost VARCHAR(255),
	accountid VARCHAR(50)    
    );

create table bank(
	cashReserves VARCHAR(255),
    openCloseFee VARCHAR(255),
    txFee VARCHAR(255),
    transferFee VARCHAR(255),
    withdrawalFee VARCHAR(255),
    savingsInterestRate VARCHAR(255),
    loanInterestRate VARCHAR(255),
    minCheckingSavingsInitialDeposit VARCHAR(255),
    minSavingsBalanceToEarnInterest VARCHAR(255),
	minInitialSecuritiesTransfer VARCHAR(255),
	minSavingsBalanceWithSecurities VARCHAR(255),
    openSecuritiesThreshold VARCHAR(255)
    );
    

insert into user_table (db_username,db_password,firstname,lastname,email,dateofbirth,gender,nationality,ssn,phone,address,banker_or_customer,lastLoggedInDate) 
values ("pmen", "123", "Paul", "Menexas", "pmenexas@bu.edu", "8/10/1998", "male", "US", "1234", "646-295-6434" , "Main St", "c", "2021-12-10"),
("shwet", "abc", "Shweta", "Baindur", "shwetab@bu.edu", "11/11/1999", "female", "India", "4321", "123-456-7890" , "Oak Ave", "c", "2021-12-10"),
("abbi", "321", "Abbi", "Bhaska", "bhaskar9@bu.edu", "2/3/2000", "male", "India", "5678", "987-654-3210" , "Candy Ln", "c", "2021-12-10"),
("igor", "qwe", "Igor", "Solomatin", "igor@bu.edu", "5/15/2000", "male", "Russia", "8765", "987-123-4560" , "Coconut Dr", "c", "2021-12-10"),
("vit", "asd", "Vitor", "Vicente", "vitor@bu.edu", "7/1/2000", "male", "Brazil", "1928", "123-987-6540" , "Cambridge St", "b", "2021-12-10");

insert into accounts values ("1", "true", "1", "5000", "0", "Checking"),
("2", "true", "1", "10000", "0", "Savings"),
("3", "true", "1", "8000", "1000", "Securities"),
("4", "true", "2", "10000", "0", "Checking"),
("5", "true", "2", "20000", "0", "Savings"),
("6", "true", "3", "5000", "0", "Checking");

insert into bank values ("5000", "10", "5", "3", "5", "0.01", "0.05", "200", "2000", "1000", "2500", "5000");

insert into stock values ('AAPL', 'Apple Inc.'),
('TSLA', 'Tesla Inc.'),
('AMZN', 'Amazon Corp.'),
('PYPL', 'Paypal Holdings Inc.'),
('GME', 'GameStop Corp.'),
('META', 'Meta Platforms Inc.');

insert into positions values ("1", "true", "2021-12-01", "AAPL", "5", "175", "3"),
("1", "true", "2021-11-15", "TSLA", "2", "1000", "3"),
("1", "true", "2021-10-26", "GME", "10", "140", "3");

insert into loan values ("1", "1", "2000", "0.05", "2", "12", "2021-11-01", "true"),
("1", "2", "1000", "0.05", "1", "12", "2021-12-01", "true"),
("2", "3", "5000", "0.05", "5", "12", "2021-12-01", "true");

insert into stockPrice values ("1", "AAPL", "195", "2021-12-17"),
("2", "TSLA", "935", "2021-12-17"),
("3", "AMZN", "3125", "2021-12-17"),
("4", "PYPL", "180", "2021-12-17"),
("5", "GME", "170", "2021-12-17"),
("6", "META", "335", "2021-12-17");

insert into transaction values ("2021-12-08","1", "Withdrew $1000 from Savings account"),
("2021-12-08","2", "Deposited $5000 into Checking account"),
("2021-12-08","3", "Closed Securities account and transferred $1000 into Savings account"),
("2021-12-10","1", "Deposited $5000 into Checking account"),
("2021-12-10","1", "Bought 5 shares of AAPL at $175 per share"),
("2021-12-10","1", "Bought 2 shares of TSLA at $1000 per share"),
("2021-12-10","1", "Bought 10 shares of GME at $140 per share"),
("2021-12-10","2", "Opened a Savings account with a deposit of $23000"),
("2021-12-10","2", "Withdrew $3000 from Savings account"),
("2021-12-10","3", "Deposited $5000 into Checking account"),
("2021-12-10","4", "Deposited $5000 into Checking account");

How to run the files: 

Please use the Intellij IDE for ease of running this project. Open the src folder. 
Create a new project if needed and copy paste the java and .form files into the src folder. 
In the Project Structure please add the 2 Jar files, MYSQL connector jar file and poi jar file. 

Please change the username and password in the Database.java class to your respective mysql workbench username and password. 

The program can now be run from the Main.java class on the IDE.

Design Patterns used:

Null, Facade, Factory and Singleton Pattern. 
