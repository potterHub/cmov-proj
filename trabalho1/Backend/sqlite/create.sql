PRAGMA foreign_keys = ON;
PRAGMA encoding = "UTF-8";

DROP TABLE IF EXISTS customerSaleVoucher;
DROP TABLE IF EXISTS voucherItem;
DROP TABLE IF EXISTS saleItem;
DROP TABLE IF EXISTS sale;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS voucher;
DROP TABLE IF EXISTS voucherType;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS itemType;
DROP TABLE IF EXISTS creditCard;
DROP TABLE IF EXISTS creditCardType;

CREATE TABLE itemType (
  idItemType INTEGER,
  description TEXT NOT NULL,
  PRIMARY KEY (idItemType)
);

CREATE TABLE item (
  idItem INTEGER,
  idItemType INTEGER NOT NULL,
  price REAL CHECK (price IS NOT NULL AND price > 0),
  description TEXT NOT NULL,
  FOREIGN KEY (idItemType) REFERENCES itemType(idItemType),
  PRIMARY KEY (idItem)
);

CREATE TABLE creditCardType (
  idCreditCardType INTEGER,
  description TEXT NOT NULL,
  UNIQUE(description),
  PRIMARY KEY (idCreditCardType)
);

CREATE TABLE creditCard (
  idCreditCard INTEGER,
  idCreditCardType INTEGER NOT NULL,
  year INTEGER,
  month INTEGER CHECK (month >= 0 AND month <= 12),
  FOREIGN KEY (idCreditCardType) REFERENCES creditCardType(idCreditCardType),
  PRIMARY KEY (idCreditCard)
);

CREATE TABLE customer (
  idCustomer INTEGER,
  idCreditCard INTEGER NOT NULL,
  name TEXT NOT NULL,
  username TEXT CHECK (username IS NOT NULL AND length(username) > 4),
  password TEXT CHECK (password IS NOT NULL AND length(password) > 6),
  PIN INTEGER CHECK (PIN IS NOT NULL AND length(PIN) = 4),
  FOREIGN KEY (idCreditCard) REFERENCES creditCard(idCreditCard),
  UNIQUE(username),
  PRIMARY KEY (idCustomer)
);

CREATE TABLE voucherType (
  idVoucherType INTEGER,
  description TEXT NOT NULL,
  UNIQUE(description),
  PRIMARY KEY (idVoucherType)
);

CREATE TABLE voucher (
  idVoucher INTEGER,
  idVoucherType INTEGER NOT NULL,
  idItemType INTEGER,
  description TEXT NOT NULL,
  value REAL CHECK (value >= 0),
  FOREIGN KEY (idVoucherType) REFERENCES voucherType(idVoucherType),
  FOREIGN KEY (idItemType) REFERENCES itemType(idItemType),
  PRIMARY KEY (idVoucher)
);

CREATE TABLE voucherItem (
  idVoucher INTEGER,
  idItem INTEGER,
  FOREIGN KEY (idVoucher) REFERENCES voucher(idVoucher),
  FOREIGN KEY (idItem) REFERENCES item(idItem),
  PRIMARY KEY (idItem, idVoucher)
);

CREATE TABLE sale (
  idSale INTEGER,
  idCustomer INTEGER NOT NULL,
  total INTEGER CHECK (total IS NOT NULL AND total > 0),
  myDateTime DATETIME NOT NULL,
  FOREIGN KEY (idCustomer) REFERENCES customer(idCustomer),
  PRIMARY KEY (idSale)
);

CREATE TABLE saleItem (
  idSale INTEGER,
  idItem INTEGER,
  quantity INTEGER CHECK (quantity IS NOT NULL AND quantity > 0),
  FOREIGN KEY (idSale) REFERENCES sale(idSale),
  FOREIGN KEY (idItem) REFERENCES item(idItem),
  PRIMARY KEY (idSale, idItem)
);

CREATE TABLE customerSaleVoucher (
  idCustomer INTEGER NOT NULL,
  idSale INTEGER,
  idVoucher INTEGER NOT NULL,
  gotVoucher DATETIME NOT NULL,
  usedVoucher DATETIME,
  code TEXT CHECK (code IS NOT NULL AND LENGTH(code) >= 28),
  FOREIGN KEY (idCustomer) REFERENCES customer(idCustomer),
  FOREIGN KEY (idSale) REFERENCES sale(idSale),
  FOREIGN KEY (idVoucher) REFERENCES voucher(idVoucher),
  UNIQUE (code),
  PRIMARY KEY (idCustomer, idVoucher, idSale)
);
