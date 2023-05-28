DELETE FROM PAYMENTOPTION WHERE 1 = 1;
DELETE FROM ARTICLE_CATEGORY WHERE 1 = 1;
DELETE FROM CATEGORY_ARTICLE WHERE 1 = 1;
DELETE FROM CATEGORY WHERE 1 = 1;
DELETE FROM BID WHERE 1 = 1;
DELETE FROM ARTICLE WHERE 1 = 1;
DELETE FROM CUSTOMER WHERE 1 = 1;


INSERT INTO APP.CATEGORY (ID, NAME) VALUES
                                        (1, 'Electronics'),
                                        (2, 'Sports'),
                                        (3, 'Home'),
                                        (4, 'Arts');


INSERT INTO APP.CUSTOMER (ID, EMAIL, FIRSTNAME, LASTNAME, PAY_CITY, PAY_COUNTRY, PAY_STATE, PAY_STREET, PAY_ZIPCODE, SHIP_CITY, SHIP_COUNTRY, SHIP_STATE, SHIP_STREET, SHIP_ZIPCODE) VALUES
                                                                                                                                                                                         (1, 'johndoe@example.com', 'John', 'Doe', 'New York', 'USA', 'NY', '123 Main St', '10001', 'New York', 'USA', 'NY', '123 Main St', '10001'),
                                                                                                                                                                                         (2, 'janedoe@example.com', 'Jane', 'Doe', 'Los Angeles', 'USA', 'CA', '456 Oak St', '90001', 'Los Angeles', 'USA', 'CA', '456 Oak St', '90001'),
                                                                                                                                                                                         (3, 'bobsmith@example.com', 'Bob', 'Smith', 'Chicago', 'USA', 'IL', '789 Maple Ave', '60601', 'Chicago', 'USA', 'IL', '789 Maple Ave', '60601');

INSERT INTO APP.PAYMENTOPTION (DTYPE, ID, OWNER, BANKACCOUNTNUMBER, BANKIDENTIFIER, CARDVERIFICATIONVALUE, CREDITCARDNUMBER, CREDITCARDVALIDTO, CUSTOMER_ID) VALUES ('BankPaymentOption', 1, null, 'AT4912312312', 'RZBCCZPP', null, null, null, 1);
INSERT INTO APP.PAYMENTOPTION (DTYPE, ID, OWNER, BANKACCOUNTNUMBER, BANKIDENTIFIER, CARDVERIFICATIONVALUE, CREDITCARDNUMBER, CREDITCARDVALIDTO, CUSTOMER_ID) VALUES ('CreditcardPaymentOption', 2, null, null, null, '777 ', '5431 1111 1111 1111', '2025-01-01', 1);

INSERT INTO APP.PAYMENTOPTION (DTYPE, ID, OWNER, BANKACCOUNTNUMBER, BANKIDENTIFIER, CARDVERIFICATIONVALUE, CREDITCARDNUMBER, CREDITCARDVALIDTO, CUSTOMER_ID) VALUES ('BankPaymentOption', 3, null, 'DE4912312312', 'BBBCCZPP', null, null, null, 2);
INSERT INTO APP.PAYMENTOPTION (DTYPE, ID, OWNER, BANKACCOUNTNUMBER, BANKIDENTIFIER, CARDVERIFICATIONVALUE, CREDITCARDNUMBER, CREDITCARDVALIDTO, CUSTOMER_ID) VALUES ('CreditcardPaymentOption', 4, null, null, null, '555 ', '4111 1111 1111 1111', '2024-01-01', 2);

INSERT INTO APP.PAYMENTOPTION (DTYPE, ID, OWNER, BANKACCOUNTNUMBER, BANKIDENTIFIER, CARDVERIFICATIONVALUE, CREDITCARDNUMBER, CREDITCARDVALIDTO, CUSTOMER_ID) VALUES ('CreditcardPaymentOption', 5, null, null, null, '223 ', '4555 1111 1111 1111', '2024-01-01', 3);

