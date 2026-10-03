package lahiru_rajakaruna.retail_manager.CreditAccount;


import jakarta.transaction.Transactional;
import lahiru_rajakaruna.retail_manager.CreditAccount.DTOs.CreateCreditAccountDTO;
import lahiru_rajakaruna.retail_manager.CreditAccount.DTOs.PatchCreditAccountDTO;
import lahiru_rajakaruna.retail_manager.CreditAccount.DTOs.ResponseCreditAccountDTO;
import lahiru_rajakaruna.retail_manager.Shop.IShopRepository;
import lahiru_rajakaruna.retail_manager.Shop.Shop;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CreditAccountService {

    private final ICreditAccountRepository creditAccountRepo;
    private final IShopRepository shopRepo;

    public CreditAccountService(ICreditAccountRepository creditAccountRepo, IShopRepository shopRepo) {
        this.creditAccountRepo = creditAccountRepo;
        this.shopRepo = shopRepo;
        checkInternalComponentsPresence();
    }

    private void checkInternalComponentsPresence() {
        Objects.requireNonNull(creditAccountRepo, "Credit Account Repository Not Found");
        Objects.requireNonNull(shopRepo, "Shop Repository Not Found");
    }

    @Transactional
    public ResponseCreditAccountDTO createCreditAccount(CreateCreditAccountDTO dto) {
        requireNonNull(dto, "Credit account data is null");
        requireNonNull(dto.getShopId(), "Must provide a shop for the credit account");
        requireNonNull(dto.getName(), "Must provide a name for the credit account");
        requireNonNull(dto.getPhone(), "Must provide a phone for the credit account");

        Shop shop = shopRepo.findById(dto.getShopId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Could not find shop with ID: %s".formatted(dto.getShopId())));

        if (creditAccountRepo.existsByShop_IdAndPhone(shop.getId(), dto.getPhone())) {
            throw new IllegalStateException("A credit account with this phone already exists in the shop");
        }

        CreditAccount account = CreditAccountMapper.convertToCreditAccount(dto, shop);
        CreditAccount saved = creditAccountRepo.saveAndFlush(account);
        return CreditAccountMapper.convertToDTO(saved);
    }

    @Transactional
    public ResponseCreditAccountDTO findById(UUID id) {
        requireNonNull(id, "ID parameter is null");
        return CreditAccountMapper.convertToDTO(findCreditAccountOrThrow(id));
    }

    /**
     * Lists a shop's accounts; when {@code term} is non-blank, filters by name or phone (partial match).
     */
    @Transactional
    public List<ResponseCreditAccountDTO> findByShopId(UUID shopId, String term) {
        requireNonNull(shopId, "Shop ID parameter is null");

        List<CreditAccount> accounts = (term == null || term.isBlank())
                ? creditAccountRepo.findAllByShop_Id(shopId)
                : creditAccountRepo.search(shopId, term.trim());

        return accounts.stream()
                       .map(CreditAccountMapper::convertToDTO)
                       .toList();
    }

    @Transactional
    public ResponseCreditAccountDTO findByPhone(UUID shopId, String phone) {
        requireNonNull(shopId, "Shop ID parameter is null");
        requireNonNull(phone, "Phone cannot be null");

        CreditAccount account = creditAccountRepo.findByPhone(shopId, phone)
                                                 .orElseThrow(() -> new RuntimeException(
                                                         "Cannot find credit account associated with phone: %s".formatted(
                                                                 phone)));

        return CreditAccountMapper.convertToDTO(account);
    }

    @Transactional
    public ResponseCreditAccountDTO findByPhoneOrCreate(UUID shopId, String phone, String name) {
        requireNonNull(shopId, "Shop ID parameter is null");
        requireNonNull(phone, "Phone parameter is null");
        requireNonNull(name, "Name parameter cannot be null");

        CreditAccount existingCreditAccount = creditAccountRepo.findByPhone(shopId, phone)
                                                               .orElse(null);

        if (existingCreditAccount != null) {
            return CreditAccountMapper.convertToDTO(existingCreditAccount);
        }
        CreateCreditAccountDTO createDTO = new CreateCreditAccountDTO(shopId, name, phone);
        ResponseCreditAccountDTO newAccount = createCreditAccount(createDTO);
        return newAccount;
    }

    @Transactional
    public ResponseCreditAccountDTO patchById(UUID id, PatchCreditAccountDTO updates) {
        requireNonNull(id, "ID parameter is null");
        requireNonNull(updates, "Updates are null");

        CreditAccount account = findCreditAccountOrThrow(id);

        if (updates.getName() != null) {
            account.setName(updates.getName());
        }
        if (updates.getPhone() != null && !updates.getPhone()
                                                  .equals(account.getPhone())) {
            if (creditAccountRepo.existsByShop_IdAndPhone(account.getShop()
                                                                 .getId(), updates.getPhone())) {
                throw new IllegalStateException("A credit account with this phone already exists in the shop");
            }
            account.setPhone(updates.getPhone());
        }

        CreditAccount saved = creditAccountRepo.saveAndFlush(account);
        return CreditAccountMapper.convertToDTO(saved);
    }

    private CreditAccount findCreditAccountOrThrow(UUID id) {
        return creditAccountRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                        "Could not find credit account with ID: %s".formatted(id)));
    }

    private void requireNonNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
