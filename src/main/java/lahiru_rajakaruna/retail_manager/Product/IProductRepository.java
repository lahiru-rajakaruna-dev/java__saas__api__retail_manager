/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lahiru_rajakaruna.retail_manager.Product;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bl4z3
 */
@Repository
public interface IProductRepository extends JpaRepository<Product, UUID>, IProductRepositoryExtention {
}