INSERT INTO APP.ARTICLE (ID, AUCTIONENDDATE, AUCTIONSTARTDATE, DESCRIPTION, HAMMERPRICE, RESERVEPRICE, STATUS, BUYER_ID, SELLER_ID) VALUES
                                                                                                                                        (1, '2023-04-01 12:00:00', '2023-03-27 12:00:00', 'MacBook Pro', 2000.00, 1800.00, 1, NULL, 1),
                                                                                                                                        (2, '2023-04-10 12:00:00', '2023-03-27 12:00:00', 'Sofa', 500.00, 400.00, 0, NULL, 2),
                                                                                                                                        (3, '2023-04-15 12:00:00', '2023-03-27 12:00:00', 'T-shirt', 20.00, 15.00, 1, NULL, 3),
                                                                                                                                        (4, '2023-04-27 10:00:00', '2023-04-20 10:00:00', 'Harry Potter and the Philosopher''s Stone', 100.0, 80.0, 1, NULL, 1),
                                                                                                                                        (5, '2023-04-27 11:00:00', '2023-04-20 11:00:00', 'Apple iPhone 13 Pro Max', 1500.0, 1200.0, 1, 2, 1),
                                                                                                                                        (6, '2023-04-28 10:00:00', '2023-04-21 10:00:00', 'Dyson V11 Absolute Cordless Vacuum Cleaner', 400.0, 300.0, 1, NULL, 3),
                                                                                                                                        (7, '2023-04-28 11:00:00', '2023-04-21 11:00:00', 'Lego Star Wars Ultimate Millennium Falcon', 800.0, 600.0, 1, NULL, 3);

INSERT INTO APP.ARTICLE_CATEGORY (ARTICLE_ID, CATEGORIES_ID) VALUES (1, 1);
INSERT INTO APP.ARTICLE_CATEGORY (ARTICLE_ID, CATEGORIES_ID) VALUES (2, 3);
INSERT INTO APP.ARTICLE_CATEGORY (ARTICLE_ID, CATEGORIES_ID) VALUES (3, 4);
INSERT INTO APP.ARTICLE_CATEGORY (ARTICLE_ID, CATEGORIES_ID) VALUES (4, 4);
INSERT INTO APP.ARTICLE_CATEGORY (ARTICLE_ID, CATEGORIES_ID) VALUES (5, 1);
INSERT INTO APP.ARTICLE_CATEGORY (ARTICLE_ID, CATEGORIES_ID) VALUES (6, 3);
INSERT INTO APP.ARTICLE_CATEGORY (ARTICLE_ID, CATEGORIES_ID) VALUES (7, 4);


INSERT INTO APP.BID (ID, BID, DATE, ARTICLE_ID, BIDDER_ID) VALUES (1, 100.00, '2022-01-01 12:00:00', 1, 1);
INSERT INTO APP.BID (ID, BID, DATE, ARTICLE_ID, BIDDER_ID) VALUES (2, 120.00, '2022-01-02 12:00:00', 1, 2);
INSERT INTO APP.BID (ID, BID, DATE, ARTICLE_ID, BIDDER_ID) VALUES (3, 150.00, '2022-01-03 12:00:00', 1, 3);
INSERT INTO APP.BID (ID, BID, DATE, ARTICLE_ID, BIDDER_ID) VALUES (4, 200.00, '2022-01-04 12:00:00', 1, 1);
INSERT INTO APP.BID (ID, BID, DATE, ARTICLE_ID, BIDDER_ID) VALUES (5, 400.00, '2022-01-05 12:00:00', 2, 2);
INSERT INTO APP.BID (ID, BID, DATE, ARTICLE_ID, BIDDER_ID) VALUES (6, 500.00, '2022-01-06 12:00:00', 2, 3);
INSERT INTO APP.BID (ID, BID, DATE, ARTICLE_ID, BIDDER_ID) VALUES (7, 550.00, '2022-01-07 12:00:00', 2, 2);
INSERT INTO APP.BID (ID, BID, DATE, ARTICLE_ID, BIDDER_ID) VALUES (8, 600.00, '2022-01-08 12:00:00', 2, 3);
INSERT INTO APP.BID (ID, BID, DATE, ARTICLE_ID, BIDDER_ID) VALUES (9, 700.00, '2022-01-09 12:00:00', 2, 2);
INSERT INTO APP.BID (ID, BID, DATE, ARTICLE_ID, BIDDER_ID) VALUES (10, 800.00, '2022-01-10 12:00:00', 3, 1);
INSERT INTO APP.BID (ID, BID, DATE, ARTICLE_ID, BIDDER_ID) VALUES (11, 900.00, '2022-01-11 12:00:00', 3, 3);
