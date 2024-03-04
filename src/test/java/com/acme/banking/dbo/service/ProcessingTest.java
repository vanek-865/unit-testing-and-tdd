package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.Cash;
import com.acme.banking.dbo.domain.Client;
import com.acme.banking.dbo.domain.SavingAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProcessingTest {

    @Test
    void shouldCreateNewClient() {
        var mockRepository = mock(ClientRepository.class);
        var dummyRepository = mock(SavingAccountRepository.class);
        var id = 2;
        when(mockRepository.generateId()).thenReturn(id);
        var expectedResultClient = new Client(4, "Иван");
        when(mockRepository.save(any())).thenReturn(expectedResultClient);
        var sut = new Processing(mockRepository, Cash::log, dummyRepository);
        var name = "name";
        var expectedSavedClient = new Client(id, name);

        var result = sut.createClient(name);

        verify(mockRepository).save(expectedSavedClient);
        assertEquals(expectedResultClient, result);
    }

    @Test
    void shouldNotCreateNewClientWhenNameIsIncorrect() {
        var mockRepository = mock(ClientRepository.class);
        var dummyRepository = mock(SavingAccountRepository.class);
        var id = 2;
        when(mockRepository.generateId()).thenReturn(id);
        var sut = new Processing(mockRepository, Cash::log, dummyRepository);
        String name = null;
        
        assertThrows(IllegalArgumentException.class, () -> sut.createClient(name));

        verify(mockRepository, times(0)).save(any());
    }

    @Test
    void getAccountsByClientId() {
    }

    @Test
    void shouldTransferMany() {
        var dummyClientRepository = mock(ClientRepository.class);
        var mockSavingAccountRepository = mock(SavingAccountRepository.class);
        int fromAccountId = 1;
        int toAccountId = 2;
        double amount = 3.5d;

        Client client = new Client(1, "Name");
        SavingAccount fromAccount = new SavingAccount(fromAccountId, client, amount);
        when(mockSavingAccountRepository.getAccountById(fromAccountId)).thenReturn(fromAccount);

        SavingAccount toAccount = new SavingAccount(toAccountId, client, amount);
        when(mockSavingAccountRepository.getAccountById(toAccountId)).thenReturn(toAccount);

        var sut = new Processing(dummyClientRepository, Cash::log, mockSavingAccountRepository);

        sut.transfer(fromAccountId, toAccountId, amount);

        assertEquals(0d, fromAccount.getAmount());
        assertEquals(7d, toAccount.getAmount());

        verify(mockSavingAccountRepository).save(fromAccount);
        verify(mockSavingAccountRepository).save(toAccount);

    }

    @Test
    public void shouldNotTransferManyWhenTransferAmountMoreAccountAmount(){

        var dummyClientRepository = mock(ClientRepository.class);
        var mockSavingAccountRepository = mock(SavingAccountRepository.class);
        int fromAccountId = 1;
        int toAccountId = 2;
        double amount = 3.5d;
        double incorrectAmount = 5.5d;

        Client client = new Client(1, "Name");
        SavingAccount fromAccount = new SavingAccount(fromAccountId, client, amount);
        when(mockSavingAccountRepository.getAccountById(fromAccountId)).thenReturn(fromAccount);

        SavingAccount toAccount = new SavingAccount(toAccountId, client, amount);
        when(mockSavingAccountRepository.getAccountById(toAccountId)).thenReturn(toAccount);

        var sut = new Processing(dummyClientRepository, Cash::log, mockSavingAccountRepository);

        assertThrows(IllegalStateException.class, () -> sut.transfer(fromAccountId, toAccountId, incorrectAmount));

        verify(mockSavingAccountRepository, never()).save(any());

    }

    @Test
    public void shouldNotTransferManyWhenAccountIdIncorrect(){

        var dummyClientRepository = mock(ClientRepository.class);
        var dummySavingAccountRepository = mock(SavingAccountRepository.class);
        int incorrectAccountId = -1;
        int correctAccountId = 1;
        double amount = 3.5d;

        var sut = new Processing(dummyClientRepository, Cash::log, dummySavingAccountRepository);

        assertAll(
                ()-> assertThrows(IllegalArgumentException.class, () -> sut.transfer(incorrectAccountId, correctAccountId, amount)),
                ()-> assertThrows(IllegalArgumentException.class, () -> sut.transfer(correctAccountId, incorrectAccountId, amount))
        );

    }

    @Test
    public void shouldNotTransferManyWhenAccountNotFound(){

        var dummyClientRepository = mock(ClientRepository.class);
        var mockSavingAccountRepository = mock(SavingAccountRepository.class);
        int fromAccountId = 1;
        int incorrectFromAccountId = 5;
        int toAccountId = 2;
        int incorrectToAccountId = 7;
        double amount = 3.5d;

        Client client = new Client(1, "Name");
        SavingAccount fromAccount = new SavingAccount(fromAccountId, client, amount);
        when(mockSavingAccountRepository.getAccountById(fromAccountId)).thenReturn(fromAccount);

        SavingAccount toAccount = new SavingAccount(toAccountId, client, amount);
        when(mockSavingAccountRepository.getAccountById(toAccountId)).thenReturn(toAccount);

        var sut = new Processing(dummyClientRepository, Cash::log, mockSavingAccountRepository);

        assertAll(
                ()-> assertThrows(IllegalStateException.class, () -> sut.transfer(fromAccountId, incorrectToAccountId, amount)),
                ()-> assertThrows(IllegalStateException.class, () -> sut.transfer(incorrectFromAccountId, toAccountId, amount))
        );

    }


    @Test
    void cash() {
        var dummy = mock(ClientRepository.class);
        var dummyRepository = mock(SavingAccountRepository.class);
        var mockLog = mock(CashApi.class);
        var sut = new Processing(dummy, mockLog, dummyRepository);
        var amount = 1.4;
        var fromAccountId = 1;

        sut.cash(amount, fromAccountId);

        verify(mockLog).log(amount, fromAccountId);
    }

}