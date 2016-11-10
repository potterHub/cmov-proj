PRAGMA foreign_keys = ON;
PRAGMA encoding = "UTF-8";

SELECT
voucher.idVoucher, voucher.code, voucher.gotVoucher,
voucherTemplate.idVoucherTemplate, voucherTemplate.description,
voucherTemplate.value, voucherTemplateType.idVoucherTemplateType,
voucherTemplateType.description, itemType.idItemType, itemType.description
FROM voucher
JOIN voucherTemplate on voucherTemplate.idVoucherTemplate = voucher.idVoucherTemplate
JOIN voucherTemplateType on voucherTemplateType.idVoucherTemplateType = voucherTemplate.idVoucherTemplateType
LEFT JOIN itemType on itemType.idItemType = voucherTemplate.idItemType
WHERE voucher.idSale IS NULL AND voucher.idCustomer = 1;
