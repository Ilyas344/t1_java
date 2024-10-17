package ru.t1.java.demo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.t1.java.demo.kafka.KafkaTransactionProducer;
import ru.t1.java.demo.mapper.TransactionMapper;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.TransactionType;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.ClientRepository;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    @Value("${spring.kafka.topic.transaction-errors}")
    private String errorTopic;

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final KafkaTransactionProducer kafkaProducer;
    private final TransactionMapper transactionMapper;


    @Override
    public void registerTransactions(List<Transaction> transactions) {
        Assert.notEmpty(transactions, "Список транзакций не может быть пустым");
        transactionRepository.saveAll(transactions)
                .forEach(transaction -> kafkaProducer.send(transaction.getId()));
    }

    @Transactional
    public void registerTransaction(List<TransactionDto> transactionDtos) {
        transactionDtos.forEach(this::processTransaction);
    }

    private void processTransaction(TransactionDto transactionDto) {
        try {
            Transaction transaction = createTransaction(transactionDto);
            if (isAccountValid(transaction)) {
                executeTransaction(transaction);
                transactionRepository.save(transaction);
                log.debug("Transaction is saved. ID: {}", transaction.getId());
            }
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            log.error("Error processing transaction: {}", e.getMessage());
            kafkaProducer.sendTo(errorTopic, transactionDto);
        }
    }

    private Transaction createTransaction(TransactionDto dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        Account account = accountRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setClientId(client.getId());
        transaction.setAccount(account);
        transaction.setTransactionType(dto.getTransactionType());
        return transaction;
    }

    private boolean isAccountValid(Transaction transaction) {
        if (transaction.getAccount().getIsBlocked()) {
            log.warn("The account is blocked. Account ID: {}", transaction.getAccount().getId());
            transaction.setProcessed(false);
            transactionRepository.save(transaction);
            kafkaProducer.sendTo(errorTopic, transactionMapper.toDto(transaction));
            return false;
        }
        return true;
    }

    private void executeTransaction(Transaction transaction) {
        switch (transaction.getTransactionType()) {
            case WRITE_OFF -> withdraw(transaction.getAccount(), transaction.getAmount());
            case PAYMENT -> deposit(transaction.getAccount(), transaction.getAmount());
            case CANCEL -> cancel(transaction.getAccount().getId());
            default -> throw new IllegalArgumentException("Неподдерживаемый тип транзакции");
        }
    }

    private void withdraw(Account account, BigDecimal amount) {
        BigDecimal newBalance = account.getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient balance for transaction.");
        }
        updateAccountBalance(account, newBalance);
    }

    private void deposit(Account account, BigDecimal amount) {
        BigDecimal newBalance = account.getBalance().add(amount);
        updateAccountBalance(account, newBalance);
    }

    private void updateAccountBalance(Account account, BigDecimal newBalance) {
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    private void cancel(Long accountId) {
        Transaction lastTransaction = transactionRepository.findLatestTransaction(accountId)
                .orElseThrow(() -> new EntityNotFoundException("No transaction found for account"));

        Account account = lastTransaction.getAccount();
        BigDecimal adjustmentAmount = lastTransaction.getAmount();

        switch (lastTransaction.getTransactionType()) {
            case WRITE_OFF -> updateAccountBalance(account, account.getBalance().add(adjustmentAmount));
            case PAYMENT -> updateAccountBalance(account, account.getBalance().subtract(adjustmentAmount));
            default -> throw new IllegalArgumentException("Invalid transaction type for cancellation");
        }

        transactionRepository.delete(lastTransaction);
    }
}



