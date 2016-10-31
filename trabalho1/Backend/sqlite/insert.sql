PRAGMA foreign_keys = ON;
PRAGMA encoding = "UTF-8";

INSERT INTO itemType (idItemType, description) VALUES
  (1, "Beverage"),
  (2, "Fresh Juice"),
  (3, "Tea"),
  (4, "Ice Cream"),
  (5, "Sandwich"),
  (6, "Soup"),
  (7, "Snack"),
  (8, "Coffee");

INSERT INTO item (idItem, idItemType, price, description) VALUES
  (1, 1, 1.35, "Pepsi"),
  (2, 1, 1.30, "Coca cola"),
  (3, 1, 1.32, "Sprite"),
  (4, 1, 1.10, "Brisa Maracujá"),
  (5, 1, 1.15, "Laranjada"),
  (6, 1, 0.50, "Compal Pêssego"),
  (7, 1, 0.45, "Compal Laranja"),
  (8, 1, 0.55, "Compal Manga Laranja"),
  (9, 1, 0.60, "Santal Classic"),
  (10, 1, 0.65, "Nestea Lemon"),
  (11, 2, 0.60, "Tomato"),
  (12, 2, 0.75, "Apple"),
  (13, 2, 0.80, "Orange"),
  (14, 2, 1.60, "Mango"),
  (15, 2, 3.00, "Blueberry"),
  (16, 3, 0.45, "Black"),
  (17, 3, 0.40, "Green"),
  (18, 3, 0.55, "Rooibos"),
  (19, 3, 0.40, "Ginger"),
  (20, 4, 0.76, "Perna de Pau"),
  (21, 4, 1.30, "Mangum Amêndoas"),
  (22, 4, 1.00, "Mangum Branco"),
  (23, 4, 0.90, "Mangum Clássico"),
  (24, 4, 0.95, "Cornetto Chocolate"),
  (25, 4, 0.90, "Cornetto Nata"),
  (26, 5, 1.75, "Cheesesteak"),
  (27, 5, 1.50, "Po' Boys"),
  (28, 5, 1.35, "Breakfast"),
  (29, 5, 2.00, "Reuben"),
  (30, 5, 1.45, "Roast Beef"),
  (31, 5, 1.20, "Grilled Cheese"),
  (32, 5, 1.90, "Pulled Pork"),
  (33, 6, 1.65, "Loaded Potato"),
  (34, 6, 1.75, "French Onion"),
  (35, 6, 3.00, "Beef Daube Provençal"),
  (36, 7, 0.95, "Snickers"),
  (37, 7, 0.65, "Skittles"),
  (38, 7, 0.85, "Smarties"),
  (39, 7, 1.05, "Ruffles Jámon"),
  (40, 7, 0.95, "Lay's Classic"),
  (41, 7, 0.50, "Popcorn"),
  (42, 8, 0.75, "Cappuccino"),
  (43, 8, 0.65, "Americano"),
  (44, 8, 0.80, "Caffe Mocha");

INSERT INTO creditCardType (idCreditCardType, description) VALUES
  (1, "Visa"),
  (2, "Mastercard");

INSERT INTO voucherTemplateType (idVoucherTemplateType, description) VALUES
  (1, "Global discount"),
  (2, "Discount Item"),
  (3, "Discount Item type"),
  (4, "Discount Items"),
  (5, "Free Item"),
  (6, "Free Item type"),
  (7, "Free Items");

INSERT INTO voucherTemplate (idVoucherTemplate, idVoucherTemplateType, idItemType, description, value) VALUES
  (1, 1, NULL, "5% Discount", 0.05),
  (2, 5, NULL, "Free popcorn", 1),
  (3, 6, 8, "Free coffee", 1);

INSERT INTO voucherTemplateItem (idVoucherTemplate, idItem) VALUES
  (2, 41);

-- INSERT INTO voucher (idVoucher, idVoucherTemplate, idCustomer, idSale, code, gotVoucher, usedVoucher) VALUES

-- INSERT INTO creditCard (idCreditCard, idCreditCardType, code, year, month) VALUES
-- INSERT INTO customer (idCustomer, idCreditCard, name, username, password, PIN) VALUES
-- INSERT INTO sale (idSale, idCustomer, total, myDateTime) VALUES
-- INSERT INTO saleItem (idSale, idItem, quantity) VALUES
