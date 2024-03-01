package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.Cash;
import com.acme.banking.dbo.domain.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProcessingTest {

    @Test
    void shouldCreateNewClient() {
        var mockRepository = mock(ClientRepository.class);
        var id = 2;
        when(mockRepository.generateId()).thenReturn(id);
        var expectedResultClient = new Client(4, "Иван");
        when(mockRepository.save(any())).thenReturn(expectedResultClient);
        var sut = new Processing(mockRepository, Cash::log);
        var name = "name";
        var expectedSavedClient = new Client(id, name);

        var result = sut.createClient(name);

        verify(mockRepository).save(expectedSavedClient);
        assertEquals(expectedResultClient, result);
    }

    @Test
    void shouldNotCreateNewClientWhenNameIsIncorrect() {
        var mockRepository = mock(ClientRepository.class);
        var id = 2;
        when(mockRepository.generateId()).thenReturn(id);
        var sut = new Processing(mockRepository, Cash::log);
        String name = null;
        
        assertThrows(IllegalArgumentException.class, () -> sut.createClient(name));

        verify(mockRepository, times(0)).save(any());
    }

    @Test
    void getAccountsByClientId() {
    }

    @Test
    void transfer() {
    }

    @Test
    void cash() {
        var dummy = mock(ClientRepository.class);
        var mockLog = mock(CashApi.class);
        var sut = new Processing(dummy, mockLog);
        var amount = 1.4;
        var fromAccountId = 1;

        sut.cash(amount, fromAccountId);

        verify(mockLog).log(amount, fromAccountId);
    }

}