/* Here are all of my tables, I only added an Itinerary table to connect the Customer and Flights tables
   I also included some INSERT INTO and SELECT statements to make it easier to test */

CREATE TABLE Customer (
    userID INT,
    [name] VARCHAR(50),
    handle VARCHAR(15) UNIQUE,
    [password] VARCHAR(50),
    PRIMARY KEY (userID)

);
CREATE TABLE Itinerary (
    [uID] INT FOREIGN KEY REFERENCES Customer(userID),
    [fID] INT FOREIGN KEY REFERENCES Flights(fid),
    PRIMARY KEY ([uID], [fID])

);

SELECT * FROM Customer
INSERT INTO Customer VALUES(1636743, 'Samuel', 'samueb3', 'sampassword')
INSERT INTO Customer VALUES(1654172, 'Michael', 'mich2', 'michpassword')
INSERT INTO Customer VALUES(3511845, 'Sarah', 'sara5', 'sarapassword')
INSERT INTO Customer VALUES(2483743, 'Meagan', 'mea3', 'meapassword')

SELECT * FROM Itinerary
INSERT INTO Itinerary VALUES(1636743, 0)
INSERT INTO Itinerary VALUES(1636743, 1)

CREATE TABLE Carriers (
    cid VARCHAR(5) NOT NULL PRIMARY KEY,
    [name] VARCHAR(20)
);
SELECT * FROM Flights
CREATE TABLE Flights (
    fid INT NOT NULL PRIMARY KEY,
    flight_num VARCHAR(20), 
    origin_city VARCHAR(50),
    dest_city VARCHAR(50),
    actual_time FLOAT,
    [year] INT,
    month_id INT,
    day_of_month INT,
    carrier_id VARCHAR(5) FOREIGN KEY REFERENCES Carriers(cid)
);
INSERT INTO Carriers
VALUES ('AA', 'American Airlines')
INSERT INTO Carriers
VALUES ('AB', 'American Boating')

INSERT INTO Flights
VALUES (4, 'SB001', 'Seattle WA', 'Boston MA', 0.5, 2015, 7, 10, 'AA')
INSERT INTO Flights
VALUES (3, 'SB003', 'San Francisco CA', 'Seattle WA', 1, 2015, 9, 10, 'AB')


