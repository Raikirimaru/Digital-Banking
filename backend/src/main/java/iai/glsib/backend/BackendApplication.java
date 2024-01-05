package iai.glsib.backend;


import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import iai.glsib.backend.entities.BankAccount;
import iai.glsib.backend.entities.Customer;
import iai.glsib.backend.exceptions.BalanceNotSufficientException;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;
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
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name + "@gmail.com");
				bankAccountService.saveCustomer(customer);
			});

			bankAccountService.listCustomers().forEach(c -> {
				try {
					bankAccountService.saveCurrentBankAccount(Math.random() * 9000, 9522, c.getId());
					bankAccountService.saveSavingBankAccount(Math.random() * 12000, 6.8, c.getId());
					List<BankAccount> bankAccounts = bankAccountService.listbankAccounts();
					for (BankAccount bankAccount : bankAccounts) {
						for (int i = 0; i < 10; i++) {
							bankAccountService.credit(bankAccount.getId(), 1000 + Math.random() * 12000, "Credit");
							bankAccountService.debit(bankAccount.getId(), 1000 + Math.random() * 9000, "Debit");
						}
					}
				} catch (CustomerNotFoundException | BankAccountNotFoundException | BalanceNotSufficientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			});
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
