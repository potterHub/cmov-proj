PRAGMA foreign_keys = ON;
PRAGMA encoding = "UTF-8";

INSERT INTO sale(idSale, idCustomer, myDateTime, total) VALUES
  (1, 1, "111-111", 4.62),
  (2, 1, "112-112", 4.35);

INSERT INTO saleItem(idSale, idItem, quantity) VALUES
  (1, 1, 1),
  (1, 2, 1),
  (1, 3, 1),
  (1, 10, 1),
  (2, 30, 3);

INSERT INTO voucher(idVoucher, idVoucherTemplate, idCustomer, idSale, code, gotVoucher) VALUES
  (NULL,1,1,2,"333333333333333333333333333333333333333333333","333-333"),
  (NULL,2,1,2,"777777777777777777777777777777777777777777777","777-777");
