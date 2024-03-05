package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.Branch;
import com.acme.banking.dbo.domain.Client;
import com.acme.banking.dbo.domain.SavingAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ReportingTest {

    @Mock
    Branch branch;

    @Mock
    SavingAccount account;
    @Mock
    Client client;

    @Test
    public void shouldGetReport(){
        when(branch.getAccounts()).thenReturn(Arrays.asList(account));
        when(account.getReportString()).thenReturn("       ### acc 0: 100.0");
        when(account.getAmount()).thenReturn(100.0);
        when(account.getClient()).thenReturn(client);
        when(client.getReportString()).thenReturn("  ## (1) Z K");

        Reporting sut = new Reporting();

        assertEquals("# BRANCH 1 (400.0)" +
                              "  ## (1) Z K" +
                              "       ### acc 0: 100.0" +
                              "  # BRANCH 1-1 (300.0)" +
                              "    ## (2) Vas Pupking" +
                              "       ### acc 1: 100.0" +
                              "       ### acc 2: 200.0"
                ,sut.getReport(branch)
        );
    }

    @Test
    public void shouldReturnEmptyReportWhenBranchIsNull(){

        Reporting sut = new Reporting();

        assertEquals( "# BRANCH is empty", sut.getReport(null));
    }

    @Test
    public void shouldReturnEmptyReportWhenAccountAndChildBranchIsEmpty(){

        Reporting sut = new Reporting();

        assertEquals("# BRANCH 1 (0.0)"
                ,sut.getReport(branch)
        );
    }

    @Test
    public void shouldReturnEmptyReportWhenHasAccountsAndChildBranchIsEmpty(){

        Reporting sut = new Reporting();
        when(branch.getAccounts()).thenReturn(Arrays.asList(account));
        when(account.getReportString()).thenReturn("       ### acc 0: 100.0");
        when(account.getAmount()).thenReturn(100.0);
        when(account.getClient()).thenReturn(client);
        when(client.getReportString()).thenReturn("  ## (1) Z K");

        assertEquals("# BRANCH 1 (100.0)" +
                              "  ## (1) Z K" +
                              "       ### acc 0: 100.0"
                ,sut.getReport(branch)
        );
    }


    @Test
    public void shouldReturnEmptyReportWhenHasChildBranchAndAccountsIsEmpty(){

        Reporting sut = new Reporting();

        assertEquals("# BRANCH 1 (300.0)" +
                              "  # BRANCH 1-1 (300.0)" +
                              "    ## (2) Vas Pupking" +
                              "       ### acc 1: 100.0" +
                              "       ### acc 2: 200.0"
                ,sut.getReport(branch)
        );
    }

}