# RetailManager by LAR

---

This is a CRUD application for managing daily interactions of a retail shop.

The idea came from one of my freelance project of a similar kind which asked for a offline first desktop application for
managing the day-to-day interactions of one of the biggest reatil shops in the area.

Thie version manages

- Sales
- Prodcuts
- Stock
- Debts
- Payments

records.

## Design

---

### Highlevel

```mermaid
flowchart TD
    subgraph FrontEnd
        UI[React/Angular]
    end
    subgraph BackEnd
        API[Java + Spring]
    end
    subgraph Cache
        CacheStore[(Redis)]
    end
    subgraph Database
        DB[(Postgres/H2)]
    end
    UI --- API
    API --- CacheStore
    API --- DB
```

### Servlet Architecture

```mermaid
flowchart TB
    subgraph Apache Tomcat
        subgraph FitlerChain
            filter_1[Filter_1] -.- filter_n[Filter_n]
            subgraph Delegating Filter Proxy
                filter_chain_proxy[Filter Chain Proxy]
            end
            filter_n ---> filter_chain_proxy
            filter_chain_proxy ---> filter_m
            filter_m ---> servlet
        end
        filter_chain_proxy ---> security_filter_chain
    end
```

#### Security

```mermaid
flowchart TB
    subgraph Delegating Filter Proxy
        filter_chain_proxy[Filter Chain Proxy]
    end
    subgraph Security Filter Chain
        security_filter_1[Security Filter 1] -.-> custom_user_authentication_filter[Custom User Authentication Filter]
        custom_user_authentication_filter -.-> anonymouse_authentication_filter[Anonymouse Authentication Filter]
        anonymouse_authentication_filter -.-> exception_translation_filter[Exception Translation Filter]
        exception_translation_filter ---> access_denied_handler[Access Denied Handler]
        exception_translation_filter ---> authentication_entrypoint[Custom Authentication Entrypoint]
    end
    subgraph SessionManager
        subgraph Redis Sentinal
            redis_master[(Redis Master)] ---> redis_slave[(Redis Slave)]
        end
    end
    subgraph Security Context Holder
        subgraph Security Context
            authentication
        end
    end
    filter_chain_proxy ---> security_filter_1
    custom_user_authentication_filter ---> redis_master
    custom_user_authentication_filter ---> authentication
```

#### API Design

```mermaid
flowchart TD
    Tenant --->|Has| Shop
    Shop --->|Holds| Product
    Shop --->|Has| Sale
    Sale --->|Contains| Product
    Sale --->|Has| SalePayment
    Shop --->|Has| DebtAccount
    DebtAccount --->|Connected To| Debtor
    DebtAccount --->|Contains| DebtEntry
    Debtor --->|Pays| DebtPayment
    DebtPayment --->|Clears| DebtEntry
```