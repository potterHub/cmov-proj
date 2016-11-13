PRAGMA foreign_keys = ON;
PRAGMA encoding = "UTF-8";

DROP TABLE IF EXISTS voucher;
DROP TABLE IF EXISTS voucherTemplateItem;
DROP TABLE IF EXISTS voucherTemplate;
DROP TABLE IF EXISTS voucherTemplateType;
DROP TABLE IF EXISTS saleItem;
DROP TABLE IF EXISTS sale;
DROP TABLE IF EXISTS customer;
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
  name TEXT NOT NULL,
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
  code TEXT NOT NULL,
  year INTEGER,
  month INTEGER CHECK (month > 0 AND month <= 12),
  FOREIGN KEY (idCreditCardType) REFERENCES creditCardType(idCreditCardType),
  PRIMARY KEY (idCreditCard)
);

CREATE TABLE customer (
  idCustomer INTEGER,
  idCreditCard INTEGER NOT NULL,
  name TEXT CHECK (name IS NOT NULL AND length(name) >= 3),
  username TEXT CHECK (username IS NOT NULL AND length(username) >= 3),
  password TEXT CHECK (password IS NOT NULL AND length(password) >= 6),
  PIN INTEGER CHECK (PIN IS NOT NULL AND length(PIN) = 4),
  FOREIGN KEY (idCreditCard) REFERENCES creditCard(idCreditCard),
  UNIQUE(username),
  PRIMARY KEY (idCustomer)
);

CREATE TABLE voucherTemplateType (
  idVoucherTemplateType INTEGER,
  description TEXT NOT NULL,
  UNIQUE(description),
  PRIMARY KEY (idVoucherTemplateType)
);

CREATE TABLE voucherTemplate (
  idVoucherTemplate INTEGER,
  idVoucherTemplateType INTEGER,
  idItemType INTEGER,
  description TEXT NOT NULL,
  value REAL CHECK (value >= 0),
  UNIQUE(description),
  FOREIGN KEY (idItemType) REFERENCES itemType(idItemType),
  FOREIGN KEY (idVoucherTemplateType) REFERENCES voucherTemplateType(idVoucherTemplateType),
  PRIMARY KEY (idVoucherTemplate)
);

CREATE TABLE voucherTemplateItem (
  idVoucherTemplate INTEGER,
  idItem INTEGER,
  FOREIGN KEY (idVoucherTemplate) REFERENCES voucherTemplate(idVoucherTemplate),
  FOREIGN KEY (idItem) REFERENCES item(idItem),
  PRIMARY KEY (idItem, idVoucherTemplate)
);

CREATE TABLE voucher (
  idVoucher INTEGER,
  idVoucherTemplate INTEGER NOT NULL,
  idCustomer INTEGER NOT NULL,
  idSale INTEGER,
  code TEXT CHECK (code IS NOT NULL AND LENGTH(code) >= 20),
  gotVoucher DATETIME NOT NULL,
  UNIQUE (code),
  FOREIGN KEY (idVoucherTemplate) REFERENCES voucherTemplate(idVoucherTemplate),
  FOREIGN KEY (idCustomer) REFERENCES customer(idCustomer),
  FOREIGN KEY (idSale) REFERENCES sale(idSale),
  PRIMARY KEY (idVoucher)
);

CREATE TABLE sale (
  idSale INTEGER,
  idCustomer INTEGER NOT NULL,
  myDateTime DATETIME NOT NULL,
  total REAL CHECK (total IS NOT NULL AND total >= 0),
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

CREATE TABLE blacklist (
  idCustomer INTEGER,
  FOREIGN KEY (idCustomer) REFERENCES customer(idCustomer),
  PRIMARY KEY (idCustomer)
);
