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

INSERT INTO item (idItem, idItemType, price, name, description) VALUES
  (1, 1, 1.35, "Pepsi", "Pepsi"),
  (2, 1, 1.30, "Coca cola", "Coca cola"),
  (3, 1, 1.32, "Sprite", "Sprite"),
  (4, 1, 1.10, "Brisa Maracujá", "Brisa Maracujá"),
  (5, 1, 1.15, "Laranjada", "Laranjada"),
  (6, 1, 0.50, "Compal Pêssego", "Compal Pêssego"),
  (7, 1, 0.45, "Compal Laranja", "Compal Laranja"),
  (8, 1, 0.55, "Compal Manga Laranja", "Compal Manga Laranja"),
  (9, 1, 0.60, "Santal Classic", "Santal Classic"),
  (10, 1, 0.65, "Nestea Lemon", "Nestea Lemon"),
  (11, 2, 0.60, "Tomato", "Tomato"),
  (12, 2, 0.75, "Apple", "Apple"),
  (13, 2, 0.80, "Orange", "Orange"),
  (14, 2, 1.60, "Mango", "Mango"),
  (15, 2, 3.00, "Blueberry", "Blueberry"),
  (16, 3, 0.45, "Black", "Black"),
  (17, 3, 0.40, "Green", "Green"),
  (18, 3, 0.55, "Rooibos", "Rooibos"),
  (19, 3, 0.40, "Ginger", "Ginger"),
  (20, 4, 0.76, "Perna de Pau", "Perna de Pau"),
  (21, 4, 1.30, "Mangum Amêndoas", "Mangum Amêndoas"),
  (22, 4, 1.00, "Mangum Branco", "Mangum Branco"),
  (23, 4, 0.90, "Mangum Clássico", "Mangum Clássico"),
  (24, 4, 0.95, "Cornetto Chocolate", "Cornetto Chocolate"),
  (25, 4, 0.90, "Cornetto Nata", "Cornetto Nata"),
  (26, 5, 1.75, "Cheesesteak", "Cheesesteak"),
  (27, 5, 1.50, "Po' Boys", "Po' Boys"),
  (28, 5, 1.35, "Breakfast", "Breakfast"),
  (29, 5, 2.00, "Reuben", "Reuben"),
  (30, 5, 1.45, "Roast Beef", "Roast Beef"),
  (31, 5, 1.20, "Grilled Cheese", "Grilled Cheese"),
  (32, 5, 1.90, "Pulled Pork", "Pulled Pork"),
  (33, 6, 1.65, "Loaded Potato", "Loaded Potato"),
  (34, 6, 1.75, "French Onion", "French Onion"),
  (35, 6, 3.00, "Beef Daube Provençal", "Beef Daube Provençal"),
  (36, 7, 0.95, "Snickers", "Snickers"),
  (37, 7, 0.65, "Skittles", "Skittles"),
  (38, 7, 0.85, "Smarties", "Smarties"),
  (39, 7, 1.05, "Ruffles Jámon", "Ruffles Jámon"),
  (40, 7, 0.95, "Lay's Classic", "Lay's Classic"),
  (41, 7, 0.50, "Popcorn", "Popcorn"),
  (42, 8, 0.75, "Cappuccino", "Cappuccino"),
  (43, 8, 0.65, "Americano", "Americano"),
  (44, 8, 0.80, "Caffe Mocha", "Caffe Mocha");

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
