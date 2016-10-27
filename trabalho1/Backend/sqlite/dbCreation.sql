PRAGMA foreign_keys = ON;
PRAGMA encoding = "UTF-8";

DROP TABLE IF EXISTS cmov;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS itemType;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS voucher;
DROP TABLE IF EXISTS voucherType;
DROP TABLE IF EXISTS creditCard;
DROP TABLE IF EXISTS creditCardType;
DROP TABLE IF EXISTS saleItem;
DROP TABLE IF EXISTS saleVoucher;
DROP TABLE IF EXISTS itemVoucher;
DROP TABLE IF EXISTS customerVoucher;

CREATE TABLE sale (
  idSale INTEGER,
  idCustomer INTEGER NOT NULL,
  total INTEGER CHECK (total IS NOT NULL AND total > 0 ),
  myDateTime DATETIME NOT NULL,
  FOREIGN KEY (idCustomer) REFERENCES customer(idCustomer),
  PRIMARY KEY (idSale)
);

CREATE TABLE item (
  idItem INTEGER,
  idItemType INTEGER NOT NULL,
  price REAL CHECK (price IS NOT NULL AND price > 0),
  description TEXT NOT NULL,
  FOREIGN KEY (idItemType) REFERENCES itemType(idItemType),
  PRIMARY KEY (idItem)
);

CREATE TABLE itemType (
  idItemType INTEGER,
  description TEXT NOT NULL,
  PRIMARY KEY (idItemType)
);

CREATE TABLE customer (
  idCustomer INTEGER,
  creditCardId INTEGER NOT NULL,
  name TEXT NOT NULL,
  username TEXT CHECK (username IS NOT NULL AND length(username) > 4),
  password TEXT CHECK (password IS NOT NULL AND length(password) > 6),
  PIN INTEGER CHECK (PIN IS NOT NULL AND length(PIN) = 4),
  FOREIGN KEY (creditCardId) REFERENCES creditCard(creditCardId),
  UNIQUE(username),
  PRIMARY KEY (idCustomer)
);

CREATE TABLE voucher (
  voucherId INTEGER,
  voucherTypeId INTEGER NOT NULL,
  description TEXT NOT NULL,
  FOREIGN KEY (voucherTypeId) REFERENCES voucherType(voucherTypeId),
  PRIMARY KEY (voucherId)
);

CREATE TABLE voucherType (
  voucherTypeId INTEGER,
  description TEXT NOT NULL,
  PRIMARY KEY (voucherTypeId)
);

CREATE TABLE creditCard (
  creditCardId INTEGER,
  creditCardTypeId INTEGER NOT NULL,
  year TEXT,
  month TEXT,
  FOREIGN KEY (creditCardTypeId) REFERENCES creditCardType(creditCardTypeId),
  PRIMARY KEY (creditCardId)
);

CREATE TABLE creditCardType (
  creditCardTypeId INTEGER,
  description TEXT NOT NULL,
  PRIMARY KEY (creditCardTypeID)
);

CREATE TABLE saleItem (
  idSale INTEGER,
  idItem INTEGER,
  quantity INTEGER CHECK (quantity IS NOT NULL AND quantity > 0),
  FOREIGN KEY (idSale) REFERENCES sale(idSale),
  FOREIGN KEY (idItem) REFERENCES item(idItem),
  PRIMARY KEY (idSale, idItem)
);

CREATE TABLE saleVoucher (
  idSale INTEGER,
  idVoucher INTEGER,
  FOREIGN KEY (idSale) REFERENCES sale(idSale),
  FOREIGN KEY (idVoucher) REFERENCES voucher(idVoucher),
  PRIMARY KEY (idSale, idVoucher)
);

CREATE TABLE itemVoucher (
  idItem INTEGER,
  idVoucher INTEGER,
  FOREIGN KEY (idItem) REFERENCES item(idItem),
  FOREIGN KEY (idVoucher) REFERENCES voucher(idVoucher)
);

CREATE TABLE customerVoucher (
  idVoucher INTEGER,
  idCustomer INTEGER,
  myDateTime DATETIME NOT NULL,
  code TEXT CHECK (code IS NOT NULL AND length(code) >= 28)
);
