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
    void shouldTransferMoney() {
        var dummyClientRepository = mock(ClientRepository.class);
        var mockSavingAccountRepository = mock(SavingAccountRepository.class);
        var mockFromSavingAccount = mock(SavingAccount.class);
        var mockToSavingAccount = mock(SavingAccount.class);
        int fromAccountId = 1;
        int toAccountId = 2;
        double amount = 1.0d;

        when(mockSavingAccountRepository.getAccountById(fromAccountId)).thenReturn(mockFromSavingAccount);

        when(mockSavingAccountRepository.getAccountById(toAccountId)).thenReturn(mockToSavingAccount);

        var sut = new Processing(dummyClientRepository, Cash::log, mockSavingAccountRepository);

        sut.transfer(fromAccountId, toAccountId, amount);

        verify(mockFromSavingAccount).withDraw(amount);
        verify(mockToSavingAccount).deposit(amount);

        verify(mockSavingAccountRepository).save(mockFromSavingAccount);
        verify(mockSavingAccountRepository).save(mockToSavingAccount);

    }

    @Test
    public void shouldNotTransferMoneyWhenAccountIdIncorrect(){

        var dummyClientRepository = mock(ClientRepository.class);
        var dummySavingAccountRepository = mock(SavingAccountRepository.class);
        int incorrectAccountId = -1;
        int correctAccountId = 1;
        double amount = 1.0d;

        var sut = new Processing(dummyClientRepository, Cash::log, dummySavingAccountRepository);

        assertAll(
                ()-> assertThrows(IllegalArgumentException.class, () -> sut.transfer(incorrectAccountId, correctAccountId, amount)),
                ()-> assertThrows(IllegalArgumentException.class, () -> sut.transfer(correctAccountId, incorrectAccountId, amount))
        );

    }

    @Test
    public void shouldNotTransferMoneyWhenAccountNotFound(){

        var dummyClientRepository = mock(ClientRepository.class);
        var mockSavingAccountRepository = mock(SavingAccountRepository.class);
        var mockSavingAccount = mock(SavingAccount.class);
        int fromAccountId = 1;
        int notFoundFromAccountId = 2;
        int toAccountId = 3;
        int notFoundToAccountId = 4;
        double amount = 1.0d;

        when(mockSavingAccountRepository.getAccountById(fromAccountId)).thenReturn(mockSavingAccount);
        when(mockSavingAccountRepository.getAccountById(toAccountId)).thenReturn(mockSavingAccount);

        var sut = new Processing(dummyClientRepository, Cash::log, mockSavingAccountRepository);

        assertAll(
                ()-> assertThrows(IllegalStateException.class, () -> sut.transfer(fromAccountId, notFoundToAccountId, amount)),
                ()-> assertThrows(IllegalStateException.class, () -> sut.transfer(notFoundFromAccountId, toAccountId, amount))
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