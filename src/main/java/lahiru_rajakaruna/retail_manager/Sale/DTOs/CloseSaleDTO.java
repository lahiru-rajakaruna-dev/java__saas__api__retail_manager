package lahiru_rajakaruna.retail_manager.Sale.DTOs;

import lahiru_rajakaruna.retail_manager.CreditAccount.DTOs.CreateCreditAccountDTO;
import lahiru_rajakaruna.retail_manager.SalePayment.DTOs.ResponseSalePaymentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CloseSaleDTO {
    private ResponseSalePaymentDTO payment;
    private CreateCreditAccountDTO customerDetails;
}
