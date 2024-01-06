package iai.glsib.backend;


import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import iai.glsib.backend.dtos.BankAccountDTO;
import iai.glsib.backend.dtos.CurrentBankAccountDTO;
import iai.glsib.backend.dtos.CustomerDTO;
import iai.glsib.backend.dtos.SavingBankAccountDTO;
import iai.glsib.backend.exceptions.CustomerNotFoundException;
import iai.glsib.backend.services.BankAccountService;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
		return args -> {
			Stream.of("Rose", "Jean", "George", "Alexia", "Thomas").forEach( name -> {
				CustomerDTO customerDto = new CustomerDTO();
				customerDto.setName(name);
				customerDto.setEmail(name + "@gmail.com");
				bankAccountService.saveCustomer(customerDto);
			});

			bankAccountService.listCustomers().forEach(c -> {
				try {
					bankAccountService.saveCurrentBankAccount(Math.random() * 9000, 9522, c.getId());
					bankAccountService.saveSavingBankAccount(Math.random() * 12000, 6.8, c.getId());
				} catch (CustomerNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			});

			List<BankAccountDTO> bankAccounts = bankAccountService.listbankAccounts();
			for (BankAccountDTO bankAccount : bankAccounts) {
				for (int i = 0; i < 10; i++) {
					String accountId;
					if (bankAccount instanceof SavingBankAccountDTO) {
						accountId = ((SavingBankAccountDTO) bankAccount).getId();
					} else {
						accountId = ((CurrentBankAccountDTO) bankAccount).getId();
					}
					bankAccountService.credit(accountId, 1000 + Math.random() * 12000, "Credit");
					bankAccountService.debit(accountId, 1000 + Math.random() * 9000, "Debit");
				}
			}
		};
	}

	/* @Bean
	CommandLineRunner start(
		CustomerRepository customerRepository,
		BankAccountRepository bankAccountRepository,
		AccountOperationRepository accountOperationRepository
	) {
		return args -> {
			Stream.of("Hassan", "Yassine", "Aicha").forEach( name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name + "@gmail.com");
				customerRepository.save(customer);
			});

			customerRepository.findAll().forEach( c -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*90000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(c);
				currentAccount.setOverDraft(9058);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*90000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(c);
				savingAccount.setInterestRate(5.5);
				bankAccountRepository.save(savingAccount);
			});

			bankAccountRepository.findAll().forEach( acc -> {
				for (int i = 0; i < 10; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random() * 12000);
					accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);
				}
			});
		};
	} */

}
