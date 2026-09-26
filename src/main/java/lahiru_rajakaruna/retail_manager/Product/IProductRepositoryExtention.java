/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Product;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author bl4z3
 */
public interface IProductRepositoryExtention {

	List<Product> findAllByShopId(UUID shopId);

}
