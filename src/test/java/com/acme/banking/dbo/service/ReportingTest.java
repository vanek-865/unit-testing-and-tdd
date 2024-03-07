package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.Branch;
import com.acme.banking.dbo.domain.Client;
import com.acme.banking.dbo.domain.SavingAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportingTest {

    @Mock
    Branch branch;

    @Mock
    Branch childBranch;
    Branch nullBranch = null;

    @Mock
    SavingAccount rootBranchAccount;

    @Mock
    SavingAccount firstChildBranchAccount;
    @Mock
    SavingAccount secondChildBranchAccount;
    @Mock
    Client client;

    Reporting sut;

    @BeforeEach
    void setUp() {
        sut = new Reporting();
    }


    @Test
    public void shouldReturnEmptyReportWhenBranchIsNull() {

        assertEquals("# BRANCH is empty", sut.getReport(nullBranch));
    }

    @Test
    public void shouldReturnEmptyReportWhenAccountAndChildBranchIsEmpty() {

        when(branch.getId()).thenReturn(1);
        when(branch.getAccounts()).thenReturn(null);
        when(branch.getChildren()).thenReturn(null);

        assertEquals("# BRANCH 1 (0.0)"
                , sut.getReport(branch)
        );
    }

    @Test
    public void shouldReturnEmptyReportWhenHasAccountsAndChildBranchIsEmpty() {

        when(branch.getId()).thenReturn(1);
        when(branch.getAccounts()).thenReturn(Collections.singletonList(rootBranchAccount));
        when(branch.getChildren()).thenReturn(null);

        when(rootBranchAccount.getReportString()).thenReturn("       ### acc 0: 100.0");
        when(rootBranchAccount.getAmount()).thenReturn(100.0);
        when(rootBranchAccount.getClient()).thenReturn(client);

        when(client.getReportString()).thenReturn("  ## (1) Z K");

        assertEquals("# BRANCH 1 (100.0)" +
                        "  ## (1) Z K" +
                        "       ### acc 0: 100.0"
                , sut.getReport(branch)
        );
    }


    @Test
    public void shouldReturnEmptyReportWhenHasChildBranchAndAccountsIsEmpty() {

        when(branch.getId()).thenReturn(1);
        when(branch.getAccounts()).thenReturn(null);
        when(branch.getChildren()).thenReturn(Collections.singletonList(childBranch));

        when(childBranch.getId()).thenReturn(1);
        when(childBranch.getAccounts()).thenReturn(Arrays.asList(firstChildBranchAccount, secondChildBranchAccount));

        when(firstChildBranchAccount.getReportString()).thenReturn("       ### acc 1: 100.0");
        when(firstChildBranchAccount.getAmount()).thenReturn(100.0);
        when(firstChildBranchAccount.getClient()).thenReturn(client);

        when(secondChildBranchAccount.getReportString()).thenReturn("       ### acc 2: 200.0");
        when(secondChildBranchAccount.getAmount()).thenReturn(200.0);
        when(secondChildBranchAccount.getClient()).thenReturn(client);

        when(client.getReportString()).thenReturn("    ## (2) Vas Pupking");

        assertEquals("# BRANCH 1 (300.0)" +
                        "  # BRANCH 1-1 (300.0)" +
                        "    ## (2) Vas Pupking" +
                        "       ### acc 1: 100.0" +
                        "       ### acc 2: 200.0"
                , sut.getReport(branch)
        );
    }

    @Test
    public void shouldGetReport() {

        when(branch.getId()).thenReturn(1);
        when(branch.getAccounts()).thenReturn(Collections.singletonList(rootBranchAccount));
        when(branch.getChildren()).thenReturn(Collections.singletonList(childBranch));

        when(childBranch.getId()).thenReturn(1);
        when(childBranch.getAccounts()).thenReturn(Arrays.asList(firstChildBranchAccount, secondChildBranchAccount));

        when(firstChildBranchAccount.getReportString()).thenReturn("       ### acc 1: 100.0");
        when(firstChildBranchAccount.getAmount()).thenReturn(100.0);
        when(firstChildBranchAccount.getClient()).thenReturn(client);

        when(secondChildBranchAccount.getReportString()).thenReturn("       ### acc 2: 200.0");
        when(secondChildBranchAccount.getAmount()).thenReturn(200.0);
        when(secondChildBranchAccount.getClient()).thenReturn(client);

        when(rootBranchAccount.getReportString()).thenReturn("       ### acc 0: 100.0");
        when(rootBranchAccount.getAmount()).thenReturn(100.0);
        when(rootBranchAccount.getClient()).thenReturn(client);

        when(client
                .getReportString()).thenReturn("  ## (1) Z K")
                .thenReturn("    ## (2) Vas Pupking");

        assertEquals("# BRANCH 1 (400.0)" +
                        "  ## (1) Z K" +
                        "       ### acc 0: 100.0" +
                        "  # BRANCH 1-1 (300.0)" +
                        "    ## (2) Vas Pupking" +
                        "       ### acc 1: 100.0" +
                        "       ### acc 2: 200.0"
                , sut.getReport(branch)
        );
    }

}